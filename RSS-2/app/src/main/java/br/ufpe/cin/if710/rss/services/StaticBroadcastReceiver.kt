package br.ufpe.cin.if710.rss.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import br.ufpe.cin.if710.rss.R
import br.ufpe.cin.if710.rss.activities.MainActivity

class StaticBroadcastReceiver : BroadcastReceiver() {

    companion object { const val CHANNEL_ID = "12345" }

    override fun onReceive(context: Context, intent: Intent) {
        // Se a aplicação estiver em segundo plano uma notificação será exibida
        // Ao clicar na notificação o usuário é direcionado à MainActivity exibindo o
        // feed das notícias não lidas
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

            val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val notificationChannel = NotificationChannel(CHANNEL_ID, "RSS Notification", importance)
                notificationChannel.also {
                    it.enableLights(true)
                    it.lightColor = Color.RED
                    it.enableVibration(true)
                    mNotificationManager.createNotificationChannel(it)
                }
            }
            mNotificationManager.notify(0, mBuilder.build())
        }
    }
}
