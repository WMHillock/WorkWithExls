import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class Abiturient {
    private String name = "";
    private Profession faculty1;
    private Profession faculty2;
    private Profession faculty3;
    private double grades = 0.0;
    private String factor13 = "";
    public static int usersCount = 0;
    //TODO Переделать Abiturient.class с профессиями в списке а не по одной
    private List<Profession> myFacultets = new ArrayList<>(3);

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Profession getFaculty1() {
        return faculty1;
    }
    public void setFaculty1(Profession faculty1) {
        this.faculty1 = faculty1;
    }

    public Profession getFaculty2() {
        return faculty2;
    }
    public void setFaculty2(Profession faculty2) {
        this.faculty2 = faculty2;
    }

    public Profession getFaculty3() {
        return faculty3;
    }
    public void setFaculty3(Profession faculty3) {
        this.faculty3 = faculty3;
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

    public Abiturient() {
        usersCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Abiturient that = (Abiturient) o;
        return Double.compare(that.grades, grades) == 0
                && Objects.equals(name, that.name)
                && Objects.equals(faculty1, that.faculty1)
                && Objects.equals(faculty2, that.faculty2)
                && Objects.equals(faculty3, that.faculty3)
                && Objects.equals(factor13, that.factor13);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, faculty1, grades, faculty2, faculty3, factor13);
    }

    @Override
    public String toString() {

        StringBuilder str = new StringBuilder();
        str.append("***" + "\n" + getName() + " (" + getGrades() + ") -> \n"
                + "Основное направление - " + getFaculty1() + "\n");
        if (getFaculty2() != null) {
            str.append("Дополнительное направление 1 - " + getFaculty2() + "\n");
            if (getFaculty3() != null) {
                str.append("Дополнительное направление 2 - " + getFaculty3() + "\n");
            }
        }
        if (!getFactor13().isBlank()) {
            str.append("Сданы оригиналы!" + "\n");
        }
        str.append("***" + "\n");
        return String.valueOf(str);
    }
}
