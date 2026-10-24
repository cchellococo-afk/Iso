package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GlassPhysicsConfig
import com.example.model.LauncherApp
import com.example.model.LiquidWallpaper
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LiquidWallpaperCanvas(
    wallpaper: LiquidWallpaper,
    isOverlayOpen: Boolean,
    overlayBlurDp: Float,
    brightnessLevel: Float,
    torchEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "liquid_wallpaper_motion")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orb_phase"
    )
    val secondaryPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ribbon_phase"
    )

    val animatedBlur by animateFloatAsState(
        targetValue = if (isOverlayOpen) overlayBlurDp else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "wallpaper_blur"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(if (animatedBlur > 0.5f) Modifier.blur(animatedBlur.dp) else Modifier)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val minDim = size.minDimension

            // Base Deep Spatial Gradient
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        wallpaper.baseColor,
                        wallpaper.orbColors.getOrElse(1) { wallpaper.baseColor }.copy(alpha = 0.32f),
                        wallpaper.baseColor
                    )
                )
            )

            // Floating Bioluminescent Liquid Glass Orbs
            val orb0 = wallpaper.orbColors.getOrElse(0) { Color.Cyan }
            val orb1 = wallpaper.orbColors.getOrElse(1) { Color.Magenta }
            val orb2 = wallpaper.orbColors.getOrElse(2) { Color.Blue }
            val orb3 = wallpaper.orbColors.getOrElse(3) { Color.Green }

            val center0 = Offset(
                x = w * (0.28f + 0.22f * cos(phase)),
                y = h * (0.22f + 0.14f * sin(phase))
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(orb0.copy(alpha = 0.62f), orb0.copy(alpha = 0.12f), Color.Transparent),
                    center = center0,
                    radius = minDim * 0.72f
                ),
                radius = minDim * 0.72f,
                center = center0
            )

            val center1 = Offset(
                x = w * (0.76f - 0.20f * sin(phase * 0.8f)),
                y = h * (0.46f + 0.18f * cos(phase * 0.8f))
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(orb1.copy(alpha = 0.56f), orb1.copy(alpha = 0.10f), Color.Transparent),
                    center = center1,
                    radius = minDim * 0.78f
                ),
                radius = minDim * 0.78f,
                center = center1
            )

            val center2 = Offset(
                x = w * (0.34f + 0.25f * sin(secondaryPhase)),
                y = h * (0.78f - 0.14f * cos(secondaryPhase))
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(orb2.copy(alpha = 0.52f), orb2.copy(alpha = 0.08f), Color.Transparent),
                    center = center2,
                    radius = minDim * 0.68f
                ),
                radius = minDim * 0.68f,
                center = center2
            )

            val center3 = Offset(
                x = w * (0.68f + 0.15f * cos(secondaryPhase)),
                y = h * (0.16f + 0.10f * sin(secondaryPhase))
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(orb3.copy(alpha = 0.45f), Color.Transparent),
                    center = center3,
                    radius = minDim * 0.52f
                ),
                radius = minDim * 0.52f,
                center = center3
            )

            // Refractive Caustic Glass Ribbon Curves (iOS 27 signature glass waves)
            val wavePath = Path().apply {
                moveTo(0f, h * (0.35f + 0.04f * sin(phase)))
                cubicTo(
                    w * 0.35f, h * (0.22f + 0.06f * cos(secondaryPhase)),
                    w * 0.65f, h * (0.48f - 0.05f * sin(phase)),
                    w, h * (0.32f + 0.04f * cos(phase))
                )
                lineTo(w, h * 0.65f)
                cubicTo(
                    w * 0.65f, h * (0.58f + 0.05f * sin(secondaryPhase)),
                    w * 0.30f, h * (0.74f - 0.04f * cos(phase)),
                    0f, h * 0.62f
                )
                close()
            }
            drawPath(
                path = wavePath,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.11f),
                        orb0.copy(alpha = 0.09f),
                        orb2.copy(alpha = 0.12f),
                        Color.Transparent
                    ),
                    start = Offset(0f, h * 0.25f),
                    end = Offset(w, h * 0.7f)
                )
            )

            // Specular Caustic Rim Line along the glass wave
            val rimCurve = Path().apply {
                moveTo(0f, h * (0.35f + 0.04f * sin(phase)))
                cubicTo(
                    w * 0.35f, h * (0.22f + 0.06f * cos(secondaryPhase)),
                    w * 0.65f, h * (0.48f - 0.05f * sin(phase)),
                    w, h * (0.32f + 0.04f * cos(phase))
                )
            }
            drawPath(
                path = rimCurve,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.32f),
                        orb0.copy(alpha = 0.42f),
                        Color.White.copy(alpha = 0.24f),
                        Color.Transparent
                    )
                ),
                style = Stroke(width = 2.5f, cap = StrokeCap.Round)
            )

            // Screen Torch / Brightness Atmospheric Overlay
            if (torchEnabled) {
                drawRect(color = Color.White.copy(alpha = 0.24f))
            }
            val dimAlpha = (1f - brightnessLevel).coerceIn(0f, 0.65f)
            if (dimAlpha > 0.01f) {
                drawRect(color = Color.Black.copy(alpha = dimAlpha))
            }
        }
    }
}

