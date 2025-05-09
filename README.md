# 📱 Proyecto Android con Kotlin + Retrofit

Aplicación de registro de usuarios que consume un API REST local con Node.js.

---

## 📋 Requisitos previos

- Android Studio Hedgehog o superior  
- Android SDK 34  
- Dispositivo/emulador con Android 8.0+  
- Backend Node.js corriendo en `http://localhost:3000` (VER PPT BACKEND)

---

## 🔄 ¿Qué es una API REST?

Una **API REST** (Representational State Transfer) es un estilo arquitectónico para diseñar servicios web que utiliza:

✔️ **Protocolo HTTP** como base  
✔️ **Operaciones CRUD** (Create, Read, Update, Delete) mediante métodos HTTP:  
   - `GET` → Obtener recursos  
   - `POST` → Crear recursos  
   - `PUT`/`PATCH` → Actualizar recursos  
   - `DELETE` → Eliminar recursos  

✔️ **Formatos estándar** como JSON para intercambio de datos  
✔️ **Stateless** (sin estado entre peticiones)  




## 🏗 Estructura del proyecto

```
proyecto/
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/com/example/myapplication/
│       │   │   ├── MainActivity.kt
│       │   │   ├── RegistroActivity.kt
│       │   │   ├── models/
│       │   │   │   └── Usuario.kt
│       │   │   └── Network/
│       │   │       ├── ApiService.kt
│       │   │       └── RetrofitClient.kt
│       │   └── res/
│       │       └── layout/
│       │           └── activity_registro.xml
└── build.gradle
```

---

## 🔌 Configuración inicial

### 1. Permisos en `AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<application
    android:usesCleartextTraffic="true"
    ... >
```

### 2. Añadir dependencias en `build.gradle`

```kotlin
dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}
```

---

## 🧩 Componentes clave

### 1. Modelo de datos - `Usuario.kt`

```kotlin
data class Usuario(
    val nombre: String,
    val correo: String,
    val contrasena: String
)
```

### 2. Cliente Retrofit - `RetrofitClient.kt`

```kotlin
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
```

### 3. Interfaz de API - `ApiService.kt`

```kotlin
interface ApiService {

    // Operaciones CRUD completas
    @GET("usuarios")
    fun obtenerTodosUsuarios(): Call<List<Usuario>>

    @GET("usuarios/{id}")
    fun obtenerUsuarioPorId(@Path("id") id: Int): Call<Usuario>

    @POST("usuarios")
    fun crearUsuario(@Body usuario: Usuario): Call<Usuario>

    @PUT("usuarios/{id}")
    fun actualizarUsuario(@Path("id") id: Int, @Body usuario: Usuario): Call<Usuario>

    @DELETE("usuarios/{id}")
    fun eliminarUsuario(@Path("id") id: Int): Call<Void>
}

```

---

## 🖥 Lógica de registro - `RegistroActivity.kt`

### Inicialización de vistas

```kotlin
etNombre = findViewById(R.id.etNombre)
btnRegister = findViewById(R.id.btnRegister)
```

### Validación y envío

```kotlin
btnRegister.setOnClickListener {
    if (camposValidos()) {
        registrarUsuario(
            etNombre.text.toString(),
            etPassword.text.toString(),
            etEmail.text.toString()
        )
    }
}
```

## 🛠 Funcionalidades CRUD Implementadas (Código) Fecha.09.05.2025

### 1. Crear Usuario
```kotlin
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
```
### 2. Leer Usuarios (Listar todos)
```
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
```
### 3. Buscar Usuario por ID
```
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
```
### 4. Actualizar Usuario
```
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
```
### 5. Eliminar Usuario
```
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
```




### Manejo de respuesta

```kotlin
call.enqueue(object : Callback<Usuario> {
    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
        if (response.isSuccessful) {
            Toast.makeText(this@RegistroActivity, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(call: Call<Usuario>, t: Throwable) {
        Toast.makeText(this@RegistroActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
    }
})
```

---

## 🚀 Cómo ejecutar el proyecto

1. Clonar el repositorio  
2. Abrir en Android Studio  
3. Ejecutar en emulador o dispositivo  
4. Asegurarse de que el backend esté corriendo en `http://10.0.2.2:3000`
5. Url del respositorio back-end node.js: `https://github.com/WilldiazRaM/backsqlite` 

---

## 📚 Recursos de aprendizaje

- [Documentación oficial de Retrofit](https://square.github.io/retrofit/)  
- [Guías de desarrollo Android](https://developer.android.com/kotlin)

---

## 🤔 Problemas comunes

| Error                        | Solución                                                      |
|-----------------------------|---------------------------------------------------------------|
| CLEARTEXT not permitted     | Verificar que usesCleartextTraffic esté en AndroidManifest    |
| Timeout                     | Verificar conexión de red y que el backend esté activo        |
| 404 Not Found               | Confirmar que la ruta del API sea correcta                    |

---

