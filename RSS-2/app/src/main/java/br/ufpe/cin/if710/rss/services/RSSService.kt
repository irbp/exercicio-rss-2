package br.ufpe.cin.if710.rss.services

import android.app.IntentService
import android.content.Intent
import android.os.SystemClock
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import br.ufpe.cin.if710.rss.db.SQLiteRSSHelper
import br.ufpe.cin.if710.rss.models.ItemRSS
import br.ufpe.cin.if710.rss.utils.ParserRSS

class RSSService : IntentService("RSSService") {

    private var notify = false

    override fun onHandleIntent(intent: Intent?) {
        val rssUrl = intent?.getStringExtra("rssUrl")
        val feedXML = ParserRSS.getRssFeed(rssUrl!!)
        val itemsRss = ParserRSS.parse(feedXML)
        saveInDatabase(itemsRss, rssUrl)

        sendBroadcast(Intent("$packageName.RSS_FEED"))
        if (notify) sendBroadcast(Intent(this,
                StaticBroadcastReceiver::class.java))
    }

    private fun saveInDatabase(itemsRss: List<ItemRSS>, rssUrl: String) {
        val db = SQLiteRSSHelper.getInstance(this)
        SQLiteRSSHelper.currentRssUrl = rssUrl

        itemsRss.forEach {
            if (db.getItemRSS(it.link) == null) {
                db.insertItem(it, rssUrl)
                notify = true
            }
        }
    }

}
