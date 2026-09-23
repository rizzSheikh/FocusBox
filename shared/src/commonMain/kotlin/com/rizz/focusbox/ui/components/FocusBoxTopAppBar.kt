package com.rizz.focusbox.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.PreviewLightDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusBoxTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    TopAppBar(title = { Text(title) }, modifier = modifier)
}

@PreviewLightDark
@Composable
private fun FocusBoxTopAppBarPreview() {
    FocusBoxTheme {
        FocusBoxTopAppBar(title = "Statistics")
    }
}
