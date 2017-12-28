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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.vrem.util.TextUtils;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;

import org.apache.commons.lang3.StringUtils;

public class VendorFragment extends ListFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vendor_content, container, false);
        VendorAdapter vendorAdapter = new VendorAdapter(getActivity(), MainContext.INSTANCE.getVendorService());
        setListAdapter(vendorAdapter);

        EditText editText = view.findViewById(R.id.vendorSearchText);
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editText.addTextChangedListener(new OnChange(vendorAdapter));

        return view;
    }

    static class OnChange implements TextWatcher {
        private final VendorAdapter vendorAdapter;

        OnChange(@NonNull VendorAdapter vendorAdapter) {
            this.vendorAdapter = vendorAdapter;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            String filter = TextUtils.trim(s == null ? StringUtils.EMPTY : s.toString());
            vendorAdapter.update(filter);
        }
    }

}
