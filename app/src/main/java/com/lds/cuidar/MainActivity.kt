package com.lds.cuidar

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.compose.runtime.collectAsState
import com.lds.cuidar.core.network.NetworkProvider
import com.lds.cuidar.data.location.LocationServiceImpl
import com.lds.cuidar.data.remote.PanicApi
import com.lds.cuidar.data.remote.PanicRemoteDataSourceImpl
import com.lds.cuidar.data.repository.PanicRepositoryImpl
import com.lds.cuidar.domain.usecase.TriggerPanicUseCase
import com.lds.cuidar.presentation.navigation.NavigationEvent
import com.lds.cuidar.presentation.state.PanicUiState
import com.lds.cuidar.presentation.viewmodel.PanicViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val retrofit = NetworkProvider.createRetrofit()
        val panicApi = retrofit.create(PanicApi::class.java)

        val remoteDataSource = PanicRemoteDataSourceImpl(panicApi)
        val repository = PanicRepositoryImpl(remoteDataSource)
        val triggerPanicUseCase = TriggerPanicUseCase(repository)
        val locationService = LocationServiceImpl(this)
        val viewModel = PanicViewModel(triggerPanicUseCase, locationService)

        setContent {
            PanicButtonApp(viewModel = viewModel)
        }
    }
}

@Composable
fun PanicButtonApp(viewModel: PanicViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity
    val state by viewModel.uiState.collectAsState()
    val vibrator = context.getSystemService(Vibrator::class.java)

    val hasPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    LaunchedEffect(state) {
        when (state) {
            is PanicUiState.Sent -> {
                vibrator?.vibrate(
                    VibrationEffect.createOneShot(
                        120,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
            is PanicUiState.Error -> {
                vibrator?.vibrate(
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 80, 80, 80),
                        -1
                    )
                )
            }
            else -> Unit
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                NavigationEvent.OpenSafeWebsite -> {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.merlo.gob.ar/project/tramites-online/#")
                    )
                    context.startActivity(intent)
                    activity?.moveTaskToBack(true)
                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onPanicClicked()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            PanicUiState.Idle -> {
                Button(
                    onClick = {
                        if (hasPermission) {
                            viewModel.onPanicClicked()
                        } else {
                            permissionLauncher.launch(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        }
                    },
                    enabled = state == PanicUiState.Idle,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier.size(200.dp)
                ) {
                    Text("EMERGENCIA", color = Color.White)
                }
            }
            PanicUiState.Sending -> {
                CircularProgressIndicator()
            }
            PanicUiState.Sent -> {
                Text(
                    text = "Alerta enviada",
                    color = Color(0xFF4CAF50),
                    fontSize = 18.sp
                )
            }
            is PanicUiState.Error -> {
                Text(
                    text = "Error al enviar",
                    color = Color(0xFFF44336),
                    fontSize = 18.sp
                )
            }
        }
    }
}
