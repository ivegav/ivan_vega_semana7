package com.iacc.ivan_vega_semana7

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.iacc.ivan_vega_semana7.data.UsuariosData

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    val usuarioData = UsuariosData()

    override fun onCreate(savedInstanceState: Bundle?) {

        //se agrega una pausa solo para poder visualizar el splash screen
        Thread.sleep(6000)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        //se verifica si el usuario ya ha iniciado sesión para acceder directamente a la siguiente pantalla
        if (sharedPreferences.contains("username") && sharedPreferences.contains("password")) {
            val usuario = sharedPreferences.getString("username", "")
            val password = sharedPreferences.getString("password", "")
            if (usuarioData.validarUsuario(usuario.toString(), password.toString())) {
                Toast.makeText(this, "Usuario logueado", Toast.LENGTH_SHORT).show()
                //redirige a FeriadosActivity
                Intent(this, FeriadosActivity::class.java).also {
                    startActivity(it)
                }

            }else{
                //En caso que se hallan eliminado las credenciales y ya no sean válidas, las elimina
                //del sharedPreferences
                sharedPreferences.edit().clear().apply()
                Toast.makeText(this, "Error en los datos", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "No hay sesion iniciada", Toast.LENGTH_SHORT).show()
        }

        val btnLogin = findViewById<Button>(R.id.buttonLogin)

        btnLogin.setOnClickListener {
            login()
        }

    }

    fun login(){
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)

        val usuario = editTextUsername.text.toString()
        val password = editTextPassword.text.toString()

        //Si las credenciales ingresadas son correctas, se inserta el usuario en el sharedPreferences
        if (usuarioData.validarUsuario(usuario, password)) {
            editor.putString("username", usuario)
            editor.putString("password", password)
            editor.apply()
            Log.d("MainActivity", "Usuario logueado")
            Toast.makeText(this, "Usuario logueado", Toast.LENGTH_SHORT).show()
            Intent(this, FeriadosActivity::class.java).also {
                startActivity(it)
            }
        } else {
            //Si las credenciales ingresadas son incorrectas, muestra un toast con un mensaje de error
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", "Usuario o contraseña incorrectos")
        }
    }
}