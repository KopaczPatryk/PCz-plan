package pl.kopsoft.pczplan.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_semester.view.*

import pl.kopsoft.pczplan.R
import pl.kopsoft.pczplan.interfaces.RecyclerViewClickListener
import pl.kopsoft.pczplan.models.Semester

class SemestersAdapter(
        private val semesters: List<Semester>,
        private var clickListener: RecyclerViewClickListener?) : RecyclerView.Adapter<SemestersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_semester, viewGroup, false)

        return ViewHolder(itemView, clickListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.termName.text = semesters[i].termName
    }

    override fun getItemCount(): Int {
        return semesters.size
    }

    inner class ViewHolder(itemView: View, private val listener: RecyclerViewClickListener?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val termName: TextView = itemView.term_name

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            listener?.onRecyclerItemClick(v, adapterPosition)
        }
    }
}
