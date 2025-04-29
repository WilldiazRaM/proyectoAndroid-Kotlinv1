# 📱 Proyecto Android con Kotlin + Retrofit

Aplicación de registro de usuarios que consume un API REST local con Node.js.

---

## 📋 Requisitos previos

- Android Studio Hedgehog o superior  
- Android SDK 34  
- Dispositivo/emulador con Android 8.0+  
- Backend Node.js corriendo en `http://localhost:3000` (VER PPT BACKEND)

---

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
    @POST("usuarios")
    fun registrarUsuario(@Body usuario: Usuario): Call<Usuario>
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

