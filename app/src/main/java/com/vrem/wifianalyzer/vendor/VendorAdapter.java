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

package com.vrem.wifianalyzer.vendor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

class VendorAdapter extends ArrayAdapter<String> {
    private SortedMap<String, List<String>> vendors;

    VendorAdapter(@NonNull Context context, @NonNull SortedMap<String, List<String>> vendors) {
        super(context, R.layout.vendor_details, new ArrayList<>(vendors.keySet()));
        this.vendors = vendors;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = MainContext.INSTANCE.getMainActivity().getLayoutInflater();
            view = layoutInflater.inflate(R.layout.vendor_details, parent, false);
        }
        String name = getItem(position);
        ((TextView) view.findViewById(R.id.vendor_name)).setText(name);

        StringBuilder stringBuilder = new StringBuilder();
        IterableUtils.forEach(vendors.get(name), new MacsClosure(stringBuilder));
        ((TextView) view.findViewById(R.id.vendor_macs)).setText(stringBuilder.toString());
        return view;
    }

    SortedMap<String, List<String>> getVendors() {
        return vendors;
    }

    void setVendors(@NonNull SortedMap<String, List<String>> vendors) {
        this.vendors = vendors;
        clear();
        addAll(new ArrayList<>(vendors.keySet()));
    }

    private class MacsClosure implements Closure<String> {
        private final StringBuilder stringBuilder;

        private MacsClosure(@NonNull StringBuilder stringBuilder) {
            this.stringBuilder = stringBuilder;
        }

        @Override
        public void execute(String input) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            String macAddress =
                input.length() < 6
                    ? "*" + input + "*"
                    : String.format("%s:%s:%s", input.substring(0, 2), input.substring(2, 4), input.substring(4, 6));
            stringBuilder.append(macAddress);
        }
    }
}
