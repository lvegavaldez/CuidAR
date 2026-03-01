package com.lds.cuidar

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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
            .baseUrl("https://webhook.site/1d1d8769-1ac8-4f22-8fc1-9f7d2235f829/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val panicApi = retrofit.create(PanicApi::class.java)
        val remoteDataSource = PanicRemoteDataSourceImpl(panicApi)
        val repository = PanicRepositoryImpl(remoteDataSource)
        val locationService = LocationServiceImpl(this)
        val viewModel = PanicViewModel(locationService, repository)
        setContent {
            PanicButtonApp {
                viewModel.onPanicClicked()
            }
        }
    }


}

@Composable
fun PanicButtonApp(onPanicClick: () -> Unit) {
    val context = LocalContext.current

    val hasPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPanicClick()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                if (hasPermission) {
                    onPanicClick()
                } else {
                    permissionLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            ),
            modifier = Modifier.size(200.dp)
        ) {
            Text("EMERGENCIA", color = Color.White)
        }
    }
}
