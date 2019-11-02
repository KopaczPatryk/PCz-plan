package pl.kopsoft.pczplan.core.semester


import pl.kopsoft.pczplan.models.Semester

internal interface GetSemestersListener {
    fun onSemestersGet(semesters: List<Semester>)
}
