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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrem.wifianalyzer.wifi.filter.adapter.EnumFilterAdapter;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

abstract class EnumFilter<T extends Enum, U extends EnumFilterAdapter<T>> {
    private final U filter;

    EnumFilter(@NonNull Map<T, Integer> ids, @NonNull U filter, @NonNull Dialog dialog, int id) {
        this.filter = filter;
        IterableUtils.forEach(ids.keySet(), new EnumFilterClosure(ids, dialog));
        dialog.findViewById(id).setVisibility(View.VISIBLE);
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

    private class EnumFilterClosure implements Closure<T> {
        private final Map<T, Integer> ids;
        private final Dialog dialog;

        private EnumFilterClosure(@NonNull Map<T, Integer> ids, @NonNull Dialog dialog) {
            this.ids = ids;
            this.dialog = dialog;
        }

        @Override
        public void execute(T input) {
            setInformation(dialog, ids.get(input), input);
        }

        private void setInformation(@NonNull Dialog dialog, int id, @NonNull T object) {
            View view = dialog.findViewById(id);
            view.setOnClickListener(new OnClickListener(object));
            setColor(view, object);
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
