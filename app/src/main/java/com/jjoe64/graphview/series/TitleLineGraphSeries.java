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

package com.jjoe64.graphview.series;

/**
 * modified version of com.jjoe64.graphview.series.LineGraphSeries
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;

import com.jjoe64.graphview.GraphView;

import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;

public class TitleLineGraphSeries<E extends DataPointInterface> extends BaseSeries<E> {
    private final Paint paint;
    private final TextPaint paintTitle;
    private final Paint paintBackground;
    private final Path path;
    private final Path pathBackground;
    private int thickness = 5;
    private int backgroundColor = Color.argb(100, 172, 218, 255);

    public TitleLineGraphSeries(E[] data) {
        super(data);

        paint = new Paint();
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);

        paintTitle = new TextPaint();
        paintTitle.setTextAlign(Paint.Align.CENTER);

        paintBackground = new Paint();

        path = new Path();
        pathBackground = new Path();
    }

    @Override
    public void draw(GraphView graphView, Canvas canvas, boolean isSecondScale) {
        paint.setColor(getColor());
        paint.setStrokeWidth(thickness);
        paintBackground.setColor(backgroundColor);
        pathBackground.reset();

        resetDataPoints();

        // get data
        double maxX = graphView.getViewport().getMaxX(false);
        double minX = graphView.getViewport().getMinX(false);

        double maxY;
        double minY;
        if (isSecondScale) {
            maxY = graphView.getSecondScale().getMaxY();
            minY = graphView.getSecondScale().getMinY();
        } else {
            maxY = graphView.getViewport().getMaxY(false);
            minY = graphView.getViewport().getMinY(false);
        }

        // draw data
        double diffY = maxY - minY;
        double diffX = maxX - minX;

        float graphHeight = graphView.getGraphContentHeight();
        float graphWidth = graphView.getGraphContentWidth();
        float graphLeft = graphView.getGraphContentLeft();
        float graphTop = graphView.getGraphContentTop();

        // draw background
        double lastEndY = 0;
        double lastEndX = 0;

        double titleY = 0;

        double lastUsedEndX = 0;
        float firstX = 0;
        int i = 0;
        Iterator<E> values = getValues(minX, maxX);
        while (values.hasNext()) {
            E value = values.next();

            double valY = value.getY() - minY;
            double ratY = valY / diffY;
            double y = graphHeight * ratY;

            double valX = value.getX() - minX;
            double ratX = valX / diffX;
            double x = graphWidth * ratX;

            double orgX = x;
            double orgY = y;

            if (i > 0) {
                // overdraw
                if (x > graphWidth) { // end right
                    double b = ((graphWidth - lastEndX) * (y - lastEndY) / (x - lastEndX));
                    y = lastEndY + b;
                    x = graphWidth;
                }
                if (y < 0) { // end bottom
                    double b = ((0 - lastEndY) * (x - lastEndX) / (y - lastEndY));
                    x = lastEndX + b;
                    y = 0;
                }
                if (y > graphHeight) { // end top
                    double b = ((graphHeight - lastEndY) * (x - lastEndX) / (y - lastEndY));
                    x = lastEndX + b;
                    y = graphHeight;
                }
                if (lastEndY < 0) { // start bottom
                    double b = ((0 - y) * (x - lastEndX) / (lastEndY - y));
                    lastEndX = x - b;
                    lastEndY = 0;
                }
                if (lastEndX < 0) { // start left
                    double b = ((0 - x) * (y - lastEndY) / (lastEndX - x));
                    lastEndY = y - b;
                    lastEndX = 0;
                }
                if (lastEndY > graphHeight) { // start top
                    double b = ((graphHeight - y) * (x - lastEndX) / (lastEndY - y));
                    lastEndX = x - b;
                    lastEndY = graphHeight;
                }

                float startX = (float) lastEndX + (graphLeft + 1);
                float startY = (float) (graphTop - lastEndY) + graphHeight;
                float endX = (float) x + (graphLeft + 1);
                float endY = (float) (graphTop - y) + graphHeight;

                registerDataPoint(endX, endY, value);

                path.reset();
                path.moveTo(startX, startY);
                path.lineTo(endX, endY);
                canvas.drawPath(path, paint);
                if (i == 1) {
                    firstX = startX;
                    pathBackground.moveTo(startX, startY);
                }
                pathBackground.lineTo(endX, endY);
                lastUsedEndX = endX;
            }
            lastEndY = orgY;
            lastEndX = orgX;
            titleY = Math.max(titleY, orgY);
            i++;
        }

        pathBackground.lineTo((float) lastUsedEndX, graphHeight + graphTop);
        pathBackground.lineTo(firstX, graphHeight + graphTop);
        pathBackground.close();
        canvas.drawPath(pathBackground, paintBackground);

        if (StringUtils.isNotBlank(getTitle()) && lastUsedEndX > 0) {
            float x = (float) (lastUsedEndX + firstX) / 2;
            float y = (float) (graphTop - titleY) + graphHeight - 10;

            paintTitle.setColor(getColor());
            paintTitle.setTextSize(graphView.getLegendRenderer().getTextSize());
            canvas.drawText(getTitle(), x, y, paintTitle);
        }
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void zeroThickness() {
        this.thickness = 0;
    }
}
