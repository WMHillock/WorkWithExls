import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Abiturient {
    private String name = "";
    private List<Profession> myProfessions =
            new ArrayList<>(Arrays.asList(null, null, null));
    private double grades = 0.0;
    private String factor13 = "";
    public static int usersCount = 0;

    public Profession getMyProfessions(int indexOfProfession) {
        return myProfessions.get(indexOfProfession);
    }
    public void setMyProfessions(int indexOfProfession, Profession profession) {
        this.myProfessions.set(indexOfProfession, profession);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getGrades() {
        return grades;
    }
    public void setGrades(double grades) {
        this.grades = grades;
    }

    public String getFactor13() {
        return factor13;
    }
    public void setFactor13(String factor13) {
        this.factor13 = factor13;
    }

    public Abiturient() { usersCount++; }

    @Override
    public String toString() {

        StringBuilder str = new StringBuilder();
        str.append("***" + "\n" + getName() + " (" + getGrades() + ") -> \n");

        for (int i = 0; i < myProfessions.size(); i++) {
            if (myProfessions.get(i) != null)
            str.append("Направление № " + (i + 1) + " - " + myProfessions.get(i).getFullName() + "\n");
        }
        if (!getFactor13().isBlank()) {
            str.append("Сданы оригиналы!" + "\n");
        }
        str.append("***" + "\n");
        return String.valueOf(str);
    }
}
