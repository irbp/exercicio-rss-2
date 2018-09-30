package br.ufpe.cin.if710.rss.activities

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import br.ufpe.cin.if710.rss.R
import br.ufpe.cin.if710.rss.services.DynamicBroadcastReceiver
import br.ufpe.cin.if710.rss.services.RSSService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private lateinit var RSS_FEED: String
    private lateinit var dynamicBroadcastReceiver: DynamicBroadcastReceiver

    companion object { var isActivityVisible = false }

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

        // Instanciando o broadcast dinâmico
        dynamicBroadcastReceiver = DynamicBroadcastReceiver(conteudoRSS)
    }

    override fun onResume() {
        super.onResume()
        isActivityVisible = true

        // Registrando um broadcast receiver dinâmico
        val intentFilter = IntentFilter("$packageName.RSS_FEED")
        registerReceiver(dynamicBroadcastReceiver, intentFilter)

        refreshContent()
    }

    override fun onPause() {
        super.onPause()
        isActivityVisible = false

        // Desregistrando o receiver dinâmico
        unregisterReceiver(dynamicBroadcastReceiver)
    }

    // Infla as opções do menu da MainActivity
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Executa uma ação dependendo do menu escolhido
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            // Muda para a acitvity que contem um PreferenceFragment
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

        // Inicia um IntentService para fazer o download e o parsing do feed rss
        val rssServiceIntent = Intent(this, RSSService::class.java)
        rssServiceIntent.putExtra("rssUrl", RSS_FEED)
        startService(rssServiceIntent)
    }
}
