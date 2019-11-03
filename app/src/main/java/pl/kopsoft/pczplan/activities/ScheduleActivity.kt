package pl.kopsoft.pczplan.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_schedule.*
import pl.kopsoft.pczplan.R
import pl.kopsoft.pczplan.fragments.SchoolDayFragment
import pl.kopsoft.pczplan.models.SchoolWeekSchedule

class ScheduleActivity : AppCompatActivity() {
    private var schedule = SchoolWeekSchedule()

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        schedule = intent.getSerializableExtra(ARG_WEEK_SCHEDULE) as SchoolWeekSchedule
        //        schedule.daySchedules = new ArrayList<>();
        //        SchoolDaySchedule daySchedule = new SchoolDaySchedule();
        //        daySchedule.SetTestData();
        //        schedule.daySchedules.add(daySchedule);
        //        schedule.daySchedules.add(daySchedule);
        //        schedule.daySchedules.add(daySchedule);
        //        schedule.daySchedules.add(daySchedule);
        //        schedule.daySchedules.add(daySchedule);

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = container
        mViewPager?.adapter = mSectionsPagerAdapter

        tabs.removeAllTabs()
        for (i in 0 until schedule.daySchedules.size) {
            tabs.addTab(tabs.newTab().setText(schedule.daySchedules[i].dayOfWeek))
        }
        mSectionsPagerAdapter?.notifyDataSetChanged()

        mViewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(mViewPager))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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
