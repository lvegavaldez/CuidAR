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
import com.lds.cuidar.data.remote.PanicApi
import com.lds.cuidar.data.remote.PanicRemoteDataSourceImpl
import com.lds.cuidar.data.repository.PanicRepositoryImpl
import com.lds.cuidar.domain.LocationServiceImpl
import com.lds.cuidar.ui.theme.PanicViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://tu-backend.com/") // CAMBIAR
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val panicApi = retrofit.create(PanicApi::class.java)
        val remoteDataSource = PanicRemoteDataSourceImpl(panicApi)
        val repository = PanicRepositoryImpl(remoteDataSource)
        val locationService = LocationServiceImpl(this)
        val viewModel = PanicViewModel(locationService, repository)
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


