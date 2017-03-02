/*
 * WiFi Analyzer
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
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

abstract class EnumFilterView<T extends Enum, U extends EnumFilter<T>> {
    private final U filter;

    EnumFilterView(@NonNull Map<T, Integer> ids, @NonNull U filter, @NonNull Dialog dialog, int id) {
        this.filter = filter;
        for (T object : ids.keySet()) {
            setInformation(dialog, ids.get(object), object);
        }
        dialog.findViewById(id).setVisibility(View.VISIBLE);
    }

    private void setInformation(@NonNull Dialog dialog, int id, @NonNull T object) {
        View view = dialog.findViewById(id);
        view.setOnClickListener(new OnClickListener(object));
        setColor(view, object);
    }

    private void setColor(@NonNull View view, @NonNull T object) {
        int colorId = filter.getColor(object);
        int color = ContextCompat.getColor(view.getContext(), colorId);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        } else if (view instanceof ImageView) {
            ((ImageView) view).setColorFilter(color);
        }
    }

    private class OnClickListener implements View.OnClickListener {
        private final T object;

        OnClickListener(@NonNull T object) {
            this.object = object;
        }

        @Override
        public void onClick(View view) {
            filter.toggle(object);
            setColor(view, object);
        }
    }
}
