package pl.kopsoft.pczplan.core.semester;


import java.util.List;

import pl.kopsoft.pczplan.models.Semester;

interface GetSemestersListener {
    void OnSemestersGet(List<Semester> semesters);
}
