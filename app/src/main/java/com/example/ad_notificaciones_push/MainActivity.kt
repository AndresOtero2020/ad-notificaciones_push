package com.example.ad_notificaciones_push

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import android.annotation.SuppressLint

import androidx.activity.enableEdgeToEdge

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat




class MainActivity : AppCompatActivity() {
    private lateinit var etToken: TextView
    private lateinit var btnObtenerToken: Button
    private lateinit var btnCopiarToken: Button


    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
       /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets*/
        etToken = findViewById(R.id.etToken)
        btnObtenerToken = findViewById(R.id.btnObtenerToken)
        btnCopiarToken = findViewById(R.id.btnCopiarToken)

        /*EMPIEZA EL IF */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions( this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }/*-------TERMINA LE IF*/

        btnObtenerToken.setOnClickListener {
            try {
                FirebaseMessaging.getInstance().token.addOnCompleteListener { tarea ->
                    if(tarea.isSuccessful){
                        val token = tarea.result
                        etToken.text = token
                    }/*finaliza el if*/
                    else {
                        etToken.text = "Error al obtener el token: ${tarea.exception?.message}"
                        Log.e("FCM", "Error al obtener el token", tarea.exception)
                    }/*finaliza el else*/

                }
            }
            catch (e: Exception)        {
                etToken.text="EXCEPCION:${e.message}"
                Log.e("FCM", "Error al obtener el token", e)
            }

        }
        /*finaliza el obtner token */

        btnCopiarToken.setOnClickListener {
            val token = etToken.text.toString()
            if(token.isNotEmpty() && token != "TOKEN GENERADO" && !token.startsWith("Error:") && !token.startsWith("EXCEPCION:"))
                {
                val portapapeles = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
                    val clipData = ClipData.newPlainText("Token FCM", token)
                    portapapeles.setPrimaryClip(clipData)
                    Toast.makeText(this, "Token Copiado al portapales", Toast.LENGTH_SHORT).show()
                }
            else {
                Toast.makeText(this, "Primero Obten un token valido ", Toast.LENGTH_SHORT).show()
               // textoTokenGenerado.text = "No hay token para copiar"
            }
            /*finaliza el else*/


        }
        // 🔹 Manejar la respuesta de los permisos
         fun onRequestPermissionsResult(
            codigoSolicitud: Int,
            permisos: Array<out String>,
            resultados: IntArray
        )

        {
          super.onRequestPermissionsResult(codigoSolicitud, permisos, resultados)
            if(codigoSolicitud == 1 )
            {
                if (resultados.isNotEmpty() && resultados[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("FCM", "Permiso de notificacion concedido por el usuario")
                    Log.d("FCM", "✅ Permiso de notificaciones concedido")
                }
                else {
                    Log.d("FCM", "Permiso de notoficacion denegado por el usuario")
                    Log.e("FCM", "❌ Permiso de notificaciones denegado")
                }


            }


        }


    }

}


