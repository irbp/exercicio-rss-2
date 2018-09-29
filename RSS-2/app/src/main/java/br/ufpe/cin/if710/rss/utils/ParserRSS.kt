package br.ufpe.cin.if710.rss.utils

import br.ufpe.cin.if710.rss.models.ItemRSS
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory

import java.io.IOException
import java.io.StringReader
import java.util.ArrayList

object ParserRSS {

    //Se quiser, teste primeiro com o parser simples para exibir lista de titulos - sem informacao de link
    @Throws(XmlPullParserException::class, IOException::class)
    fun parserSimples(rssFeed: String): List<String> {
        // pegando instancia da XmlPullParserFactory [singleton]
        val factory = XmlPullParserFactory.newInstance()
        // criando novo objeto do tipo XmlPullParser
        val parser = factory.newPullParser()
        // Definindo a entrada do nosso parser - argumento passado como parametro
        parser.setInput(StringReader(rssFeed))
        // Definindo retorno
        val items = ArrayList<String>()

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG) {
                val tag = parser.name
                //delimitando que estamos apenas interessados em tags <item>
                if (tag == "item") {
                    var title = ""
                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.eventType == XmlPullParser.START_TAG) {
                            val tagAberta = parser.name
                            //pegando as tags <title>
                            if (tagAberta == "title") {
                                title = parser.nextText()
                                items.add(title)
                            } else {
                                parser.next()
                            }
                            parser.nextTag()
                        }
                    }
                }
            }
        }
        return items
    }

    //Este metodo faz o parsing de RSS gerando objetos ItemRSS
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(rssFeed: String): List<ItemRSS> {
        val factory = XmlPullParserFactory.newInstance()
        val xpp = factory.newPullParser()
        xpp.setInput(StringReader(rssFeed))
        xpp.nextTag()
        return readRss(xpp)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun readRss(parser: XmlPullParser): List<ItemRSS> {
        val items = ArrayList<ItemRSS>()
        parser.require(XmlPullParser.START_TAG, null, "rss")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            if (name == "channel") {
                items.addAll(readChannel(parser))
            } else {
                skip(parser)
            }
        }
        return items
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun readChannel(parser: XmlPullParser): List<ItemRSS> {
        val items = ArrayList<ItemRSS>()
        parser.require(XmlPullParser.START_TAG, null, "channel")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            if (name == "item") {
                items.add(readItem(parser))
            } else {
                skip(parser)
            }
        }
        return items
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun readItem(parser: XmlPullParser): ItemRSS {
        var title: String? = null
        var link: String? = null
        var pubDate: String? = null
        var description: String? = null
        parser.require(XmlPullParser.START_TAG, null, "item")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            if (name == "title") {
                title = readData(parser, "title")
            } else if (name == "link") {
                link = readData(parser, "link")
            } else if (name == "pubDate") {
                pubDate = readData(parser, "pubDate")
            } else if (name == "description") {
                description = readData(parser, "description")
            } else {
                skip(parser)
            }
        }
        return ItemRSS(title!!, link!!, pubDate!!, description!!)
    }

    // Processa tags de forma parametrizada no feed.
    @Throws(IOException::class, XmlPullParserException::class)
    fun readData(parser: XmlPullParser, tag: String): String {
        parser.require(XmlPullParser.START_TAG, null, tag)
        val data = readText(parser)
        parser.require(XmlPullParser.END_TAG, null, tag)
        return data
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

    /**/

}
