package com.rizz.focusbox.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rizz.focusbox.ui.theme.FocusBoxTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusBoxTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    TopAppBar(title = { Text(title) }, modifier = modifier)
}

@Preview
@Composable
private fun FocusBoxTopAppBarLightPreview() {
    FocusBoxTheme(darkTheme = false) {
        FocusBoxTopAppBar(title = "Statistics")
    }
}

@Preview
@Composable
private fun FocusBoxTopAppBarDarkPreview() {
    FocusBoxTheme(darkTheme = true) {
        FocusBoxTopAppBar(title = "Statistics")
    }
}
