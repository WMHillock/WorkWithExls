import java.util.Objects;
public class Abiturient {
    private String name = "";
    //TODO Переделать поле факультет со String на Profession
    private String faculty1 = "";
    private String faculty2 = "";
    private String faculty3 = "";
    private double grades = 0.0;
    private String factor13 = "";
    public static int usersCount = 0;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty1() {
        return faculty1;
    }
    public void setFaculty1(String faculty1) {
        this.faculty1 = faculty1;
    }

    public String getFaculty2() {
        return faculty2;
    }
    public void setFaculty2(String faculty2) {
        this.faculty2 = faculty2;
    }

    public String getFaculty3() {
        return faculty3;
    }
    public double getGrades() {
        return grades;
    }

    public void setGrades(double grades) {
        this.grades = grades;
    }
    public void setFaculty3(String faculty3) {
        this.faculty3 = faculty3;
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
        if (!getFaculty2().isBlank()) {
            str.append("Дополнительное направление 1 - " + getFaculty2() + "\n");
            if (!getFaculty3().isBlank()) {
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
