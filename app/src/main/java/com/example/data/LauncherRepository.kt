package com.example.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.AppCategory
import com.example.model.AudioTrackItem
import com.example.model.BuiltInAppType
import com.example.model.GlassPhysicsConfig
import com.example.model.LauncherApp
import com.example.model.LiquidWallpaper
import com.example.model.LiquidWidgetType
import com.example.model.SpatialTaskItem
import com.example.model.WeatherCityData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private val Context.launcherDataStore by preferencesDataStore(name = "liquidos_preferences")

class LauncherRepository(private val context: Context) {

    private object Keys {
        val WALLPAPER_ID = stringPreferencesKey("wallpaper_id")
        val FROST_OPACITY = floatPreferencesKey("frost_opacity")
        val SPECULAR_STRENGTH = floatPreferencesKey("specular_strength")
        val CHROMATIC_RIM = floatPreferencesKey("chromatic_rim")
        val ICON_CORNER_PERCENT = intPreferencesKey("icon_corner_percent")
        val BACKGROUND_BLUR_DP = floatPreferencesKey("background_blur_dp")
        val BOUNCY_SPRINGS = booleanPreferencesKey("bouncy_springs")
        val ENABLED_WIDGETS = stringSetPreferencesKey("enabled_widgets")
        val CITY_INDEX = intPreferencesKey("city_index")
        val QUICK_NOTE = stringPreferencesKey("quick_note")
    }

    val wallpapers: List<LiquidWallpaper> = listOf(
        LiquidWallpaper(
            id = "aurora_27",
            name = "Aurora Borealis 27",
            subtitle = "Bioluminescent Cyan, Indigo & Violet",
            baseColor = Color(0xFF060B19),
            orbColors = listOf(
                Color(0xFF00E5FF),
                Color(0xFF6366F1),
                Color(0xFFD946EF),
                Color(0xFF0EA5E9)
            ),
            accentColor = Color(0xFF5CE1E6)
        ),
        LiquidWallpaper(
            id = "solaris_prism",
            name = "Solaris Prism",
            subtitle = "Warm Coral, Liquid Gold & Rose Quartz",
            baseColor = Color(0xFF14091D),
            orbColors = listOf(
                Color(0xFFFB7185),
                Color(0xFFF59E0B),
                Color(0xFFA855F7),
                Color(0xFFEC4899)
            ),
            accentColor = Color(0xFFFB7185)
        ),
        LiquidWallpaper(
            id = "vision_titanium",
            name = "VisionOS Titanium",
            subtitle = "Refractive Pearl, Ice Sapphire & Slate",
            baseColor = Color(0xFF0B1324),
            orbColors = listOf(
                Color(0xFF38BDF8),
                Color(0xFF94A3B8),
                Color(0xFF818CF8),
                Color(0xFF2DD4BF)
            ),
            accentColor = Color(0xFF7DD3FC)
        ),
        LiquidWallpaper(
            id = "emerald_biolume",
            name = "Abyssal Biolume",
            subtitle = "Neon Mint, Deep Emerald & Electric Cyan",
            baseColor = Color(0xFF041418),
            orbColors = listOf(
                Color(0xFF10B981),
                Color(0xFF06B6D4),
                Color(0xFF3B82F6),
                Color(0xFF34D399)
            ),
            accentColor = Color(0xFF34D399)
        ),
        LiquidWallpaper(
            id = "cyber_amethyst",
            name = "Cyber Amethyst",
            subtitle = "Ultraviolet, Fuchsia & Obsidian Glass",
            baseColor = Color(0xFF0D061A),
            orbColors = listOf(
                Color(0xFFC084FC),
                Color(0xFFF43F5E),
                Color(0xFF4F46E5),
                Color(0xFF22D3EE)
            ),
            accentColor = Color(0xFFC084FC)
        )
    )

