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


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import androidx.annotation.NonNull;

import static androidx.test.espresso.Espresso.pressBack;

class InstrumentedUtils {
    private static final int SLEEP_TIME_SHORT = 5000;
    private static final int SLEEP_TIME_LONG = SLEEP_TIME_SHORT * 3;

    private InstrumentedUtils() {
        throw new IllegalStateException("Utility class");
    }

    static void pressBackButton() {
        pauseShort();
        pressBack();
    }

    static void pauseShort() {
        pause(SLEEP_TIME_SHORT);
    }

    static void pauseLong() {
        pause(SLEEP_TIME_LONG);
    }

    private static void pause(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class ChildAtPosition extends TypeSafeMatcher<View> {
        private final Matcher<View> parentMatcher;
        private final int position;

        ChildAtPosition(@NonNull Matcher<View> parentMatcher, int position) {
            this.parentMatcher = parentMatcher;
            this.position = position;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("Child at position " + position + " in parent ");
            parentMatcher.describeTo(description);
        }

        @Override
        public boolean matchesSafely(View view) {
            ViewParent parent = view.getParent();
            return parent instanceof ViewGroup && parentMatcher.matches(parent)
                && view.equals(((ViewGroup) parent).getChildAt(position));
        }
    }


}
