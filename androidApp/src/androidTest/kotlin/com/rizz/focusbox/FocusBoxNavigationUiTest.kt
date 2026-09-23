package com.rizz.focusbox

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rizz.focusbox.ui.test.UiTestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FocusBoxNavigationUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun skipOnboarding_navigatesToHome() {
        waitForOnboarding()

        composeTestRule.onNodeWithTag(UiTestTags.ONBOARDING_SKIP).performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag(UiTestTags.HOME_SCREEN)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeTestRule.onNodeWithTag(UiTestTags.HOME_SCREEN).assertIsDisplayed()
    }

    @Test
    fun completeOnboardingViaNotificationRationale_navigatesToHome() {
        waitForOnboarding()

        repeat(2) {
            composeTestRule.onNodeWithTag(UiTestTags.ONBOARDING_PRIMARY).performClick()
        }
        composeTestRule.onNodeWithTag(UiTestTags.ONBOARDING_PRIMARY).performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag(UiTestTags.NOTIFICATION_NOT_NOW)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeTestRule.onNodeWithTag(UiTestTags.NOTIFICATION_NOT_NOW).performClick()

        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag(UiTestTags.HOME_SCREEN)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeTestRule.onNodeWithTag(UiTestTags.HOME_SCREEN).assertIsDisplayed()
    }

    private fun waitForOnboarding() {
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule
                .onAllNodesWithTag(UiTestTags.ONBOARDING_SKIP)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }
}
