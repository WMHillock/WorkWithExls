import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
public class MyExtraMethods {
    //TODO каст это плохой вариант надо поссотреть как отсортировать Deque
    public static List<Abiturient> sortAbiturientsDeque(List<Abiturient> abiturientDeque) {
        return abiturientDeque.stream()
                .sorted((a, b) -> Double.compare(b.getGrades(), a.getGrades()))
                .collect(Collectors.toList());
    }
    public static List collectAbiturientsToProfMainWay(List<Abiturient> abiturientList, Profession professions) {
        return abiturientList.stream()
                .filter((a) -> a.getFaculty1().equals(professions))
                .limit(professions.getPlaceLimit())
                .collect(Collectors.toList());
    }
    public static Abiturient abiturientHandler(Abiturient abiturient,
                                               List<Deque<Abiturient>> finalDistributionList,
                                               Deque<Abiturient> sortedAbiturientList) {
        Profession[] profession = Profession.values();

        for (Profession professions : profession) {
            if(abiturient.getFaculty1().equals(professions)) {
                //Проверяем средний балл последнего и нашего нового
                if (finalDistributionList.get(professions.getEnumIndex()).getLast().getGrades() < abiturient.getGrades() ||
                        finalDistributionList.get(professions.getEnumIndex()).isEmpty()) {
                    //Если у нового больше, то :
                    //Вынимаем его из очереди профессии и добавляем его первым в очередь на проверку
                    sortedAbiturientList.addFirst(finalDistributionList.get(professions.getEnumIndex()).pollLast());
                    //Добавляем нового участника в очередь профессии
                    finalDistributionList.get(professions.getEnumIndex()).add(abiturient);
                    //И заново сортируем очередь
                    //TODO Мы ее отсортировали, но теперь надо ее записать или переписать ...
                    finalDistributionList.get(professions.getEnumIndex()).stream()
                            .sorted((a, b) -> Double.compare(b.getGrades(), a.getGrades()))
                            .collect(Collectors.toCollection(LinkedList::new));

                }   /*
                Оу, у нас тут по ходу 3 раза будет куртиться цикл профессий потому как логика такая
                мы берем человека по его 1 приоритету, проверяем его, подходит - ок, если нет, проверяем
                есть ли 2 приоритет, если есть крутим барабан профессий, проверям
                    */
            }
        }
        return abiturient;
    }
    public static List<Deque<Abiturient>> distributionOfAbiturients(Deque<Abiturient> sortedAbiturientList) {
        Deque<Abiturient> abiturientDeque = new LinkedList<>(sortedAbiturientList);
        //TODO вот тут и работаем с DEQUE
        Profession[] profession = Profession.values();
        List<Deque<Abiturient>> finalDistributionList = new ArrayList<>();
        for (Profession professions : profession) {finalDistributionList.add(new LinkedList<>());}

        for (Abiturient abiturientsDeque : abiturientDeque) {
            abiturientHandler(abiturientsDeque, finalDistributionList, sortedAbiturientList);
        }

        return finalDistributionList;
    }

}
