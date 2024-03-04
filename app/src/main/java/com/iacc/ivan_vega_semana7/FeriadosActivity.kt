package com.iacc.ivan_vega_semana7

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class FeriadosActivity : AppCompatActivity() {

    private lateinit var textViewUsername: TextView
    private lateinit var listViewFeriados: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var buttonCerrarSesion: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feriados)
        buttonCerrarSesion = findViewById(R.id.buttonCerrarSesion)

        textViewUsername = findViewById(R.id.textViewUsername)
        listViewFeriados = findViewById(R.id.listViewFeriados)
        progressBar = findViewById(R.id.progressBar)

        // Ocultar la lista de feriados y mostrar el indicador de progreso
        listViewFeriados.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        // Obtener el nombre de usuario en sesión desde SharedPreferences
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")

        // Mostrar el nombre de usuario en sesión
        "Bienvenido $username".also { textViewUsername.text = it }

        // Cargar la lista de feriados desde la API
        cargarFeriadosDesdeAPI()

        buttonCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun cargarFeriadosDesdeAPI() {
        val url = "https://apis.digital.gob.cl/fl/feriados/2023"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val feriadosList = mutableListOf<String>()
                    for (i in 0 until response.length()) {
                        val feriado = response.getJSONObject(i)
                        val nombre = feriado.getString("nombre")
                        val fecha = feriado.getString("fecha")
                        feriadosList.add("$fecha : $nombre")
                    }
                    // Actualizar la lista de feriados en el ListView
                    val adapter =
                        ArrayAdapter(this, android.R.layout.simple_list_item_1, feriadosList)
                    listViewFeriados.adapter = adapter

                    // Mostrar la lista de feriados y ocultar el indicador de progreso
                    listViewFeriados.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            })

        // Agregar la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(jsonArrayRequest)
    }

    private fun cerrarSesion() {
        // Borrar el usuario en sesión de SharedPreferences
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // Redirigir al usuario a la actividad de inicio de sesión
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}