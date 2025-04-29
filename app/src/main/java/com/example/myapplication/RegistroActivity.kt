package com.example.myapplication


// Android framework imports
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// Retrofit imports
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Local project imports
import com.example.myapplication.Network.RetrofitClient
import com.example.myapplication.models.Usuario

class RegistroActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etPassword: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicializar las vistas (asegúrate de actualizar los IDs en tu layout XML)
        etNombre = findViewById(R.id.etNombre)
        etPassword = findViewById(R.id.etPassword)
        etEmail = findViewById(R.id.etEmail)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val nombre = etNombre.text.toString()
            val contrasena = etPassword.text.toString()
            val correo = etEmail.text.toString()

            if (nombre.isNotEmpty() && contrasena.isNotEmpty() && correo.isNotEmpty()) {
                registrarUsuario(nombre, contrasena, correo)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarUsuario(nombre: String, contrasena: String, correo: String) {
        val usuario = Usuario(
            nombre = nombre,
            correo = correo,
            contrasena = contrasena
        )

        val apiService = RetrofitClient.instance
        val call = apiService.registrarUsuario(usuario)

        call.enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegistroActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    // Opcional: limpiar campos después de registro exitoso
                    etNombre.text.clear()
                    etPassword.text.clear()
                    etEmail.text.clear()
                } else {
                    Toast.makeText(
                        this@RegistroActivity,
                        "Error en el registro: ${response.code()} - ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                Toast.makeText(
                    this@RegistroActivity,
                    "Error de conexión: ${t.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}