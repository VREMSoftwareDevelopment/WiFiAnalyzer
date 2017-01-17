/*
 * WiFi Analyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.view.MotionEvent;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class OnSwipeTouchListenerTest {
    private MainActivity mainActivity;
    private OnSwipeTouchListener fixture;
    private boolean onSwipe;
    private MotionEvent e1;
    private MotionEvent e2;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();
        onSwipe = false;
        e1 = mock(MotionEvent.class);
        e2 = mock(MotionEvent.class);
    }

    @Test
    public void testOnSwipeRight() throws Exception {
        // setup
        when(e1.getY()).thenReturn(0f);
        when(e1.getX()).thenReturn(0f);
        when(e2.getY()).thenReturn(0f);
        when(e2.getX()).thenReturn((float) OnSwipeTouchListener.SWIPE_THRESHOLD);
        fixture = new OnSwipeTouchListener(mainActivity) {
            @Override
            public void onSwipeRight(@NonNull MainActivity mainActivity) {
                onSwipe = true;
            }
        };
        OnSwipeTouchListener.GestureListener gestureListener = fixture.getGestureListener();
        // execute
        gestureListener.onFling(e1, e2, OnSwipeTouchListener.SWIPE_VELOCITY_THRESHOLD, OnSwipeTouchListener.SWIPE_VELOCITY_THRESHOLD);
        // validate
        assertTrue(onSwipe);
    }

    @Test
    public void testOnSwipeLeft() throws Exception {
        // setup
        when(e1.getY()).thenReturn(0f);
        when(e1.getX()).thenReturn((float) OnSwipeTouchListener.SWIPE_THRESHOLD);
        when(e2.getY()).thenReturn(0f);
        when(e2.getX()).thenReturn(0f);
        fixture = new OnSwipeTouchListener(mainActivity) {
            @Override
            public void onSwipeLeft(@NonNull MainActivity mainActivity) {
                onSwipe = true;
            }
        };
        OnSwipeTouchListener.GestureListener gestureListener = fixture.getGestureListener();
        // execute
        gestureListener.onFling(e1, e2, OnSwipeTouchListener.SWIPE_VELOCITY_THRESHOLD, OnSwipeTouchListener.SWIPE_VELOCITY_THRESHOLD);
        // validate
        assertTrue(onSwipe);
    }

    @Test
    public void testOnSwipeBottom() throws Exception {
        // setup
        when(e1.getY()).thenReturn(0f);
        when(e1.getX()).thenReturn(0f);
        when(e2.getY()).thenReturn((float) OnSwipeTouchListener.SWIPE_THRESHOLD);
        when(e2.getX()).thenReturn(0f);
        fixture = new OnSwipeTouchListener(mainActivity) {
            @Override
            public void onSwipeBottom(@NonNull MainActivity mainActivity) {
                onSwipe = true;
            }
        };
        OnSwipeTouchListener.GestureListener gestureListener = fixture.getGestureListener();
        // execute
        gestureListener.onFling(e1, e2, OnSwipeTouchListener.SWIPE_VELOCITY_THRESHOLD, OnSwipeTouchListener.SWIPE_VELOCITY_THRESHOLD);
        // validate
        assertTrue(onSwipe);
    }


    @Test
    public void testOnSwipeTop() throws Exception {
        // setup
        when(e1.getY()).thenReturn((float) OnSwipeTouchListener.SWIPE_THRESHOLD);
        when(e1.getX()).thenReturn(0f);
        when(e2.getY()).thenReturn(0f);
        when(e2.getX()).thenReturn(0f);
        fixture = new OnSwipeTouchListener(mainActivity) {
            @Override
            public void onSwipeTop(@NonNull MainActivity mainActivity) {
                onSwipe = true;
            }
        };
        OnSwipeTouchListener.GestureListener gestureListener = fixture.getGestureListener();
        // execute
        gestureListener.onFling(e1, e2, OnSwipeTouchListener.SWIPE_VELOCITY_THRESHOLD, OnSwipeTouchListener.SWIPE_VELOCITY_THRESHOLD);
        // validate
        assertTrue(onSwipe);
    }

    @Test
    public void testOnDown() throws Exception {
        // setup
        fixture = new OnSwipeTouchListener(mainActivity);
        OnSwipeTouchListener.GestureListener gestureListener = fixture.getGestureListener();
        // execute & validate
        assertTrue(gestureListener.onDown(e1));
    }
}
