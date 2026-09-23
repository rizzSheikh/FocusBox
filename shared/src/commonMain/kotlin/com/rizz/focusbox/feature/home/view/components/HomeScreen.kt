package com.rizz.focusbox.feature.home.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.rizz.focusbox.ui.components.FocusBoxDestination
import com.rizz.focusbox.ui.components.FocusBoxNavigationBar
import com.rizz.focusbox.ui.components.FocusBoxTopAppBar
import com.rizz.focusbox.ui.test.UiTestTags
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.PreviewLightDark

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var selectedDestination by remember { mutableStateOf(FocusBoxDestination.Timer) }

    Scaffold(
        modifier = modifier.testTag(UiTestTags.HOME_SCREEN),
        topBar = {
            FocusBoxTopAppBar(
                title = "focusBox",
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                    }
                },
            )
        },
        bottomBar = {
            FocusBoxNavigationBar(
                selected = selectedDestination,
                onSelect = { selectedDestination = it },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

        }
    }
}

@PreviewLightDark
@Composable
private fun HomeScreenPreview() {
    FocusBoxTheme { HomeScreen() }
}
