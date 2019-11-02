package pl.kopsoft.pczplan.models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SchoolWeekSchedule implements Serializable {
    public List<SchoolDaySchedule> DaySchedules;

    public SchoolWeekSchedule() {
        DaySchedules = new ArrayList<>();
    }

    public void TrimAll() {
        for (int i = 0; i < DaySchedules.size(); i++) {
            if (DaySchedules.get(i) != null) {
                DaySchedules.get(i).TrimEmptySubjects();
            }
        }
    }
}
