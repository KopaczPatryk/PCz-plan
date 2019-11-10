package pl.kopsoft.pczplan.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_schoolday_subject_row.view.*
import pl.kopsoft.pczplan.R
import pl.kopsoft.pczplan.models.SchoolDaySchedule
import pl.kopsoft.pczplan.models.SubjectType

class SchoolDaysAdapter(private val daySchedule: SchoolDaySchedule) :
    RecyclerView.Adapter<SchoolDaysAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_schoolday_subject_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subject = daySchedule.subjects[position]
        val colorLab = ContextCompat.getColor(holder.itemView.context, R.color.subject_lab)
        val colorLect = ContextCompat.getColor(holder.itemView.context, R.color.subject_lec)
        when (subject.type) {
            SubjectType.Laboratory -> {
                holder.subjectType.setText(R.string.subjecttype_lab)
                holder.subjectType.setBackgroundColor(colorLab)
            }
            SubjectType.Exercise -> {
                holder.subjectType.setText(R.string.subjecttype_exe)
                holder.subjectType.setBackgroundColor(colorLab)
            }
            SubjectType.Lecture -> {
                holder.subjectType.setText(R.string.subjecttype_lect)
                holder.subjectType.setBackgroundColor(colorLect)
            }
            SubjectType.Freiheit -> {
                holder.subjectType.text = ""
            }
            SubjectType.Gap -> {
                holder.subjectType.setText(R.string.subjecttype_gap)
            }
        }
        holder.startHour.text = subject.hourStart
        holder.subjectName.text = subject.subjectName
        holder.subjectTeacher.text = subject.teacher
        holder.subjectRoom.text = subject.room
    }

    override fun getItemCount(): Int {
        return daySchedule.subjects.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectLayout: ViewGroup = itemView.row_container
        val subjectType: TextView = itemView.subject_type
        val startHour: TextView = itemView.subject_duration
        val subjectName: TextView = itemView.group_name
        val subjectTeacher: TextView = itemView.subject_teacher
        val subjectRoom: TextView = itemView.subject_room
    }
}
