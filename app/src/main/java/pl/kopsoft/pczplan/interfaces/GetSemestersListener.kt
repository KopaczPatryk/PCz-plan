package pl.kopsoft.pczplan.interfaces


import pl.kopsoft.pczplan.models.Semester

internal interface GetSemestersListener {
    fun onSemestersGet(semesters: List<Semester>)
}