@Composable
fun LiquidGlassSurface(
    config: GlassPhysicsConfig,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 28.dp,
    accentTint: Color = Color(0xFF5CE1E6),
    elevatedGlow: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.965f else 1f,
        animationSpec = spring(
            dampingRatio = if (config.bouncySprings) Spring.DampingRatioMediumBouncy else Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "glass_press_scale"
    )

    val frostAlpha = config.frostOpacity.coerceIn(0.08f, 0.55f)
    val specAlpha = config.specularStrength.coerceIn(0.2f, 1.0f)
    val rimAlpha = config.chromaticRim.coerceIn(0.1f, 1.0f)

    val surfaceBackgroundBrush = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = (frostAlpha * 1.15f).coerceAtMost(0.65f)),
            accentTint.copy(alpha = (frostAlpha * 0.52f).coerceAtMost(0.42f)),
            Color(0xFF0F172A).copy(alpha = (frostAlpha * 1.45f).coerceAtMost(0.75f))
        ),
        start = Offset(0f, 0f),
        end = Offset(700f, 700f)
    )

    val borderBrush = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = (specAlpha * 0.88f).coerceAtMost(0.95f)),
            accentTint.copy(alpha = (rimAlpha * 0.72f).coerceAtMost(0.85f)),
            Color.White.copy(alpha = (specAlpha * 0.18f).coerceAtMost(0.35f)),
            accentTint.copy(alpha = (rimAlpha * 0.55f).coerceAtMost(0.75f))
        ),
        start = Offset(0f, 0f),
        end = Offset(600f, 600f)
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .background(brush = surfaceBackgroundBrush, shape = shape)
            .border(
                width = if (elevatedGlow) 1.5.dp else 1.1.dp,
                brush = borderBrush,
                shape = shape
            )
            .drawWithContent {
                // Top Specular Lens Sheen (simulates iOS 27 curved liquid edge refraction)
                val sheenHeight = size.height * 0.42f
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = specAlpha * 0.24f),
                            Color.White.copy(alpha = specAlpha * 0.04f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = sheenHeight
                    ),
                    size = Size(size.width, sheenHeight),
                    cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
                )
                drawContent()
            }
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = ripple(color = accentTint.copy(alpha = 0.4f)),
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            ),
        content = content
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LiquidAppIconItem(
    app: LauncherApp,
    config: GlassPhysicsConfig,
    isEditMode: Boolean,
    showLabel: Boolean = true,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "icon_press_scale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "jiggle_transition")
    val jiggleAngle by infiniteTransition.animateFloat(
        initialValue = -2.6f,
        targetValue = 2.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 150, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "jiggle_angle"
    )

    val cornerShape = RoundedCornerShape(percent = config.iconCornerPercent)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .minimumInteractiveComponentSize()
            .scale(pressScale)
            .graphicsLayer {
                rotationZ = if (isEditMode) jiggleAngle else 0f
            }
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick
            )
            .testTag("app_icon_${app.id}")
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            // Liquid Glass 3D Squircle Icon Container
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(60.dp)
                    .clip(cornerShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                app.primaryTint.copy(alpha = 0.48f),
                                app.secondaryTint.copy(alpha = 0.34f),
                                Color.White.copy(alpha = config.frostOpacity * 0.9f)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(160f, 160f)
                        ),
                        shape = cornerShape
                    )
                    .border(
                        width = 1.3.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = (config.specularStrength * 0.95f).coerceAtMost(1f)),
                                app.primaryTint.copy(alpha = 0.75f),
                                Color.White.copy(alpha = 0.22f)
                            )
                        ),
                        shape = cornerShape
                    )
                    .drawWithContent {
                        // Inner liquid droplet highlight at top of icon
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.42f * config.specularStrength),
                                    Color.Transparent
                                ),
                                startY = 0f,
                                endY = size.height * 0.55f
                            ),
                            cornerRadius = CornerRadius(20f, 20f)
                        )
                        drawContent()
                    }
            ) {
                // Subtle glowing core behind glyph
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    app.primaryTint.copy(alpha = 0.45f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                Icon(
                    imageVector = app.iconVector,
                    contentDescription = app.name,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Notification or Edit Badge
            if (isEditMode) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .offset(x = 4.dp, y = (-4).dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.88f))
                ) {
                    Text(
                        text = "✦",
                        color = Color(0xFF0F172A),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (app.badgeCount > 0) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .offset(x = 4.dp, y = (-4).dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFFB7185), Color(0xFFE11D48))
                            )
                        )
                        .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                ) {
                    Text(
                        text = app.badgeCount.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (showLabel) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = app.name,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.94f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(68.dp)
            )
        }
    }
}
