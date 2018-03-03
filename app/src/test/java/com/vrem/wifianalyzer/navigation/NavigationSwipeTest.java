/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.navigation;

import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.gestures.SwipeDirection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NavigationSwipeTest {
    private MainActivity mainActivity;
    private NavigationSwipe fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new NavigationSwipe(mainActivity);
    }

    @Test
    public void testSwipe() throws Exception {
        // swipe left expect channel rating
        fixture.swipe(SwipeDirection.LEFT);
        validateSwipeRight(NavigationMenu.CHANNEL_RATING);
        // swipe right expect access point
        fixture.swipe(SwipeDirection.RIGHT);
        validateSwipeRight(NavigationMenu.ACCESS_POINTS);
    }

    private void validateSwipeRight(@NonNull NavigationMenu expected) {
        assertEquals(expected, mainActivity.getCurrentNavigationMenu());
        assertEquals(expected.ordinal(), mainActivity.getCurrentMenuItem().getItemId());
    }
}