package com.example.chickenfarmapp.ui.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*

val farmEnterTransition: EnterTransition = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 4 }
val farmExitTransition: ExitTransition = fadeOut(tween(200)) + slideOutVertically(tween(200)) { -it / 4 }

@Composable
fun EggCountAnimation(count: Int, content: @Composable (Int) -> Unit) {
    val animated by animateIntAsState(count, tween(600), label = "egg_count")
    content(animated)
}

@Composable
fun ChickenCardScale(index: Int, modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier, content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { kotlinx.coroutines.delay(index * 70L); visible = true }
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(spring(Spring.DampingRatioMediumBouncy)) + fadeIn(tween(250)),
        modifier = modifier
    ) { content() }
}

@Composable
fun SunrisePulse(): Float {
    val t = rememberInfiniteTransition(label = "sunrise")
    return t.animateFloat(0.9f, 1.1f, infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse), "pulse").value
}
