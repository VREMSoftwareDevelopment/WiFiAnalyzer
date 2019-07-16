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

package com.vrem.wifianalyzer.wifi.filter;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.vrem.util.TextUtils;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.filter.adapter.SSIDAdapter;

import org.apache.commons.lang3.StringUtils;

import androidx.annotation.NonNull;

class SSIDFilter {
    SSIDFilter(@NonNull SSIDAdapter ssidAdapter, @NonNull Dialog dialog) {
        String value = TextUtils.join(ssidAdapter.getValues());

        EditText editText = dialog.findViewById(R.id.filterSSIDtext);
        editText.setText(value);
        editText.addTextChangedListener(new OnChange(ssidAdapter));

        dialog.findViewById(R.id.filterSSID).setVisibility(View.VISIBLE);
    }

    static class OnChange implements TextWatcher {
        private final SSIDAdapter ssidAdapter;

        OnChange(@NonNull SSIDAdapter ssidAdapter) {
            this.ssidAdapter = ssidAdapter;
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
            ssidAdapter.setValues(TextUtils.split(s == null ? StringUtils.EMPTY : s.toString()));
        }
    }
}
