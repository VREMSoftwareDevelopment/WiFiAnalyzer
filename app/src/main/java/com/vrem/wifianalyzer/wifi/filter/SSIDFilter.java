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

package com.vrem.wifianalyzer.wifi.filter;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.filter.adapter.BasicFilterAdapter;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class SSIDFilter {
    private static final char SEPARATOR_CHAR = ' ';

    private final BasicFilterAdapter<String> filterAdapter;

    SSIDFilter(@NonNull BasicFilterAdapter<String> filterAdapter, @NonNull Dialog dialog) {
        this.filterAdapter = filterAdapter;

        String value = StringUtils.join(filterAdapter.getValues(), SEPARATOR_CHAR);

        EditText editText = (EditText) dialog.findViewById(R.id.filterSSIDtext);
        editText.setText(value);
        editText.addTextChangedListener(new onChange());

        dialog.findViewById(R.id.filterSSID).setVisibility(View.VISIBLE);
    }

    private class onChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Set<String> values = new HashSet<>(Arrays.asList(StringUtils.split(s.toString(), SEPARATOR_CHAR)));
            filterAdapter.setValues(values);
        }
    }
}
