package com.rizz.focusbox.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.PreviewLightDark
import focusbox.shared.generated.resources.Res
import focusbox.shared.generated.resources.ic_onboarding_focus
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun OnboardingIllustration(
    icon: DrawableResource,
    containerColor: Color,
    onContainerColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(200.dp)
            .clip(RoundedCornerShape(48.dp))
            .background(containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(140.dp)) {
            val stroke = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
            drawArc(
                color = onContainerColor.copy(alpha = 0.16f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke,
            )
            drawArc(
                color = onContainerColor,
                startAngle = -90f,
                sweepAngle = 280f,
                useCenter = false,
                style = stroke,
            )
        }
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(onContainerColor),
            modifier = Modifier.size(44.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun OnboardingIllustrationPreview() {
    FocusBoxTheme {
        OnboardingIllustration(
            icon = Res.drawable.ic_onboarding_focus,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            onContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}
