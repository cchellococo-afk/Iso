package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.SpatialAudioOff
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AudioTrackItem
import com.example.model.DynamicCapsuleMode
import com.example.model.GlassPhysicsConfig
import com.example.model.SpatialTaskItem
import com.example.model.WeatherCityData
import java.util.Locale

@Composable
fun LiquidTopStatusBarAndCapsule(
    timeText: String,
    batteryLevel: Int,
    wifiEnabled: Boolean,
    capsuleMode: DynamicCapsuleMode,
    currentTrack: AudioTrackItem,
    isPlayingAudio: Boolean,
    audioProgressSec: Int,
    timerRemainingSec: Int,
    isTimerRunning: Boolean,
    weather: WeatherCityData,
    config: GlassPhysicsConfig,
    onCapsuleClick: () -> Unit,
    onCapsuleModeSelect: (DynamicCapsuleMode) -> Unit,
    onPlayPauseAudio: () -> Unit,
    onNextTrack: () -> Unit,
    onPrevTrack: () -> Unit,
    onToggleTimer: () -> Unit,
    onResetTimer: () -> Unit,
    onCycleCity: () -> Unit,
    onOpenControlCenter: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // Top Row: Time Pill, Dynamic Island Capsule (Compact), Control Center Pill
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Left Status Time Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.10f))
                    .border(0.8.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(50))
                    .clickable { onCapsuleModeSelect(DynamicCapsuleMode.TIMER_EXPANDED) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // Center Dynamic Liquid Capsule Trigger
            LiquidGlassSurface(
                config = config,
                cornerRadius = 26.dp,
                accentTint = currentTrack.accentColor,
                elevatedGlow = true,
                onClick = onCapsuleClick,
                modifier = Modifier
                    .testTag("dynamic_liquid_capsule")
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = if (config.bouncySprings) Spring.DampingRatioLowBouncy else Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    AnimatedAudioEqualizerDots(
                        isPlaying = isPlayingAudio,
                        accentColor = currentTrack.accentColor
                    )
                    Text(
                        text = if (isTimerRunning) {
                            formatDuration(timerRemainingSec)
                        } else {
                            currentTrack.title
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(96.dp)
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(weather.accentColor.copy(alpha = 0.28f))
                    ) {
                        Text(
                            text = "${weather.tempCelsius}°",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Right Control Center Status Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(0.8.dp, Color.White.copy(alpha = 0.30f), RoundedCornerShape(50))
                    .clickable(onClick = onOpenControlCenter)
                    .padding(horizontal = 11.dp, vertical = 6.dp)
                    .testTag("control_center_button")
            ) {
                if (wifiEnabled) {
                    Icon(
                        imageVector = Icons.Filled.Wifi,
                        contentDescription = "Wi-Fi Connected",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Filled.BatteryChargingFull,
                    contentDescription = "Battery $batteryLevel percent",
                    tint = Color(0xFF34D399),
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "$batteryLevel%",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Expanded Morphing Dynamic Capsule Drawer
        AnimatedContent(
            targetState = capsuleMode,
            transitionSpec = {
                fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
            },
            label = "capsule_expanded_content"
        ) { mode ->
            if (mode != DynamicCapsuleMode.COMPACT) {
                Spacer(modifier = Modifier.height(8.dp))
                LiquidGlassSurface(
                    config = config,
                    cornerRadius = 28.dp,
                    accentTint = when (mode) {
                        DynamicCapsuleMode.AUDIO_EXPANDED -> currentTrack.accentColor
                        DynamicCapsuleMode.TIMER_EXPANDED -> Color(0xFFFB7185)
                        DynamicCapsuleMode.WEATHER_EXPANDED -> weather.accentColor
                        DynamicCapsuleMode.COMPACT -> Color.Cyan
                    },
                    elevatedGlow = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .testTag("expanded_dynamic_capsule")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Mode Switcher Pills inside Expanded Capsule
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                CapsuleTabChip(
                                    label = "Spatial Audio",
                                    selected = mode == DynamicCapsuleMode.AUDIO_EXPANDED,
                                    onClick = { onCapsuleModeSelect(DynamicCapsuleMode.AUDIO_EXPANDED) }
                                )
                                CapsuleTabChip(
                                    label = "Focus Pulse",
                                    selected = mode == DynamicCapsuleMode.TIMER_EXPANDED,
                                    onClick = { onCapsuleModeSelect(DynamicCapsuleMode.TIMER_EXPANDED) }
                                )
                                CapsuleTabChip(
                                    label = "Sky Dome",
                                    selected = mode == DynamicCapsuleMode.WEATHER_EXPANDED,
                                    onClick = { onCapsuleModeSelect(DynamicCapsuleMode.WEATHER_EXPANDED) }
                                )
                            }
                            Text(
                                text = "Collapse",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.75f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .clickable { onCapsuleModeSelect(DynamicCapsuleMode.COMPACT) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        when (mode) {
                            DynamicCapsuleMode.AUDIO_EXPANDED -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = currentTrack.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${currentTrack.artist} • ${currentTrack.spatialMode}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White.copy(alpha = 0.76f)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        LinearProgressIndicator(
                                            progress = {
                                                (audioProgressSec.toFloat() / currentTrack.durationSec.toFloat())
                                                    .coerceIn(0f, 1f)
                                            },
                                            color = currentTrack.accentColor,
                                            trackColor = Color.White.copy(alpha = 0.18f),
                                            modifier = Modifier
                                                .fillMaxWidth(0.9f)
                                                .height(5.dp)
                                                .clip(RoundedCornerShape(50))
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = onPrevTrack) {
                                            Icon(
                                                imageVector = Icons.Filled.SkipPrevious,
                                                contentDescription = "Previous Track",
                                                tint = Color.White
                                            )
                                        }
                                        IconButton(
                                            onClick = onPlayPauseAudio,
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(CircleShape)
                                                .background(currentTrack.accentColor.copy(alpha = 0.35f))
                                        ) {
                                            Icon(
                                                imageVector = if (isPlayingAudio) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                                contentDescription = if (isPlayingAudio) "Pause" else "Play",
                                                tint = Color.White
                                            )
                                        }
                                        IconButton(onClick = onNextTrack) {
                                            Icon(
                                                imageVector = Icons.Filled.SkipNext,
                                                contentDescription = "Next Track",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            }

                            DynamicCapsuleMode.TIMER_EXPANDED -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column {
                                        Text(
                                            text = formatDuration(timerRemainingSec),
                                            style = MaterialTheme.typography.displaySmall,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = if (isTimerRunning) "Deep Spatial Flow Active" else "Focus Session Ready",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White.copy(alpha = 0.76f)
                                        )
                                    }
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        IconButton(
                                            onClick = onToggleTimer,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(Color(0xFFFB7185).copy(alpha = 0.35f))
                                        ) {
                                            Icon(
                                                imageVector = if (isTimerRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                                contentDescription = "Toggle Focus Timer",
                                                tint = Color.White
                                            )
                                        }
                                        IconButton(
                                            onClick = onResetTimer,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(Color.White.copy(alpha = 0.14f))
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Refresh,
                                                contentDescription = "Reset Focus Timer",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            }

                            DynamicCapsuleMode.WEATHER_EXPANDED -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(onClick = onCycleCity)
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Filled.LocationOn,
                                                contentDescription = null,
                                                tint = weather.accentColor,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${weather.city} • ${weather.condition}",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = Color.White
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Humidity ${weather.humidity}% • UV ${weather.uvIndex} • Wind ${weather.windKmh} km/h (Tap for next city)",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White.copy(alpha = 0.75f)
                                        )
                                    }
                                    Text(
                                        text = "${weather.tempCelsius}°",
                                        style = MaterialTheme.typography.displaySmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            DynamicCapsuleMode.COMPACT -> Unit
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CapsuleTabChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                if (selected) Color.White.copy(alpha = 0.26f)
                else Color.White.copy(alpha = 0.08f)
            )
            .border(
                width = 0.8.dp,
                color = if (selected) Color.White.copy(alpha = 0.7f) else Color.Transparent,
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun AnimatedAudioEqualizerDots(
    isPlaying: Boolean,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "eq_bars")
    val bar1 by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(420, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "eq1"
    )
    val bar2 by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(360, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "eq2"
    )
    val bar3 by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(510, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "eq3"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.5.dp),
        modifier = modifier.height(14.dp)
    ) {
        val heights = if (isPlaying) listOf(bar1, bar2, bar3) else listOf(0.35f, 0.35f, 0.35f)
        heights.forEach { frac ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height((14f * frac).dp)
                    .clip(RoundedCornerShape(50))
                    .background(accentColor)
            )
        }
    }
}

@Composable
fun ChronoWeatherGlassWidget(
    timeText: String,
    dateText: String,
    weather: WeatherCityData,
    config: GlassPhysicsConfig,
    onCycleCity: () -> Unit,
    onOpenWeatherApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassSurface(
        config = config,
        cornerRadius = 30.dp,
        accentTint = weather.accentColor,
        elevatedGlow = true,
        onClick = onOpenWeatherApp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("widget_chrono_weather")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Left: iOS 27 Refractive Time & Date
                Column {
                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.labelMedium,
                        color = weather.accentColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = timeText,
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Liquid Glass OS 27 • Spatial Edition",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.68f)
                    )
                }

                // Right: Interactive Weather Dome Pill
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color.White.copy(alpha = 0.10f))
                        .border(0.8.dp, Color.White.copy(alpha = 0.28f), RoundedCornerShape(22.dp))
                        .clickable(onClick = onCycleCity)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .testTag("weather_city_cycle_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.WbSunny,
                            contentDescription = weather.condition,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${weather.tempCelsius}°C",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = weather.city,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                    Text(
                        text = "H:${weather.highCelsius}° L:${weather.lowCelsius}° • Tap city",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.72f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom: Hourly Refractive Bar Forecast & Telemetry Pills
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                WeatherMetricPill(
                    icon = Icons.Filled.WaterDrop,
                    label = "${weather.humidity}% Hum",
                    tint = Color(0xFF38BDF8)
                )
                WeatherMetricPill(
                    icon = Icons.Filled.WbSunny,
                    label = "UV ${weather.uvIndex}",
                    tint = Color(0xFFFBBF24)
                )
                WeatherMetricPill(
                    icon = Icons.Filled.Air,
                    label = "${weather.windKmh} km/h",
                    tint = Color(0xFF34D399)
                )
                WeatherMetricPill(
                    icon = Icons.Filled.Bolt,
                    label = "AQI ${weather.aqi}",
                    tint = Color(0xFFA78BFA)
                )
            }
        }
    }
}

@Composable
private fun WeatherMetricPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.09f))
            .padding(horizontal = 9.dp, vertical = 5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(13.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.9f)
        )
    }
}

@Composable
fun PrismAudioGlassWidget(
    track: AudioTrackItem,
    isPlaying: Boolean,
    progressSec: Int,
    isSpatialHeadTracked: Boolean,
    config: GlassPhysicsConfig,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onToggleSpatial: () -> Unit,
    onOpenAudioApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassSurface(
        config = config,
        cornerRadius = 28.dp,
        accentTint = track.accentColor,
        onClick = onOpenAudioApp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("widget_prism_audio")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Refractive Vinyl / Spatial Orb
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(track.accentColor, track.secondaryColor)
                                )
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.GraphicEq,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = track.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${track.artist} • ${track.album}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.74f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Spatial 3D Audio Chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSpatialHeadTracked) track.accentColor.copy(alpha = 0.30f)
                            else Color.White.copy(alpha = 0.10f)
                        )
                        .border(
                            0.8.dp,
                            if (isSpatialHeadTracked) track.accentColor else Color.White.copy(alpha = 0.25f),
                            RoundedCornerShape(50)
                        )
                        .clickable(onClick = onToggleSpatial)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (isSpatialHeadTracked) Icons.Filled.Headphones else Icons.Filled.SpatialAudioOff,
                        contentDescription = "Toggle Spatial Audio",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = if (isSpatialHeadTracked) "360° Spatial" else "Stereo",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar + Transport Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    LinearProgressIndicator(
                        progress = {
                            (progressSec.toFloat() / track.durationSec.toFloat()).coerceIn(0f, 1f)
                        },
                        color = track.accentColor,
                        trackColor = Color.White.copy(alpha = 0.16f),
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .height(5.dp)
                            .clip(RoundedCornerShape(50))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(0.92f)
                    ) {
                        Text(
                            text = formatDuration(progressSec),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            text = formatDuration(track.durationSec),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onPrev,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SkipPrevious,
                            contentDescription = "Previous",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = onPlayPause,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(track.accentColor.copy(alpha = 0.38f))
                            .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                            .testTag("audio_play_pause_button")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = onNext,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SkipNext,
                            contentDescription = "Next",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NeuralTelemetryGlassWidget(
    batteryLevel: Int,
    thermalCelsius: Int,
    memoryPercent: Int,
    config: GlassPhysicsConfig,
    onOptimizeMemory: () -> Unit,
    onOpenTelemetryApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassSurface(
        config = config,
        cornerRadius = 28.dp,
        accentTint = Color(0xFF34D399),
        onClick = onOpenTelemetryApp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("widget_neural_telemetry")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Memory,
                        contentDescription = null,
                        tint = Color(0xFF34D399),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Liquid Glass Telemetry Matrix",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF34D399).copy(alpha = 0.24f))
                        .border(0.8.dp, Color(0xFF34D399).copy(alpha = 0.7f), RoundedCornerShape(50))
                        .clickable(onClick = onOptimizeMemory)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("optimize_glass_buffer_button")
                ) {
                    Text(
                        text = "Boost Buffer",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularGlassGauge(
                    valueText = "$batteryLevel%",
                    label = "Power",
                    progress = batteryLevel / 100f,
                    color = Color(0xFF34D399)
                )
                CircularGlassGauge(
                    valueText = "${thermalCelsius}°C",
                    label = "Thermal",
                    progress = (thermalCelsius / 50f).coerceIn(0.2f, 1f),
                    color = Color(0xFF38BDF8)
                )
                CircularGlassGauge(
                    valueText = "$memoryPercent%",
                    label = "Glass RAM",
                    progress = memoryPercent / 100f,
                    color = Color(0xFFA78BFA)
                )
                CircularGlassGauge(
                    valueText = "120Hz",
                    label = "Fluidity",
                    progress = 0.96f,
                    color = Color(0xFFF472B6)
                )
            }
        }
    }
}

@Composable
private fun CircularGlassGauge(
    valueText: String,
    label: String,
    progress: Float,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(56.dp)
        ) {
            Canvas(modifier = Modifier.size(56.dp)) {
                drawArc(
                    color = Color.White.copy(alpha = 0.14f),
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(color.copy(alpha = 0.7f), color, Color.White)
                    ),
                    startAngle = 135f,
                    sweepAngle = 270f * progress.coerceIn(0f, 1f),
                    useCenter = false,
                    style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Text(
                text = valueText,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.75f)
        )
    }
}

@Composable
fun GlassNotesFocusWidget(
    tasks: List<SpatialTaskItem>,
    config: GlassPhysicsConfig,
    onToggleTask: (String) -> Unit,
    onOpenNotesApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassSurface(
        config = config,
        cornerRadius = 28.dp,
        accentTint = Color(0xFFA78BFA),
        onClick = onOpenNotesApp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("widget_glass_notes")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Spatial Focus & Quick Flow",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${tasks.count { it.completed }}/${tasks.size} Done",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF5CE1E6),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            tasks.take(3).forEach { task ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onToggleTask(task.id) }
                        .padding(vertical = 6.dp, horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = if (task.completed) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                        contentDescription = task.text,
                        tint = if (task.completed) Color(0xFF34D399) else Color.White.copy(alpha = 0.65f),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = task.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (task.completed) Color.White.copy(alpha = 0.55f) else Color.White,
                        textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = task.tag,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

fun formatDuration(totalSeconds: Int): String {
    val mins = (totalSeconds / 60).coerceAtLeast(0)
    val secs = (totalSeconds % 60).coerceAtLeast(0)
    return String.format(Locale.US, "%02d:%02d", mins, secs)
}
