import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
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
    public static int abiturientHandler(Abiturient abiturient,
                                               List<Deque<Abiturient>> finalDistributionList,
                                               Deque<Abiturient> sortedAbiturientList, int i) {
                //TODO Неправильная структура условий, сейчас мы находим примерно ничего!
                if (finalDistributionList.get(abiturient.getMyProfessions(i).getEnumIndex()).size() <
                        abiturient.getMyProfessions(i).getPlaceLimit() ||
                        finalDistributionList.get(abiturient.getMyProfessions(i).getEnumIndex()).getLast().getGrades()
                                < abiturient.getGrades()) {
                    //Добавляем нового участника в очередь профессии
                    finalDistributionList.get(abiturient.getMyProfessions(i).getEnumIndex()).add(abiturient);
                    //Заново сортируем очередь и переписываем значение ...
                    finalDistributionList.set(abiturient.getMyProfessions(i).getEnumIndex(),
                            finalDistributionList.get(abiturient.getMyProfessions(i).getEnumIndex()).stream()
                            .sorted((a, b) -> Double.compare(b.getGrades(), a.getGrades()))
                            .collect(Collectors.toCollection(LinkedList::new)));
                    //TODO Удалить sout после работ
                    //System.out.println(finalDistributionList.get(abiturient.getMyProfessions(i).getEnumIndex()));
                    return 1;

                } else if (finalDistributionList.get(abiturient.getMyProfessions(i).getEnumIndex()).size() >=
                        abiturient.getMyProfessions(i).getPlaceLimit() ||
                        finalDistributionList.get(abiturient.getMyProfessions(i).getEnumIndex()).getLast().getGrades()
                                < abiturient.getGrades()) {
                    //Если у нового больше, то :
                    //Вынимаем выбывающего из очереди профессии и добавляем его первым в очередь на проверку
                    sortedAbiturientList.addFirst(finalDistributionList.get(abiturient.getMyProfessions(i).getEnumIndex()).pollLast());
                    //Добавляем нового участника в очередь профессии
                    finalDistributionList.get(abiturient.getMyProfessions(i).getEnumIndex()).add(abiturient);
                    //Заново сортируем очередь и переписываем значение ...
                    finalDistributionList.set(abiturient.getMyProfessions(i).getEnumIndex(),
                            finalDistributionList.get(abiturient.getMyProfessions(i).getEnumIndex()).stream()
                                    .sorted((a, b) -> Double.compare(b.getGrades(), a.getGrades()))
                                    .collect(Collectors.toCollection(LinkedList::new)));
                    return 1;
                } else if (finalDistributionList.get(abiturient.getMyProfessions(i).getEnumIndex()).getLast().getGrades()
                        < abiturient.getGrades()) {
                    return 0;
                } else {
                    return -1;
                }

    }



    public static List<Deque<Abiturient>> distributionOfAbiturients(Deque<Abiturient> sortedAbiturientList) {
        Deque<Abiturient> abiturientDeque = new LinkedList<>(sortedAbiturientList);
        //TODO вот тут и работаем с DEQUE
        Profession[] profession = Profession.values();
        List<Deque<Abiturient>> finalDistributionList = new ArrayList<>();
        for (Profession professions : profession) {finalDistributionList.add(new LinkedList<>());}

        //TODO Идея хорошая реализация говно, не уверен что так хорошо делать
        for (Abiturient abiturient : abiturientDeque) {
            if (abiturientHandler(abiturient, finalDistributionList, sortedAbiturientList, 0) == 0) {

                for (int i = 1; i < 3; i++) {
                    if (abiturient.getMyProfessions(i) != null) {
                        abiturientHandler(abiturient, finalDistributionList, sortedAbiturientList, i);
                    }
                }
            }
        }

        return finalDistributionList;
    }

}
