package com.rizz.focusbox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.PreviewLightDark

@Composable
fun FocusBoxStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange = 1..120,
    step: Int = 1,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        StepperButton(
            symbol = "−",
            enabled = value - step >= range.first,
            onClick = { onValueChange((value - step).coerceIn(range)) },
        )
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        StepperButton(
            symbol = "+",
            enabled = value + step <= range.last,
            onClick = { onValueChange((value + step).coerceIn(range)) },
        )
    }
}

@Composable
private fun StepperButton(symbol: String, enabled: Boolean, onClick: () -> Unit) {
    val background = if (enabled) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val content = if (enabled) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = Modifier
            .size(32.dp)
            .background(background, CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = symbol, color = content, style = MaterialTheme.typography.titleMedium)
    }
}

@PreviewLightDark
@Composable
private fun FocusBoxStepperPreview() {
    FocusBoxTheme {
        FocusBoxStepper(value = 25, onValueChange = {}, modifier = Modifier.padding(16.dp))
    }
}
