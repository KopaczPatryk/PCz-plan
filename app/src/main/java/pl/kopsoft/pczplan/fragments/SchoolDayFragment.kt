package pl.kopsoft.pczplan.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_schoolday_list.*
import pl.kopsoft.pczplan.R
import pl.kopsoft.pczplan.adapters.SchoolDaysAdapter
import pl.kopsoft.pczplan.models.SchoolDaySchedule

class SchoolDayFragment : Fragment() {
    private var daySchedule: SchoolDaySchedule? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            daySchedule = it.getSerializable(ARG_DAY_SCHEDULE) as SchoolDaySchedule?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schoolday_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        daySchedule?.let {
            if (!it.isFreeDay()) {
                happy_face_label.visibility = View.GONE
                schoolday_recycler.adapter = SchoolDaysAdapter(it)
            } else {
                happy_face_label.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        @JvmStatic
        private val ARG_DAY_SCHEDULE = "daySchedule"

        @JvmStatic
        fun newInstance(daySchedule: SchoolDaySchedule): SchoolDayFragment {
            val fragment = SchoolDayFragment()
            val args = Bundle()
            args.putSerializable(ARG_DAY_SCHEDULE, daySchedule)
            fragment.arguments = args
            return fragment
        }
    }
}
