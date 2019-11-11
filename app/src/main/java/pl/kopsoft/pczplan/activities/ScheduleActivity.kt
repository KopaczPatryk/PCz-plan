package pl.kopsoft.pczplan.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_schedule.*
import pl.kopsoft.pczplan.R
import pl.kopsoft.pczplan.fragments.SchoolDayFragment
import pl.kopsoft.pczplan.models.SchoolWeekSchedule

class ScheduleActivity : AppCompatActivity() {
    private var schedule = SchoolWeekSchedule()

    private lateinit var daySchedulePagerAdapter: DaySchedulePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        intent?.let {
            schedule = it.getSerializableExtra(ARG_WEEK_SCHEDULE) as SchoolWeekSchedule
        }

        daySchedulePagerAdapter = DaySchedulePagerAdapter(supportFragmentManager)
        schedule_viewpager.adapter = daySchedulePagerAdapter

        tabs.removeAllTabs()
        for (i in 0 until schedule.daySchedules.size) {
            tabs.addTab(tabs.newTab().setText(schedule.daySchedules[i].dayOfWeek))
        }
        daySchedulePagerAdapter.notifyDataSetChanged()

        schedule_viewpager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(schedule_viewpager))
    }

    inner class DaySchedulePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return SchoolDayFragment.newInstance(schedule.daySchedules[position])
        }

        override fun getCount(): Int {
            return schedule.daySchedules.size
        }
    }

    companion object {
        @JvmStatic
        val ARG_WEEK_SCHEDULE = "timetable"
    }
}
