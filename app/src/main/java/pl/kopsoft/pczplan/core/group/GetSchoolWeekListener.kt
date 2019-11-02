package pl.kopsoft.pczplan.core.group


import pl.kopsoft.pczplan.models.SchoolWeekSchedule

interface GetSchoolWeekListener {
    fun onSchoolWeekReceived(timetable: SchoolWeekSchedule)
}
