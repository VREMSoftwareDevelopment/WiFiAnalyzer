/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.settings;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import com.vrem.wifianalyzer.R;

public class ScanIntervalPreference extends DialogPreference {
    private final int valueDefault;
    private final int valueMin;
    private final int valueMax;
    private final CharSequence summary;
    private Integer value;
    private NumberPicker numberPicker;

    public ScanIntervalPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.scan_interval_dialog);

        Resources resources = context.getResources();

        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        valueDefault = resources.getInteger(R.integer.scan_interval_default);
        valueMin = resources.getInteger(R.integer.scan_interval_min);
        valueMax = resources.getInteger(R.integer.scan_interval_max);

        summary = super.getSummary();
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        numberPicker = view.findViewById(R.id.scan_interval_picker);
        numberPicker.setMinValue(valueMin);
        numberPicker.setMaxValue(valueMax);
        if (value != null) {
            numberPicker.setValue(value);
        }
        numberPicker.setWrapSelectorWheel(false);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            value = numberPicker.getValue();
            persistInt(value);
            setSummary(summary);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            value = getPersistedInt(valueDefault);
        } else {
            value = (int) defaultValue;
            persistInt(value);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, valueDefault);
    }

    @Override
    public CharSequence getSummary() {
        if (summary != null) {
            return String.format(summary.toString(), getPersistedInt(valueDefault));
        }
        return null;
    }

    @Override
    public void setSummary(CharSequence summary) {
        if (this.summary != null) {
            super.setSummary(String.format(this.summary.toString(), getPersistedInt(valueDefault)));
        }
    }

    int getValue() {
        return value;
    }
}