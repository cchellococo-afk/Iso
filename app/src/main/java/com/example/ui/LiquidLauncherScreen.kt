package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.BuiltInAppType
import com.example.model.LiquidWidgetType
import com.example.model.OverlayMode
import com.example.ui.components.BuiltInSpatialAppOverlay
import com.example.ui.components.ChronoWeatherGlassWidget
import com.example.ui.components.GlassNotesFocusWidget
import com.example.ui.components.GlassStudioOverlay
import com.example.ui.components.LiquidAppIconItem
import com.example.ui.components.LiquidControlCenterOverlay
import com.example.ui.components.LiquidGlassSurface
import com.example.ui.components.LiquidTopStatusBarAndCapsule
import com.example.ui.components.LiquidWallpaperCanvas
import com.example.ui.components.NeuralTelemetryGlassWidget
import com.example.ui.components.PrismAudioGlassWidget
import com.example.ui.components.SpotlightAndAppLibraryOverlay
import kotlin.math.absoluteValue
import kotlinx.coroutines.launch

@Composable
fun LiquidLauncherScreen(
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val glassConfig by viewModel.glassConfig.collectAsStateWithLifecycle()
    val enabledWidgets by viewModel.enabledWidgets.collectAsStateWithLifecycle()
    val currentWallpaper by viewModel.currentWallpaper.collectAsStateWithLifecycle()
    val currentWeather by viewModel.currentCityWeather.collectAsStateWithLifecycle()
    val quickNote by viewModel.quickNote.collectAsStateWithLifecycle()

    val currentTrack = viewModel.getCurrentTrack(uiState.currentTrackIndex)
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        viewModel.setHomePage(pagerState.currentPage)
    }

    val isOverlayOpen = uiState.overlayMode != OverlayMode.NONE

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()
            .testTag("liquid_launcher_root")
    ) {
        // 1. Animated iOS 27 Liquid Glass Procedural Wallpaper with Real-Time Depth Blur
        LiquidWallpaperCanvas(
            wallpaper = currentWallpaper,
            isOverlayOpen = isOverlayOpen,
            overlayBlurDp = glassConfig.backgroundBlurDp,
            brightnessLevel = uiState.brightnessLevel,
            torchEnabled = uiState.torchEnabled
        )

        // 2. Main Foreground Home Screen Content (Scales & Recedes smoothly when overlay opens)
        val homeScale = if (isOverlayOpen) 0.94f else 1f
        val homeAlpha = if (isOverlayOpen) 0.25f else 1f

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .graphicsLayer {
                    scaleX = homeScale
                    scaleY = homeScale
                    alpha = homeAlpha
                }
        ) {
            // Top Status Bar & Morphing Dynamic Liquid Capsule
            LiquidTopStatusBarAndCapsule(
                timeText = uiState.currentTimeText,
                batteryLevel = uiState.batteryLevel,
                wifiEnabled = uiState.wifiEnabled,
                capsuleMode = uiState.capsuleMode,
                currentTrack = currentTrack,
                isPlayingAudio = uiState.isPlayingAudio,
                audioProgressSec = uiState.audioProgressSec,
                timerRemainingSec = uiState.timerRemainingSec,
                isTimerRunning = uiState.isTimerRunning,
                weather = currentWeather,
                config = glassConfig,
                onCapsuleClick = viewModel::cycleDynamicCapsuleMode,
                onCapsuleModeSelect = viewModel::setDynamicCapsuleMode,
                onPlayPauseAudio = viewModel::togglePlayPauseAudio,
                onNextTrack = viewModel::nextAudioTrack,
                onPrevTrack = viewModel::previousAudioTrack,
                onToggleTimer = viewModel::toggleFocusTimer,
                onResetTimer = { viewModel.resetFocusTimer(25) },
                onCycleCity = viewModel::cycleWeatherCity,
                onOpenControlCenter = { viewModel.openOverlay(OverlayMode.CONTROL_CENTER) }
            )

            // Jiggle Edit Mode Floating Banner (when active)
            AnimatedVisibility(visible = uiState.isEditMode) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.18f))
                        .border(1.dp, Color.White.copy(alpha = 0.55f), RoundedCornerShape(50))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("jiggle_edit_mode_banner")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Widgets,
                            contentDescription = null,
                            tint = Color(0xFF5CE1E6),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Edit Mode • Customize Glass & Widgets",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFF5CE1E6).copy(alpha = 0.35f))
                                .clickable { viewModel.openOverlay(OverlayMode.GLASS_STUDIO) }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "Studio",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFF34D399).copy(alpha = 0.40f))
                                .clickable { viewModel.exitEditMode() }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .testTag("done_edit_mode_button")
                        ) {
                            Text(
                                text = "Done",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Fluid Multi-Page Horizontal Pager with Parallax & Depth Physics
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .testTag("launcher_home_pager")
            ) { page ->
                val pageOffset = (
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                    ).absoluteValue

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            val scale = lerp(1f, 0.90f, pageOffset.coerceIn(0f, 1f))
                            scaleX = scale
                            scaleY = scale
                            alpha = lerp(1f, 0.50f, pageOffset.coerceIn(0f, 1f))
                        }
                ) {
                    if (page == 0) {
                        // PAGE 0: Primary Spatial Widgets + Core App Grid
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (enabledWidgets.contains(LiquidWidgetType.CHRONO_WEATHER.id)) {
                                item {
                                    ChronoWeatherGlassWidget(
                                        timeText = uiState.currentTimeText,
                                        dateText = uiState.currentDateText,
                                        weather = currentWeather,
                                        config = glassConfig,
                                        onCycleCity = viewModel::cycleWeatherCity,
                                        onOpenWeatherApp = {
                                            viewModel.openBuiltInExperience(BuiltInAppType.SPATIAL_WEATHER)
                                        }
                                    )
                                }
                            }

                            if (enabledWidgets.contains(LiquidWidgetType.PRISM_AUDIO.id)) {
                                item {
                                    PrismAudioGlassWidget(
                                        track = currentTrack,
                                        isPlaying = uiState.isPlayingAudio,
                                        progressSec = uiState.audioProgressSec,
                                        isSpatialHeadTracked = uiState.isSpatialAudioHeadTracked,
                                        config = glassConfig,
                                        onPlayPause = viewModel::togglePlayPauseAudio,
                                        onNext = viewModel::nextAudioTrack,
                                        onPrev = viewModel::previousAudioTrack,
                                        onToggleSpatial = viewModel::toggleSpatialAudioHeadTracked,
                                        onOpenAudioApp = {
                                            viewModel.openBuiltInExperience(BuiltInAppType.PRISM_AUDIO)
                                        }
                                    )
                                }
                            }

                            // 2x4 Spatial App Icon Grid on Home Screen
                            val homeRows = uiState.homePageApps.chunked(4)
                            items(homeRows) { rowApps ->
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    rowApps.forEach { app ->
                                        LiquidAppIconItem(
                                            app = app,
                                            config = glassConfig,
                                            isEditMode = uiState.isEditMode,
                                            onClick = { viewModel.onAppClick(app) },
                                            onLongClick = { viewModel.toggleEditMode() }
                                        )
                                    }
                                }
                            }

                            if (enabledWidgets.contains(LiquidWidgetType.NEURAL_TELEMETRY.id)) {
                                item {
                                    NeuralTelemetryGlassWidget(
                                        batteryLevel = uiState.batteryLevel,
                                        thermalCelsius = uiState.thermalCelsius,
                                        memoryPercent = uiState.memoryUsagePercent,
                                        config = glassConfig,
                                        onOptimizeMemory = viewModel::optimizeMemoryMatrix,
                                        onOpenTelemetryApp = {
                                            viewModel.openBuiltInExperience(BuiltInAppType.SYSTEM_TELEMETRY)
                                        }
                                    )
                                }
                            }

                            if (enabledWidgets.contains(LiquidWidgetType.GLASS_NOTES.id)) {
                                item {
                                    GlassNotesFocusWidget(
                                        tasks = uiState.spatialTasks,
                                        config = glassConfig,
                                        onToggleTask = viewModel::toggleSpatialTask,
                                        onOpenNotesApp = {
                                            viewModel.openBuiltInExperience(BuiltInAppType.GLASS_NOTES)
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        // PAGE 1: Quick Liquid Refraction Deck + Installed Android Apps & Smart Vault
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                LiquidGlassSurface(
                                    config = glassConfig,
                                    cornerRadius = 28.dp,
                                    accentTint = currentWallpaper.accentColor,
                                    elevatedGlow = true,
                                    onClick = { viewModel.openOverlay(OverlayMode.GLASS_STUDIO) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(18.dp)
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Active Theme: ${currentWallpaper.name}",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "Frost ${(glassConfig.frostOpacity * 100).toInt()}% • Specular ${(glassConfig.specularStrength * 100).toInt()}% • Blur ${glassConfig.backgroundBlurDp.toInt()}dp",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color.White.copy(alpha = 0.78f)
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .background(currentWallpaper.accentColor.copy(alpha = 0.32f))
                                                .border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(50))
                                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                        ) {
                                            Text(
                                                text = "Tune Glass",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            item {
                                GlassNotesFocusWidget(
                                    tasks = uiState.spatialTasks,
                                    config = glassConfig,
                                    onToggleTask = viewModel::toggleSpatialTask,
                                    onOpenNotesApp = {
                                        viewModel.openBuiltInExperience(BuiltInAppType.GLASS_NOTES)
                                    }
                                )
                            }

                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "All Installed & Spatial Apps (${uiState.allApps.size})",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Open Vault →",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color(0xFF5CE1E6),
                                        modifier = Modifier.clickable {
                                            viewModel.openOverlay(OverlayMode.APP_LIBRARY)
                                        }
                                    )
                                }
                            }

                            val page2Rows = uiState.allApps.chunked(4)
                            items(page2Rows) { rowApps ->
                                LiquidGlassSurface(
                                    config = glassConfig,
                                    cornerRadius = 26.dp,
                                    accentTint = Color(0xFF38BDF8),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp, horizontal = 6.dp)
                                    ) {
                                        rowApps.forEach { app ->
                                            LiquidAppIconItem(
                                                app = app,
                                                config = glassConfig,
                                                isEditMode = uiState.isEditMode,
                                                onClick = { viewModel.onAppClick(app) },
                                                onLongClick = { viewModel.toggleEditMode() }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Status Toast Banner (when memory optimized or action triggered)
            AnimatedVisibility(visible = uiState.statusToastMessage != null) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF0F172A).copy(alpha = 0.85f))
                        .border(1.dp, Color(0xFF34D399), RoundedCornerShape(50))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = uiState.statusToastMessage ?: "",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF34D399),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // iOS 27 Translucent Page Dots + Spotlight Search Pill + Edit Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                // Page Switcher Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.14f))
                        .border(0.8.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(50))
                        .clickable {
                            val nextPage = if (pagerState.currentPage == 0) 1 else 0
                            coroutineScope.launch { pagerState.animateScrollToPage(nextPage) }
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("page_indicator_pill")
                ) {
                    repeat(2) { idx ->
                        val active = pagerState.currentPage == idx
                        Box(
                            modifier = Modifier
                                .size(if (active) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (active) Color.White else Color.White.copy(alpha = 0.4f)
                                )
                        )
                    }
                }

                // iOS 27 Glass Spotlight Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.16f))
                        .border(0.8.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(50))
                        .clickable { viewModel.openOverlay(OverlayMode.SPOTLIGHT_SEARCH) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("home_spotlight_pill")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Spotlight Search",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Customize / Jiggle Mode Trigger Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (uiState.isEditMode) Color(0xFF5CE1E6).copy(alpha = 0.35f)
                            else Color.White.copy(alpha = 0.14f)
                        )
                        .border(0.8.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(50))
                        .clickable { viewModel.toggleEditMode() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("customize_home_pill")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Tune,
                        contentDescription = "Customize Home",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = if (uiState.isEditMode) "Editing" else "Edit",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // 3. Floating iOS 27 Refractive Liquid Glass Dock
            LiquidGlassSurface(
                config = glassConfig,
                cornerRadius = 34.dp,
                accentTint = currentWallpaper.accentColor,
                elevatedGlow = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("liquid_glass_dock")
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 12.dp)
                ) {
                    uiState.dockApps.forEach { dockApp ->
                        LiquidAppIconItem(
                            app = dockApp,
                            config = glassConfig,
                            isEditMode = uiState.isEditMode,
                            showLabel = true,
                            onClick = { viewModel.onAppClick(dockApp) },
                            onLongClick = { viewModel.toggleEditMode() }
                        )
                    }
                }
            }
        }

        // 4. Fluid Spring-Animated Overlays
        val enterAnim = scaleIn(
            initialScale = 0.90f,
            animationSpec = spring(
                dampingRatio = if (glassConfig.bouncySprings) Spring.DampingRatioLowBouncy else Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        ) + fadeIn(animationSpec = tween(200))

        val exitAnim = scaleOut(
            targetScale = 0.92f,
            animationSpec = tween(180)
        ) + fadeOut(animationSpec = tween(180))

        AnimatedVisibility(
            visible = uiState.overlayMode == OverlayMode.CONTROL_CENTER,
            enter = enterAnim,
            exit = exitAnim
        ) {
            LiquidControlCenterOverlay(
                config = glassConfig,
                wifiEnabled = uiState.wifiEnabled,
                bluetoothEnabled = uiState.bluetoothEnabled,
                cellular5GEnabled = uiState.cellular5GEnabled,
                focusModeEnabled = uiState.focusModeEnabled,
                torchEnabled = uiState.torchEnabled,
                spatialModeEnabled = uiState.spatialModeEnabled,
                brightnessLevel = uiState.brightnessLevel,
                volumeLevel = uiState.volumeLevel,
                onToggleWifi = viewModel::toggleWifi,
                onToggleBluetooth = viewModel::toggleBluetooth,
                onToggleCellular = viewModel::toggleCellular5G,
                onToggleFocus = viewModel::toggleFocusMode,
                onToggleTorch = viewModel::toggleTorch,
                onToggleSpatial = viewModel::toggleSpatialMode,
                onBrightnessChange = viewModel::setBrightness,
                onVolumeChange = viewModel::setVolume,
                onFrostOpacityChange = viewModel::setFrostOpacity,
                onOpenGlassStudio = { viewModel.openOverlay(OverlayMode.GLASS_STUDIO) },
                onOpenHomeSettings = viewModel::launchDefaultHomeChooser,
                onDismiss = viewModel::closeOverlays
            )
        }

        AnimatedVisibility(
            visible = uiState.overlayMode == OverlayMode.SPOTLIGHT_SEARCH ||
                uiState.overlayMode == OverlayMode.APP_LIBRARY,
            enter = enterAnim,
            exit = exitAnim
        ) {
            SpotlightAndAppLibraryOverlay(
                allApps = uiState.allApps,
                searchQuery = uiState.searchQuery,
                initialFocusSearch = uiState.overlayMode == OverlayMode.SPOTLIGHT_SEARCH,
                config = glassConfig,
                onSearchQueryChange = viewModel::updateSearchQuery,
                onAppClick = { app ->
                    viewModel.closeOverlays()
                    viewModel.onAppClick(app)
                },
                onDismiss = viewModel::closeOverlays
            )
        }

        AnimatedVisibility(
            visible = uiState.overlayMode == OverlayMode.GLASS_STUDIO,
            enter = enterAnim,
            exit = exitAnim
        ) {
            GlassStudioOverlay(
                config = glassConfig,
                wallpapers = viewModel.repository.wallpapers,
                enabledWidgets = enabledWidgets,
                onSelectWallpaper = viewModel::selectWallpaper,
                onFrostOpacityChange = viewModel::setFrostOpacity,
                onSpecularStrengthChange = viewModel::setSpecularStrength,
                onChromaticRimChange = viewModel::setChromaticRim,
                onBlurDpChange = viewModel::setBackgroundBlurDp,
                onCornerPercentChange = viewModel::setIconCornerPercent,
                onBouncySpringsChange = viewModel::setBouncySprings,
                onToggleWidget = viewModel::toggleWidgetEnabled,
                onDismiss = viewModel::closeOverlays
            )
        }

        AnimatedVisibility(
            visible = uiState.overlayMode == OverlayMode.BUILT_IN_APP,
            enter = enterAnim,
            exit = exitAnim
        ) {
            BuiltInSpatialAppOverlay(
                appType = uiState.activeBuiltInApp,
                config = glassConfig,
                weatherCities = viewModel.repository.weatherCities,
                currentWeather = currentWeather,
                audioTracks = viewModel.repository.audioTracks,
                currentTrack = currentTrack,
                isPlayingAudio = uiState.isPlayingAudio,
                audioProgressSec = uiState.audioProgressSec,
                timerRemainingSec = uiState.timerRemainingSec,
                isTimerRunning = uiState.isTimerRunning,
                spatialTasks = uiState.spatialTasks,
                quickNote = quickNote,
                installedApps = uiState.installedSystemApps,
                batteryLevel = uiState.batteryLevel,
                thermalCelsius = uiState.thermalCelsius,
                memoryPercent = uiState.memoryUsagePercent,
                onSelectCity = viewModel::selectWeatherCity,
                onPlayPauseAudio = viewModel::togglePlayPauseAudio,
                onNextTrack = viewModel::nextAudioTrack,
                onPrevTrack = viewModel::previousAudioTrack,
                onSeekAudio = viewModel::seekAudio,
                onToggleTimer = viewModel::toggleFocusTimer,
                onResetTimer = viewModel::resetFocusTimer,
                onToggleTask = viewModel::toggleSpatialTask,
                onAddTask = viewModel::addSpatialTask,
                onSaveQuickNote = viewModel::saveQuickNote,
                onOptimizeMemory = viewModel::optimizeMemoryMatrix,
                onOpenAndroidSettings = viewModel::launchAndroidSettings,
                onOpenDefaultHomeChooser = viewModel::launchDefaultHomeChooser,
                onLaunchInstalledApp = { app ->
                    viewModel.closeOverlays()
                    viewModel.onAppClick(app)
                },
                onDismiss = viewModel::closeOverlays
            )
        }
    }
}
