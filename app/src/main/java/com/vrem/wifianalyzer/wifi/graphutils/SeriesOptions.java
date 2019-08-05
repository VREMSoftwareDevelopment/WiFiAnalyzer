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

package com.vrem.wifianalyzer.wifi.graphutils;

import com.jjoe64.graphview.series.BaseSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.TitleLineGraphSeries;

import androidx.annotation.NonNull;

class SeriesOptions {
    private GraphColors graphColors;

    SeriesOptions() {
        setGraphColors(new GraphColors());
    }

    void setGraphColors(@NonNull GraphColors graphColors) {
        this.graphColors = graphColors;
    }

    void highlightConnected(@NonNull BaseSeries<DataPoint> series, boolean connected) {
        int thickness = connected ? GraphConstants.THICKNESS_CONNECTED : GraphConstants.THICKNESS_REGULAR;
        if (series instanceof LineGraphSeries) {
            ((LineGraphSeries<DataPoint>) series).setThickness(thickness);
        } else if (series instanceof TitleLineGraphSeries) {
            TitleLineGraphSeries<DataPoint> titleLineGraphSeries = (TitleLineGraphSeries<DataPoint>) series;
            titleLineGraphSeries.setThickness(thickness);
            titleLineGraphSeries.setTextBold(connected);
        }
    }

    void setSeriesColor(@NonNull BaseSeries<DataPoint> series) {
        GraphColor graphColor = graphColors.getColor();
        series.setColor((int) graphColor.getPrimary());
        if (series instanceof LineGraphSeries) {
            ((LineGraphSeries<DataPoint>) series).setBackgroundColor((int) graphColor.getBackground());
        } else if (series instanceof TitleLineGraphSeries) {
            ((TitleLineGraphSeries<DataPoint>) series).setBackgroundColor((int) graphColor.getBackground());
        }
    }

    void drawBackground(@NonNull BaseSeries<DataPoint> series, boolean drawBackground) {
        if (series instanceof LineGraphSeries) {
            ((LineGraphSeries<DataPoint>) series).setDrawBackground(drawBackground);
        } else if (series instanceof TitleLineGraphSeries) {
            ((TitleLineGraphSeries<DataPoint>) series).setDrawBackground(drawBackground);
        }
    }

    void removeSeriesColor(@NonNull BaseSeries<DataPoint> series) {
        graphColors.addColor(series.getColor());
    }

}
