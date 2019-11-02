package pl.kopsoft.pczplan.models


import java.io.Serializable
import java.util.*

class SchoolDaySchedule : Serializable {
    var dayOfWeek: String? = null
    var subjects = ArrayList<Subject>()

    init {
        subjects = ArrayList()
    }

    fun SetTestData() {
        //dayOfWeek = "Pon";
        val s = Subject()
        s.SetTestData()
        subjects.add(s)
        subjects.add(s)
        subjects.add(s)
        subjects.add(s)
        subjects.add(s)
        subjects.add(s)
        subjects.add(s)
        subjects.add(s)
    }

    fun trimEmptySubjects() {
        var lowerBound = 0
        var upperBound = 0
        for (i in subjects.indices) {
            if (!subjects[i].IsEmpty()) {
                lowerBound = i
                break
            }
        }
        for (i in subjects.size - 1 downTo lowerBound) {
            if (!subjects[i].IsEmpty()) {
                upperBound = i + 1
                break
            }
        }
        //        if(lowerBound > upperBound)
        //        {
        //            Log.e("subject_trim", "bound mismatch");
        //
        //        }
        subjects = ArrayList(subjects.subList(lowerBound, upperBound))
        for (i in subjects.indices) {
            if (subjects[i].IsEmpty()) {
                subjects[i].type = SubjectType.Gap
            }
        }
    }
}
