import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class MyExtraMethods {

    public static List<Abiturient> sortAbiturientsList(List<Abiturient> abiturientList) {
        return abiturientList.stream()
                .sorted((a, b) -> Double.compare(b.getGrades(), a.getGrades()))
                .collect(Collectors.toList());
    }

    public static void separator() {
        System.out.println("********************");
    }

    public static synchronized int abiturientHandler(List<Deque<Abiturient>> finalDistributionList,
                                                     Deque<Abiturient> abiturientDeque, int i) {

        int indexOfDeque = abiturientDeque.peekFirst().getMyProfessions(i).getEnumIndex();
        int groupLimitValue = abiturientDeque.peekFirst().getMyProfessions(i).getPlaceLimit();
        double minimalGradeValueInThisGroup = finalDistributionList.get(indexOfDeque).peekLast() != null ?
                finalDistributionList.get(indexOfDeque).peekLast().getGrades() : 0.0;
        double abiturientGradeValue = abiturientDeque.peekFirst().getGrades();

        separator();
        System.out.println("Индекс группы - " + indexOfDeque + "\n"
                + "Лимит группы - " + groupLimitValue + "\n"
                + "Минимальный балл в группе - " + minimalGradeValueInThisGroup + "\n"
                + "Текущее количество людей в группе - " + finalDistributionList.get(indexOfDeque).size() + "\n"
                + abiturientDeque.peekFirst().getName() + " - " + abiturientGradeValue + "\n"
                + abiturientDeque.peekFirst().getMyProfessions(0) + ", "
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
        } else if (minimalGradeValueInThisGroup >= abiturientGradeValue) {

            if (i == 0 && abiturientDeque.peekFirst().getMyProfessions(1) == null) {
                finalDistributionList.get(11).offer(abiturientDeque.pollFirst());
                System.out.println("Удален по признаку - отсутствует 2 приоритет");
                return 1;
            }
            if (i == 1 && abiturientDeque.peekFirst().getMyProfessions(2) == null) {
                finalDistributionList.get(11).offer(abiturientDeque.pollFirst());
                System.out.println("Удален по признаку - отсутствует 3 приоритет");
                return 1;
            }
            if (i == 2 && minimalGradeValueInThisGroup >= abiturientGradeValue) {
                finalDistributionList.get(11).offer(abiturientDeque.pollFirst());
                System.out.println("Удален по признаку не прошел по баллу в 3 приоритет");
                return 1;

            }
            System.out.println("Получен код - 0, возврат к проверке направлений");
            return 0;
        } else {
            System.out.println("Ты никогда не должен видеть это сообщение, иначе проблема в abiturientHandler");
            return -1;
        }
    }

    public static synchronized List<Deque<Abiturient>> distributionOfAbiturients(Deque<Abiturient> sortedAbiturientList) {
        Deque<Abiturient> abiturientDeque = new ConcurrentLinkedDeque<>(sortedAbiturientList);
        Profession[] profession = Profession.values();
        List<Deque<Abiturient>> finalDistributionList = new CopyOnWriteArrayList<>();
        for (int i = 0; i <= profession.length; i++) {
            finalDistributionList.add(new ConcurrentLinkedDeque<>());
        }

        while (true) {

            if (abiturientDeque.peekFirst() != null) {
                //TODO Логика все еще порочна, выдает не вполне корректный результат
                if (abiturientHandler(finalDistributionList, abiturientDeque, 0) == 0) {
                    System.out.println("Проверили прошел ли по 1 приоритету");
                    if (abiturientDeque.peekFirst().getMyProfessions(1) != null &&
                            abiturientHandler(finalDistributionList, abiturientDeque, 1) == 0) {
                        System.out.println("Проверили прошел ли по 2 приоритету");
                        if (abiturientDeque.peekFirst().getMyProfessions(2) != null &&
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
            } else {
                for (Deque<Abiturient> abDeq : finalDistributionList) {
                    System.out.println(abDeq.peekFirst() != null ?
                            abDeq.peekFirst().getMyProfessions(0) + "\n"
                                    + "Людей в списке - " + abDeq.size()
                                    + "\nМинимальный балл - " + abDeq.peekLast().getGrades()
                            // + abDeq
                            : "Формируем группу ...");
                }
                finalDistributionList.get(finalDistributionList.size() - 1)
                        .addAll(abiturientsForManualVerification(finalDistributionList));
                System.out.println("Абитуриенты на проверку " + "(" + finalDistributionList.get(finalDistributionList.size() - 1).size() + " человек):"
                        + "\n" + finalDistributionList.get(finalDistributionList.size() - 1));
                break;
            }
        }
        return finalDistributionList;
    }

    public static List<Abiturient> abiturientsForManualVerification(List<Deque<Abiturient>> finalDistributionList) {
        double minGrade = 5.0;
        List<Abiturient> abiturientsForManualVerification = new ArrayList<>();
        int indexOfOutAbiturients = finalDistributionList.size() - 1;
        for (int i = 0; i < finalDistributionList.size() - 1; i++) {
            minGrade = Math.min(minGrade, finalDistributionList.get(i).peekLast().getGrades());
        }

        while (finalDistributionList.get(indexOfOutAbiturients).peekFirst() != null) {
            if (finalDistributionList.get(indexOfOutAbiturients).peekFirst().getGrades() >= minGrade &&
                    finalDistributionList.get(indexOfOutAbiturients).peekFirst().getMyProfessions(1) != null) {
                abiturientsForManualVerification.add(finalDistributionList.get(indexOfOutAbiturients).pollFirst());
            } else {
                finalDistributionList.get(indexOfOutAbiturients).removeFirst();
            }
        }
        return abiturientsForManualVerification;
    }
}