    val weatherCities: List<WeatherCityData> = listOf(
        WeatherCityData(
            city = "Cupertino",
            condition = "Clear Glass Sky",
            tempCelsius = 23,
            highCelsius = 26,
            lowCelsius = 15,
            humidity = 48,
            uvIndex = 6,
            windKmh = 14,
            aqi = 24,
            hourlyTemps = listOf(21, 23, 25, 26, 24, 20, 18),
            accentColor = Color(0xFF38BDF8)
        ),
        WeatherCityData(
            city = "Tokyo",
            condition = "Neon Mist & Dew",
            tempCelsius = 19,
            highCelsius = 22,
            lowCelsius = 14,
            humidity = 72,
            uvIndex = 4,
            windKmh = 19,
            aqi = 31,
            hourlyTemps = listOf(18, 19, 21, 22, 20, 17, 16),
            accentColor = Color(0xFFA78BFA)
        ),
        WeatherCityData(
            city = "Reykjavik",
            condition = "Aurora Frost",
            tempCelsius = 6,
            highCelsius = 9,
            lowCelsius = 1,
            humidity = 64,
            uvIndex = 2,
            windKmh = 28,
            aqi = 12,
            hourlyTemps = listOf(4, 6, 8, 9, 7, 5, 3),
            accentColor = Color(0xFF34D399)
        ),
        WeatherCityData(
            city = "Zurich",
            condition = "Alpine Crystal",
            tempCelsius = 16,
            highCelsius = 19,
            lowCelsius = 9,
            humidity = 54,
            uvIndex = 5,
            windKmh = 11,
            aqi = 18,
            hourlyTemps = listOf(13, 16, 18, 19, 17, 14, 11),
            accentColor = Color(0xFF60A5FA)
        )
    )

    val audioTracks: List<AudioTrackItem> = listOf(
        AudioTrackItem(
            id = "track_1",
            title = "Refractive Horizon",
            artist = "Aether & Glass",
            album = "iOS 27 Spatial Sessions",
            durationSec = 214,
            accentColor = Color(0xFF5CE1E6),
            secondaryColor = Color(0xFF818CF8),
            spatialMode = "Dolby Atmos 360°"
        ),
        AudioTrackItem(
            id = "track_2",
            title = "Subsurface Scattering",
            artist = "Nova Prism",
            album = "Crystal Frequencies",
            durationSec = 188,
            accentColor = Color(0xFFF472B6),
            secondaryColor = Color(0xFFA78BFA),
            spatialMode = "Head-Tracked Spatial"
        ),
        AudioTrackItem(
            id = "track_3",
            title = "Bioluminescent Tide",
            artist = "Kora Borealis",
            album = "Deep Glass Vol. IV",
            durationSec = 246,
            accentColor = Color(0xFF34D399),
            secondaryColor = Color(0xFF06B6D4),
            spatialMode = "Lossless Spatial Studio"
        )
    )

    val glassConfigFlow: Flow<GlassPhysicsConfig> = context.launcherDataStore.data.map { prefs ->
        GlassPhysicsConfig(
            wallpaperId = prefs[Keys.WALLPAPER_ID] ?: "aurora_27",
            frostOpacity = prefs[Keys.FROST_OPACITY] ?: 0.22f,
            specularStrength = prefs[Keys.SPECULAR_STRENGTH] ?: 0.78f,
            chromaticRim = prefs[Keys.CHROMATIC_RIM] ?: 0.65f,
            iconCornerPercent = prefs[Keys.ICON_CORNER_PERCENT] ?: 28,
            backgroundBlurDp = prefs[Keys.BACKGROUND_BLUR_DP] ?: 22f,
            bouncySprings = prefs[Keys.BOUNCY_SPRINGS] ?: true
        )
    }

    val enabledWidgetsFlow: Flow<Set<String>> = context.launcherDataStore.data.map { prefs ->
        prefs[Keys.ENABLED_WIDGETS] ?: setOf(
            LiquidWidgetType.CHRONO_WEATHER.id,
            LiquidWidgetType.PRISM_AUDIO.id,
            LiquidWidgetType.NEURAL_TELEMETRY.id,
            LiquidWidgetType.GLASS_NOTES.id
        )
    }

