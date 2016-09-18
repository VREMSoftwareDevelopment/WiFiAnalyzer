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

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

class VendorAdapter extends ArrayAdapter<String> {
    private SortedMap<String, List<String>> vendors;

    VendorAdapter(@NonNull Context context, @NonNull SortedMap<String, List<String>> vendors) {
        super(context, R.layout.vendor_details, new ArrayList<>(vendors.keySet()));
        this.vendors = vendors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = MainContext.INSTANCE.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.vendor_details, parent, false);
        }
        String name = getItem(position);
        ((TextView) view.findViewById(R.id.vendor_name)).setText(name);

        StringBuilder stringBuilder = new StringBuilder();
        for (String mac : vendors.get(name)) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            String macAddress =
                    mac.length() < 6
                            ? "*" + mac + "*"
                            : String.format("%s:%s:%s", mac.substring(0, 2), mac.substring(2, 4), mac.substring(4, 6));
            stringBuilder.append(macAddress);
        }
        ((TextView) view.findViewById(R.id.vendor_macs)).setText(stringBuilder.toString());
        return view;
    }

    SortedMap<String, List<String>> getVendors() {
        return vendors;
    }

    public void setVendors(@NonNull SortedMap<String, List<String>> vendors) {
        this.vendors = vendors;
        clear();
        addAll(new ArrayList<>(vendors.keySet()));
    }
}
