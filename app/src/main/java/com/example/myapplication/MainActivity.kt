package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.util.Log


class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity" // Constante para el tag de logs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate iniciado") // Log de creación
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        onRegisterClick = {
                            Log.d(TAG, "Botón registro clickeado") // Log de click
                            openRegistrationActivity()
                        }
                    )
                }
            }
        }
    }

    private fun openRegistrationActivity() {
        try {
            Log.d(TAG, "Intentando abrir RegistroActivity")
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
            Log.d(TAG, "Actividad iniciada con éxito")
        } catch (e: Exception) {
            Log.e(TAG, "Error al abrir RegistroActivity", e) // Log de error
            e.printStackTrace()
        }
    }
}
@Composable
fun MyAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

@Composable
fun MainScreen(
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mejor manejo de temas y estilos
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón más accesible
        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 4.dp
            )
        ) {
            Text(
                text = "Ir a Registro",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyAppTheme {
        val context = LocalContext.current
        MainScreen(
            onRegisterClick = {
                // Simulación para el preview
                context.startActivity(Intent(context, MainActivity::class.java))
            }
        )
    }
}