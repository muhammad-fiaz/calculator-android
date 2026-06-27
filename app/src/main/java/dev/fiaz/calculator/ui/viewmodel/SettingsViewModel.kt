package dev.fiaz.calculator.ui.viewmodel

import android.app.Activity
import android.content.Context
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewManager
import dev.fiaz.calculator.BuildConfig
import dev.fiaz.calculator.domain.model.AppSettings
import dev.fiaz.calculator.domain.model.AppThemeMode
import dev.fiaz.calculator.domain.model.CalculatorMode
import dev.fiaz.calculator.domain.repository.AppSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: AppSettingsRepository,
    private val reviewManager: ReviewManager,
    val updateManager: AppUpdateManager
) : ViewModel() {
    private val _settings = MutableStateFlow(SettingsScreenState())
    val settings: StateFlow<SettingsScreenState> = _settings.asStateFlow()

    init {
        viewModelScope.launch {
            repository.settings.collectLatest { settings ->
                _settings.value = _settings.value.copy(appSettings = settings)
            }
        }
    }

    fun updateTheme(theme: AppThemeMode) = viewModelScope.launch {
        repository.updateTheme(theme)
    }

    fun updateHaptics(enabled: Boolean) = viewModelScope.launch {
        repository.updateHaptics(enabled)
    }

    fun updateMode(mode: CalculatorMode) = viewModelScope.launch {
        repository.updateDefaultMode(mode)
    }

    fun refreshAppInfo(context: Context) {
        val packageInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(context.packageName, android.content.pm.PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION") context.packageManager.getPackageInfo(context.packageName, 0)
        }
        _settings.value = _settings.value.copy(
            versionName = packageInfo.versionName ?: BuildConfig.VERSION_NAME,
            versionCode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION") packageInfo.versionCode.toLong()
            }
        )
    }

    fun requestUpdateInfo(onAvailable: (AppUpdateInfo) -> Unit, onError: (String) -> Unit) {
        _settings.value = _settings.value.copy(updateInProgress = true)
        updateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                onAvailable(info)
            } else {
                onError("No update available")
            }
            _settings.value = _settings.value.copy(updateInProgress = false)
        }.addOnFailureListener {
            _settings.value = _settings.value.copy(updateInProgress = false)
            onError("Unable to check updates right now")
        }
    }

    fun launchReview(activity: Activity, onUnavailable: () -> Unit) {
        reviewManager.requestReviewFlow().addOnCompleteListener { request ->
            if (!request.isSuccessful) {
                onUnavailable()
                return@addOnCompleteListener
            }
            reviewManager.launchReviewFlow(activity, request.result).addOnCompleteListener { }
        }
    }

    fun shareApp(context: Context) {
        ShareCompat.IntentBuilder(context)
            .setType("text/plain")
            .setText("Calculator Pro by Fiaz Technologies")
            .startChooser()
    }
}

data class SettingsScreenState(
    val appSettings: AppSettings = AppSettings(),
    val versionName: String = BuildConfig.VERSION_NAME,
    val versionCode: Long = BuildConfig.VERSION_CODE.toLong(),
    val updateInProgress: Boolean = false
)