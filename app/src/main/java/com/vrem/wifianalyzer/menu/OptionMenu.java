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

package com.vrem.wifianalyzer.menu;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.filter.Filter;

public class OptionMenu {
    private Menu menu;

    public void create(@NonNull Activity activity, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.optionmenu, menu);
        this.menu = menu;
    }

    public void select(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scanner:
                if (MainContext.INSTANCE.getScanner().isRunning()) {
                    pause();
                } else {
                    resume();
                }
                break;
            case R.id.action_filter:
                Filter.build().show();
                break;
            default:
                // do nothing
                break;
        }
    }

    public void pause() {
        MainContext.INSTANCE.getScanner().pause();
    }

    public void resume() {
        MainContext.INSTANCE.getScanner().resume();
    }

    public Menu getMenu() {
        return menu;
    }

}
