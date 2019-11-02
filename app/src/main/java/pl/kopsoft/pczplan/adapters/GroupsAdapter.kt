package pl.kopsoft.pczplan.adapters


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.group_row.view.*
import pl.kopsoft.pczplan.R
import pl.kopsoft.pczplan.RecyclerViewClickListener
import pl.kopsoft.pczplan.models.Group

class GroupsAdapter(private val groups: List<Group>, private var clickListener: RecyclerViewClickListener?) : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = View.inflate(viewGroup.context, R.layout.group_row, null)
//        val v = View.inflate(viewGroup.context, R.layout.group_row, viewGroup)
//        LayoutInflater.from(viewGroup.context).inflate(R.layout.group_row, viewGroup, false)

        return ViewHolder(v, clickListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemView.group_name.text = groups[i].groupName
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
