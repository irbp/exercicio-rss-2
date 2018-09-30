package br.ufpe.cin.if710.rss.services

import android.app.IntentService
import android.content.Intent
import br.ufpe.cin.if710.rss.db.SQLiteRSSHelper
import br.ufpe.cin.if710.rss.models.ItemRSS
import br.ufpe.cin.if710.rss.utils.ParserRSS

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
class RSSService : IntentService("RSSService") {

    override fun onHandleIntent(intent: Intent?) {
        val rssUrl = intent?.getStringExtra("rssUrl")
        val feedXML = ParserRSS.getRssFeed(rssUrl!!)
        val itemsRss = ParserRSS.parse(feedXML)
        saveInDatabase(itemsRss)
        Intent().also {
            it.action = "$packageName.RSS_FEED"
            sendBroadcast(it)
        }
    }

    private fun saveInDatabase(itemsRss: List<ItemRSS>) {
        val db = SQLiteRSSHelper.getInstance(this)

        itemsRss.forEach { if (db.getItemRSS(it.link) == null) db.insertItem(it) }
    }

}
