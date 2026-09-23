package com.rizz.focusbox.feature.onboarding.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.rizz.focusbox.feature.onboarding.view.event.OnBoardingScreenEvents
import com.rizz.focusbox.ui.test.UiTestTags
import com.rizz.focusbox.ui.components.FocusBoxButton
import com.rizz.focusbox.ui.theme.FocusBoxTheme
import com.rizz.focusbox.ui.theme.PreviewLightDark
import focusbox.shared.generated.resources.Res
import focusbox.shared.generated.resources.ic_onboarding_breaks
import focusbox.shared.generated.resources.ic_onboarding_focus
import focusbox.shared.generated.resources.ic_onboarding_progress
import focusbox.shared.generated.resources.onboarding_action_get_started
import focusbox.shared.generated.resources.onboarding_action_next
import focusbox.shared.generated.resources.onboarding_action_skip
import focusbox.shared.generated.resources.onboarding_breaks_subtitle
import focusbox.shared.generated.resources.onboarding_breaks_title
import focusbox.shared.generated.resources.onboarding_focus_subtitle
import focusbox.shared.generated.resources.onboarding_focus_title
import focusbox.shared.generated.resources.onboarding_progress_subtitle
import focusbox.shared.generated.resources.onboarding_progress_title
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

private const val PAGE_COUNT = 3

@Composable
fun OnboardingPagerScreen(
    onEvent: (OnBoardingScreenEvents) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { PAGE_COUNT })
    val coroutineScope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == PAGE_COUNT - 1

    val pages = listOf(
        OnboardingPageContent(
            icon = Res.drawable.ic_onboarding_focus,
            title = stringResource(Res.string.onboarding_focus_title),
            subtitle = stringResource(Res.string.onboarding_focus_subtitle),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            onContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        OnboardingPageContent(
            icon = Res.drawable.ic_onboarding_breaks,
            title = stringResource(Res.string.onboarding_breaks_title),
            subtitle = stringResource(Res.string.onboarding_breaks_subtitle),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            onContainerColor = MaterialTheme.colorScheme.onTertiaryContainer,
        ),
        OnboardingPageContent(
            icon = Res.drawable.ic_onboarding_progress,
            title = stringResource(Res.string.onboarding_progress_title),
            subtitle = stringResource(Res.string.onboarding_progress_subtitle),
            containerColor = FocusBoxTheme.extraColors.amberContainer,
            onContainerColor = FocusBoxTheme.extraColors.onAmberContainer,
        ),
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                OnboardingPageScreen(content = pages[page])
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(PAGE_COUNT) { page ->
                    val selected = page == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (selected) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surfaceContainerHighest
                                },
                            ),
                    )
                }
            }

            FocusBoxButton(
                text = if (isLastPage) {
                    stringResource(Res.string.onboarding_action_get_started)
                } else {
                    stringResource(Res.string.onboarding_action_next)
                },
                onClick = {
                    if (isLastPage) {
                        onEvent(OnBoardingScreenEvents.OnNextClick)
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .testTag(UiTestTags.ONBOARDING_PRIMARY)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
            )
        }

        if (!isLastPage) {
            TextButton(
                onClick = {
                    onEvent(OnBoardingScreenEvents.OnSkipClick)
                },
                modifier = Modifier
                    .testTag(UiTestTags.ONBOARDING_SKIP)
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp),
            ) {
                Text(stringResource(Res.string.onboarding_action_skip))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun OnboardingPagerScreenPreview() {
    FocusBoxTheme {
        OnboardingPagerScreen(onEvent = {})
    }
}
