package com.lds.cuidar

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lds.cuidar.domain.LocationServiceImpl
import com.lds.cuidar.ui.theme.PanicViewModel

class MainActivity : ComponentActivity() {
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // El usuario dio permiso, puedes iniciar la lógica de ubicación aquí
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val locationService = LocationServiceImpl(this)
        val viewModel = PanicViewModel(locationService)
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        setContent {
            PanicButtonApp {
                viewModel.onPanicClicked()
            }
        }
    }


}

@Composable
fun PanicButtonApp(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            ),
            modifier = Modifier.size(200.dp)
        ) {
            Text("EMERGENCIA", color = Color.White)
        }
    }
}




