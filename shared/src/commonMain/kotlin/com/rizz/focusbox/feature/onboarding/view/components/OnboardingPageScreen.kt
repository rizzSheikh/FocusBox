package com.rizz.focusbox.feature.onboarding.view.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rizz.focusbox.ui.components.OnboardingIllustration
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.PreviewLightDark
import focusbox.shared.generated.resources.Res
import focusbox.shared.generated.resources.ic_onboarding_focus
import org.jetbrains.compose.resources.DrawableResource

data class OnboardingPageContent(
    val icon: DrawableResource,
    val title: String,
    val subtitle: String,
    val containerColor: Color,
    val onContainerColor: Color,
)

@Composable
fun OnboardingPageScreen(content: OnboardingPageContent, modifier: Modifier = Modifier) {
    OnboardingPageBody(
        title = content.title,
        subtitle = content.subtitle,
        modifier = modifier,
        illustration = {
            OnboardingIllustration(
                icon = content.icon,
                containerColor = content.containerColor,
                onContainerColor = content.onContainerColor,
            )
        },
    )
}

@PreviewLightDark
@Composable
private fun OnboardingPageScreenPreview() {
    FocusBoxTheme {
        OnboardingPageScreen(
            content = OnboardingPageContent(
                icon = Res.drawable.ic_onboarding_focus,
                title = "Focus in boxes of time",
                subtitle = "Work in short, focused 25-minute sessions. One box at a time — no multitasking.",
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                onContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        )
    }
}
