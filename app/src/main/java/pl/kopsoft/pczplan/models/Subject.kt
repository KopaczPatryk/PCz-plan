package pl.kopsoft.pczplan.models


import java.io.Serializable

class Subject : Serializable {
    var hourStart: String? = null
    var subjectName: String? = null
    var teacher: String? = null
    var type: SubjectType = SubjectType.Freiheit
    var room: String? = null


    fun isEmpty(): Boolean {
        subjectName?.let {
            return it.isEmpty()
        }
        return true
    }
}