    val cityIndexFlow: Flow<Int> = context.launcherDataStore.data.map { prefs ->
        prefs[Keys.CITY_INDEX] ?: 0
    }

    val quickNoteFlow: Flow<String> = context.launcherDataStore.data.map { prefs ->
        prefs[Keys.QUICK_NOTE]
            ?: " Tap Dynamic Capsule to morph audio & timer\n Long-press any icon for Jiggle Edit Mode\n Customize refraction in Glass Studio"
    }

    suspend fun updateWallpaper(wallpaperId: String) {
        context.launcherDataStore.edit { it[Keys.WALLPAPER_ID] = wallpaperId }
    }

    suspend fun updateFrostOpacity(value: Float) {
        context.launcherDataStore.edit { it[Keys.FROST_OPACITY] = value.coerceIn(0.08f, 0.55f) }
    }

    suspend fun updateSpecularStrength(value: Float) {
        context.launcherDataStore.edit { it[Keys.SPECULAR_STRENGTH] = value.coerceIn(0.2f, 1.0f) }
    }

    suspend fun updateChromaticRim(value: Float) {
        context.launcherDataStore.edit { it[Keys.CHROMATIC_RIM] = value.coerceIn(0.1f, 1.0f) }
    }

    suspend fun updateIconCornerPercent(percent: Int) {
        context.launcherDataStore.edit { it[Keys.ICON_CORNER_PERCENT] = percent.coerceIn(16, 50) }
    }

    suspend fun updateBackgroundBlurDp(blurDp: Float) {
        context.launcherDataStore.edit { it[Keys.BACKGROUND_BLUR_DP] = blurDp.coerceIn(4f, 36f) }
    }

    suspend fun updateBouncySprings(enabled: Boolean) {
        context.launcherDataStore.edit { it[Keys.BOUNCY_SPRINGS] = enabled }
    }

    suspend fun toggleWidget(widgetId: String) {
        context.launcherDataStore.edit { prefs ->
            val current = prefs[Keys.ENABLED_WIDGETS] ?: setOf(
                LiquidWidgetType.CHRONO_WEATHER.id,
                LiquidWidgetType.PRISM_AUDIO.id,
                LiquidWidgetType.NEURAL_TELEMETRY.id,
                LiquidWidgetType.GLASS_NOTES.id
            )
            prefs[Keys.ENABLED_WIDGETS] = if (current.contains(widgetId)) {
                current - widgetId
            } else {
                current + widgetId
            }
        }
    }

    suspend fun updateCityIndex(index: Int) {
        context.launcherDataStore.edit { it[Keys.CITY_INDEX] = index }
    }

    suspend fun updateQuickNote(note: String) {
        context.launcherDataStore.edit { it[Keys.QUICK_NOTE] = note }
    }

