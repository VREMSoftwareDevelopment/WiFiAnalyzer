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


import android.support.annotation.NonNull;
import android.view.MotionEvent;

import static android.view.GestureDetector.SimpleOnGestureListener;

public class SwipeOnGestureListener extends SimpleOnGestureListener {
    static final int SWIPE_THRESHOLD = 100;
    static final int SWIPE_VELOCITY_THRESHOLD = 100;

    private final SwipeAction swipeAction;

    public SwipeOnGestureListener(@NonNull SwipeAction swipeAction) {
        this.swipeAction = swipeAction;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float velocityX, float velocityY) {
        try {
            float e1Y = motionEvent1.getY();
            float e1X = motionEvent1.getX();
            float e2Y = motionEvent2.getY();
            float e2X = motionEvent2.getX();
            float diffY = Math.abs(e2Y - e1Y);
            float diffX = Math.abs(e2X - e1X);
            if (isFlingLeftOrRight(diffY, diffX)) {
                if (isSwipeRightOrLeft(velocityX, diffX)) {
                    swipeAction.swipe(isSwipeRight(e1X, e2X) ? SwipeDirection.RIGHT : SwipeDirection.LEFT);
                }
            } else {
                if (isSwipeUpOrDown(velocityY, diffY)) {
                    swipeAction.swipe(isSwipeUp(e1Y, e2Y) ? SwipeDirection.UP : SwipeDirection.DOWN);
                }
            }
        } catch (Exception e) {
            // do not perform any swipe events
        }
        return false;
    }

    private boolean isSwipeUpOrDown(float velocityY, float diffY) {
        return diffY >= SWIPE_THRESHOLD && Math.abs(velocityY) >= SWIPE_VELOCITY_THRESHOLD;
    }

    private boolean isSwipeRightOrLeft(float velocityX, float diffX) {
        return diffX >= SWIPE_THRESHOLD && Math.abs(velocityX) >= SWIPE_VELOCITY_THRESHOLD;
    }

    private boolean isSwipeUp(float e1Y, float e2Y) {
        return e2Y > e1Y;
    }

    private boolean isSwipeRight(float e1X, float e2X) {
        return e2X > e1X;
    }

    private boolean isFlingLeftOrRight(float diffY, float diffX) {
        return diffX > diffY;
    }
}