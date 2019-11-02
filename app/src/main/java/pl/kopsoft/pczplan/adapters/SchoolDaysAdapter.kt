package pl.kopsoft.pczplan.adapters


import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_schoolday_subject_row.view.*
import pl.kopsoft.pczplan.R
import pl.kopsoft.pczplan.models.SchoolDaySchedule
import pl.kopsoft.pczplan.models.SubjectType

class SchoolDaysAdapter(private val mValues: SchoolDaySchedule) :
    RecyclerView.Adapter<SchoolDaysAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_schoolday_subject_row, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subject = mValues.subjects[position]
        when (subject.type) {
            SubjectType.Laboratory -> {
                holder.subjectType.text = "Labka"
                holder.subjectLayout.setBackgroundColor(Color.parseColor("#a5d8ff"))
            }
            SubjectType.Exercise -> {
                holder.subjectType.text = "Ćw"
                holder.subjectLayout.setBackgroundColor(Color.parseColor("#a5d8ff"))
            }
            SubjectType.Lecture -> {
                holder.subjectType.text = "Wykład"
                holder.subjectLayout.setBackgroundColor(Color.parseColor("#b1dda6"))
            }
            SubjectType.Freiheit -> {
                holder.subjectType.text = ""
                holder.subjectLayout.setBackgroundColor(Color.parseColor("#a5d8ff"))
            }
            SubjectType.Gap -> {
                holder.subjectType.text = "Okienko"
                holder.subjectName.text = "Przykro mi"
            }
        }
        holder.startHour.text = subject.hourStart
        holder.subjectName.text = subject.subjectName
        holder.subjectTeacher.text = subject.teacher
        holder.subjectRoom.text = subject.room
    }

    override fun getItemCount(): Int {
        return mValues.subjects.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectLayout: LinearLayout = itemView.row_container
        val subjectType: TextView = itemView.subject_type
        val startHour: TextView = itemView.subject_duration
        val subjectName: TextView = itemView.group_name
        val subjectTeacher: TextView = itemView.subject_teacher
        val subjectRoom: TextView = itemView.subject_room
    }
}
