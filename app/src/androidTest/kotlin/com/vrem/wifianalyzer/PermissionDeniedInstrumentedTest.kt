/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.vrem.wifianalyzer

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

private const val DENY_BUTTON = "com.android.permissioncontroller:id/permission_deny_button"

private const val TIMEOUT = 5000L

@LargeTest
@RunWith(AndroidJUnit4::class)
class PermissionDeniedInstrumentedTest {
    private val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Test
    fun denyingLocationPermissionFinishesActivity() {
        // Arrange
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Act
        onView(withText(android.R.string.ok)).perform(click())
        device.wait(Until.hasObject(By.res(DENY_BUTTON)), TIMEOUT)
        device.findObject(By.res(DENY_BUTTON)).click()
        device.wait(Until.gone(By.res(DENY_BUTTON)), TIMEOUT)

        // Assert
        assertThat(scenario.state).isEqualTo(Lifecycle.State.DESTROYED)
    }
}
