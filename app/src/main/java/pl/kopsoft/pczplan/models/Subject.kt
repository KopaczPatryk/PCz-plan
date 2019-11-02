package pl.kopsoft.pczplan.models


import java.io.Serializable

class Subject() : Serializable {
    var hourStart: String? = null
    var subjectName: String? = null
    var teacher: String? = null
    var type: SubjectType = SubjectType.Freiheit
    var room: String? = null


    constructor(subjectName: String) : this() {
        hourStart = "8"
        this.subjectName = subjectName
    }

    fun SetTestData() {
        hourStart = "8"
        subjectName = "Sztuczna inteligencja"
        teacher = "Woldan piotr"
        type = SubjectType.Laboratory
        room = "s514"
    }

    fun IsEmpty(): Boolean {
        subjectName?.let {
            return it.isEmpty()
        }
        return true
    }
}
