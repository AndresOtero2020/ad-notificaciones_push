package com.example.ad_notificaciones_push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import android.util.Log

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(mensajeRemoto: RemoteMessage) {
        super.onMessageReceived(mensajeRemoto)

        // Si el mensaje tiene una notificación
        mensajeRemoto.notification?.let {
            Log.d("FCM", "Mensaje Recibido: Titulo=${it.title}, Cuerpo=${it.body}")
            mostrarNotificacion(it.title ?: "Titulo", it.body ?: "Cuerpo")
        }

        // Si el mensaje tiene datos adicionales
        mensajeRemoto.data.isNotEmpty().let {
            val tipo = mensajeRemoto.data["tipo"]
            val message = mensajeRemoto.data["message"]
            Log.d("FCM", "Mensaje Recibido: Titulo=${tipo}, Cuerpo=${message}")
        }
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Nuevo token: $token")
    }


    private fun mostrarNotificacion(Titulo: String, Cuerpo: String) {
        val idCanal = "cana_predeterminado"
        val idNotificacion = 1

        val administradorNotificaciones = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear el canal de notificación en Android 8.0 o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                idCanal,
                "Canal Predeterminado",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            administradorNotificaciones.createNotificationChannel(canal)
        }

        // Construir la notificación
        val notificacion = NotificationCompat.Builder(this, idCanal)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(Titulo)
            .setContentText(Cuerpo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        administradorNotificaciones.notify(idNotificacion, notificacion)

    }

}