    fun getBuiltInSpatialApps(): List<LauncherApp> = listOf(
        // Pinned Dock Apps
        LauncherApp(
            id = "dock_glass_studio",
            name = "Glass Studio",
            category = AppCategory.CREATIVE,
            iconVector = Icons.Filled.Palette,
            primaryTint = Color(0xFF5CE1E6),
            secondaryTint = Color(0xFF6366F1),
            builtInType = BuiltInAppType.GLASS_STUDIO,
            pageIndex = 0,
            isPinnedToDock = true
        ),
        LauncherApp(
            id = "dock_prism_audio",
            name = "Prism Audio",
            category = AppCategory.MEDIA,
            iconVector = Icons.Filled.GraphicEq,
            primaryTint = Color(0xFFF472B6),
            secondaryTint = Color(0xFFA855F7),
            builtInType = BuiltInAppType.PRISM_AUDIO,
            pageIndex = 0,
            isPinnedToDock = true
        ),
        LauncherApp(
            id = "dock_spotlight",
            name = "Spotlight",
            category = AppCategory.SPATIAL,
            iconVector = Icons.Filled.Search,
            primaryTint = Color(0xFF38BDF8),
            secondaryTint = Color(0xFF2DD4BF),
            builtInType = BuiltInAppType.SPOTLIGHT_PORTAL,
            pageIndex = 0,
            isPinnedToDock = true
        ),
        LauncherApp(
            id = "dock_app_library",
            name = "App Vault",
            category = AppCategory.SPATIAL,
            iconVector = Icons.Filled.Apps,
            primaryTint = Color(0xFFA78BFA),
            secondaryTint = Color(0xFFEC4899),
            builtInType = BuiltInAppType.APP_LIBRARY_PORTAL,
            pageIndex = 0,
            isPinnedToDock = true
        ),

        // Page 0 Spatial Core Apps
        LauncherApp(
            id = "app_spatial_weather",
            name = "Weather 27",
            category = AppCategory.SPATIAL,
            iconVector = Icons.Filled.WbSunny,
            primaryTint = Color(0xFF38BDF8),
            secondaryTint = Color(0xFFFBBF24),
            builtInType = BuiltInAppType.SPATIAL_WEATHER,
            pageIndex = 0
        ),
        LauncherApp(
            id = "app_chrono_pulse",
            name = "Chrono Pulse",
            category = AppCategory.PRODUCTIVITY,
            iconVector = Icons.Filled.Timer,
            primaryTint = Color(0xFFFB7185),
            secondaryTint = Color(0xFFF59E0B),
            builtInType = BuiltInAppType.CHRONO_PULSE,
            pageIndex = 0
        ),
        LauncherApp(
            id = "app_glass_notes",
            name = "Glass Notes",
            category = AppCategory.PRODUCTIVITY,
            iconVector = Icons.Filled.Checklist,
            primaryTint = Color(0xFF34D399),
            secondaryTint = Color(0xFF06B6D4),
            badgeCount = 3,
            builtInType = BuiltInAppType.GLASS_NOTES,
            pageIndex = 0
        ),
        LauncherApp(
            id = "app_system_telemetry",
            name = "Matrix Core",
            category = AppCategory.SYSTEM,
            iconVector = Icons.Filled.Memory,
            primaryTint = Color(0xFF818CF8),
            secondaryTint = Color(0xFF5CE1E6),
            builtInType = BuiltInAppType.SYSTEM_TELEMETRY,
            pageIndex = 0
        ),
        LauncherApp(
            id = "app_optics_tuning",
            name = "Refraction",
            category = AppCategory.CREATIVE,
            iconVector = Icons.Filled.Tune,
            primaryTint = Color(0xFFE879F9),
            secondaryTint = Color(0xFF6366F1),
            builtInType = BuiltInAppType.GLASS_STUDIO,
            pageIndex = 0
        ),
        LauncherApp(
            id = "app_spatial_deck",
            name = "Soundscape",
            category = AppCategory.MEDIA,
            iconVector = Icons.Filled.MusicNote,
            primaryTint = Color(0xFF2DD4BF),
            secondaryTint = Color(0xFF3B82F6),
            builtInType = BuiltInAppType.PRISM_AUDIO,
            pageIndex = 0
        ),
        LauncherApp(
            id = "app_sky_dome",
            name = "Atmosphere",
            category = AppCategory.SPATIAL,
            iconVector = Icons.Filled.Cloud,
            primaryTint = Color(0xFF60A5FA),
            secondaryTint = Color(0xFFA78BFA),
            builtInType = BuiltInAppType.SPATIAL_WEATHER,
            pageIndex = 0
        ),
        LauncherApp(
            id = "app_focus_timer",
            name = "Flow State",
            category = AppCategory.PRODUCTIVITY,
            iconVector = Icons.Filled.Bolt,
            primaryTint = Color(0xFFFBBF24),
            secondaryTint = Color(0xFFEF4444),
            builtInType = BuiltInAppType.CHRONO_PULSE,
            pageIndex = 0
        )
    )

