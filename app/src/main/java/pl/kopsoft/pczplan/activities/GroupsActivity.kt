package pl.kopsoft.pczplan.activities


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_groups.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import pl.kopsoft.pczplan.LinkHelper
import pl.kopsoft.pczplan.R
import pl.kopsoft.pczplan.adapters.GroupsAdapter
import pl.kopsoft.pczplan.interfaces.GetGroupsListener
import pl.kopsoft.pczplan.interfaces.GetSchoolWeekListener
import pl.kopsoft.pczplan.interfaces.RecyclerViewClickListener
import pl.kopsoft.pczplan.models.*
import java.io.IOException

fun Elements.getDay(id: Int, stationary: Boolean): SchoolDaySchedule {
    val daySchedule = SchoolDaySchedule()
    val name = this.dayName(id, stationary)

//    var subjects: List<Subject> = this.subjects(id)

    daySchedule.dayOfWeek = name
    return daySchedule
}

fun Elements.dayName(id: Int, stationary: Boolean): String {
    val cell = this[0].child(id + 1)
    return if (stationary)
        cell.text().takeLast(3)
    else {
        cell.html().split("<br>").joinToString(separator = " ") { it }
    }
}

fun Elements.hour(id: Int): String {
    val cell = this[id + 1].child(0)

    return cell.html().split("<br>").last { it.isNotEmpty() }
}

//fun Elements.subjects(day: Int): List<Subject> {
//    val subjectCount = this.size
//    for (hourId in 1 until subjectCount) {
//
//    }
//}
//
//fun Elements.subject(): Subject {
//
//}

