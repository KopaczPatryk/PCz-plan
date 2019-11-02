package pl.kopsoft.pczplan.core.group;


import pl.kopsoft.pczplan.models.SchoolWeekSchedule;

public interface GetSchoolWeekListener {
    void OnSchoolWeekReceived(SchoolWeekSchedule timetable);
}
