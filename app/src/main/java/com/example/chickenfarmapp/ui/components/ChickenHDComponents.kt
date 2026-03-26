package com.example.chickenfarmapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chickenfarmapp.ui.theme.*

@Composable
fun SunriseHeaderCard(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
        .background(Brush.verticalGradient(listOf(SunriseGradStart, SunriseGradEnd)))
        .padding(24.dp)
    ) {
        Column {
            Text(title, style = MaterialTheme.typography.headlineMedium.copy(
                color = Color.White, fontWeight = FontWeight.Bold
            ))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(0.85f)))
        }
    }
}

@Composable
fun EggStatCard(label: String, count: Int, color: Color, modifier: Modifier = Modifier) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.95f else 1f, spring(Spring.DampingRatioMediumBouncy), label = "egg_scale")
    Card(
        modifier = modifier.graphicsLayer { scaleX = scale; scaleY = scale }.shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(0.12f))
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🥚", fontSize = 32.sp)
            Spacer(Modifier.height(4.dp))
            Text("$count", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = color))
            Text(label, style = MaterialTheme.typography.bodySmall.copy(color = color.copy(0.8f)))
        }
    }
}

@Composable
fun ChickenHealthBadge(healthy: Boolean, modifier: Modifier = Modifier) {
    val color = if (healthy) GrassGreen40 else RoosterRed40
    val label = if (healthy) "Healthy" else "Attention"
    Surface(modifier = modifier.clip(RoundedCornerShape(50)), color = color.copy(0.15f)) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).clip(RoundedCornerShape(50)).background(color))
            Spacer(Modifier.width(6.dp))
            Text(label, style = MaterialTheme.typography.labelMedium.copy(color = color, fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
fun ShimmerChickenRow(modifier: Modifier = Modifier) {
    val t = rememberInfiniteTransition(label = "shimmer")
    val alpha by t.animateFloat(0.2f, 0.7f, infiniteRepeatable(tween(850), RepeatMode.Reverse), "alpha")
    Row(modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(52.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.onSurface.copy(alpha)))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Box(Modifier.fillMaxWidth(0.55f).height(14.dp).clip(RoundedCornerShape(7.dp)).background(MaterialTheme.colorScheme.onSurface.copy(alpha)))
            Spacer(Modifier.height(8.dp))
            Box(Modifier.fillMaxWidth(0.35f).height(10.dp).clip(RoundedCornerShape(5.dp)).background(MaterialTheme.colorScheme.onSurface.copy(alpha * 0.6f)))
        }
        Box(Modifier.size(24.dp).clip(RoundedCornerShape(50)).background(MaterialTheme.colorScheme.onSurface.copy(alpha * 0.4f)))
    }
}