    suspend fun loadAllApps(): List<LauncherApp> = withContext(Dispatchers.IO) {
        val builtIn = getBuiltInSpatialApps()
        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val resolvedList = try {
            pm.queryIntentActivities(mainIntent, 0)
        } catch (_: Exception) {
            emptyList()
        }

        val palette = listOf(
            Color(0xFF38BDF8) to Color(0xFF6366F1),
            Color(0xFF34D399) to Color(0xFF0EA5E9),
            Color(0xFFF472B6) to Color(0xFFA855F7),
            Color(0xFFFBBF24) to Color(0xFFFB7185),
            Color(0xFFA78BFA) to Color(0xFF2DD4BF),
            Color(0xFF60A5FA) to Color(0xFFE879F9)
        )

        val selfPkg = context.packageName
        val installedApps = resolvedList
            .mapNotNull { info ->
                val pkg = info.activityInfo?.packageName ?: return@mapNotNull null
                if (pkg == selfPkg) return@mapNotNull null
                val label = info.loadLabel(pm)?.toString()?.takeIf { it.isNotBlank() } ?: pkg.substringAfterLast('.')
                val iconDrawable = try {
                    info.loadIcon(pm)
                } catch (_: Exception) {
                    null
                }
                val hash = kotlin.math.abs(pkg.hashCode())
                val (c1, c2) = palette[hash % palette.size]
                val iconVec = when {
                    label.contains("Phone", true) || label.contains("Dial", true) -> Icons.Filled.Phone
                    label.contains("Message", true) || label.contains("Chat", true) -> Icons.Filled.ChatBubble
                    label.contains("Camera", true) -> Icons.Filled.CameraAlt
                    label.contains("Photo", true) || label.contains("Gallery", true) -> Icons.Filled.PhotoLibrary
                    label.contains("Setting", true) -> Icons.Filled.Settings
                    label.contains("Chrome", true) || label.contains("Browser", true) -> Icons.Filled.Public
                    label.contains("Mail", true) || label.contains("Gmail", true) -> Icons.Filled.Email
                    label.contains("Map", true) -> Icons.Filled.Map
                    label.contains("Calendar", true) -> Icons.Filled.CalendarMonth
                    label.contains("Clock", true) -> Icons.Filled.Timer
                    label.contains("Calc", true) -> Icons.Filled.Calculate
                    label.contains("Contact", true) -> Icons.Filled.Contacts
                    label.contains("File", true) -> Icons.Filled.Folder
                    label.contains("Video", true) || label.contains("YouTube", true) -> Icons.Filled.PlayCircle
                    else -> Icons.Filled.Android
                }
                val category = when {
                    label.contains("Setting", true) || label.contains("File", true) || label.contains("Phone", true) -> AppCategory.SYSTEM
                    label.contains("Camera", true) || label.contains("Photo", true) || label.contains("Video", true) -> AppCategory.MEDIA
                    label.contains("Calendar", true) || label.contains("Mail", true) || label.contains("Calc", true) -> AppCategory.PRODUCTIVITY
                    else -> AppCategory.SYSTEM
                }
                LauncherApp(
                    id = "pkg_$pkg",
                    name = label,
                    category = category,
                    iconVector = iconVec,
                    primaryTint = c1,
                    secondaryTint = c2,
                    builtInType = BuiltInAppType.EXTERNAL_PACKAGE,
                    packageName = pkg,
                    systemDrawable = iconDrawable,
                    pageIndex = 1
                )
            }
            .distinctBy { it.packageName }
            .sortedBy { it.name.lowercase() }

        builtIn + installedApps
    }

    fun launchExternalPackage(packageName: String): Boolean {
        return try {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launchIntent)
                true
            } else {
                false
            }
        } catch (_: Exception) {
            false
        }
    }

    fun openSystemSettings(): Boolean {
        return try {
            val intent = Intent(android.provider.Settings.ACTION_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun openHomeSettings(): Boolean {
        return try {
            val intent = Intent(android.provider.Settings.ACTION_HOME_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            openSystemSettings()
        }
    }
}
