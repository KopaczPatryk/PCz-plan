package pl.kopsoft.pczplan.core.group


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_groups.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import pl.kopsoft.pczplan.LinkHelper
import pl.kopsoft.pczplan.R
import pl.kopsoft.pczplan.RecyclerViewClickListener
import pl.kopsoft.pczplan.adapters.GroupsAdapter
import pl.kopsoft.pczplan.core.schedule.ScheduleActivity
import pl.kopsoft.pczplan.models.*
import java.io.IOException
import java.util.*

class GroupsActivity : AppCompatActivity(), GetGroupsListener, GetSchoolWeekListener, RecyclerViewClickListener {
    private lateinit var retrievedGroups: List<Group>
    private lateinit var semester: Semester

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)

        if (savedInstanceState != null) {
            semester = savedInstanceState.getSerializable(TERM_BUNDLE_ID) as Semester
        } else {
            semester = intent.getSerializableExtra(TERM_BUNDLE_ID) as Semester
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
        GetSchoolWeekSchedule(this, semester.isStationary).execute(fullLink)
    }

    override fun onSchoolWeekReceived(timetable: SchoolWeekSchedule) {
        timetable.trimAll()
        val intent = Intent(this, ScheduleActivity::class.java)
        intent.putExtra(ScheduleActivity.ARG_WEEK_SCHEDULE, timetable)
        startActivity(intent)
    }

    class GetGroups(private val listener: GetGroupsListener) : AsyncTask<String, Void, List<Group>>() {

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

    class GetSchoolWeekSchedule(private val listener: GetSchoolWeekListener, private val IsStationary: Boolean) : AsyncTask<String, Void, SchoolWeekSchedule>() {

        override fun doInBackground(vararg strings: String): SchoolWeekSchedule {
            var document: Document? = null
            try {
                document = Jsoup.connect(strings[0]).timeout(5000).get()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val week = SchoolWeekSchedule()

            document?.let {
                val rows: Elements = it.select("tbody > tr")

                for (x in 1 until rows[0].children().size) {
                    val day = SchoolDaySchedule()
                    if (IsStationary) {
                        day.dayOfWeek = rows[0].child(x).text().substring(0, 3)
                    } else {
                        day.dayOfWeek = rows[0].child(x).html().split("<br>".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] + " " + rows[0].child(x).html().split("<br>".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].substring(0, 3)
                    }
                    week.daySchedules.add(day)
                    for (y in 1 until rows.size) {
                        val s = Subject()
                        day.subjects.add(s)

                        val hour = rows[y].child(0).text()
                        Log.i("scraped", hour)
                        val split = hour.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        s.hourStart = split[1].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]

                        //subject name
                        val cellText = rows[y].child(x).html()
                        if (cellText != "&nbsp;") {

                            //split("[\\w.]+\\.");
                            val cellSplit = cellText.split("<br>".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            cellSplit[0] = cellSplit[0].trim { it <= ' ' }
                            if (cellSplit[0].toLowerCase(Locale.ROOT).contains("wyk") || cellSplit[0].toLowerCase(Locale.ROOT).contains("lab") || cellSplit[0].toLowerCase(Locale.ROOT).contains("cw")) {
                                val subjectName = cellSplit[0].substring(0, cellSplit[0].lastIndexOf(' '))
                                s.subjectName = subjectName.trim { it <= ' ' }
                            } else {
                                val subjectName = cellSplit[0]
                                s.subjectName = subjectName.trim { it <= ' ' }
                            }

                            when {
                                cellSplit[0].toLowerCase(Locale.ROOT).contains("wyk") -> s.type = SubjectType.Lecture
                                cellSplit[0].toLowerCase(Locale.ROOT).contains("lab") -> s.type = SubjectType.Laboratory
                                cellSplit[0].toLowerCase(Locale.ROOT).contains("ćw") -> s.type = SubjectType.Exercise
                            }
                            if (cellSplit.size > 1) {
                                s.teacher = cellSplit[1]
                            }
                            if (cellSplit.size > 2) {
                                s.room = cellSplit[cellSplit.size - 1]
                            }
                        }

                    }
                }
            }

            return week
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
