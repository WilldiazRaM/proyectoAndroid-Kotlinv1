package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Network.RetrofitClient
import com.example.myapplication.models.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GestionUsuariosActivity : AppCompatActivity() {

    private val TAG = "GestionUsuarios"
    private lateinit var etNombre: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText
    private lateinit var etIdUsuario: EditText
    private lateinit var btnCrearUsuario: Button
    private lateinit var btnActualizarUsuario: Button
    private lateinit var btnEliminarUsuario: Button
    private lateinit var btnBuscarUsuario: Button
    private lateinit var lvUsuarios: ListView
    private lateinit var progressBar: ProgressBar

    private val usuariosList = mutableListOf<Usuario>()
    private lateinit var adapter: UsuarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_gestion_usuarios)


        initViews()
        setupListView()
        setupButtons()
        cargarUsuarios()
    }

    private fun initViews() {
        etIdUsuario = findViewById(R.id.etIdUsuario)
        etNombre = findViewById(R.id.etNombre)
        etCorreo = findViewById(R.id.etCorreo)
        etContrasena = findViewById(R.id.etContrasena)
        btnCrearUsuario = findViewById(R.id.btnCrearUsuario)
        btnActualizarUsuario = findViewById(R.id.btnActualizarUsuario)
        btnEliminarUsuario = findViewById(R.id.btnEliminarUsuario)
        btnBuscarUsuario = findViewById(R.id.btnBuscarUsuario)
        lvUsuarios = findViewById(R.id.lvUsuarios)
        progressBar = findViewById(R.id.mnopressBar)
    }

    private fun setupListView() {
        adapter = UsuarioAdapter(this, usuariosList)
        lvUsuarios.adapter = adapter

        lvUsuarios.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val usuario = usuariosList[position]
            etIdUsuario.setText(usuario.id.toString())
            etNombre.setText(usuario.nombre)
            etCorreo.setText(usuario.correo)
            etContrasena.setText("") // Por seguridad no mostramos la contraseña
        }
    }

    private fun setupButtons() {
        btnCrearUsuario.setOnClickListener {
            if (validarCampos()) {
                crearUsuario()
            }
        }

        btnActualizarUsuario.setOnClickListener {
            if (validarCampos() && validarId()) {
                actualizarUsuario()
            }
        }

        btnEliminarUsuario.setOnClickListener {
            if (validarId()) {
                mostrarDialogoConfirmacion()
            }
        }

        btnBuscarUsuario.setOnClickListener {
            if (validarId()) {
                buscarUsuario()
            }
        }
    }

    private fun validarCampos(): Boolean {
        if (etNombre.text.isEmpty() || etCorreo.text.isEmpty() || etContrasena.text.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validarId(): Boolean {
        if (etIdUsuario.text.isEmpty()) {
            Toast.makeText(this, "Ingrese un ID válido", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun mostrarDialogoConfirmacion() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Está seguro que desea eliminar este usuario?")
            .setPositiveButton("Eliminar") { _, _ -> eliminarUsuario() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cargarUsuarios() {
        progressBar.visibility = View.VISIBLE
        RetrofitClient.instance.obtenerTodosUsuarios().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    usuariosList.clear()
                    response.body()?.let { usuariosList.addAll(it) }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@GestionUsuariosActivity,
                        "Error al cargar usuarios: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@GestionUsuariosActivity,
                    "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun crearUsuario() {
        val usuario = Usuario(
            nombre = etNombre.text.toString(),
            correo = etCorreo.text.toString(),
            contrasena = etContrasena.text.toString()
        )

        progressBar.visibility = View.VISIBLE
        RetrofitClient.instance.crearUsuario(usuario).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@GestionUsuariosActivity,
                        "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                    cargarUsuarios()
                } else {
                    Toast.makeText(this@GestionUsuariosActivity,
                        "Error al crear usuario: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@GestionUsuariosActivity,
                    "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun actualizarUsuario() {
        val id = etIdUsuario.text.toString().toInt()
        val usuario = Usuario(
            nombre = etNombre.text.toString(),
            correo = etCorreo.text.toString(),
            contrasena = etContrasena.text.toString()
        )

        progressBar.visibility = View.VISIBLE
        RetrofitClient.instance.actualizarUsuario(id, usuario).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@GestionUsuariosActivity,
                        "Usuario actualizado exitosamente", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                    cargarUsuarios()
                } else {
                    Toast.makeText(this@GestionUsuariosActivity,
                        "Error al actualizar usuario: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@GestionUsuariosActivity,
                    "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminarUsuario() {
        val id = etIdUsuario.text.toString().toInt()

        progressBar.visibility = View.VISIBLE
        RetrofitClient.instance.eliminarUsuario(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@GestionUsuariosActivity,
                        "Usuario eliminado exitosamente", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                    cargarUsuarios()
                } else {
                    Toast.makeText(this@GestionUsuariosActivity,
                        "Error al eliminar usuario: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@GestionUsuariosActivity,
                    "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun buscarUsuario() {
        val id = etIdUsuario.text.toString().toInt()

        progressBar.visibility = View.VISIBLE
        RetrofitClient.instance.obtenerUsuarioPorId(id).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    response.body()?.let { usuario ->
                        etNombre.setText(usuario.nombre)
                        etCorreo.setText(usuario.correo)
                        // No mostramos la contraseña por seguridad
                    } ?: run {
                        Toast.makeText(this@GestionUsuariosActivity,
                            "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@GestionUsuariosActivity,
                        "Error al buscar usuario: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@GestionUsuariosActivity,
                    "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun limpiarCampos() {
        etIdUsuario.text.clear()
        etNombre.text.clear()
        etCorreo.text.clear()
        etContrasena.text.clear()
    }

    private inner class UsuarioAdapter(
        context: Context,
        private val usuarios: List<Usuario>
    ) : ArrayAdapter<Usuario>(context, android.R.layout.simple_list_item_2, usuarios) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_2, parent, false)

            val usuario = getItem(position)
            view.findViewById<TextView>(android.R.id.text1).text = usuario?.nombre
            view.findViewById<TextView>(android.R.id.text2).text = context.getString(
                R.string.user_info_format,
                usuario?.id ?: 0,
                usuario?.correo ?: ""
            )

            return view
        }
    }
}