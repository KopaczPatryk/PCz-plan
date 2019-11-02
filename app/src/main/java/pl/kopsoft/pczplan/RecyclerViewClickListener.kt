package pl.kopsoft.pczplan


import android.view.View

interface RecyclerViewClickListener {
    fun onRecyclerItemClick(view: View, position: Int)
}
