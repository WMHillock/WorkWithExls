import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
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
        separator();
        Integer indexOfDeque = abiturientDeque.peekFirst().getMyProfessions(i).getEnumIndex();
        Integer groupLimitValue = abiturientDeque.peekFirst().getMyProfessions(i).getPlaceLimit();
        double minimalGradeValueInThisGroup = 0.0;
        if(finalDistributionList.get(indexOfDeque).peekLast() != null) {
            minimalGradeValueInThisGroup = finalDistributionList.get(indexOfDeque).peekLast().getGrades();
        }
        double abiturientGradeValue = abiturientDeque.peekFirst().getGrades();

        System.out.println("Индекс группы - " + indexOfDeque);
        System.out.println("Лимит группы - " + groupLimitValue);
        System.out.println("Минимальный балл в группе - " + minimalGradeValueInThisGroup);
        System.out.println("Текущее количество людей в группе - " + finalDistributionList.get(indexOfDeque).size());
        System.out.println(abiturientDeque.peekFirst().getName() + " - " + abiturientGradeValue + "\n"
                            + abiturientDeque.peekFirst().getMyProfessions(0)  + ", "
                + abiturientDeque.peekFirst().getMyProfessions(1) + ", "
                + abiturientDeque.peekFirst().getMyProfessions(2));
        separator();

                if (finalDistributionList.get(indexOfDeque).size() < groupLimitValue) {
                    //Добавляем нового участника в очередь профессии
                    finalDistributionList.get(indexOfDeque).offer(abiturientDeque.pollFirst());
                    System.out.println("Без конкурса добавлен в - "
                            + indexOfDeque);
                    //Заново сортируем очередь и переписываем значение ...
                        finalDistributionList.set(indexOfDeque,
                                finalDistributionList.get(indexOfDeque).stream()
                                        .sorted((a, b) -> Double.compare(b.getGrades(), a.getGrades()))
                                        .collect(Collectors.toCollection(LinkedList::new)));
                    return 1;
                //TODO Видимо удалить этот сегмент else if
                } else if (finalDistributionList.get(indexOfDeque).size() >= groupLimitValue &&
                        minimalGradeValueInThisGroup < abiturientGradeValue) {
                    //Если у нового больше, то:
                    //Вынимаем выбывающего из очереди профессии
                    Abiturient tempAbiturient = finalDistributionList.get(indexOfDeque).pollLast();
                    System.out.println("Вытащили абитуриента - " + tempAbiturient);
                    //Добавляем нового участника в очередь профессии
                    finalDistributionList.get(indexOfDeque).offer(abiturientDeque.pollFirst());
                    //Вставляем в общую очередь нового абитуриента, на первое место для проверки
                    abiturientDeque.offerFirst(tempAbiturient);
                    //Заново сортируем очередь и переписываем значение ...
                    finalDistributionList.set(indexOfDeque,
                            finalDistributionList.get(indexOfDeque).stream()
                                    .sorted((a, b) -> Double.compare(b.getGrades(), a.getGrades()))
                                    .collect(Collectors.toCollection(LinkedList::new)));
                    return 1;
                } else if (minimalGradeValueInThisGroup >= abiturientGradeValue) {

                    if (i == 0 && abiturientDeque.peekFirst().getMyProfessions(1) == null) {
                        abiturientDeque.removeFirst();
                        System.out.println("Удален по признаку - отсутствует 2 приоритет");
                        return 1;
                    }
                    if (i == 1 && abiturientDeque.peekFirst().getMyProfessions(2) == null) {
                        abiturientDeque.removeFirst();
                        System.out.println("Удален по признаку - отсутствует 3 приоритет");
                        return 1;
                    }
                    if(i == 2 && minimalGradeValueInThisGroup >= abiturientGradeValue) {
                        abiturientDeque.removeFirst();
                        System.out.println("Удален по признаку не прошел по баллу в 3 приоритет");
                        return 1;

                    }
                    System.out.println("Получен return 0 возврат к проверке направлений");

                    return 0;
                } else {
                    System.out.println("Я хз как это произошло но ошибка в abiturientHandler, " +
                            "не выполнилось ни одно из условий Оо");
                    return -1;
                }
    }

    public static void separator() {
        System.out.println("********************");
    }

    public static synchronized List<Deque<Abiturient>> distributionOfAbiturients(Deque<Abiturient> sortedAbiturientList)
            throws InterruptedException {
        Deque<Abiturient> abiturientDeque = new ConcurrentLinkedDeque<>(sortedAbiturientList);
        //TODO вот тут и работаем с DEQUE
        Profession[] profession = Profession.values();
        List<Deque<Abiturient>> finalDistributionList = new CopyOnWriteArrayList<>();
        for (Profession ignored : profession) {finalDistributionList.add(new ConcurrentLinkedDeque<>());}

        while (true) {
            //Условие окончания цикла
            if (abiturientDeque.peekFirst() != null) {
                //TODO Логика все еще порочна, выдает не вполне корректный результат
                if(abiturientHandler(finalDistributionList, abiturientDeque, 0) == 0) {
                    System.out.println("Проверили прошел ли по 1 приоритету");
                    if(abiturientDeque.peekFirst().getMyProfessions(1) != null &&
                            abiturientHandler(finalDistributionList, abiturientDeque, 1) == 0) {
                            System.out.println("Проверили прошел ли по 2 приоритету");
                        if(abiturientDeque.peekFirst().getMyProfessions(2) != null &&
                                abiturientHandler(finalDistributionList, abiturientDeque, 2) == 0) {
                                System.out.println("Проверили прошел ли по 3 приоритету");
                        } else {
                            System.out.println("Выход после 3 приоритета");
                        }
                    } else {
                        System.out.println("Выход после 2 приоритета");
                    }
                } else {
                    System.out.println("Выход после 1 приоритета");
                }
//                Thread.sleep(300);

            } else {
                for (Deque<Abiturient> abDeq : finalDistributionList) {
                    System.out.println(abDeq.peekFirst() != null ?
                            abDeq.peekFirst().getMyProfessions(0) + "\n"
                                    + "Людей в списке - " + abDeq.size()
                                    + "\nМинимальный балл - " + abDeq.peekLast().getGrades()
                            // + abDeq
                            : "Формируем группу ...");
                }
                break;
            }
        }
        return finalDistributionList;
    }
}
