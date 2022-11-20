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
                                               Deque<Abiturient> sortedAbiturientList) {
        Profession[] profession = Profession.values();
        //TODO ждем переработки класса Abiturient со списокм профессий а не по одной
        //TODO forEach для профессий нахер тут не нужон
        for (Profession professions : profession) {
            if(abiturient.getMyProfessions(0).equals(professions)) {

                //Проверяем средний балл последнего и нашего нового
                //TODO Неправильная структура условий, сейчас мы находим примерно ничего!
                if (finalDistributionList.get(professions.getEnumIndex()).size() < professions.getPlaceLimit() ||
                        finalDistributionList.get(professions.getEnumIndex()).getLast().getGrades() < abiturient.getGrades()) {
                    //Если у нового больше, то :
                    //Вынимаем выбывающего из очереди профессии и добавляем его первым в очередь на проверку
                    sortedAbiturientList.addFirst(finalDistributionList.get(professions.getEnumIndex()).pollLast());
                    //Добавляем нового участника в очередь профессии
                    finalDistributionList.get(professions.getEnumIndex()).add(abiturient);
                    //Заново сортируем очередь и переписываем значение ...
                    finalDistributionList.set(professions.getEnumIndex(), finalDistributionList.get(professions.getEnumIndex()).stream()
                            .sorted((a, b) -> Double.compare(b.getGrades(), a.getGrades()))
                            .collect(Collectors.toCollection(LinkedList::new)));
                    System.out.println(finalDistributionList.get(professions.getEnumIndex()));
                    return 1;
                } else if (finalDistributionList.get(professions.getEnumIndex()).getLast().getGrades() > abiturient.getGrades()) {
                    if (abiturient.getMyProfessions(1) != null) {
                        return 0;
                    } else {
                        return -1;
                    }

                }

            } else {
                continue;
            }
        }
        return -1;
    }
    public static List<Deque<Abiturient>> distributionOfAbiturients(Deque<Abiturient> sortedAbiturientList) {
        Deque<Abiturient> abiturientDeque = new LinkedList<>(sortedAbiturientList);
        //TODO вот тут и работаем с DEQUE
        Profession[] profession = Profession.values();
        List<Deque<Abiturient>> finalDistributionList = new ArrayList<>();
        for (Profession professions : profession) {finalDistributionList.add(new LinkedList<>());}

        for (Abiturient abiturient : abiturientDeque) {
            abiturientHandler(abiturient, finalDistributionList, sortedAbiturientList);

        }

        return finalDistributionList;
    }

}
