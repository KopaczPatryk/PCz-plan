package pl.kopsoft.pczplan.models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SchoolDaySchedule implements Serializable {
    public String DayOfWeek;
    public List<Subject> Subjects;

    public SchoolDaySchedule() {
        Subjects = new ArrayList<>();
    }

    public void SetTestData() {
        //DayOfWeek = "Pon";
        Subject s = new Subject();
        s.SetTestData();
        Subjects.add(s);
        Subjects.add(s);
        Subjects.add(s);
        Subjects.add(s);
        Subjects.add(s);
        Subjects.add(s);
        Subjects.add(s);
        Subjects.add(s);
    }

    public void TrimEmptySubjects() {
        int lowerBound = 0;
        int upperBound = 0;
        for (int i = 0; i < Subjects.size(); i++) {
            if (!Subjects.get(i).IsEmpty()) {
                lowerBound = i;
                break;
            }
        }
        for (int i = Subjects.size() - 1; i >= lowerBound; i--) {
            if (!Subjects.get(i).IsEmpty()) {
                upperBound = i + 1;
                break;
            }
        }
//        if(lowerBound > upperBound)
//        {
//            Log.e("subject_trim", "bound mismatch");
//
//        }
        Subjects = new ArrayList<>(Subjects.subList(lowerBound, upperBound));
        for (int i = 0; i < Subjects.size(); i++) {
            if (Subjects.get(i).IsEmpty()) {
                Subjects.get(i).Type = SubjectType.Gap;
            }
        }
    }
}
