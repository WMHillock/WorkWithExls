import java.util.*;
import java.util.stream.Collectors;
public class MyExtraMethods {
    //TODO каст это плохой вариант надо поссотреть как отсортировать Deque
    public static List<Abiturient> sortAbiturientsList(List<Abiturient> abiturientList) {
        return abiturientList.stream()
                .sorted((a, b) -> Double.compare(b.getGrades(), a.getGrades()))
                .collect(Collectors.toList());
    }
    public static List collectAbiturientsToProfMainWay(List<Abiturient> abiturientList, Profession professions) {
        return abiturientList.stream()
                .filter((a) -> a.getMyProfessions(0).equals(professions))
                .limit(professions.getPlaceLimit())
                .collect(Collectors.toList());
    }

    //TODO Метод в работе, пока все некорректно
    public static synchronized int abiturientHandler(List<Deque<Abiturient>> finalDistributionList,
                                        Deque<Abiturient> abiturientDeque, int i) {
        assert abiturientDeque.peekFirst() != null;

        //TODO Проблема в моей оптимизации XD Потому как переменные периодически становятся null
        //TODO и ту для каждого метода тогда писать if хотя не сработает скорей всего
        //TODO или возвращаемся назад к длинным цепочкам методов
        Integer indexOfDeque = abiturientDeque.peekFirst().getMyProfessions(i).getEnumIndex();
        System.out.println("Это индекс - " + indexOfDeque);

        Integer groupLimitValue = abiturientDeque.peekFirst().getMyProfessions(i).getPlaceLimit();
        System.out.println("Это групЛимит - " + groupLimitValue);

        double minimalGradeValueInThisGroup = 0.0;
        if(finalDistributionList.get(indexOfDeque).peekLast() != null) {
            minimalGradeValueInThisGroup = finalDistributionList.get(indexOfDeque).peekLast().getGrades();
        }
        System.out.println("Это минималка в группе - " + minimalGradeValueInThisGroup);

        double abiturientGradeValue = abiturientDeque.peekFirst().getGrades();
        System.out.println("Это значение самого абитуриента - " + abiturientGradeValue);

                //TODO Неправильная структура условий, сейчас мы находим примерно ничего!
                if (finalDistributionList.get(indexOfDeque).size() < groupLimitValue) {
                    //Добавляем нового участника в очередь профессии
                    finalDistributionList.get(indexOfDeque).add(abiturientDeque.pollFirst());
                    //Заново сортируем очередь и переписываем значение ...
                    if(finalDistributionList.get(indexOfDeque).size() >= groupLimitValue) {
                        finalDistributionList.set(indexOfDeque,
                                finalDistributionList.get(indexOfDeque).stream()
                                        .sorted((a, b) -> Double.compare(b.getGrades(), a.getGrades()))
                                        .collect(Collectors.toCollection(LinkedList::new)));
                    }
                    return 1;

                } else if (finalDistributionList.get(indexOfDeque).size() >= groupLimitValue &&
                        minimalGradeValueInThisGroup < abiturientGradeValue) {
                    //Если у нового больше, то:
                    //Вынимаем выбывающего из очереди профессии
                    Abiturient tempAbiturient = finalDistributionList.get(indexOfDeque).pollLast();
                    //Добавляем нового участника в очередь профессии
                    finalDistributionList.get(indexOfDeque).add(abiturientDeque.pollFirst());
                    //Вставляем в общую очередь нового абитуриента, на первое место для проверки
                    abiturientDeque.addFirst(tempAbiturient);
                    //Заново сортируем очередь и переписываем значение ...
                    finalDistributionList.set(indexOfDeque,
                            finalDistributionList.get(indexOfDeque).stream()
                                    .sorted((a, b) -> Double.compare(b.getGrades(), a.getGrades()))
                                    .collect(Collectors.toCollection(LinkedList::new)));
                    return 1;
                } else if (minimalGradeValueInThisGroup >= abiturientGradeValue) {

                    if(abiturientDeque.getFirst().getMyProfessions(1) == null) {
                        abiturientDeque.removeFirst();
                    }
                    if(abiturientDeque.getFirst().getMyProfessions(2) == null) {
                        abiturientDeque.removeFirst();
                    }
                    return 0;
                } else {
                    System.out.println("Я хз как это произошло но ошибка в abiturientHandler, " +
                            "не выполнилось ни одно из условий Оо");
                    return -1;
                }
    }

    public static double minimalGrades(List<Deque<Abiturient>> finalDistributionList) {
        double minValue = 5;
        for (Deque<Abiturient> dequeue : finalDistributionList) {
            if( !(dequeue.peekLast() == null) && minValue > dequeue.peekLast().getGrades()) {
                minValue = dequeue.peekLast().getGrades();
            } else  {
                minValue = 0;
            }
        }
        return minValue;
    }


    public static List<Deque<Abiturient>> distributionOfAbiturients(Deque<Abiturient> sortedAbiturientList) {
        Deque<Abiturient> abiturientDeque = new ArrayDeque<>(sortedAbiturientList);
        //TODO вот тут и работаем с DEQUE
        Profession[] profession = Profession.values();
        List<Deque<Abiturient>> finalDistributionList = new ArrayList<>();
        for (Profession ignored : profession) {finalDistributionList.add(new LinkedList<>());}

        while (true) {
            //Условие окончания цикла
            if (abiturientDeque.peekFirst().getGrades() >= minimalGrades(finalDistributionList)) {
                int i = 0;
                if(abiturientHandler(finalDistributionList, abiturientDeque, i) == 0) {
                    i = 1;
                    if(abiturientHandler(finalDistributionList, abiturientDeque, i) == 0) {
                        i = 2;
                        if(abiturientHandler(finalDistributionList, abiturientDeque, i) == 0) {
                            break;
                        }
                    }
                }
            } else {
                break;
            }
        }
        return finalDistributionList;
    }

}
