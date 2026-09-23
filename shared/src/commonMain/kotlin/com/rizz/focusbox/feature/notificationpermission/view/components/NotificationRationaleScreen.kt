package com.rizz.focusbox.feature.notificationpermission.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.components.FocusBoxButton
import com.rizz.focusbox.ui.components.OnboardingIllustration
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.PreviewLightDark
import focusbox.shared.generated.resources.Res
import focusbox.shared.generated.resources.ic_notification_bell
import focusbox.shared.generated.resources.notification_rationale_allow
import focusbox.shared.generated.resources.notification_rationale_not_now
import focusbox.shared.generated.resources.notification_rationale_subtitle
import focusbox.shared.generated.resources.notification_rationale_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationRationaleScreen(
    onEnableNotifications: () -> Unit,
    onNotNow: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        OnboardingIllustration(
            icon = Res.drawable.ic_notification_bell,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            onContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = stringResource(Res.string.notification_rationale_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
        )
        Text(
            text = stringResource(Res.string.notification_rationale_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        )
        FocusBoxButton(
            text = stringResource(Res.string.notification_rationale_allow),
            onClick = onEnableNotifications,
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
        )
        TextButton(onClick = onNotNow, modifier = Modifier.padding(top = 8.dp)) {
            Text(stringResource(Res.string.notification_rationale_not_now))
        }
    }
}

@PreviewLightDark
@Composable
private fun NotificationRationaleScreenPreview() {
    FocusBoxTheme {
        NotificationRationaleScreen(onEnableNotifications = {}, onNotNow = {})
    }
}
