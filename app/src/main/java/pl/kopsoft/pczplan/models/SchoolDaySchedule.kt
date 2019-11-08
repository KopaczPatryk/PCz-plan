package pl.kopsoft.pczplan.models


import java.io.Serializable
import java.util.*

class SchoolDaySchedule : Serializable {
    var dayOfWeek: String? = null
    var subjects = ArrayList<Subject>()

    init {
        subjects = ArrayList()
    }

    fun trimEmptySubjects() {
        var lowerBound = 0
        var upperBound = 0
        for (i in subjects.indices) {
            if (!subjects[i].isEmpty()) {
                lowerBound = i
                break
            }
        }
        for (i in subjects.size - 1 downTo lowerBound) {
            if (!subjects[i].isEmpty()) {
                upperBound = i + 1
                break
            }
        }

        subjects = ArrayList(subjects.subList(lowerBound, upperBound))
        for (i in subjects.indices) {
            if (subjects[i].isEmpty()) {
                subjects[i].type = SubjectType.Gap
            }
        }
    }
}
