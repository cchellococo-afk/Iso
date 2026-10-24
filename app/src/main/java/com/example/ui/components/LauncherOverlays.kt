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
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SpatialAudio
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.AppCategory
import com.example.model.GlassPhysicsConfig
import com.example.model.LauncherApp

@Composable
fun LiquidControlCenterOverlay(
    config: GlassPhysicsConfig,
    wifiEnabled: Boolean,
    bluetoothEnabled: Boolean,
    cellular5GEnabled: Boolean,
    focusModeEnabled: Boolean,
    torchEnabled: Boolean,
    spatialModeEnabled: Boolean,
    brightnessLevel: Float,
    volumeLevel: Float,
    onToggleWifi: () -> Unit,
    onToggleBluetooth: () -> Unit,
    onToggleCellular: () -> Unit,
    onToggleFocus: () -> Unit,
    onToggleTorch: () -> Unit,
    onToggleSpatial: () -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onFrostOpacityChange: (Float) -> Unit,
    onOpenGlassStudio: () -> Unit,
    onOpenHomeSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050914).copy(alpha = 0.56f))
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .testTag("control_center_overlay")
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "iOS 27 Control Center",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Liquid Glass Refraction & Connectivity",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.72f)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.16f))
                            .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                            .testTag("close_control_center_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close Control Center",
                            tint = Color.White
                        )
                    }
                }
            }

            item {
                // 2x3 Connectivity & Spatial Toggle Grid
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ControlToggleTile(
                            title = "Wi-Fi 7",
                            subtitle = if (wifiEnabled) "LiquidNet 6GHz" else "Off",
                            icon = Icons.Filled.Wifi,
                            active = wifiEnabled,
                            activeColor = Color(0xFF38BDF8),
                            config = config,
                            onClick = onToggleWifi,
                            modifier = Modifier.weight(1f)
                        )
                        ControlToggleTile(
                            title = "Bluetooth",
                            subtitle = if (bluetoothEnabled) "Spatial Pods Pro" else "Off",
                            icon = Icons.Filled.Bluetooth,
                            active = bluetoothEnabled,
                            activeColor = Color(0xFF6366F1),
                            config = config,
                            onClick = onToggleBluetooth,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ControlToggleTile(
                            title = "5G Ultra",
                            subtitle = if (cellular5GEnabled) "Low Latency" else "Standby",
                            icon = Icons.Filled.CellTower,
                            active = cellular5GEnabled,
                            activeColor = Color(0xFF34D399),
                            config = config,
                            onClick = onToggleCellular,
                            modifier = Modifier.weight(1f)
                        )
                        ControlToggleTile(
                            title = "Focus Mode",
                            subtitle = if (focusModeEnabled) "Deep Spatial" else "Standard",
                            icon = Icons.Filled.DoNotDisturbOn,
                            active = focusModeEnabled,
                            activeColor = Color(0xFFA78BFA),
                            config = config,
                            onClick = onToggleFocus,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ControlToggleTile(
                            title = "Screen Torch",
                            subtitle = if (torchEnabled) "Lumen Boost On" else "Off",
                            icon = Icons.Filled.FlashlightOn,
                            active = torchEnabled,
                            activeColor = Color(0xFFFBBF24),
                            config = config,
                            onClick = onToggleTorch,
                            modifier = Modifier.weight(1f)
                        )
                        ControlToggleTile(
                            title = "Spatial 360°",
                            subtitle = if (spatialModeEnabled) "Head Tracked" else "Stereo",
                            icon = Icons.Filled.SpatialAudio,
                            active = spatialModeEnabled,
                            activeColor = Color(0xFFF472B6),
                            config = config,
                            onClick = onToggleSpatial,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                // Liquid Glass Sliders Card (Brightness, Volume, Frost Refraction)
                LiquidGlassSurface(
                    config = config,
                    cornerRadius = 28.dp,
                    accentTint = Color(0xFF5CE1E6),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        GlassSliderRow(
                            icon = Icons.Filled.BrightnessHigh,
                            label = "Display Luminance",
                            valueText = "${(brightnessLevel * 100).toInt()}%",
                            value = brightnessLevel,
                            valueRange = 0.15f..1.0f,
                            accentColor = Color(0xFFFBBF24),
                            onValueChange = onBrightnessChange
                        )
                        GlassSliderRow(
                            icon = Icons.Filled.VolumeUp,
                            label = "Spatial Audio Gain",
                            valueText = "${(volumeLevel * 100).toInt()}%",
                            value = volumeLevel,
                            valueRange = 0.0f..1.0f,
                            accentColor = Color(0xFF38BDF8),
                            onValueChange = onVolumeChange
                        )
                        GlassSliderRow(
                            icon = Icons.Filled.Palette,
                            label = "Live Glass Frost Density",
                            valueText = "${(config.frostOpacity * 100).toInt()}%",
                            value = config.frostOpacity,
                            valueRange = 0.08f..0.55f,
                            accentColor = Color(0xFFA78BFA),
                            onValueChange = onFrostOpacityChange
                        )
                    }
                }
            }

            item {
                // Quick Actions Row: Open Glass Studio & Set Default Launcher
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LiquidGlassSurface(
                        config = config,
                        cornerRadius = 24.dp,
                        accentTint = Color(0xFF5CE1E6),
                        onClick = onOpenGlassStudio,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp, horizontal = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Palette,
                                contentDescription = null,
                                tint = Color(0xFF5CE1E6),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Glass Studio",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    LiquidGlassSurface(
                        config = config,
                        cornerRadius = 24.dp,
                        accentTint = Color(0xFF34D399),
                        onClick = onOpenHomeSettings,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp, horizontal = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = null,
                                tint = Color(0xFF34D399),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Default Home",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ControlToggleTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    active: Boolean,
    activeColor: Color,
    config: GlassPhysicsConfig,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassSurface(
        config = config,
        cornerRadius = 24.dp,
        accentTint = if (active) activeColor else Color.White,
        elevatedGlow = active,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (active) activeColor.copy(alpha = 0.38f)
                        else Color.White.copy(alpha = 0.12f)
                    )
                    .border(
                        1.dp,
                        if (active) Color.White.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.2f),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (active) activeColor else Color.White.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun GlassSliderRow(
    icon: ImageVector,
    label: String,
    valueText: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    accentColor: Color,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
            }
            Text(
                text = valueText,
                style = MaterialTheme.typography.labelLarge,
                color = accentColor,
                fontWeight = FontWeight.Bold
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = accentColor,
                inactiveTrackColor = Color.White.copy(alpha = 0.18f)
            )
        )
    }
}

@Composable
fun SpotlightAndAppLibraryOverlay(
    allApps: List<LauncherApp>,
    searchQuery: String,
    initialFocusSearch: Boolean,
    config: GlassPhysicsConfig,
    onSearchQueryChange: (String) -> Unit,
    onAppClick: (LauncherApp) -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)

    var selectedCategoryFilter by remember { mutableStateOf<AppCategory?>(null) }

    val filteredApps = remember(allApps, searchQuery, selectedCategoryFilter) {
        allApps.filter { app ->
            val matchesQuery = searchQuery.isBlank() ||
                app.name.contains(searchQuery, ignoreCase = true) ||
                app.category.title.contains(searchQuery, ignoreCase = true)
            val matchesCat = selectedCategoryFilter == null || app.category == selectedCategoryFilter
            matchesQuery && matchesCat
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF060A17).copy(alpha = 0.62f))
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("app_library_overlay")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Search Bar & Dismiss Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            text = if (initialFocusSearch) "Spotlight Search apps & optics…" else "Search App Library…",
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF5CE1E6)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.White.copy(alpha = 0.14f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.10f),
                        focusedBorderColor = Color(0xFF5CE1E6),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("spotlight_search_input")
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.16f))
                        .border(1.dp, Color.White.copy(alpha = 0.35f), CircleShape)
                        .testTag("close_app_library_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close App Library",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Category Filter Pills
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CategoryFilterChip(
                    label = "All (${allApps.size})",
                    selected = selectedCategoryFilter == null,
                    onClick = { selectedCategoryFilter = null }
                )
                AppCategory.entries.take(3).forEach { cat ->
                    CategoryFilterChip(
                        label = cat.title.substringBefore(" "),
                        selected = selectedCategoryFilter == cat,
                        onClick = {
                            selectedCategoryFilter = if (selectedCategoryFilter == cat) null else cat
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // If no search query & no filter, show iOS 27 Smart Glass Category Pods first
                if (searchQuery.isBlank() && selectedCategoryFilter == null) {
                    item {
                        Text(
                            text = "Spatial Smart Folders",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    val categories = AppCategory.entries.chunked(2)
                    items(categories) { rowCats ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowCats.forEach { category ->
                                val catApps = allApps.filter { it.category == category }
                                SmartFolderGlassCard(
                                    category = category,
                                    apps = catApps,
                                    config = config,
                                    onClick = { selectedCategoryFilter = category },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowCats.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "All Spatial & Installed Apps",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // App Rows (4 columns per row)
                val rows = filteredApps.chunked(4)
                items(rows) { rowApps ->
                    LiquidGlassSurface(
                        config = config,
                        cornerRadius = 26.dp,
                        accentTint = Color(0xFF38BDF8),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp, horizontal = 8.dp)
                        ) {
                            rowApps.forEach { app ->
                                LiquidAppIconItem(
                                    app = app,
                                    config = config,
                                    isEditMode = false,
                                    onClick = { onAppClick(app) },
                                    onLongClick = {}
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
private fun CategoryFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                if (selected) Color(0xFF5CE1E6).copy(alpha = 0.32f)
                else Color.White.copy(alpha = 0.10f)
            )
            .border(
                width = 1.dp,
                color = if (selected) Color(0xFF5CE1E6) else Color.White.copy(alpha = 0.24f),
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun SmartFolderGlassCard(
    category: AppCategory,
    apps: List<LauncherApp>,
    config: GlassPhysicsConfig,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = apps.firstOrNull()?.primaryTint ?: Color(0xFF5CE1E6)
    LiquidGlassSurface(
        config = config,
        cornerRadius = 26.dp,
        accentTint = accent,
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // 2x2 Mini App Icons Preview
            val previewApps = apps.take(4)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                previewApps.chunked(2).forEach { pair ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        pair.forEach { app ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                app.primaryTint.copy(alpha = 0.5f),
                                                app.secondaryTint.copy(alpha = 0.35f)
                                            )
                                        )
                                    )
                                    .border(
                                        0.8.dp,
                                        Color.White.copy(alpha = 0.55f),
                                        RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = app.iconVector,
                                    contentDescription = app.name,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${apps.size} apps • ${category.subtitle}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
