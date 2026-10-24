package com.example.model

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppCategory(val title: String, val subtitle: String) {
    SPATIAL("Spatial Core", "Vision & Liquid OS"),
    PRODUCTIVITY("Focus & Flow", "Workspace & Notes"),
    MEDIA("Prism Media", "Spatial Audio & Pulse"),
    CREATIVE("Glass Studio", "Optics & Design"),
    SYSTEM("Device & System", "Installed & Telemetry")
}

enum class BuiltInAppType {
    GLASS_STUDIO,
    SPATIAL_WEATHER,
    CHRONO_PULSE,
    PRISM_AUDIO,
    GLASS_NOTES,
    SYSTEM_TELEMETRY,
    APP_LIBRARY_PORTAL,
    SPOTLIGHT_PORTAL,
    EXTERNAL_PACKAGE
}

data class LauncherApp(
    val id: String,
    val name: String,
    val category: AppCategory,
    val iconVector: ImageVector,
    val primaryTint: Color,
    val secondaryTint: Color,
    val badgeCount: Int = 0,
    val builtInType: BuiltInAppType = BuiltInAppType.EXTERNAL_PACKAGE,
    val packageName: String? = null,
    val systemDrawable: Drawable? = null,
    val pageIndex: Int = 0,
    val isPinnedToDock: Boolean = false
)

enum class LiquidWidgetType(
    val id: String,
    val title: String,
    val subtitle: String
) {
    CHRONO_WEATHER("chrono_weather", "Spatial Weather & Chrono", "Live refractive sky dome & multi-city forecast"),
    PRISM_AUDIO("prism_audio", "Prism Spatial Audio", "Lossless 3D waveform synthesizer & deck"),
    NEURAL_TELEMETRY("neural_telemetry", "Liquid Telemetry Matrix", "Battery, thermal core, RAM & refraction load"),
    GLASS_NOTES("glass_notes", "Glass Focus & Checklist", "Interactive spatial tasks & quick memo pad")
}

enum class DynamicCapsuleMode {
    COMPACT,
    AUDIO_EXPANDED,
    TIMER_EXPANDED,
    WEATHER_EXPANDED
}

enum class OverlayMode {
    NONE,
    CONTROL_CENTER,
    SPOTLIGHT_SEARCH,
    APP_LIBRARY,
    GLASS_STUDIO,
    FOLDER_DETAIL,
    BUILT_IN_APP
}

data class LiquidWallpaper(
    val id: String,
    val name: String,
    val subtitle: String,
    val baseColor: Color,
    val orbColors: List<Color>,
    val accentColor: Color
)

data class GlassPhysicsConfig(
    val wallpaperId: String = "aurora_27",
    val frostOpacity: Float = 0.22f,
    val specularStrength: Float = 0.78f,
    val chromaticRim: Float = 0.65f,
    val iconCornerPercent: Int = 28,
    val backgroundBlurDp: Float = 22f,
    val bouncySprings: Boolean = true
)

data class WeatherCityData(
    val city: String,
    val condition: String,
    val tempCelsius: Int,
    val highCelsius: Int,
    val lowCelsius: Int,
    val humidity: Int,
    val uvIndex: Int,
    val windKmh: Int,
    val aqi: Int,
    val hourlyTemps: List<Int>,
    val accentColor: Color
)

data class AudioTrackItem(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationSec: Int,
    val accentColor: Color,
    val secondaryColor: Color,
    val spatialMode: String
)

data class SpatialTaskItem(
    val id: String,
    val text: String,
    val completed: Boolean,
    val tag: String
)
