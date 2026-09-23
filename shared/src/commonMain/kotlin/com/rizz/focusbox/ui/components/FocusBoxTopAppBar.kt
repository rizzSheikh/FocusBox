package com.rizz.focusbox.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    actions: @Composable () -> Unit = {},
) {
    TopAppBar(title = { Text(title) }, actions = { actions() }, modifier = modifier)
}

@PreviewLightDark
@Composable
private fun FocusBoxTopAppBarPreview() {
    FocusBoxTheme {
        FocusBoxTopAppBar(title = "Statistics")
    }
}

@PreviewLightDark
@Composable
private fun FocusBoxTopAppBarWithActionPreview() {
    FocusBoxTheme {
        FocusBoxTopAppBar(
            title = "focusBox",
            actions = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                }
            },
        )
    }
}
