package dev.fiaz.calculator.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CalculatorButton(
    label: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineSmall,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else 1f,
        animationSpec = spring(stiffness = 500f),
        label = "buttonScale"
    )

    Surface(
        modifier = modifier
            .scale(scale)
            .semantics {
                role = Role.Button
                contentDescription = label
            },
        color = backgroundColor,
        tonalElevation = if (pressed) 0.dp else 2.dp,
        shape = RoundedCornerShape(28.dp) // Modern rounded look
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = androidx.compose.material3.ripple(
                        bounded = true,
                        color = contentColor
                    ),
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = textStyle.copy(fontWeight = FontWeight.Medium),
                color = contentColor
            )
        }
    }
}