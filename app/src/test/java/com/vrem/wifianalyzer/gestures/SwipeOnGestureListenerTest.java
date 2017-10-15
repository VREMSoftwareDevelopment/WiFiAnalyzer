/*
 * WiFiAnalyzer
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

package com.vrem.wifianalyzer.gestures;

import android.view.MotionEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 1) (0,0) is top left corner.
 * 2) (maxX,0) is top right corner
 * 3) (0,maxY) is bottom left corner
 * 4) (maxX,maxY) is bottom right corner
 */
@RunWith(MockitoJUnitRunner.class)
public class SwipeOnGestureListenerTest {
    @Mock
    private MotionEvent e1;
    @Mock
    private MotionEvent e2;
    @Mock
    private SwipeAction swipeAction;

    private SwipeOnGestureListener fixture;

    @Before
    public void setUp() {
        fixture = new SwipeOnGestureListener(swipeAction);
    }

    @Test
    public void testOnFlingSwipesRight() throws Exception {
        // setup
        when(e1.getY()).thenReturn(0f);
        when(e2.getY()).thenReturn(0f);
        when(e1.getX()).thenReturn(0f);
        when(e2.getX()).thenReturn((float) SwipeOnGestureListener.SWIPE_THRESHOLD);
        // execute
        boolean actual = fixture.onFling(e1, e2, SwipeOnGestureListener.SWIPE_VELOCITY_THRESHOLD, SwipeOnGestureListener.SWIPE_VELOCITY_THRESHOLD);
        // validate
        assertFalse(actual);
        verify(swipeAction).swipe(SwipeDirection.RIGHT);
    }

    @Test
    public void testOnFlingSwipesLeft() throws Exception {
        // setup
        when(e1.getY()).thenReturn(0f);
        when(e2.getY()).thenReturn(0f);
        when(e1.getX()).thenReturn((float) SwipeOnGestureListener.SWIPE_THRESHOLD);
        when(e2.getX()).thenReturn(0f);
        // execute
        boolean actual = fixture.onFling(e1, e2, SwipeOnGestureListener.SWIPE_VELOCITY_THRESHOLD, SwipeOnGestureListener.SWIPE_VELOCITY_THRESHOLD);
        // validate
        assertFalse(actual);
        verify(swipeAction).swipe(SwipeDirection.LEFT);
    }

    @Test
    public void testOnFlingSwipesDown() throws Exception {
        // setup
        when(e1.getY()).thenReturn(0f);
        when(e2.getY()).thenReturn((float) SwipeOnGestureListener.SWIPE_THRESHOLD);
        when(e1.getX()).thenReturn(0f);
        when(e2.getX()).thenReturn(0f);
        // execute
        boolean actual = fixture.onFling(e1, e2, SwipeOnGestureListener.SWIPE_VELOCITY_THRESHOLD, SwipeOnGestureListener.SWIPE_VELOCITY_THRESHOLD);
        // validate
        assertFalse(actual);
        verify(swipeAction).swipe(SwipeDirection.UP);
    }

    @Test
    public void testOnFlingSwipesUp() throws Exception {
        // setup
        when(e1.getY()).thenReturn((float) SwipeOnGestureListener.SWIPE_THRESHOLD);
        when(e2.getY()).thenReturn(0f);
        when(e1.getX()).thenReturn(0f);
        when(e2.getX()).thenReturn(0f);
        // execute
        boolean actual = fixture.onFling(e1, e2, SwipeOnGestureListener.SWIPE_VELOCITY_THRESHOLD, SwipeOnGestureListener.SWIPE_VELOCITY_THRESHOLD);
        // validate
        assertFalse(actual);
        verify(swipeAction).swipe(SwipeDirection.DOWN);
    }

    @Test
    public void testOnDownAlwaysReturnTrue() throws Exception {
        // execute & validate
        assertTrue(fixture.onDown(e1));
    }
}
