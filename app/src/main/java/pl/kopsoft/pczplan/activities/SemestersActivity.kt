package pl.kopsoft.pczplan.activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_terms.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import pl.kopsoft.pczplan.R
import pl.kopsoft.pczplan.interfaces.RecyclerViewClickListener
import pl.kopsoft.pczplan.adapters.SemestersAdapter
import pl.kopsoft.pczplan.interfaces.GetSemestersListener
import pl.kopsoft.pczplan.models.Semester
import java.io.IOException


class SemestersActivity : AppCompatActivity(),
    GetSemestersListener, RecyclerViewClickListener {
    private var yearSemesters: List<Semester>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        GetTerms(this).execute()
    }

    override fun onSemestersGet(semesters: List<Semester>) {
        yearSemesters = semesters
        termSelect.adapter = SemestersAdapter(semesters, this)
    }

    override fun onRecyclerItemClick(view: View, position: Int) {
        val intent = Intent(this, GroupsActivity::class.java)
        intent.putExtra(GroupsActivity.TERM_BUNDLE_ID, yearSemesters!![position])
        startActivity(intent)
    }

    internal class GetTerms(private val listener: GetSemestersListener) : AsyncTask<Void, Void, List<Semester>>() {
        override fun doInBackground(vararg voids: Void): List<Semester> {
            var document: Document? = null
            try {
                document = Jsoup.connect("https://wimii.pcz.pl/pl/plan-zajec").timeout(5000).get()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            var elements: Elements? = null

            document?.let {
                elements = it.select(".field-item > p")
                //Log.d("jsoup", e.html());
            }

            val semesters = ArrayList<Semester>()
            elements?.let {
                for (elem in it) {
                    if (!elem.text().contains("nauczycie")) {
                        //Log.d("jsoup", elem.text());
                        val link = elem.select("a").attr("href")
                        val t = Semester(
                                termName = elem.text(),
                                hyperLink = link,
                                isStationary = !link.contains("niesta")
                        )

                        Log.d("scraper", t.hyperLink)
                        semesters.add(t)
                    }
                }
            }

            return semesters
        }

        override fun onPostExecute(semesters: List<Semester>) {
            super.onPostExecute(semesters)
            listener.onSemestersGet(semesters)
        }
    }
}

