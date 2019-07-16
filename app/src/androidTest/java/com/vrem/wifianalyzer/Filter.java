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
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.vrem.wifianalyzer.InstrumentedUtils.ChildAtPosition;
import static com.vrem.wifianalyzer.InstrumentedUtils.pauseLong;
import static com.vrem.wifianalyzer.InstrumentedUtils.pauseShort;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

class Filter implements Runnable {
    private static final int FILTER_BUTTON_OPEN = 2;
    private static final int FILTER_BUTTON_CLOSE = 0;
    private static final int FILTER_ACTION = 0;
    private static final String FILTER_BUTTON_TAG = "Filter";
    private static final String FILTER_CLOSE_TAG = "Close";

    @Override
    public void run() {
        pauseShort();
        ViewInteraction actionMenuItemView = onView(
            allOf(withId(R.id.action_filter), withContentDescription(FILTER_BUTTON_TAG),
                new ChildAtPosition(
                    new ChildAtPosition(withId(R.id.toolbar), FILTER_BUTTON_OPEN),
                    FILTER_ACTION),
                isDisplayed()));
        actionMenuItemView.perform(click());

        pauseLong();
        ViewInteraction appCompatButton = onView(
            allOf(withId(android.R.id.button3), withText(FILTER_CLOSE_TAG),
                new ChildAtPosition(
                    new ChildAtPosition(withClassName(is("android.widget.ScrollView")),
                        FILTER_BUTTON_CLOSE),
                    FILTER_ACTION)));
        appCompatButton.perform(scrollTo(), click());
    }
}
