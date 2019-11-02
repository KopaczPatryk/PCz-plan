package pl.kopsoft.pczplan.core.group;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.kopsoft.pczplan.LinkHelper;
import pl.kopsoft.pczplan.R;
import pl.kopsoft.pczplan.RecyclerViewClickListener;
import pl.kopsoft.pczplan.adapters.GroupsAdapter;
import pl.kopsoft.pczplan.core.schedule.ScheduleActivity;
import pl.kopsoft.pczplan.models.Group;
import pl.kopsoft.pczplan.models.SchoolDaySchedule;
import pl.kopsoft.pczplan.models.SchoolWeekSchedule;
import pl.kopsoft.pczplan.models.Semester;
import pl.kopsoft.pczplan.models.Subject;
import pl.kopsoft.pczplan.models.SubjectType;

public class GroupsActivity extends AppCompatActivity implements GetGroupsListener, GetSchoolWeekListener, RecyclerViewClickListener {
    public static final String TERM_BUNDLE_ID = "hyperlinksuffix";
    private RecyclerView GroupsRecyclerView;
    private List<Group> RetrievedGroups;
    private pl.kopsoft.pczplan.models.Semester Semester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        GroupsRecyclerView = findViewById(R.id.groups_recycler);
        if (savedInstanceState != null) {
            Semester = (Semester) savedInstanceState.getSerializable(TERM_BUNDLE_ID);
        } else {
            Semester = (Semester) getIntent().getSerializableExtra(TERM_BUNDLE_ID);
        }
        assert Semester != null;
        String hyperlink = LinkHelper.DOMAIN.concat(Semester.HyperLink);
        new GetGroups(this).execute(hyperlink);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(TERM_BUNDLE_ID, Semester);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Semester = (Semester) savedInstanceState.getSerializable(TERM_BUNDLE_ID);
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public void OnGroupsGet(List<Group> groups) {
        Collections.sort(groups, new Comparator<Group>() {
            @Override
            public int compare(Group o1, Group o2) {
                return o1.GroupName.compareToIgnoreCase(o2.GroupName);
            }
        });
        List<Group> processedGroups = new ArrayList<>();
        for (int i = 0; i < groups.size(); i++) {
            if (!groups.get(i).GroupName.isEmpty()) {
                processedGroups.add(groups.get(i));
            }
        }
        RetrievedGroups = processedGroups;
        GroupsRecyclerView.setAdapter(new GroupsAdapter(processedGroups, this));
    }

    @Override
    public void OnRecyclerItemClick(View view, int position) {
        Group g = RetrievedGroups.get(position);
        String fullLink;
        if (Semester.IsStationary) {
            fullLink = LinkHelper.STATIONARY_TTS + "/" + RetrievedGroups.get(position).Hyperlink;
        } else {
            fullLink = LinkHelper.NONSTATIONARY_TTS + "/" + RetrievedGroups.get(position).Hyperlink;
        }
        new GetSchoolWeekSchedule(this, Semester.IsStationary).execute(fullLink);
//        Toast.makeText(this, RetrievedGroups.get(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnSchoolWeekReceived(SchoolWeekSchedule weekSchedule) {
        weekSchedule.TrimAll();
        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra(ScheduleActivity.ARG_WEEKSCHEDULE, weekSchedule);
        startActivity(intent);
    }

    public static class GetGroups extends AsyncTask<String, Void, List<Group>> {
        private GetGroupsListener listener;

        public GetGroups(GetGroupsListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Group> doInBackground(String... hyperlinks) {
            Document document = null;
            try {
                document = Jsoup.connect(hyperlinks[0]).timeout(30000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert document != null;
            Elements elements = document.select("td > a");
            List<Group> groups = new ArrayList<>();
            for (Element element : elements) {
                Group g = new Group();
                g.GroupName = element.text();
                g.Hyperlink = element.attr("href");
                groups.add(g);
            }
            return groups;
        }

        @Override
        protected void onPostExecute(List<Group> subjects) {
            super.onPostExecute(subjects);
            listener.OnGroupsGet(subjects);
        }
    }

    public static class GetSchoolWeekSchedule extends AsyncTask<String, Void, SchoolWeekSchedule> {
        private GetSchoolWeekListener listener;
        private boolean IsStatinary;

        public GetSchoolWeekSchedule(GetSchoolWeekListener listener, boolean stationary) {
            this.listener = listener;
            IsStatinary = stationary;
        }

        @Override
        protected SchoolWeekSchedule doInBackground(String... strings) {
            Document document = null;
            try {
                document = Jsoup.connect(strings[0]).timeout(5000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert document != null;
            Elements rows = document.select("tbody > tr");
            SchoolWeekSchedule week = new SchoolWeekSchedule();

            for (int x = 1; x < rows.get(0).children().size(); x++) {
                SchoolDaySchedule day = new SchoolDaySchedule();
                if (IsStatinary) {
                    day.DayOfWeek = rows.get(0).child(x).text().substring(0, 3);
                } else {
                    day.DayOfWeek = rows.get(0).child(x).html().split("<br>")[0] + " " + rows.get(0).child(x).html().split("<br>")[1].substring(0, 3);
                }
                week.DaySchedules.add(day);
                for (int y = 1; y < rows.size(); y++) {
                    Subject s = new Subject();
                    day.Subjects.add(s);

                    String hour = rows.get(y).child(0).text();
                    Log.i("scraped", hour);
                    String[] split = hour.split(" ");
                    s.HourStart = split[1].split("-")[0];

                    //subject name
                    String cellText = rows.get(y).child(x).html();
                    if (!cellText.equals("&nbsp;")) {

                        //split("[\\w.]+\\.");
                        String[] cellSplit = cellText.split("<br>");
                        cellSplit[0] = cellSplit[0].trim();
                        if (cellSplit[0].toLowerCase().contains("wyk") || cellSplit[0].toLowerCase().contains("lab") || cellSplit[0].toLowerCase().contains("cw")) {
                            String subjectName = cellSplit[0].substring(0, cellSplit[0].lastIndexOf(' '));
                            s.SubjectName = subjectName.trim();
                        } else {
                            String subjectName = cellSplit[0];
                            s.SubjectName = subjectName.trim();
                        }

                        if (cellSplit[0].toLowerCase().contains("wyk")) {
                            s.Type = SubjectType.Lecture;
                        } else if (cellSplit[0].toLowerCase().contains("lab")) {
                            s.Type = SubjectType.Laboratory;
                        } else if (cellSplit[0].toLowerCase().contains("ćw")) {
                            s.Type = SubjectType.Exercise;
                        }
                        if (cellSplit.length > 1) {
                            s.Teacher = cellSplit[1];
                        }
                        if (cellSplit.length > 2) {
                            s.Room = cellSplit[cellSplit.length - 1];
                        }
                    }

                }
            }
            return week;
        }

        @Override
        protected void onPostExecute(SchoolWeekSchedule schoolWeekSchedule) {
            super.onPostExecute(schoolWeekSchedule);
            listener.OnSchoolWeekReceived(schoolWeekSchedule);
        }
    }
}
