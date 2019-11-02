package pl.kopsoft.pczplan.models;


import java.io.Serializable;

public class Subject implements Serializable {
    public String HourStart;
    public String SubjectName;
    public String Teacher;
    public SubjectType Type;
    public String Room;

    public Subject(String subjectName) {
        HourStart = "8";
        SubjectName = subjectName;
    }

    public Subject() {
        Type = SubjectType.Freiheit;
    }

    public void SetTestData() {
        HourStart = "8";
        SubjectName = "Sztuczna inteligencja";
        Teacher = "Woldan piotr";
        Type = SubjectType.Laboratory;
        Room = "s514";
    }

    public boolean IsEmpty() {
        if (SubjectName != null) {
            return SubjectName.isEmpty();
        } else {
            return true;
        }
    }
}
