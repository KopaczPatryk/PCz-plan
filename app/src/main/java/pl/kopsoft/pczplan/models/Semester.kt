package pl.kopsoft.pczplan.models


import java.io.Serializable

data class Semester(
        var termName: String,
        var hyperLink: String,
        var isStationary: Boolean) : Serializable