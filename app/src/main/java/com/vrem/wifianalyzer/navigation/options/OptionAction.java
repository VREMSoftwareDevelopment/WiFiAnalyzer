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

package com.vrem.wifianalyzer.navigation.options;

import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.R;

import org.apache.commons.collections4.Predicate;

import androidx.annotation.NonNull;

enum OptionAction {
    NO_ACTION(-1, new NoAction()),
    SCANNER(R.id.action_scanner, new ScannerAction()),
    FILTER(R.id.action_filter, new FilterAction()),
    WIFI_BAND(R.id.action_wifi_band, new WiFiBandAction());

    private final int key;
    private final Action action;

    OptionAction(int key, @NonNull Action action) {
        this.key = key;
        this.action = action;
    }

    @NonNull
    public static OptionAction findOptionAction(int key) {
        return EnumUtils.find(OptionAction.class, new ActionPredicate(key), NO_ACTION);
    }

    int getKey() {
        return key;
    }

    @NonNull
    Action getAction() {
        return action;
    }

    private static class ActionPredicate implements Predicate<OptionAction> {
        private final int key;

        private ActionPredicate(int key) {
            this.key = key;
        }

        @Override
        public boolean evaluate(OptionAction object) {
            return key == object.key;
        }
    }

    static class NoAction implements Action {
        @Override
        public void execute() {
            // do nothing
        }
    }

}
