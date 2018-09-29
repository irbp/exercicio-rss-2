package br.ufpe.cin.if710.rss.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.if710.rss.R
import br.ufpe.cin.if710.rss.db.SQLiteRSSHelper
import br.ufpe.cin.if710.rss.models.ItemRSS
import kotlinx.android.synthetic.main.itemlista.view.*
import org.jetbrains.anko.doAsync

class RssListAdapter(private val rssList: List<ItemRSS>,
                     private val context: Context) : Adapter<RssListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflando o layout de um item do recycler view
        val view = LayoutInflater.from(context)
                .inflate(R.layout.itemlista, parent,false)
        return ViewHolder(view)
    }

    // obtendo o número total de elementos da lista
    override fun getItemCount(): Int = rssList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemRss = rssList[position]
        // fazendo o bind da view com os valores
        holder.bind(itemRss)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(itemRss: ItemRSS) {
            val title = itemView.item_titulo
            val date = itemView.item_data

            // preenchendo os campos de título e data da view
            title.text = itemRss.title
            date.text = itemRss.pubDate

            // clicando no título será feito um intent implícito para abrir o link no browser
            title.setOnClickListener {
                val db = SQLiteRSSHelper.getInstance(context)
                // marcando o item como lido no bd
                doAsync {
                    db.markAsRead(itemRss.link)
                }

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(itemRss.link))
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

                context.startActivity(intent)
            }
        }
    }

}