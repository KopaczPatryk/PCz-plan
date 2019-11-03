package pl.kopsoft.pczplan.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_label.view.*
import pl.kopsoft.pczplan.R
import pl.kopsoft.pczplan.interfaces.RecyclerViewClickListener
import pl.kopsoft.pczplan.models.Group

class GroupsAdapter(private val groups: List<Group>, private var clickListener: RecyclerViewClickListener?) : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_label, viewGroup, false)
        return ViewHolder(v, clickListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemView.row_label_textview.text = groups[i].groupName
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    class ViewHolder(itemView: View, private val listener: RecyclerViewClickListener?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            listener?.onRecyclerItemClick(v, adapterPosition)
        }
    }
}
