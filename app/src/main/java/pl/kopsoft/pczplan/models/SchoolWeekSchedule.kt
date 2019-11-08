package pl.kopsoft.pczplan.models


import java.io.Serializable

class SchoolWeekSchedule : Serializable {
    val daySchedules: ArrayList<SchoolDaySchedule> = arrayListOf()

    fun trimAll() {
        for (i in daySchedules.indices) {
            daySchedules[i].trimEmptySubjects()
        }
    }

    fun addDay(day: SchoolDaySchedule) {
        daySchedules.add(day)
    }
}
