package com.example.ui

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LauncherRepository
import com.example.model.AppCategory
import com.example.model.AudioTrackItem
import com.example.model.BuiltInAppType
import com.example.model.DynamicCapsuleMode
import com.example.model.GlassPhysicsConfig
import com.example.model.LauncherApp
import com.example.model.LiquidWallpaper
import com.example.model.OverlayMode
import com.example.model.SpatialTaskItem
import com.example.model.WeatherCityData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LauncherUiState(
    val currentTimeText: String = "09:41",
    val currentDateText: String = "FRIDAY, OCT 23",
    val currentSeconds: Int = 41,
    val allApps: List<LauncherApp> = emptyList(),
    val dockApps: List<LauncherApp> = emptyList(),
    val homePageApps: List<LauncherApp> = emptyList(),
    val installedSystemApps: List<LauncherApp> = emptyList(),
    val currentHomePage: Int = 0,
    val isEditMode: Boolean = false,
    val capsuleMode: DynamicCapsuleMode = DynamicCapsuleMode.COMPACT,
    val overlayMode: OverlayMode = OverlayMode.NONE,
    val activeBuiltInApp: BuiltInAppType = BuiltInAppType.GLASS_STUDIO,
    val activeFolderCategory: AppCategory = AppCategory.SPATIAL,
    val searchQuery: String = "",
    // Audio Deck State
    val isPlayingAudio: Boolean = true,
    val currentTrackIndex: Int = 0,
    val audioProgressSec: Int = 64,
    val isSpatialAudioHeadTracked: Boolean = true,
    // Focus Timer State
    val isTimerRunning: Boolean = false,
    val timerRemainingSec: Int = 1500, // 25:00
    val timerTotalSec: Int = 1500,
    // Control Center Toggles & Sliders
    val brightnessLevel: Float = 0.84f,
    val volumeLevel: Float = 0.72f,
    val wifiEnabled: Boolean = true,
    val bluetoothEnabled: Boolean = true,
    val cellular5GEnabled: Boolean = true,
    val focusModeEnabled: Boolean = false,
    val torchEnabled: Boolean = false,
    val spatialModeEnabled: Boolean = true,
    // Device Telemetry
    val batteryLevel: Int = 92,
    val isCharging: Boolean = true,
    val thermalCelsius: Int = 31,
    val memoryUsagePercent: Int = 42,
    // Spatial Tasks
    val spatialTasks: List<SpatialTaskItem> = listOf(
        SpatialTaskItem("t1", "Tune iOS 27 Glass Refraction & Blur", true, "Optics"),
        SpatialTaskItem("t2", "Test Dynamic Capsule Spring Physics", false, "Motion"),
        SpatialTaskItem("t3", "Explore Spatial Weather UV & Hourly Dome", false, "Atmosphere"),
        SpatialTaskItem("t4", "Swipe or Tap App Vault for Installed Apps", false, "System")
    ),
    val statusToastMessage: String? = null
)

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    val repository = LauncherRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(LauncherUiState())
    val uiState: StateFlow<LauncherUiState> = _uiState.asStateFlow()

    val glassConfig: StateFlow<GlassPhysicsConfig> = repository.glassConfigFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GlassPhysicsConfig()
    )

    val enabledWidgets: StateFlow<Set<String>> = repository.enabledWidgetsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = setOf("chrono_weather", "prism_audio", "neural_telemetry", "glass_notes")
    )

    val selectedCityIndex: StateFlow<Int> = repository.cityIndexFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val quickNote: StateFlow<String> = repository.quickNoteFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val currentWallpaper: StateFlow<LiquidWallpaper> = combine(
        glassConfig,
        MutableStateFlow(repository.wallpapers)
    ) { config, wallpapers ->
        wallpapers.find { it.id == config.wallpaperId } ?: wallpapers.first()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repository.wallpapers.first()
    )

    val currentCityWeather: StateFlow<WeatherCityData> = combine(
        selectedCityIndex,
        MutableStateFlow(repository.weatherCities)
    ) { idx, cities ->
        cities.getOrElse(idx % cities.size) { cities.first() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repository.weatherCities.first()
    )

    init {
        refreshInstalledApps()
        startLiveClockAndTelemetryLoop()
    }

    fun refreshInstalledApps() {
        viewModelScope.launch {
            val loaded = repository.loadAllApps()
            val dock = loaded.filter { it.isPinnedToDock }
            val home = loaded.filter { !it.isPinnedToDock && it.pageIndex == 0 }
            val system = loaded.filter { it.builtInType == BuiltInAppType.EXTERNAL_PACKAGE }
            _uiState.update {
                it.copy(
                    allApps = loaded,
                    dockApps = dock,
                    homePageApps = home,
                    installedSystemApps = system
                )
            }
        }
    }

    private fun startLiveClockAndTelemetryLoop() {
        viewModelScope.launch {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateFormat = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
            val secFormat = SimpleDateFormat("ss", Locale.getDefault())
            while (true) {
                val now = Date()
                val timeStr = timeFormat.format(now)
                val dateStr = dateFormat.format(now).uppercase(Locale.getDefault())
                val sec = secFormat.format(now).toIntOrNull() ?: 0

                val batteryStatus: Intent? = try {
                    getApplication<Application>().registerReceiver(
                        null,
                        IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                    )
                } catch (_: Exception) {
                    null
                }
                val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
                val pct = if (level >= 0 && scale > 0) (level * 100) / scale else 92
                val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
                val charging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
                val tempTenths = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 310) ?: 310
                val tempC = (tempTenths / 10).coerceIn(22, 48)

                val runtime = Runtime.getRuntime()
                val usedMem = runtime.totalMemory() - runtime.freeMemory()
                val maxMem = runtime.maxMemory().coerceAtLeast(1L)
                val memPct = ((usedMem.toDouble() / maxMem.toDouble()) * 100.0).toInt().coerceIn(24, 88)

                _uiState.update { state ->
                    val currentTrack = getCurrentTrack(state.currentTrackIndex)
                    val nextAudioSec = if (state.isPlayingAudio) {
                        (state.audioProgressSec + 1) % currentTrack.durationSec
                    } else {
                        state.audioProgressSec
                    }
                    val nextTimerSec = if (state.isTimerRunning && state.timerRemainingSec > 0) {
                        state.timerRemainingSec - 1
                    } else {
                        state.timerRemainingSec
                    }
                    val timerStillRunning = state.isTimerRunning && nextTimerSec > 0
                    state.copy(
                        currentTimeText = timeStr,
                        currentDateText = dateStr,
                        currentSeconds = sec,
                        batteryLevel = pct,
                        isCharging = charging,
                        thermalCelsius = tempC,
                        memoryUsagePercent = memPct,
                        audioProgressSec = nextAudioSec,
                        timerRemainingSec = nextTimerSec,
                        isTimerRunning = timerStillRunning
                    )
                }
                delay(1000L)
            }
        }
    }

    fun getCurrentTrack(index: Int = _uiState.value.currentTrackIndex): AudioTrackItem {
        val tracks = repository.audioTracks
        return tracks.getOrElse(index.coerceIn(0, tracks.lastIndex)) { tracks.first() }
    }

    fun setHomePage(page: Int) {
        _uiState.update { it.copy(currentHomePage = page.coerceIn(0, 1)) }
    }

    fun toggleEditMode() {
        _uiState.update { it.copy(isEditMode = !it.isEditMode) }
    }

    fun exitEditMode() {
        _uiState.update { it.copy(isEditMode = false) }
    }

    fun cycleDynamicCapsuleMode() {
        _uiState.update { state ->
            val next = when (state.capsuleMode) {
                DynamicCapsuleMode.COMPACT -> DynamicCapsuleMode.AUDIO_EXPANDED
                DynamicCapsuleMode.AUDIO_EXPANDED -> DynamicCapsuleMode.TIMER_EXPANDED
                DynamicCapsuleMode.TIMER_EXPANDED -> DynamicCapsuleMode.WEATHER_EXPANDED
                DynamicCapsuleMode.WEATHER_EXPANDED -> DynamicCapsuleMode.COMPACT
            }
            state.copy(capsuleMode = next)
        }
    }

    fun setDynamicCapsuleMode(mode: DynamicCapsuleMode) {
        _uiState.update { it.copy(capsuleMode = mode) }
    }

    fun openOverlay(mode: OverlayMode) {
        _uiState.update { it.copy(overlayMode = mode, isEditMode = false) }
    }

    fun closeOverlays() {
        _uiState.update {
            it.copy(
                overlayMode = OverlayMode.NONE,
                capsuleMode = DynamicCapsuleMode.COMPACT,
                searchQuery = ""
            )
        }
    }

    fun openFolder(category: AppCategory) {
        _uiState.update {
            it.copy(
                activeFolderCategory = category,
                overlayMode = OverlayMode.FOLDER_DETAIL
            )
        }
    }

    fun onAppClick(app: LauncherApp) {
        if (_uiState.value.isEditMode) return
        when (app.builtInType) {
            BuiltInAppType.SPOTLIGHT_PORTAL -> {
                _uiState.update { it.copy(overlayMode = OverlayMode.SPOTLIGHT_SEARCH, searchQuery = "") }
            }
            BuiltInAppType.APP_LIBRARY_PORTAL -> {
                _uiState.update { it.copy(overlayMode = OverlayMode.APP_LIBRARY) }
            }
            BuiltInAppType.GLASS_STUDIO -> {
                _uiState.update {
                    it.copy(
                        activeBuiltInApp = BuiltInAppType.GLASS_STUDIO,
                        overlayMode = OverlayMode.GLASS_STUDIO
                    )
                }
            }
            BuiltInAppType.EXTERNAL_PACKAGE -> {
                val pkg = app.packageName
                if (pkg != null) {
                    val launched = repository.launchExternalPackage(pkg)
                    if (!launched) {
                        showToast("Unable to launch ${app.name}")
                    }
                }
            }
            else -> {
                _uiState.update {
                    it.copy(
                        activeBuiltInApp = app.builtInType,
                        overlayMode = OverlayMode.BUILT_IN_APP
                    )
                }
            }
        }
    }

    fun openBuiltInExperience(type: BuiltInAppType) {
        if (type == BuiltInAppType.GLASS_STUDIO) {
            _uiState.update { it.copy(activeBuiltInApp = type, overlayMode = OverlayMode.GLASS_STUDIO) }
        } else {
            _uiState.update { it.copy(activeBuiltInApp = type, overlayMode = OverlayMode.BUILT_IN_APP) }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    // Audio Controls
    fun togglePlayPauseAudio() {
        _uiState.update { it.copy(isPlayingAudio = !it.isPlayingAudio) }
    }

    fun nextAudioTrack() {
        _uiState.update { state ->
            val nextIdx = (state.currentTrackIndex + 1) % repository.audioTracks.size
            state.copy(currentTrackIndex = nextIdx, audioProgressSec = 0, isPlayingAudio = true)
        }
    }

    fun previousAudioTrack() {
        _uiState.update { state ->
            val size = repository.audioTracks.size
            val prevIdx = (state.currentTrackIndex - 1 + size) % size
            state.copy(currentTrackIndex = prevIdx, audioProgressSec = 0, isPlayingAudio = true)
        }
    }

    fun seekAudio(seconds: Int) {
        _uiState.update { state ->
            val max = getCurrentTrack(state.currentTrackIndex).durationSec
            state.copy(audioProgressSec = seconds.coerceIn(0, max))
        }
    }

    fun toggleSpatialAudioHeadTracked() {
        _uiState.update { it.copy(isSpatialAudioHeadTracked = !it.isSpatialAudioHeadTracked) }
    }

    // Timer Controls
    fun toggleFocusTimer() {
        _uiState.update { it.copy(isTimerRunning = !it.isTimerRunning) }
    }

    fun resetFocusTimer(minutes: Int = 25) {
        val total = (minutes.coerceIn(1, 120)) * 60
        _uiState.update {
            it.copy(
                isTimerRunning = false,
                timerRemainingSec = total,
                timerTotalSec = total
            )
        }
    }

    // Weather City Cycling
    fun cycleWeatherCity() {
        viewModelScope.launch {
            val next = (selectedCityIndex.value + 1) % repository.weatherCities.size
            repository.updateCityIndex(next)
        }
    }

    fun selectWeatherCity(index: Int) {
        viewModelScope.launch {
            repository.updateCityIndex(index.coerceIn(0, repository.weatherCities.lastIndex))
        }
    }

    // Control Center Actions
    fun setBrightness(level: Float) {
        _uiState.update { it.copy(brightnessLevel = level.coerceIn(0.15f, 1.0f)) }
    }

    fun setVolume(level: Float) {
        _uiState.update { it.copy(volumeLevel = level.coerceIn(0.0f, 1.0f)) }
    }

    fun toggleWifi() {
        _uiState.update { it.copy(wifiEnabled = !it.wifiEnabled) }
    }

    fun toggleBluetooth() {
        _uiState.update { it.copy(bluetoothEnabled = !it.bluetoothEnabled) }
    }

    fun toggleCellular5G() {
        _uiState.update { it.copy(cellular5GEnabled = !it.cellular5GEnabled) }
    }

    fun toggleFocusMode() {
        _uiState.update { it.copy(focusModeEnabled = !it.focusModeEnabled) }
    }

    fun toggleTorch() {
        _uiState.update { it.copy(torchEnabled = !it.torchEnabled) }
    }

    fun toggleSpatialMode() {
        _uiState.update { it.copy(spatialModeEnabled = !it.spatialModeEnabled) }
    }

    // Tasks & Notes
    fun toggleSpatialTask(taskId: String) {
        _uiState.update { state ->
            state.copy(
                spatialTasks = state.spatialTasks.map { item ->
                    if (item.id == taskId) item.copy(completed = !item.completed) else item
                }
            )
        }
    }

    fun addSpatialTask(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return
        _uiState.update { state ->
            val newTask = SpatialTaskItem(
                id = "t_${System.currentTimeMillis()}",
                text = trimmed,
                completed = false,
                tag = "Spatial"
            )
            state.copy(spatialTasks = listOf(newTask) + state.spatialTasks)
        }
    }

    fun saveQuickNote(note: String) {
        viewModelScope.launch {
            repository.updateQuickNote(note)
        }
    }

    fun optimizeMemoryMatrix() {
        System.gc()
        _uiState.update { state ->
            val reduced = (state.memoryUsagePercent - 12).coerceAtLeast(22)
            state.copy(
                memoryUsagePercent = reduced,
                statusToastMessage = "Liquid Glass Buffer Optimized • ${reduced}% Load"
            )
        }
        viewModelScope.launch {
            delay(2500L)
            _uiState.update { it.copy(statusToastMessage = null) }
        }
    }

    // Glass Physics & Wallpaper Persistence
    fun selectWallpaper(wallpaperId: String) {
        viewModelScope.launch {
            repository.updateWallpaper(wallpaperId)
        }
    }

    fun setFrostOpacity(value: Float) {
        viewModelScope.launch {
            repository.updateFrostOpacity(value)
        }
    }

    fun setSpecularStrength(value: Float) {
        viewModelScope.launch {
            repository.updateSpecularStrength(value)
        }
    }

    fun setChromaticRim(value: Float) {
        viewModelScope.launch {
            repository.updateChromaticRim(value)
        }
    }

    fun setIconCornerPercent(percent: Int) {
        viewModelScope.launch {
            repository.updateIconCornerPercent(percent)
        }
    }

    fun setBackgroundBlurDp(blurDp: Float) {
        viewModelScope.launch {
            repository.updateBackgroundBlurDp(blurDp)
        }
    }

    fun setBouncySprings(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateBouncySprings(enabled)
        }
    }

    fun toggleWidgetEnabled(widgetId: String) {
        viewModelScope.launch {
            repository.toggleWidget(widgetId)
        }
    }

    fun launchAndroidSettings() {
        repository.openSystemSettings()
    }

    fun launchDefaultHomeChooser() {
        repository.openHomeSettings()
    }

    private fun showToast(msg: String) {
        _uiState.update { it.copy(statusToastMessage = msg) }
        viewModelScope.launch {
            delay(2500L)
            _uiState.update { it.copy(statusToastMessage = null) }
        }
    }
}
