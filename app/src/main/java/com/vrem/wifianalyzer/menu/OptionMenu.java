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

package com.vrem.wifianalyzer.menu;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;

public class OptionMenu {
    private Menu menu;
    private ScannerItem scannerItem;

    public OptionMenu() {
        setScannerItem(new ScannerItem());
    }

    public void create(@NonNull Activity activity, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.optionmenu, menu);
        setMenu(menu);
    }

    public void select(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_scanner) {
            if (MainContext.INSTANCE.getScanner().isRunning()) {
                scannerItem.pause(this);
            } else {
                scannerItem.resume(this);
            }
        }
    }

    public void update(boolean hasOptions) {
        if (menu != null) {
            menu.findItem(R.id.option_menu).setVisible(hasOptions);
        }
    }

    public void pause() {
        scannerItem.pause(this);
    }

    public void resume() {
        scannerItem.resume(this);
    }

    void updateItem(boolean isRunning) {
        if (menu != null) {
            MenuItem item = menu.findItem(R.id.action_scanner);
            if (isRunning) {
                item.setTitle(R.string.action_pause);
                item.setIcon(R.drawable.ic_pause_grey_500_48dp);
            } else {
                item.setTitle(R.string.action_play);
                item.setIcon(R.drawable.ic_play_arrow_grey_500_48dp);
            }
        }
    }

    void setScannerItem(@NonNull ScannerItem scannerItem) {
        this.scannerItem = scannerItem;
    }

    void setMenu(@NonNull Menu menu) {
        this.menu = menu;
    }
}
