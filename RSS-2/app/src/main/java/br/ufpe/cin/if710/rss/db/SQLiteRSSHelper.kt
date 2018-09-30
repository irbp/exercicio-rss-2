package br.ufpe.cin.if710.rss.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import br.ufpe.cin.if710.rss.models.ItemRSS


class SQLiteRSSHelper private constructor(
        private var c: Context) : SQLiteOpenHelper(c, DATABASE_NAME, null, DB_VERSION) {

    companion object {
        //Nome do Banco de Dados
        private val DATABASE_NAME = "rss"
        //Nome da tabela do Banco a ser usada
        val DATABASE_TABLE = "items"
        //Versão atual do banco
        private val DB_VERSION = 1

        private var db: SQLiteRSSHelper? = null

        //Definindo Singleton
        @Synchronized
        fun getInstance(c: Context): SQLiteRSSHelper {
            if (db == null) {
                db = SQLiteRSSHelper(c.applicationContext)
            }
            return db!!
        }

        //Definindo constantes que representam os campos do banco de dados
        val ITEM_ROWID = RssProviderContract._ID
        val ITEM_TITLE = RssProviderContract.TITLE
        val ITEM_DATE = RssProviderContract.DATE
        val ITEM_DESC = RssProviderContract.DESCRIPTION
        val ITEM_LINK = RssProviderContract.LINK
        val ITEM_UNREAD = RssProviderContract.UNREAD

        //Definindo constante que representa um array com todos os campos
        val columns = arrayOf<String>(
                ITEM_ROWID,ITEM_TITLE, ITEM_DATE, ITEM_DESC, ITEM_LINK, ITEM_UNREAD)

        //Definindo constante que representa o comando de criação da tabela no banco de dados
        private val CREATE_DB_COMMAND = "CREATE TABLE " + DATABASE_TABLE + " (" +
                ITEM_ROWID + " integer primary key autoincrement, " +
                ITEM_TITLE + " text not null, " +
                ITEM_DATE + " text not null, " +
                ITEM_DESC + " text not null, " +
                ITEM_LINK + " text not null, " +
                ITEM_UNREAD + " boolean not null);"
    }

    val items: Cursor
        @Throws(SQLException::class)
        get() {
            val db = readableDatabase
            val projection = columns
            val selection = "$ITEM_UNREAD = ?"
            val selectionArgs = arrayOf("1")

            return db.query(DATABASE_TABLE, projection, selection, selectionArgs, null,
                    null, null)
        }

    override fun onCreate(db: SQLiteDatabase) {
        //Executa o comando de criação de tabela
        db.execSQL(CREATE_DB_COMMAND)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //estamos ignorando esta possibilidade no momento
        throw RuntimeException("nao se aplica")
    }

    fun insertItem(item: ItemRSS): Long {
        return insertItem(item.title, item.pubDate, item.description, item.link)
    }

    // Inserindo um novo item na tabela e marcando ele como não lido
    fun insertItem(title: String, pubDate: String, description: String, link: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(ITEM_TITLE, title)
            put(ITEM_DATE, pubDate)
            put(ITEM_DESC, description)
            put(ITEM_LINK, link)
            put(ITEM_UNREAD, true)
        }

        Log.d("SERVICE", "Inserting $title")

        return db.insert(DATABASE_TABLE, null, values)
    }

    // Obtendo um item específico da tabela, utilizando como parâmetro o seu link
    @Throws(SQLException::class)
    fun getItemRSS(link: String): ItemRSS? {
        var itemRSS: ItemRSS? = null
        val db = readableDatabase
        val projection = arrayOf(ITEM_TITLE, ITEM_DATE, ITEM_DESC, ITEM_LINK)
        val selection = "$ITEM_LINK = ?"
        val selectionArgs = arrayOf(link)
        val cursor = db.query(DATABASE_TABLE, projection, selection, selectionArgs,
                null,null, null)

        with(cursor) {
            if (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(ITEM_TITLE))
                val pubDate = getString(getColumnIndexOrThrow(ITEM_DATE))
                val description = getString(getColumnIndexOrThrow(ITEM_DESC))
                val url = getString(getColumnIndexOrThrow(ITEM_LINK))

                itemRSS = ItemRSS(title, pubDate, description, url)
            }
        }

        return itemRSS
    }

    // Função auxiliar para modificar um item da tabela como lido ou não lido
    private fun updateUnreadField(link: String, unread: Boolean): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(ITEM_UNREAD, unread)
        }
        val selection = "$ITEM_LINK = ?"
        val selectionArgs = arrayOf(link)

        return db.update(DATABASE_TABLE, values, selection, selectionArgs)
    }

    // Modifica um item da tabela marcando-o como não lido
    fun markAsUnread(link: String): Boolean {
        val count = updateUnreadField(link, true)

        return count > 0
    }

    // Modifica um item da tabela marcando-o como lido
    fun markAsRead(link: String): Boolean {
        val count = updateUnreadField(link, false)

        return count > 0
    }

    fun getAllUnreadItems(): List<ItemRSS> {
        val itemsRss: MutableList<ItemRSS> = ArrayList()

        with(items) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(ITEM_TITLE))
                val pubDate = getString(getColumnIndexOrThrow(ITEM_DATE))
                val description = getString(getColumnIndexOrThrow(ITEM_DESC))
                val url = getString(getColumnIndexOrThrow(ITEM_LINK))

                itemsRss.add(ItemRSS(title, url, pubDate, description))
            }
        }

        return itemsRss
    }
}