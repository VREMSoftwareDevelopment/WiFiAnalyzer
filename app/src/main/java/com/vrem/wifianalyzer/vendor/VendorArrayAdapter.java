/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.vrem.wifianalyzer.vendor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;

import java.util.List;

public class VendorArrayAdapter extends ArrayAdapter<Database.VendorData> {

    private MainContext mainContext = MainContext.INSTANCE;

    public VendorArrayAdapter(@NonNull Context context, @NonNull List<Database.VendorData> vendors) {
        super(context, R.layout.vendor_content_details, vendors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mainContext.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.vendor_content_details, parent, false);
        }
        Database.VendorData item = getItem(position);
        ((TextView) convertView.findViewById(R.id.vendor_name)).setText(item.name);
        ((TextView) convertView.findViewById(R.id.vendor_id)).setText(String.format("(%d)", item.id));

        String macAddress = item.mac + "-";
        if (item.mac.length() >= 6) {
            macAddress = String.format("%s:%s:%s:",
                    item.mac.substring(0, 2),
                    item.mac.substring(2, 4),
                    item.mac.substring(4, 6));
        }
        ((TextView) convertView.findViewById(R.id.vendor_mac_from)).setText(macAddress + "00:00:00");
        ((TextView) convertView.findViewById(R.id.vendor_mac_to)).setText(macAddress + "FF:FF:FF");
        return convertView;
    }
}
