package pl.kopsoft.pczplan.interfaces


import pl.kopsoft.pczplan.models.SchoolWeekSchedule

interface GetSchoolWeekListener {
    fun onSchoolWeekReceived(timetable: SchoolWeekSchedule)
}
