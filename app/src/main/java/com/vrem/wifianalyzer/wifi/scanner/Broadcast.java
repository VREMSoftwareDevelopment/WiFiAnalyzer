/*
 * Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.vrem.wifianalyzer.wifi.scanner;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.model.WiFiData;

public class Broadcast {

    public static final String MESSAGE_WIFIDATA = "MESSAGE_WIFIDATA";

    protected void send(@NonNull WiFiData wiFiData) {
        Context context = MainContext.INSTANCE.getContext();
        Intent indent = new Intent(MESSAGE_WIFIDATA);
        indent.putExtra(MESSAGE_WIFIDATA, wiFiData);
        LocalBroadcastManager.getInstance(context).sendBroadcast(indent);
    }

    public void register(@NonNull Context context, @NonNull Receiver receiver) {
        IntentFilter indent = new IntentFilter(MESSAGE_WIFIDATA);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, indent);
    }

    public void unregister(@NonNull Context context, @NonNull Receiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }
}
