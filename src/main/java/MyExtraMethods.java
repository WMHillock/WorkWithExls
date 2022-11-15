import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class MyExtraMethods {

    public static List sortAbiturientsList(List<Abiturient> abiturientList) {
        return abiturientList.stream()
                .sorted((a, b) -> Double.compare(b.getGrades(), a.getGrades()))
                .collect(Collectors.toList());
    }
    public static List collectAbiturientsToProfMainWay(List<Abiturient> abiturientList, Profession professions) {
        return abiturientList.stream()
                .filter((a) -> a.getFaculty1().equals(professions))
                .limit(professions.getPlaceLimit())
                .collect(Collectors.toList());
    }
//    public static List abiturientHandler(Abiturient abiturient) {
//
//        return abiturientList;
//    }
    public static void distributionOfAbiturients(List<Abiturient> sortedAbiturientList) {
        Profession[] myProfession = Profession.values();
        List<List<Abiturient>> workList = new ArrayList<>();

        for (Profession myProfessions : myProfession) {
            workList.add(MyExtraMethods.collectAbiturientsToProfMainWay(sortedAbiturientList, myProfessions));
        }
        workList.add(new ArrayList<>()); //индекс 11 - отсев

        for (int i = 0; i < sortedAbiturientList.size(); i++) {
            sortedAbiturientList.get(i);
        }

    }

}
