package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LensBlur
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RoundedCorner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AudioTrackItem
import com.example.model.BuiltInAppType
import com.example.model.GlassPhysicsConfig
import com.example.model.LauncherApp
import com.example.model.LiquidWallpaper
import com.example.model.LiquidWidgetType
import com.example.model.SpatialTaskItem
import com.example.model.WeatherCityData

@Composable
fun GlassStudioOverlay(
    config: GlassPhysicsConfig,
    wallpapers: List<LiquidWallpaper>,
    enabledWidgets: Set<String>,
    onSelectWallpaper: (String) -> Unit,
    onFrostOpacityChange: (Float) -> Unit,
    onSpecularStrengthChange: (Float) -> Unit,
    onChromaticRimChange: (Float) -> Unit,
    onBlurDpChange: (Float) -> Unit,
    onCornerPercentChange: (Int) -> Unit,
    onBouncySpringsChange: (Boolean) -> Unit,
    onToggleWidget: (String) -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050814).copy(alpha = 0.60f))
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("glass_studio_overlay")
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 28.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Liquid Glass Studio 27",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tune refraction, blur depth, wallpapers & spatial widgets",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.16f))
                            .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                            .testTag("close_glass_studio_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close Glass Studio",
                            tint = Color.White
                        )
                    }
                }
            }

            // Section 1: iOS 27 Liquid Wallpapers
            item {
                LiquidGlassSurface(
                    config = config,
                    cornerRadius = 28.dp,
                    accentTint = Color(0xFF5CE1E6),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "iOS 27 Dynamic Liquid Wallpapers",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        wallpapers.forEach { wp ->
                            val isSelected = wp.id == config.wallpaperId
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(
                                        if (isSelected) wp.accentColor.copy(alpha = 0.24f)
                                        else Color.White.copy(alpha = 0.08f)
                                    )
                                    .border(
                                        width = if (isSelected) 1.4.dp else 0.8.dp,
                                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(18.dp)
                                    )
                                    .clickable { onSelectWallpaper(wp.id) }
                                    .padding(12.dp)
                                    .testTag("wallpaper_option_${wp.id}")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(
                                                brush = Brush.linearGradient(colors = wp.orbColors)
                                            )
                                            .border(1.dp, Color.White, CircleShape)
                                    )
                                    Column {
                                        Text(
                                            text = wp.name,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = wp.subtitle,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White.copy(alpha = 0.72f)
                                        )
                                    }
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = wp.accentColor
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section 2: Refraction & Blur Physics Sliders
            item {
                LiquidGlassSurface(
                    config = config,
                    cornerRadius = 28.dp,
                    accentTint = Color(0xFFA78BFA),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Optical Refraction & Blur Engine",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        GlassSliderRow(
                            icon = Icons.Filled.BlurOn,
                            label = "Frost Translucency",
                            valueText = "${(config.frostOpacity * 100).toInt()}%",
                            value = config.frostOpacity,
                            valueRange = 0.08f..0.55f,
                            accentColor = Color(0xFF5CE1E6),
                            onValueChange = onFrostOpacityChange
                        )
                        GlassSliderRow(
                            icon = Icons.Filled.AutoAwesome,
                            label = "Specular Rim Catchlight",
                            valueText = "${(config.specularStrength * 100).toInt()}%",
                            value = config.specularStrength,
                            valueRange = 0.20f..1.0f,
                            accentColor = Color(0xFFFBBF24),
                            onValueChange = onSpecularStrengthChange
                        )
                        GlassSliderRow(
                            icon = Icons.Filled.Tune,
                            label = "Chromatic Edge Dispersion",
                            valueText = "${(config.chromaticRim * 100).toInt()}%",
                            value = config.chromaticRim,
                            valueRange = 0.10f..1.0f,
                            accentColor = Color(0xFFF472B6),
                            onValueChange = onChromaticRimChange
                        )
                        GlassSliderRow(
                            icon = Icons.Filled.LensBlur,
                            label = "Overlay Depth Blur Radius",
                            valueText = "${config.backgroundBlurDp.toInt()} dp",
                            value = config.backgroundBlurDp,
                            valueRange = 4f..36f,
                            accentColor = Color(0xFFA78BFA),
                            onValueChange = onBlurDpChange
                        )
                        GlassSliderRow(
                            icon = Icons.Filled.RoundedCorner,
                            label = "Icon Squircle Curvature",
                            valueText = "${config.iconCornerPercent}%",
                            value = config.iconCornerPercent.toFloat(),
                            valueRange = 16f..50f,
                            accentColor = Color(0xFF34D399),
                            onValueChange = { onCornerPercentChange(it.toInt()) }
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "Fluid Spring Physics",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Low-damping iOS 27 elastic bounce on morph & tap",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            Switch(
                                checked = config.bouncySprings,
                                onCheckedChange = onBouncySpringsChange,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFF5CE1E6)
                                )
                            )
                        }
                    }
                }
            }

            // Section 3: Custom Home Screen Widgets Manager
            item {
                LiquidGlassSurface(
                    config = config,
                    cornerRadius = 28.dp,
                    accentTint = Color(0xFF34D399),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Widgets,
                                contentDescription = null,
                                tint = Color(0xFF34D399),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Pinned Spatial Widgets",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        LiquidWidgetType.entries.forEach { widgetType ->
                            val isPinned = enabledWidgets.contains(widgetType.id)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color.White.copy(alpha = 0.08f))
                                    .clickable { onToggleWidget(widgetType.id) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                                    .testTag("toggle_widget_${widgetType.id}")
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = widgetType.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = widgetType.subtitle,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                                Switch(
                                    checked = isPinned,
                                    onCheckedChange = { onToggleWidget(widgetType.id) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color(0xFF34D399)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BuiltInSpatialAppOverlay(
    appType: BuiltInAppType,
    config: GlassPhysicsConfig,
    weatherCities: List<WeatherCityData>,
    currentWeather: WeatherCityData,
    audioTracks: List<AudioTrackItem>,
    currentTrack: AudioTrackItem,
    isPlayingAudio: Boolean,
    audioProgressSec: Int,
    timerRemainingSec: Int,
    isTimerRunning: Boolean,
    spatialTasks: List<SpatialTaskItem>,
    quickNote: String,
    installedApps: List<LauncherApp>,
    batteryLevel: Int,
    thermalCelsius: Int,
    memoryPercent: Int,
    onSelectCity: (Int) -> Unit,
    onPlayPauseAudio: () -> Unit,
    onNextTrack: () -> Unit,
    onPrevTrack: () -> Unit,
    onSeekAudio: (Int) -> Unit,
    onToggleTimer: () -> Unit,
    onResetTimer: (Int) -> Unit,
    onToggleTask: (String) -> Unit,
    onAddTask: (String) -> Unit,
    onSaveQuickNote: (String) -> Unit,
    onOptimizeMemory: () -> Unit,
    onOpenAndroidSettings: () -> Unit,
    onOpenDefaultHomeChooser: () -> Unit,
    onLaunchInstalledApp: (LauncherApp) -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)

    val title = when (appType) {
        BuiltInAppType.SPATIAL_WEATHER -> "Spatial Weather Dome 27"
        BuiltInAppType.CHRONO_PULSE -> "Chrono Pulse & Flow Timer"
        BuiltInAppType.PRISM_AUDIO -> "Prism Spatial Audio Studio"
        BuiltInAppType.GLASS_NOTES -> "Glass Focus & Spatial Notes"
        BuiltInAppType.SYSTEM_TELEMETRY -> "Matrix Core & Device Packages"
        else -> "Spatial Experience"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF060A18).copy(alpha = 0.62f))
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(16.dp)
            .testTag("built_in_app_overlay")
    ) {
        LiquidGlassSurface(
            config = config,
            cornerRadius = 32.dp,
            accentTint = Color(0xFF5CE1E6),
            elevatedGlow = true,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Window Header with Close Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.16f))
                            .testTag("close_spatial_window_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close Window",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                when (appType) {
                    BuiltInAppType.SPATIAL_WEATHER -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    weatherCities.forEachIndexed { idx, cityData ->
                                        val selected = cityData.city == currentWeather.city
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .background(
                                                    if (selected) cityData.accentColor.copy(alpha = 0.35f)
                                                    else Color.White.copy(alpha = 0.10f)
                                                )
                                                .border(
                                                    1.dp,
                                                    if (selected) Color.White else Color.Transparent,
                                                    RoundedCornerShape(50)
                                                )
                                                .clickable { onSelectCity(idx) }
                                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                        ) {
                                            Text(
                                                text = cityData.city,
                                                style = MaterialTheme.typography.labelLarge,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }

                            item {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.WbSunny,
                                        contentDescription = null,
                                        tint = Color(0xFFFBBF24),
                                        modifier = Modifier.size(52.dp)
                                    )
                                    Text(
                                        text = "${currentWeather.tempCelsius}°C",
                                        style = MaterialTheme.typography.displayLarge,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${currentWeather.city} • ${currentWeather.condition}",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = currentWeather.accentColor
                                    )
                                    Text(
                                        text = "High ${currentWeather.highCelsius}°C  •  Low ${currentWeather.lowCelsius}°C",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.75f)
                                    )
                                }
                            }

                            item {
                                Text(
                                    text = "Hourly Refractive Forecast",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.Bottom,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(22.dp))
                                        .background(Color.White.copy(alpha = 0.08f))
                                        .padding(16.dp)
                                ) {
                                    currentWeather.hourlyTemps.forEachIndexed { idx, temp ->
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "${temp}°",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .width(18.dp)
                                                    .height((temp * 3).coerceIn(24, 90).dp)
                                                    .clip(RoundedCornerShape(50))
                                                    .background(
                                                        brush = Brush.verticalGradient(
                                                            colors = listOf(
                                                                currentWeather.accentColor,
                                                                Color.White.copy(alpha = 0.25f)
                                                            )
                                                        )
                                                    )
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = "+${idx + 1}h",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White.copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    BuiltInAppType.CHRONO_PULSE -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(18.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = formatDuration(timerRemainingSec),
                                style = MaterialTheme.typography.displayLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isTimerRunning) "Synced live with Top Dynamic Capsule" else "Select a duration & start spatial flow",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.75f)
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                listOf(15, 25, 45, 60).forEach { mins ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50))
                                            .background(Color.White.copy(alpha = 0.14f))
                                            .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(50))
                                            .clickable { onResetTimer(mins) }
                                            .padding(horizontal = 16.dp, vertical = 10.dp)
                                    ) {
                                        Text(
                                            text = "${mins}m",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                                Button(
                                    onClick = onToggleTimer,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFB7185)
                                    )
                                ) {
                                    Icon(
                                        imageVector = if (isTimerRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(if (isTimerRunning) "Pause Flow" else "Start Focus Timer")
                                }
                                Button(
                                    onClick = { onResetTimer(25) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White.copy(alpha = 0.18f)
                                    )
                                ) {
                                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Reset")
                                }
                            }
                        }
                    }

                    BuiltInAppType.PRISM_AUDIO -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = currentTrack.title,
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${currentTrack.artist} • ${currentTrack.spatialMode}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = currentTrack.accentColor
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    GlassSliderRow(
                                        icon = Icons.Filled.PlayArrow,
                                        label = "Track Position",
                                        valueText = "${formatDuration(audioProgressSec)} / ${formatDuration(currentTrack.durationSec)}",
                                        value = audioProgressSec.toFloat(),
                                        valueRange = 0f..currentTrack.durationSec.toFloat(),
                                        accentColor = currentTrack.accentColor,
                                        onValueChange = { onSeekAudio(it.toInt()) }
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(onClick = onPrevTrack) {
                                            Icon(Icons.Filled.SkipPrevious, "Previous", tint = Color.White)
                                        }
                                        IconButton(
                                            onClick = onPlayPauseAudio,
                                            modifier = Modifier
                                                .size(56.dp)
                                                .clip(CircleShape)
                                                .background(currentTrack.accentColor.copy(alpha = 0.4f))
                                        ) {
                                            Icon(
                                                if (isPlayingAudio) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                                "Play/Pause",
                                                tint = Color.White
                                            )
                                        }
                                        IconButton(onClick = onNextTrack) {
                                            Icon(Icons.Filled.SkipNext, "Next", tint = Color.White)
                                        }
                                    }
                                }
                            }
                            items(audioTracks) { track ->
                                val isCurrent = track.id == currentTrack.id
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(
                                            if (isCurrent) track.accentColor.copy(alpha = 0.25f)
                                            else Color.White.copy(alpha = 0.08f)
                                        )
                                        .padding(14.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = track.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${track.artist} • ${track.spatialMode}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White.copy(alpha = 0.72f)
                                        )
                                    }
                                    Text(
                                        text = formatDuration(track.durationSec),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    BuiltInAppType.GLASS_NOTES -> {
                        var newTaskInput by remember { mutableStateOf("") }
                        var memoInput by remember(quickNote) { mutableStateOf(quickNote) }

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = newTaskInput,
                                        onValueChange = { newTaskInput = it },
                                        placeholder = {
                                            Text("Add spatial task…", color = Color.White.copy(alpha = 0.6f))
                                        },
                                        singleLine = true,
                                        shape = RoundedCornerShape(18.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White,
                                            focusedBorderColor = Color(0xFF34D399),
                                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = {
                                            if (newTaskInput.isNotBlank()) {
                                                onAddTask(newTaskInput)
                                                newTaskInput = ""
                                            }
                                        },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color(0xFF34D399).copy(alpha = 0.35f))
                                    ) {
                                        Icon(Icons.Filled.Add, contentDescription = "Add Task", tint = Color.White)
                                    }
                                }
                            }

                            items(spatialTasks) { task ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White.copy(alpha = 0.08f))
                                        .clickable { onToggleTask(task.id) }
                                        .padding(12.dp)
                                ) {
                                    Icon(
                                        imageVector = if (task.completed) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = if (task.completed) Color(0xFF34D399) else Color.White
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = task.text,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Persistent Glass Scratchpad",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                OutlinedTextField(
                                    value = memoInput,
                                    onValueChange = {
                                        memoInput = it
                                        onSaveQuickNote(it)
                                    },
                                    shape = RoundedCornerShape(18.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = Color(0xFFA78BFA),
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                )
                            }
                        }
                    }

                    else -> {
                        // SYSTEM_TELEMETRY
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                NeuralTelemetryGlassWidget(
                                    batteryLevel = batteryLevel,
                                    thermalCelsius = thermalCelsius,
                                    memoryPercent = memoryPercent,
                                    config = config,
                                    onOptimizeMemory = onOptimizeMemory,
                                    onOpenTelemetryApp = {}
                                )
                            }
                            item {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Button(
                                        onClick = onOpenAndroidSettings,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF38BDF8).copy(alpha = 0.35f)
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Filled.Settings, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("System Settings")
                                    }
                                    Button(
                                        onClick = onOpenDefaultHomeChooser,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF34D399).copy(alpha = 0.35f)
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Filled.Home, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Set Default Home")
                                    }
                                }
                            }
                            item {
                                Text(
                                    text = "Discovered Android Packages (${installedApps.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            items(installedApps) { pkgApp ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White.copy(alpha = 0.08f))
                                        .clickable { onLaunchInstalledApp(pkgApp) }
                                        .padding(12.dp)
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = pkgApp.name,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = pkgApp.packageName ?: "",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White.copy(alpha = 0.65f)
                                        )
                                    }
                                    Text(
                                        text = "Launch →",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color(0xFF5CE1E6),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
