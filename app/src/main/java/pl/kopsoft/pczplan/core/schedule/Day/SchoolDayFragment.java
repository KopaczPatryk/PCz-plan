package pl.kopsoft.pczplan.core.schedule.Day;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pl.kopsoft.pczplan.R;
import pl.kopsoft.pczplan.adapters.SchoolDaysAdapter;
import pl.kopsoft.pczplan.models.SchoolDaySchedule;

public class SchoolDayFragment extends Fragment {
    private SchoolDaySchedule DaySchedule;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_DAYSCHEDULE = "daySchedule";
    private int mColumnCount = 2;

    public SchoolDayFragment() {
    }

    public static SchoolDayFragment newInstance(SchoolDaySchedule daySchedule) {
        SchoolDayFragment fragment = new SchoolDayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 1);
        args.putSerializable(ARG_DAYSCHEDULE, daySchedule);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            DaySchedule = (SchoolDaySchedule) getArguments().getSerializable(ARG_DAYSCHEDULE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schoolday_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new SchoolDaysAdapter(DaySchedule));
        }
        return view;
    }
}
