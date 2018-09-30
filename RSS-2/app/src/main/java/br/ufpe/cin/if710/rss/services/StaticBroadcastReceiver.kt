package br.ufpe.cin.if710.rss.services

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import br.ufpe.cin.if710.rss.R
import br.ufpe.cin.if710.rss.activities.MainActivity

class StaticBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "br.ufpe.cin.if710.rss.FEED_CHANNEL"
        const val ID = 12345
    }

    override fun onReceive(context: Context, intent: Intent) {

        if (!MainActivity.isActivityVisible) {
            val mIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, mIntent,
                    0)
            val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_rss_notification)
                    .setContentTitle("Novas Notícias")
                    .setContentText("Seu feed RSS acaba de receber novas notícias. Clique aqui!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                notify(ID, mBuilder.build())
            }
        }
    }
}
