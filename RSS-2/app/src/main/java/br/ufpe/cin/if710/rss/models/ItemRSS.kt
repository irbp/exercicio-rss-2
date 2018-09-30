package br.ufpe.cin.if710.rss.models

class ItemRSS(val title: String, val link: String, val pubDate: String, val description: String) {
    
    override fun toString(): String {
        return title
    }
}
