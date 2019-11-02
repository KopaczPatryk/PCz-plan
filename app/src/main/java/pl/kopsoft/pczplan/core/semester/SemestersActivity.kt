package pl.kopsoft.pczplan.core.semester

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import pl.kopsoft.pczplan.R
import pl.kopsoft.pczplan.RecyclerViewClickListener
import pl.kopsoft.pczplan.adapters.SemestersAdapter
import pl.kopsoft.pczplan.core.group.GroupsActivity
import pl.kopsoft.pczplan.models.Semester
import java.io.IOException


class SemestersActivity : AppCompatActivity(), GetSemestersListener, RecyclerViewClickListener {
    private var termList: RecyclerView? = null
    private var yearSemesters: List<Semester>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        termList = findViewById(R.id.termSelect)
        GetTerms(this).execute()
    }

    override fun OnSemestersGet(semesters: List<Semester>) {
        yearSemesters = semesters
        termList!!.adapter = SemestersAdapter(semesters, this)
    }

    override fun OnRecyclerItemClick(view: View, position: Int) {
        val intent = Intent(this, GroupsActivity::class.java)
        intent.putExtra(GroupsActivity.TERM_BUNDLE_ID, yearSemesters!![position])
        startActivity(intent)
    }

    internal class GetTerms(private val listener: GetSemestersListener) :
        AsyncTask<Void, Void, List<Semester>>() {

        override fun doInBackground(vararg voids: Void): List<Semester> {
            var document: Document? = null
            try {
                document = Jsoup.connect("https://wimii.pcz.pl/pl/plan-zajec").timeout(5000).get()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val e = document!!.select(".field-item > p")
            //Log.d("jsoup", e.html());
            val semesters = ArrayList<Semester>()
            for (elem in e) {
                if (!elem.text().contains("nauczycie")) {
                    //Log.d("jsoup", elem.text());

                    val t = Semester()
                    t.TermName = elem.text()
                    t.HyperLink = elem.select("a").attr("href")

                    t.IsStationary = !t.HyperLink.toLowerCase().contains("niesta")

                    Log.d("scraper", t.HyperLink)
                    semesters.add(t)
                }
            }
            return semesters
        }

        override fun onPostExecute(semesters: List<Semester>) {
            super.onPostExecute(semesters)
            listener.OnSemestersGet(semesters)
        }
    }
}

