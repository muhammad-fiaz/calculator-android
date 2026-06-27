package dev.fiaz.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import dev.fiaz.calculator.domain.model.AppThemeMode
import dev.fiaz.calculator.ui.navigation.AppNavGraph
import dev.fiaz.calculator.ui.theme.CalculatorTheme
import dev.fiaz.calculator.ui.viewmodel.SettingsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val isCrash = intent.getBooleanExtra("is_crash", false)
        val errorMessage = intent.getStringExtra("error_message")
        
        setContent {
            CalculatorAppRoot(isCrash, errorMessage)
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    this,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                    999
                )
            }
        }
    }
}

@Composable
private fun CalculatorAppRoot(isCrash: Boolean, errorMessage: String?) {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val state by settingsViewModel.settings.collectAsState()
    val navController = rememberNavController()
    val uriHandler = LocalUriHandler.current
    var showErrorDialog by remember { mutableStateOf(isCrash) }

    CalculatorTheme(
        darkTheme = when (state.appSettings.theme) {
            AppThemeMode.Dark -> true
            AppThemeMode.Light -> false
            AppThemeMode.System -> isSystemInDarkTheme()
        }
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            AppNavGraph(navController = navController)
            
            if (showErrorDialog && errorMessage != null) {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = { Text("System Error") },
                    text = { Text("An internal error occurred: $errorMessage\n\nPlease report this issue on GitHub.") },
                    confirmButton = {
                        TextButton(onClick = {
                            uriHandler.openUri("https://github.com/muhammad-fiaz/calculator-android/issues/new")
                            showErrorDialog = false
                        }) {
                            Text("Report Issue")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showErrorDialog = false }) {
                            Text("Close")
                        }
                    }
                )
            }
        }
    }
}