class GroupsActivity : AppCompatActivity(), GetGroupsListener,
    GetSchoolWeekListener, RecyclerViewClickListener {
    private lateinit var retrievedGroups: List<Group>
    private lateinit var semester: Semester

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)

        semester = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(TERM_BUNDLE_ID) as Semester
        } else {
            intent.getSerializableExtra(TERM_BUNDLE_ID) as Semester
        }
        val hyperlink = LinkHelper.DOMAIN + semester.hyperLink
        GetGroups(this).execute(hyperlink)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(TERM_BUNDLE_ID, semester)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        semester = savedInstanceState.getSerializable(TERM_BUNDLE_ID) as Semester
        super.onRestoreInstanceState(savedInstanceState)

    }

    override fun onGroupsGet(groups: List<Group>) {
        val sorted = groups.sortedBy { it.groupName }
        val processedGroups = ArrayList<Group>()

        sorted.forEach {
            if (it.groupName.isNotEmpty())
                processedGroups.add(it)
        }

        retrievedGroups = processedGroups
        groups_recycler.adapter = GroupsAdapter(processedGroups, this)
    }

    override fun onRecyclerItemClick(view: View, position: Int) {
        var fullLink: String
        retrievedGroups.let {
            fullLink = if (semester.isStationary) {
                LinkHelper.STATIONARY_TTS + "/" + it[position].hyperlink
            } else {
                LinkHelper.NONSTATIONARY_TTS + "/" + it[position].hyperlink
            }

        }
        GetSchoolWeekSchedule(
            this,
            semester.isStationary
        ).execute(fullLink)
    }

    override fun onSchoolWeekReceived(timetable: SchoolWeekSchedule) {
        timetable.trimAll()
        val intent = Intent(this, ScheduleActivity::class.java)
        intent.putExtra(ScheduleActivity.ARG_WEEK_SCHEDULE, timetable)
        startActivity(intent)
    }

    class GetGroups(private val listener: GetGroupsListener) :
        AsyncTask<String, Void, List<Group>>() {

        override fun doInBackground(vararg hyperlinks: String): List<Group> {
            var document: Document? = null
            try {
                document = Jsoup.connect(hyperlinks[0]).timeout(30000).get()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            assert(document != null)
            val elements = document!!.select("td > a")
            val groups = ArrayList<Group>()
            for (element in elements) {
                val g = Group(
                    groupName = element.text(),
                    hyperlink = element.attr("href")
                )

                groups.add(g)
            }
            return groups
        }

        override fun onPostExecute(subjects: List<Group>) {
            super.onPostExecute(subjects)
            listener.onGroupsGet(subjects)
        }
    }

    class GetSchoolWeekSchedule(
        private val listener: GetSchoolWeekListener,
        private val isStationary: Boolean
    ) : AsyncTask<String, Void, SchoolWeekSchedule>() {

        override fun doInBackground(vararg strings: String): SchoolWeekSchedule {
            var document: Document? = null
            try {
                document = Jsoup.connect(strings[0]).timeout(5000).get()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val weekSchedule = SchoolWeekSchedule()

            document?.let { doc ->
                val rows: Elements = doc.select("tbody > tr")

                val daysCount = rows.first().children().size - 1
                val subjectCount = rows.size - 1

                val headers: ArrayList<String> = ArrayList()
                val hours: ArrayList<String> = ArrayList()

                val subjectCells = arrayListOf<ArrayList<String>>()
//                for (i in 0..subjectCells.size) {
//                    subjectCells[i] = ArrayList()
//                }

                //get header htmls
                val allHeaders = rows.first().select("td")
                for (i in 1..daysCount) {
                    headers.add(allHeaders[i].html())
                }

                //get hours htmls
                for (i in 1..subjectCount) {
                    hours.add(rows[i].child(0).html())
                }

                for (day in 0 until daysCount) {
                    subjectCells.add(ArrayList())

                    for (subject in 0 until subjectCount) {
                        rows[subject + 1].child(day + 1).html()?.let {
                            subjectCells[day].add(it)
                        }
                    }
                }
                for (day in 0 until daysCount) {
                    val daySchedule = SchoolDaySchedule()
                    daySchedule.dayOfWeek = headers[day]
                    for (subject in 0 until  subjectCount) {
                        val s =Subject().apply {
                            hourStart
                        }

                        daySchedule.addSubject(s)
                    }

                    weekSchedule.addDay(daySchedule)
                }

//
//                for (column in 1 until rows[0].children().size) {
//                    val day = SchoolDaySchedule()
//                    if (isStationary) {
//                        day.dayOfWeek = rows[0].child(column).text().substring(0, 3)
//                    } else {
//                        val dayName =
//                            rows[0].child(column).html().split("<br>").dropLastWhile { it.isEmpty() }.toTypedArray()[0] + " " + rows[0].child(
//                                column
//                            ).html().split("<br>".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].substring(
//                                0,
//                                3
//                            )
//
//                        day.dayOfWeek = dayName
//                    }
//                    for (y in 1 until rows.size) {
//                        val s = Subject()
//                        day.subjects.add(s)
//
//                        val hour = rows[y].child(0).text()
//                        Log.i("scraped", hour)
//                        val split =
//                            hour.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
//                                .toTypedArray()
//                        s.hourStart =
//                            split[1].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
//
//                        //subject name
//                        val cellText = rows[y].child(column).html()
//                        if (cellText != "&nbsp;") {
//
//                            //split("[\\w.]+\\.");
//                            val cellSplit =
//                                cellText.split("<br>".toRegex()).dropLastWhile { it.isEmpty() }
//                                    .toTypedArray()
//                            cellSplit[0] = cellSplit[0].trim()
//                            if (cellSplit[0].toLowerCase(Locale.ROOT).contains("wyk") || cellSplit[0].toLowerCase(
//                                    Locale.ROOT
//                                ).contains("lab") || cellSplit[0].toLowerCase(Locale.ROOT).contains(
//                                    "cw"
//                                )
//                            ) {
//                                val subjectName =
//                                    cellSplit[0].substring(0, cellSplit[0].lastIndexOf(' '))
//                                s.subjectName = subjectName.trim()
//                            } else {
//                                val subjectName = cellSplit[0]
//                                s.subjectName = subjectName.trim()
//                            }
//
//                            when {
//                                cellSplit[0].toLowerCase(Locale.ROOT).contains("wyk") -> s.type =
//                                    SubjectType.Lecture
//                                cellSplit[0].toLowerCase(Locale.ROOT).contains("lab") -> s.type =
//                                    SubjectType.Laboratory
//                                cellSplit[0].toLowerCase(Locale.ROOT).contains("ćw") -> s.type =
//                                    SubjectType.Exercise
//                            }
//                            if (cellSplit.size > 1) {
//                                s.teacher = cellSplit[1]
//                            }
//                            if (cellSplit.size > 2) {
//                                s.room = cellSplit[cellSplit.size - 1]
//                            }
//                        }
//                    }
//                    weekSchedule.addDay(day)
//                }
            }

            return weekSchedule
        }

        override fun onPostExecute(schoolWeekSchedule: SchoolWeekSchedule) {
            super.onPostExecute(schoolWeekSchedule)
            listener.onSchoolWeekReceived(schoolWeekSchedule)
        }
    }

    companion object {
        @JvmStatic
        val TERM_BUNDLE_ID = "hyperlinksuffix"
    }
}
