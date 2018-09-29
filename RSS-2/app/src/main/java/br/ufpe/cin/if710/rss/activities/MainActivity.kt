package br.ufpe.cin.if710.rss.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import br.ufpe.cin.if710.rss.utils.ParserRSS
import br.ufpe.cin.if710.rss.R
import br.ufpe.cin.if710.rss.adapters.RssListAdapter
import br.ufpe.cin.if710.rss.services.RSSService
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : Activity() {

    private lateinit var RSS_FEED: String

    //OUTROS LINKS PARA TESTAR...
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false)
        conteudoRSS.layoutManager = layoutManager

        swipe_layout.setOnRefreshListener { refreshContent() }
    }

    override fun onStart() {
        super.onStart()

        refreshContent()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.settings -> {
                val intent= Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshContent() {
        // obtendo a url do rss a partir da shared preference rssfeed
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        RSS_FEED = sharedPref.getString("rssfeed", getString(R.string.rssfeed))

        val rssServiceIntent = Intent(this, RSSService::class.java)
        rssServiceIntent.putExtra("rssUrl", RSS_FEED)
        startService(rssServiceIntent)
    }
}
