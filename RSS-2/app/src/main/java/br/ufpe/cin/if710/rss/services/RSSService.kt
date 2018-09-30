package br.ufpe.cin.if710.rss.services

import android.app.IntentService
import android.content.Intent
import br.ufpe.cin.if710.rss.db.SQLiteRSSHelper
import br.ufpe.cin.if710.rss.models.ItemRSS
import br.ufpe.cin.if710.rss.utils.ParserRSS

class RSSService : IntentService("RSSService") {

    private var notify = false

    override fun onHandleIntent(intent: Intent?) {
        val rssUrl = intent?.getStringExtra("rssUrl")
        // Baixando o xml
        val feedXML = ParserRSS.getRssFeed(rssUrl!!)
        // Fazendo o parsing do xml
        val itemsRss = ParserRSS.parse(feedXML)
        // Salvando os itens no banco de dados
        saveInDatabase(itemsRss, rssUrl)

        // Envia um broadcast pra avisar à aplicação que os itens foram baixados
        sendBroadcast(Intent("$packageName.RSS_FEED"))
        // Se houver algum item novo, envia um broadcast para a notificação
        if (notify) sendBroadcast(Intent(this,
                StaticBroadcastReceiver::class.java))
    }

    private fun saveInDatabase(itemsRss: List<ItemRSS>, rssUrl: String) {
        val db = SQLiteRSSHelper.getInstance(this)
        SQLiteRSSHelper.currentRssUrl = rssUrl

        // Verifica se o item já existe no bd, caso não exista, ele será salvo e a flag de novo
        // item é setada para true
        itemsRss.forEach {
            if (db.getItemRSS(it.link) == null) {
                db.insertItem(it, rssUrl)
                notify = true
            }
        }
    }

}
