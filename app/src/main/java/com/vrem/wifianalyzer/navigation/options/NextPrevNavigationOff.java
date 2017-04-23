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

package com.vrem.wifianalyzer.navigation.options;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;

class NextPrevNavigationOff implements NavigationOption {
    final static OnTouchListener ON_TOUCH_LISTENER_EMPTY = new NavigationOnTouchListener(false);

    @Override
    public void apply(@NonNull MainActivity mainActivity) {
        mainActivity.findViewById(R.id.main_fragment_layout).setOnTouchListener(ON_TOUCH_LISTENER_EMPTY);
    }

    private static class NavigationOnTouchListener implements OnTouchListener {
        private final boolean result;

        private NavigationOnTouchListener(boolean result) {
            this.result = result;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return result;
        }
    }

}
