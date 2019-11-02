package pl.kopsoft.pczplan.adapters;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.kopsoft.pczplan.R;
import pl.kopsoft.pczplan.models.SchoolDaySchedule;
import pl.kopsoft.pczplan.models.Subject;

public class SchoolDaysAdapter extends RecyclerView.Adapter<SchoolDaysAdapter.ViewHolder> {

    private final SchoolDaySchedule mValues;

    public SchoolDaysAdapter(SchoolDaySchedule items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_schoolday_subject_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Subject subject = mValues.Subjects.get(position);
        if (subject.Type != null) {
            switch (subject.Type) {
                case Laboratory:
                    holder.SubjectType.setText("Labka");
                    holder.SubjectLayout.setBackgroundColor(Color.parseColor("#a5d8ff"));
                    break;
                case Exercise:
                    holder.SubjectType.setText("Ćw");
                    holder.SubjectLayout.setBackgroundColor(Color.parseColor("#a5d8ff"));
                    break;
                case Lecture:
                    holder.SubjectType.setText("Wykład");
                    holder.SubjectLayout.setBackgroundColor(Color.parseColor("#b1dda6"));
                    break;
                case Freiheit:
                    holder.SubjectType.setText("");
                    holder.SubjectLayout.setBackgroundColor(Color.parseColor("#a5d8ff"));
                    break;
                case Gap:
                    holder.SubjectType.setText("Okienko");
                    holder.SubjectName.setText("Przykro mi");
                    break;
            }
        }
        holder.StartHour.setText(subject.HourStart);
        holder.SubjectName.setText(subject.SubjectName);
        holder.SubjectTeacher.setText(subject.Teacher);
        holder.SubjectRoom.setText(subject.Room);
    }

    @Override
    public int getItemCount() {
        return mValues.Subjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout SubjectLayout;
        final TextView SubjectType;
        final TextView StartHour;
        final TextView SubjectName;
        final TextView SubjectTeacher;
        final TextView SubjectRoom;

        public ViewHolder(View view) {
            super(view);
            SubjectLayout = view.findViewById(R.id.row_container);
            SubjectType = view.findViewById(R.id.subject_type);
            StartHour = view.findViewById(R.id.subject_duration);
            SubjectName = view.findViewById(R.id.group_name);
            SubjectTeacher = view.findViewById(R.id.subject_teacher);
            SubjectRoom = view.findViewById(R.id.subject_room);
        }
    }
}
