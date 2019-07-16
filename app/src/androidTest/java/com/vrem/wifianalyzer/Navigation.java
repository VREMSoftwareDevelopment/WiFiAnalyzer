/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer;


import androidx.test.espresso.ViewInteraction;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.vrem.wifianalyzer.InstrumentedUtils.ChildAtPosition;
import static com.vrem.wifianalyzer.InstrumentedUtils.pauseLong;
import static com.vrem.wifianalyzer.InstrumentedUtils.pauseShort;
import static com.vrem.wifianalyzer.InstrumentedUtils.pressBackButton;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

class Navigation implements Runnable {
    private static final int ACCESS_POINTS = 1;
    private static final int CHANNEL_RATING = 2;
    private static final int CHANNEL_GRAPH = 3;
    private static final int TIME_GRAPH = 4;
    private static final int AVAILABLE_CHANNELS = 7;
    private static final int VENDORS = 8;
    private static final int SETTINGS = 10;
    private static final int ABOUT = 11;
    private static final int NAVIGATION_DRAWER_BUTTON = 0;
    private static final int NAVIGATION_DRAWER_ACTION = 1;
    private static final String NAVIGATION_DRAWER_TAG = "Open navigation drawer";

    @Override
    public void run() {
        selectMenuItem(CHANNEL_RATING);
        selectMenuItem(CHANNEL_GRAPH);
        selectMenuItem(TIME_GRAPH);
        pauseLong();
        selectMenuItem(AVAILABLE_CHANNELS);
        selectMenuItem(VENDORS);
        selectMenuItem(ACCESS_POINTS);
        selectMenuItem(SETTINGS);
        pressBackButton();
        selectMenuItem(ABOUT);
        pressBackButton();
    }

    private void selectMenuItem(int menuItem) {
        pauseShort();
        ViewInteraction appCompatImageButton = onView(
            allOf(withContentDescription(NAVIGATION_DRAWER_TAG),
                new ChildAtPosition(allOf(withId(R.id.toolbar),
                    new ChildAtPosition(
                        withClassName(is("com.google.android.material.appbar.AppBarLayout")),
                        NAVIGATION_DRAWER_BUTTON)),
                    NAVIGATION_DRAWER_ACTION),
                isDisplayed()));
        appCompatImageButton.perform(click());

        pauseShort();
        ViewInteraction navigationMenuItemView = onView(
            allOf(new ChildAtPosition(allOf(withId(R.id.design_navigation_view),
                new ChildAtPosition(withId(R.id.nav_drawer), NAVIGATION_DRAWER_BUTTON)), menuItem),
                isDisplayed()));
        navigationMenuItemView.perform(click());
    }
}
