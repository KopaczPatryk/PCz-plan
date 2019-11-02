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
import pl.kopsoft.pczplan.models.Semester;

public class SemestersAdapter extends RecyclerView.Adapter<SemestersAdapter.ViewHolder> {
    private List<Semester> semesters;
    private RecyclerViewClickListener clickListener;

    public SemestersAdapter(List<Semester> semesters, RecyclerViewClickListener listener) {
        this.semesters = semesters;
        clickListener = listener;
    }

    @NonNull
    @Override
    public SemestersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.semester_row, viewGroup, false);

        return new ViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SemestersAdapter.ViewHolder viewHolder, int i) {
        viewHolder.TermName.setText(semesters.get(i).TermName);
    }

    @Override
    public int getItemCount() {
        return semesters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView TermName;

        public ViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            clickListener = listener;
            TermName = itemView.findViewById(R.id.term_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.OnRecyclerItemClick(v, getAdapterPosition());
        }
    }
}
