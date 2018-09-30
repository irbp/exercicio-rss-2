package br.ufpe.cin.if710.rss.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import br.ufpe.cin.if710.rss.adapters.RssListAdapter
import br.ufpe.cin.if710.rss.db.SQLiteRSSHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DynamicBroadcastReceiver(val conteudoRSS: RecyclerView) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val db = SQLiteRSSHelper.getInstance(context)

        // Obtém do bd os items que ainda não foram clicados e exibe utilizando o adapter
        doAsync {
            val itemsRss = db.getAllUnreadItems()
            uiThread { conteudoRSS.adapter = RssListAdapter(itemsRss, context) }
        }
    }
}