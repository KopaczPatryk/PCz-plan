package pl.kopsoft.pczplan.models


import java.io.Serializable

data class Subject(
    var hourStart: String? = null,
    var subjectName: String? = null,
    var teacher: String? = null,
    var type: SubjectType = SubjectType.Freiheit,
    var room: String? = null
) : Serializable {

    fun isEmpty(): Boolean {
        subjectName?.let {
            return it.isEmpty()
        }
        return true
    }
}
