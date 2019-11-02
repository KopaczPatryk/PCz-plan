package pl.kopsoft.pczplan.core.schedule.day


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.kopsoft.pczplan.R
import pl.kopsoft.pczplan.adapters.SchoolDaysAdapter
import pl.kopsoft.pczplan.models.SchoolDaySchedule

class SchoolDayFragment : Fragment() {
    private var daySchedule: SchoolDaySchedule? = null
    private var mColumnCount = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mColumnCount = it.getInt(ARG_COLUMN_COUNT)
            daySchedule = it.getSerializable(ARG_DAY_SCHEDULE) as SchoolDaySchedule?
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_schoolday_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            if (mColumnCount <= 1) {
                view.layoutManager = LinearLayoutManager(context)
            } else {
                view.layoutManager = GridLayoutManager(context, mColumnCount)
            }
            daySchedule?.let {
                view.adapter = SchoolDaysAdapter(it)
            }

        }
        return view
    }

    companion object {
        @JvmStatic
        private val ARG_COLUMN_COUNT = "column-count"
        @JvmStatic
        private val ARG_DAY_SCHEDULE = "daySchedule"

        fun newInstance(daySchedule: SchoolDaySchedule): SchoolDayFragment {
            val fragment = SchoolDayFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, 1)
            args.putSerializable(ARG_DAY_SCHEDULE, daySchedule)
            fragment.arguments = args
            return fragment
        }
    }
}
