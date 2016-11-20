/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi;

import android.os.AsyncTask;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetAddress;

class CalculateLatency extends AsyncTask<String, Void, Long> {
    static final Long INVALID = -1L;
    private static final int TIMEOUT = 1000;
    private final ConnectionView connectionView;

    CalculateLatency(ConnectionView connectionView) {
        this.connectionView = connectionView;
    }

    @Override
    protected Long doInBackground(String... params) {
        if (params == null || params.length < 1 || StringUtils.isBlank(params[0])) {
            return INVALID;
        }
        long time = System.currentTimeMillis();
        return isReachable(params[0]) ? (System.currentTimeMillis() - time) : INVALID;
    }

    boolean isReachable(String gatewayAddress) {
        try {
            return InetAddress.getByName(gatewayAddress).isReachable(TIMEOUT);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Long result) {
        if (!INVALID.equals(result)) {
            connectionView.updateLatency(result);
        }
    }

}