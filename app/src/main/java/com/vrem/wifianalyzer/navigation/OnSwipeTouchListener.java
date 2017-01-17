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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.vrem.wifianalyzer.MainActivity;

import static android.view.GestureDetector.SimpleOnGestureListener;
import static android.view.View.OnTouchListener;

public class OnSwipeTouchListener implements OnTouchListener {
    static final int SWIPE_THRESHOLD = 100;
    static final int SWIPE_VELOCITY_THRESHOLD = 100;

    private final GestureDetector gestureDetector;
    private final GestureListener gestureListener;
    private final MainActivity mainActivity;

    public OnSwipeTouchListener(@NonNull MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.gestureListener = new GestureListener();
        this.gestureDetector = new GestureDetector(mainActivity, gestureListener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void onSwipeRight(@NonNull MainActivity mainActivity) {
        // method to override
    }

    public void onSwipeLeft(@NonNull MainActivity mainActivity) {
        // method to override
    }

    public void onSwipeTop(@NonNull MainActivity mainActivity) {
        // method to override
    }

    public void onSwipeBottom(@NonNull MainActivity mainActivity) {
        // method to override
    }

    GestureListener getGestureListener() {
        return gestureListener;
    }

    final class GestureListener extends SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) >= SWIPE_THRESHOLD && Math.abs(velocityX) >= SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight(mainActivity);
                        } else {
                            onSwipeLeft(mainActivity);
                        }
                    }
                } else {
                    if (Math.abs(diffY) >= SWIPE_THRESHOLD && Math.abs(velocityY) >= SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom(mainActivity);
                        } else {
                            onSwipeTop(mainActivity);
                        }
                    }
                }
            } catch (Exception e) {
                // do not perform any swipe events
            }
            return false;
        }
    }
}