package br.ufpe.cin.if710.rss.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.ufpe.cin.if710.rss.utils.ParserRSS
import br.ufpe.cin.if710.rss.R
import br.ufpe.cin.if710.rss.adapters.RssListAdapter
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

        // obtendo a url do rss a partir da shared preference rssfeed
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        RSS_FEED = sharedPref.getString("rssfeed", getString(R.string.rssfeed))

        try {
            refreshContent()
        } catch (e: IOException) {
            e.printStackTrace()
        }

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
        // Esse código não dá mais pau, pois estamos obtendo o xml de maneira assíncrona
        // utilizando o doAsync do anko
        doAsync {
            // obtém o xml
            val feedXML = getRssFeed(RSS_FEED)
            // faz o parsing do xml
            val itemsRss = ParserRSS.parse(feedXML)
            // populando o recycler view (na ui thread) com o que foi obtido pelo parser
            uiThread {
                conteudoRSS.adapter = RssListAdapter(itemsRss, this@MainActivity)
                swipe_layout.isRefreshing = false
            }
        }
    }

    //Opcional - pesquise outros meios de obter arquivos da internet - bibliotecas, etc.
    @Throws(IOException::class)
    private fun getRssFeed(feed: String): String {
        var inputStream: InputStream? = null
        val rssFeed: String
        try {
            val url = URL(feed)
            val conn = url.openConnection() as HttpURLConnection
            inputStream = conn.inputStream
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var count = inputStream.read(buffer)
            while (count != -1) {
                out.write(buffer, 0, count)
                count = inputStream.read(buffer)
            }
            val response = out.toByteArray()
            rssFeed = String(response, charset("UTF-8"))
        } finally {
            inputStream?.close()
        }
        return rssFeed
    }
}
