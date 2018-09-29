package br.ufpe.cin.if710.rss.db

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns


object RssProviderContract {

    val _ID = BaseColumns._ID
    val TITLE = "title"
    val DATE = "pubDate"
    val DESCRIPTION = "description"
    val LINK = "guid"
    val UNREAD = "unread"
    val ITEMS_TABLE = "items"


    val ALL_COLUMNS = arrayOf(_ID, TITLE, DATE, DESCRIPTION, LINK, UNREAD)

    private val BASE_RSS_URI = Uri.parse("content://br.ufpe.cin.if710.rss/")
    //URI para tabela
    val ITEMS_LIST_URI = Uri.withAppendedPath(BASE_RSS_URI, ITEMS_TABLE)

    // Mime type para colecao de itens
    val CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/RssProvider.data.text"

    // Mime type para um item especifico
    val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/RssProvider.data.text"

}
