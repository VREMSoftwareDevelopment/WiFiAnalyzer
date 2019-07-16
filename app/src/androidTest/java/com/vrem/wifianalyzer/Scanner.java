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
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.vrem.wifianalyzer.InstrumentedUtils.ChildAtPosition;
import static com.vrem.wifianalyzer.InstrumentedUtils.pauseLong;
import static com.vrem.wifianalyzer.InstrumentedUtils.pauseShort;
import static org.hamcrest.Matchers.allOf;

class Scanner implements Runnable {
    private static final int SCANNER_BUTTON = 2;
    private static final int SCANNER_ACTION = 1;
    private static final String SCANNER_PAUSE_TAG = "Pause";
    private static final String SCANNER_RESUME_TAG = "Play";

    @Override
    public void run() {
        scannerAction(SCANNER_PAUSE_TAG);
        pauseLong();
        scannerAction(SCANNER_RESUME_TAG);
    }

    private void scannerAction(String tag) {
        pauseShort();
        ViewInteraction actionMenuItemView = onView(
            allOf(withId(R.id.action_scanner), withContentDescription(tag),
                new ChildAtPosition(new ChildAtPosition(withId(R.id.toolbar), SCANNER_BUTTON),
                    SCANNER_ACTION),
                isDisplayed()));
        actionMenuItemView.perform(click());
    }

}
