package com.rizz.focusbox.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.ui.theme.FocusBoxTheme

/** Used for both the Stats "Week/Month" toggle and any Focus/Short break/Long break picker. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusBoxSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                selected = index == selectedIndex,
                onClick = { onSelect(index) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
            ) {
                Text(label)
            }
        }
    }
}

@Preview
@Composable
private fun FocusBoxSegmentedControlLightPreview() {
    FocusBoxTheme(darkTheme = false) {
        FocusBoxSegmentedControl(
            options = listOf("Week", "Month"),
            selectedIndex = 0,
            onSelect = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun FocusBoxSegmentedControlDarkPreview() {
    FocusBoxTheme(darkTheme = true) {
        FocusBoxSegmentedControl(
            options = listOf("Focus", "Short break", "Long break"),
            selectedIndex = 0,
            onSelect = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
