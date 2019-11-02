package pl.kopsoft.pczplan.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.kopsoft.pczplan.R;
import pl.kopsoft.pczplan.RecyclerViewClickListener;
import pl.kopsoft.pczplan.models.Group;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {
    private List<Group> Groups;
    private RecyclerViewClickListener clickListener;

    public GroupsAdapter(List<Group> groups, RecyclerViewClickListener listener) {
        this.Groups = groups;
        clickListener = listener;
    }

    @NonNull
    @Override
    public GroupsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.group_row, viewGroup, false);

        return new ViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.GroupName.setText(Groups.get(i).GroupName);
    }

    @Override
    public int getItemCount() {
        return Groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView GroupName;

        public ViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            clickListener = listener;
            GroupName = itemView.findViewById(R.id.group_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.OnRecyclerItemClick(v, getAdapterPosition());
        }
    }
}
