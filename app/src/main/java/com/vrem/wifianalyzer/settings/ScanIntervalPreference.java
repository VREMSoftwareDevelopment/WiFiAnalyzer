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

        setPositiveButtonText(resources.getText(R.string.button_ok));
        setNegativeButtonText(resources.getText(R.string.button_cancel));

        valueDefault = resources.getInteger(R.integer.scan_interval_default);
        valueMin = resources.getInteger(R.integer.scan_interval_min);
        valueMax = resources.getInteger(R.integer.scan_interval_max);

        summary = super.getSummary();
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        numberPicker = (NumberPicker) view.findViewById(R.id.scan_interval_picker);
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

    protected int getValue() {
        return value;
    }
}