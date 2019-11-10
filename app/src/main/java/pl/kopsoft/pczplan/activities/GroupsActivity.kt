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
import pl.kopsoft.pczplan.adapters.GroupsAdapter
import pl.kopsoft.pczplan.interfaces.GetGroupsListener
import pl.kopsoft.pczplan.interfaces.GetSchoolWeekListener
import pl.kopsoft.pczplan.interfaces.RecyclerViewClickListener
import pl.kopsoft.pczplan.models.*
import java.io.IOException
import java.util.regex.Pattern

class GroupsActivity : AppCompatActivity(), GetGroupsListener,
    GetSchoolWeekListener, RecyclerViewClickListener {
    private lateinit var retrievedGroups: List<Group>
    private lateinit var semester: Semester

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(pl.kopsoft.pczplan.R.layout.activity_groups)

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

                //get header htmls
                val allHeaders = rows.first().select("td")
                for (i in 1..daysCount) {
                    if (isStationary) {
                        headers.add(allHeaders[i].text().take(3))
                    } else {
                        val header = allHeaders[i].html().replace(NEW_LINE, " ")
                        headers.add(header)
                    }
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

                val startHourPattern = Pattern.compile("(\\d+[.:;]\\d+) ?-")
                subjectCells.forEachIndexed { di, day ->
                    val daySchedule = SchoolDaySchedule().apply {
                        this.dayOfWeek = headers[di]
                    }

                    day.forEachIndexed { si, subject ->
                        val strings = subject.split(NEW_LINE)

                        var name = if (strings.count() >= 1) strings.first() else ""
                        if (name == EMPTY_LINE)
                            name = ""

                        val teacher = if (strings.count() >= 2) strings[1] else ""
                        val where = if (strings.count() >= 3) strings.last() else ""
                        val type = when {
                            name.contains("wyk.", true) -> SubjectType.Lecture
                            name.contains("lec.", true) -> SubjectType.Lecture
                            name.contains("lab.", true) -> SubjectType.Laboratory
                            name.contains("ćw.", true) -> SubjectType.Exercise
                            name.contains("exe.", true) -> SubjectType.Exercise
                            name.isEmpty() -> SubjectType.Freiheit
                            else -> SubjectType.Freiheit
                        }

                        var hour = ""
                        val m = startHourPattern.matcher(hours[si])
                        if (m.find()) {
                            m.group(1)?.let {
                                hour = it
                            }
                        }

                        val subjectObject = Subject(
                            hourStart = hour,
                            subjectName = name,
                            teacher = teacher,
                            type = type,
                            room = where
                        )
                        daySchedule.addSubject(subjectObject)
                    }
                    weekSchedule.addDay(daySchedule)
                }
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

        @JvmStatic
        val NEW_LINE = "<br>"

        @JvmStatic
        val EMPTY_LINE = "&nbsp;"
    }
}
