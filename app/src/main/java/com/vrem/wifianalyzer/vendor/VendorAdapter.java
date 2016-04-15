/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
            view = MainContext.INSTANCE.getLayoutInflater().inflate(R.layout.vendor_details, parent, false);
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

    protected SortedMap<String, List<String>> getVendors() {
        return vendors;
    }

    public void setVendors(@NonNull SortedMap<String, List<String>> vendors) {
        this.vendors = vendors;
        clear();
        addAll(new ArrayList<>(vendors.keySet()));
    }
}
