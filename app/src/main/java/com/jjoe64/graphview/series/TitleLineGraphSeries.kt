/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.jjoe64.graphview.series

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.TextPaint
import android.view.animation.AccelerateInterpolator
import androidx.core.view.ViewCompat
import com.jjoe64.graphview.GraphView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/*
  modified version of com.jjoe64.graphview.series.LineGraphSeries
 */
class TitleLineGraphSeries<E : DataPointInterface?> : BaseSeries<E> {
    /**
     * wrapped styles
     */
    private var mStyles: Styles? = null
    private var mSelectionPaint: Paint? = null

    /**
     * internal paint object
     */
    private var mPaint: Paint? = null

    /**
     * paint for the background
     */
    private var mPaintBackground: Paint? = null

    /**
     * path for the background filling
     */
    private var mPathBackground: Path? = null

    /**
     * path to the line
     */
    private var mPath: Path? = null

    /**
     * custom paint that can be used.
     * this will ignore the thickness and color styles.
     */
    private var mCustomPaint: Paint? = null

    /**
     * rendering is animated
     */
    private var mAnimated = false

    /**
     * last animated value
     */
    private var mLastAnimatedValue = Double.NaN

    /**
     * time of animation start
     */
    private var mAnimationStart: Long = 0

    /**
     * animation interpolator
     */
    private var mAnimationInterpolator: AccelerateInterpolator? = null

    /**
     * number of animation frame to avoid lagging
     */
    private var mAnimationStartFrameNo = 0
    /**
     * flag whether the line should be drawn as a path
     * or with single drawLine commands (more performance)
     * By default we use drawLine because it has much more peformance.
     * For some styling reasons it can make sense to draw as path.
     */
    /**
     * flag whether the line should be drawn as a path
     * or with single drawLine commands (more performance)
     * By default we use drawLine because it has much more peformance.
     * For some styling reasons it can make sense to draw as path.
     *
     * @param isDrawAsPath true to draw as path
     */
    /**
     * flag whether the line should be drawn as a path
     * or with single drawLine commands (more performance)
     * By default we use drawLine because it has much more peformance.
     * For some styling reasons it can make sense to draw as path.
     */
    var isDrawAsPath = false
    private var paintTitle: TextPaint? = null
    private var textBold = false

    /**
     * creates a series without data
     */
    constructor() {
        init()
    }

    /**
     * creates a series with data
     *
     * @param data data points
     * important: array has to be sorted from lowest x-value to the highest
     */
    constructor(data: Array<E>?) : super(data) {
        init()
    }

    /**
     * do the initialization
     * creates internal objects
     */
    private fun init() {
        mStyles = Styles()
        mPaint = Paint()
        mPaint!!.strokeCap = Paint.Cap.ROUND
        mPaint!!.style = Paint.Style.STROKE
        mPaintBackground = Paint()
        mSelectionPaint = Paint()
        mSelectionPaint!!.color = Color.argb(80, 0, 0, 0)
        mSelectionPaint!!.style = Paint.Style.FILL
        mPathBackground = Path()
        mPath = Path()
        mAnimationInterpolator = AccelerateInterpolator(2f)
        paintTitle = TextPaint()
        paintTitle!!.textAlign = Paint.Align.CENTER
    }

    /**
     * plots the series
     * draws the line and the background
     *
     * @param graphView     graphview
     * @param canvas        canvas
     * @param isSecondScale flag if it is the second scale
     */
    override fun draw(graphView: GraphView, canvas: Canvas, isSecondScale: Boolean) {
        resetDataPoints()

        // get data
        val maxX = graphView.viewport.getMaxX(false)
        val minX = graphView.viewport.getMinX(false)
        val maxY: Double
        val minY: Double
        if (isSecondScale) {
            maxY = graphView.secondScale.getMaxY(false)
            minY = graphView.secondScale.getMinY(false)
        } else {
            maxY = graphView.viewport.getMaxY(false)
            minY = graphView.viewport.getMinY(false)
        }
        val values = getValues(minX, maxX)

        // draw background
        var lastEndY: Double
        var lastEndX: Double
        var titleY = 0.0

        // draw data
        mPaint!!.strokeWidth = mStyles?.thickness!!.toFloat()
        mPaint!!.color = color
        mPaintBackground!!.color = mStyles?.backgroundColor!!
        val paint: Paint? = if (mCustomPaint != null) {
            mCustomPaint
        } else {
            mPaint
        }
        mPath!!.reset()
        if (mStyles?.drawBackground!!) {
            mPathBackground!!.reset()
        }
        val diffY = maxY - minY
        val diffX = maxX - minX
        val graphHeight = graphView.graphContentHeight.toFloat()
        val graphWidth = graphView.graphContentWidth.toFloat()
        val graphLeft = graphView.graphContentLeft.toFloat()
        val graphTop = graphView.graphContentTop.toFloat()
        lastEndY = 0.0
        lastEndX = 0.0

        // needed to end the path for background
        var lastUsedEndX = 0.0
        var lastUsedEndY = 0.0
        var firstX = -1f
        var firstY = -1f
        var lastRenderedX = Float.NaN
        var i = 0
        var lastAnimationReferenceX = graphLeft
        var sameXSkip = false
        var minYOnSameX = 0f
        var maxYOnSameX = 0f
        while (values.hasNext()) {
            val value = values.next()
            val valY = value!!.y - minY
            val ratY = valY / diffY
            var y = graphHeight * ratY
            val valueX = value.x
            val valX = valueX - minX
            val ratX = valX / diffX
            var x = graphWidth * ratX
            val orgX = x
            val orgY = y
            if (i > 0) {
                // overdraw
                var isOverdrawY = false
                var isOverdrawEndPoint = false
                var skipDraw = false
                if (x > graphWidth) { // end right
                    val b = (graphWidth - lastEndX) * (y - lastEndY) / (x - lastEndX)
                    y = lastEndY + b
                    x = graphWidth.toDouble()
                    isOverdrawEndPoint = true
                }
                if (y < 0) { // end bottom
                    // skip when previous and this point is out of bound
                    if (lastEndY < 0) {
                        skipDraw = true
                    } else {
                        val b = (0 - lastEndY) * (x - lastEndX) / (y - lastEndY)
                        x = lastEndX + b
                    }
                    y = 0.0
                    isOverdrawEndPoint = true
                    isOverdrawY = isOverdrawEndPoint
                }
                if (y > graphHeight) { // end top
                    // skip when previous and this point is out of bound
                    if (lastEndY > graphHeight) {
                        skipDraw = true
                    } else {
                        val b = (graphHeight - lastEndY) * (x - lastEndX) / (y - lastEndY)
                        x = lastEndX + b
                    }
                    y = graphHeight.toDouble()
                    isOverdrawEndPoint = true
                    isOverdrawY = isOverdrawEndPoint
                }
                if (lastEndX < 0) { // start left
                    val b = (0 - x) * (y - lastEndY) / (lastEndX - x)
                    lastEndY = y - b
                    lastEndX = 0.0
                }

                // we need to save the X before it will be corrected when overdraw y
                val orgStartX = lastEndX.toFloat() + (graphLeft + 1)
                if (lastEndY < 0) { // start bottom
                    if (!skipDraw) {
                        val b = (0 - y) * (x - lastEndX) / (lastEndY - y)
                        lastEndX = x - b
                    }
                    lastEndY = 0.0
                    isOverdrawY = true
                }
                if (lastEndY > graphHeight) { // start top
                    // skip when previous and this point is out of bound
                    if (!skipDraw) {
                        val b = (graphHeight - y) * (x - lastEndX) / (lastEndY - y)
                        lastEndX = x - b
                    }
                    lastEndY = graphHeight.toDouble()
                    isOverdrawY = true
                }
                val startX = lastEndX.toFloat() + (graphLeft + 1)
                val startY = (graphTop - lastEndY).toFloat() + graphHeight
                val endX = x.toFloat() + (graphLeft + 1)
                val endY = (graphTop - y).toFloat() + graphHeight
                var startXAnimated = startX
                var endXAnimated = endX
                if (endX < startX) {
                    // dont draw from right to left
                    skipDraw = true
                }

                // NaN can happen when previous and current value is out of y bounds
                if (!skipDraw && !java.lang.Float.isNaN(startY) && !java.lang.Float.isNaN(endY)) {
                    // animation
                    if (mAnimated) {
                        if (java.lang.Double.isNaN(mLastAnimatedValue) || mLastAnimatedValue < valueX) {
                            val currentTime = System.currentTimeMillis()
                            if (mAnimationStart == 0L) {
                                // start animation
                                mAnimationStart = currentTime
                                mAnimationStartFrameNo = 0
                            } else {
                                // anti-lag: wait a few frames
                                if (mAnimationStartFrameNo < 15) {
                                    // second time
                                    mAnimationStart = currentTime
                                    mAnimationStartFrameNo++
                                }
                            }
                            val timeFactor = (currentTime - mAnimationStart).toFloat() / ANIMATION_DURATION
                            val factor = mAnimationInterpolator!!.getInterpolation(timeFactor)
                            if (timeFactor <= 1.0) {
                                startXAnimated = (startX - lastAnimationReferenceX) * factor + lastAnimationReferenceX
                                startXAnimated = max(startXAnimated, lastAnimationReferenceX)
                                endXAnimated = (endX - lastAnimationReferenceX) * factor + lastAnimationReferenceX
                                ViewCompat.postInvalidateOnAnimation(graphView)
                            } else {
                                // animation finished
                                mLastAnimatedValue = valueX
                            }
                        } else {
                            lastAnimationReferenceX = endX
                        }
                    }

                    // draw data point
                    if (!isOverdrawEndPoint) {
                        if (mStyles?.drawDataPoints == true) {
                            // draw first datapoint
                            val prevStyle = paint!!.style
                            paint.style = Paint.Style.FILL
                            canvas.drawCircle(endXAnimated, endY, mStyles!!.dataPointsRadius, paint)
                            paint.style = prevStyle
                        }
                        registerDataPoint(endX, endY, value)
                    }
                    if (isDrawAsPath) {
                        mPath!!.moveTo(startXAnimated, startY)
                    }
                    // performance opt.
                    if (java.lang.Float.isNaN(lastRenderedX) || abs(endX - lastRenderedX) > .3f) {
                        if (isDrawAsPath) {
                            mPath!!.lineTo(endXAnimated, endY)
                        } else {
                            // draw vertical lines that were skipped
                            if (sameXSkip) {
                                sameXSkip = false
                                renderLine(canvas, floatArrayOf(lastRenderedX, minYOnSameX, lastRenderedX, maxYOnSameX), paint)
                            }
                            renderLine(canvas, floatArrayOf(startXAnimated, startY, endXAnimated, endY), paint)
                        }
                        lastRenderedX = endX
                    } else {
                        // rendering on same x position
                        // save min+max y position and draw it as line
                        if (sameXSkip) {
                            minYOnSameX = min(minYOnSameX, endY)
                            maxYOnSameX = max(maxYOnSameX, endY)
                        } else {
                            // first
                            sameXSkip = true
                            minYOnSameX = min(startY, endY)
                            maxYOnSameX = max(startY, endY)
                        }
                    }
                }
                if (mStyles!!.drawBackground) {
                    if (isOverdrawY) {
                        // start draw original x
                        if (firstX == -1f) {
                            firstX = orgStartX
                            firstY = startY
                            mPathBackground!!.moveTo(orgStartX, startY)
                        }
                        // from original start to new start
                        mPathBackground!!.lineTo(startXAnimated, startY)
                    }
                    if (firstX == -1f) {
                        firstX = startXAnimated
                        firstY = startY
                        mPathBackground!!.moveTo(startXAnimated, startY)
                    }
                    mPathBackground!!.lineTo(startXAnimated, startY)
                    mPathBackground!!.lineTo(endXAnimated, endY)
                }
                lastUsedEndX = endXAnimated.toDouble()
                lastUsedEndY = endY.toDouble()
            } else if (mStyles!!.drawDataPoints) {
                //fix: last value not drawn as datapoint. Draw first point here, and then on every step the end values (above)
                var firstLocalX = x.toFloat() + (graphLeft + 1)
                val firstLocalY = (graphTop - y).toFloat() + graphHeight
                if (firstLocalX >= graphLeft && firstLocalY <= graphTop + graphHeight) {
                    if (mAnimated && (java.lang.Double.isNaN(mLastAnimatedValue) || mLastAnimatedValue < valueX)) {
                        val currentTime = System.currentTimeMillis()
                        if (mAnimationStart == 0L) {
                            // start animation
                            mAnimationStart = currentTime
                        }
                        val timeFactor = (currentTime - mAnimationStart).toFloat() / ANIMATION_DURATION
                        val factor = mAnimationInterpolator!!.getInterpolation(timeFactor)
                        if (timeFactor <= 1.0) {
                            firstLocalX = (firstLocalX - lastAnimationReferenceX) * factor + lastAnimationReferenceX
                            ViewCompat.postInvalidateOnAnimation(graphView)
                        } else {
                            // animation finished
                            mLastAnimatedValue = valueX
                        }
                    }
                    val prevStyle = paint!!.style
                    paint.style = Paint.Style.FILL
                    canvas.drawCircle(firstLocalX, firstLocalY, mStyles!!.dataPointsRadius, paint)
                    paint.style = prevStyle
                    registerDataPoint(firstLocalX, firstLocalY, value)
                }
            }
            lastEndY = orgY
            lastEndX = orgX
            titleY = max(titleY, orgY)
            i++
        }
        if (isDrawAsPath) {
            // draw at the end
            canvas.drawPath(mPath!!, paint!!)
        }
        if (mStyles!!.drawBackground && firstX != -1f) {
            // end / close path
            if (lastUsedEndY != (graphHeight + graphTop).toDouble()) {
                // dont draw line to same point, otherwise the path is completely broken
                mPathBackground!!.lineTo(lastUsedEndX.toFloat(), graphHeight + graphTop)
            }
            mPathBackground!!.lineTo(firstX, graphHeight + graphTop)
            if (firstY != graphHeight + graphTop) {
                // dont draw line to same point, otherwise the path is completely broken
                mPathBackground!!.lineTo(firstX, firstY)
            }
            //mPathBackground.close();
            canvas.drawPath(mPathBackground!!, mPaintBackground!!)
        }
        if (title != null && lastUsedEndX > 0 && firstX != -1f) {
            val x = (lastUsedEndX + firstX) / 2
            val y = graphTop - titleY + graphHeight - 10
            paintTitle!!.color = color
            paintTitle!!.textSize = graphView.titleTextSize
            paintTitle!!.isFakeBoldText = textBold
            canvas.drawText(title, x.toFloat(), y.toFloat(), paintTitle!!)
        }
    }

    /**
     * just a wrapper to draw lines on canvas
     *
     * @param canvas
     * @param pts
     * @param paint
     */
    private fun renderLine(canvas: Canvas, pts: FloatArray, paint: Paint?) {
        if (pts.size == 4 && pts[0] == pts[2] && pts[1] == pts[3]) {
            // avoid zero length lines, to makes troubles on some devices
            // see https://github.com/appsthatmatter/GraphView/issues/499
            return
        }
        canvas.drawLines(pts, paint!!)
    }
    /**
     * the thickness of the line.
     * This option will be ignored if you are
     * using a custom paint via [.setCustomPaint]
     *
     * @return the thickness of the line
     */
    /**
     * the thickness of the line.
     * This option will be ignored if you are
     * using a custom paint via [.setCustomPaint]
     *
     * @param thickness thickness of the line
     */
    var thickness: Int
        get() = mStyles?.thickness!!
        set(value) {
            mStyles?.thickness = value
        }
    /**
     * flag whether the area under the line to the bottom
     * of the viewport will be filled with a
     * specific background color.
     *
     * @return whether the background will be drawn
     * @see .getBackgroundColor
     */
    /**
     * flag whether the area under the line to the bottom
     * of the viewport will be filled with a
     * specific background color.
     *
     * @param isDrawBackground whether the background will be drawn
     * @see .setBackgroundColor
     */
    var isDrawBackground: Boolean
        get() = mStyles?.drawBackground!!
        set(drawBackground) {
            mStyles!!.drawBackground = drawBackground
        }
    /**
     * flag whether the data points are highlighted as
     * a visible point.
     *
     * @return flag whether the data points are highlighted
     * @see .setDataPointsRadius
     */
    /**
     * flag whether the data points are highlighted as
     * a visible point.
     *
     * @param drawDataPoints flag whether the data points are highlighted
     * @see .setDataPointsRadius
     */
    var isDrawDataPoints: Boolean
        get() = mStyles!!.drawDataPoints
        set(drawDataPoints) {
            mStyles!!.drawDataPoints = drawDataPoints
        }
    /**
     * @return the radius for the data points.
     * @see .setDrawDataPoints
     */
    /**
     * @param dataPointsRadius the radius for the data points.
     * @see .setDrawDataPoints
     */
    var dataPointsRadius: Float
        get() = mStyles!!.dataPointsRadius
        set(dataPointsRadius) {
            mStyles!!.dataPointsRadius = dataPointsRadius
        }
    /**
     * @return the background color for the filling under
     * the line.
     * @see .setDrawBackground
     */
    /**
     * @param backgroundColor the background color for the filling under
     * the line.
     * @see .setDrawBackground
     */
    var backgroundColor: Int
        get() = mStyles!!.backgroundColor
        set(backgroundColor) {
            mStyles!!.backgroundColor = backgroundColor
        }

    /**
     * custom paint that can be used.
     * this will ignore the thickness and color styles.
     *
     * @param customPaint the custom paint to be used for rendering the line
     */
    fun setCustomPaint(customPaint: Paint?) {
        mCustomPaint = customPaint
    }

    /**
     * @param animated activate the animated rendering
     */
    fun setAnimated(animated: Boolean) {
        mAnimated = animated
    }

    /**
     * @param dataPoint     values the values must be in the correct order!
     * x-value has to be ASC. First the lowest x value and at least the highest x value.
     * @param scrollToEnd   true => graphview will scroll to the end (maxX)
     * @param maxDataPoints if max data count is reached, the oldest data
     * value will be lost to avoid memory leaks
     * @param silent        set true to avoid rerender the graph
     */
    override fun appendData(dataPoint: E, scrollToEnd: Boolean, maxDataPoints: Int, silent: Boolean) {
        if (!isAnimationActive) {
            mAnimationStart = 0
        }
        super.appendData(dataPoint, scrollToEnd, maxDataPoints, silent)
    }

    /**
     * @return currently animation is active
     */
    private val isAnimationActive: Boolean
        get() {
            if (mAnimated) {
                val curr = System.currentTimeMillis()
                return curr - mAnimationStart <= ANIMATION_DURATION
            }
            return false
        }

    override fun drawSelection(graphView: GraphView, canvas: Canvas, b: Boolean, value: DataPointInterface) {
        val spanX = graphView.viewport.getMaxX(false) - graphView.viewport.getMinX(false)
        val spanXPixel = graphView.graphContentWidth.toDouble()
        val spanY = graphView.viewport.getMaxY(false) - graphView.viewport.getMinY(false)
        val spanYPixel = graphView.graphContentHeight.toDouble()
        var pointX = (value.x - graphView.viewport.getMinX(false)) * spanXPixel / spanX
        pointX += graphView.graphContentLeft.toDouble()
        var pointY = (value.y - graphView.viewport.getMinY(false)) * spanYPixel / spanY
        pointY = graphView.graphContentTop + spanYPixel - pointY

        // border
        canvas.drawCircle(pointX.toFloat(), pointY.toFloat(), 30f, mSelectionPaint!!)

        // fill
        val prevStyle = mPaint!!.style
        mPaint!!.style = Paint.Style.FILL
        canvas.drawCircle(pointX.toFloat(), pointY.toFloat(), 23f, mPaint!!)
        mPaint!!.style = prevStyle
    }

    fun setTextBold(textBold: Boolean) {
        this.textBold = textBold
    }

    /**
     * wrapped styles regarding the line
     */
    private inner class Styles {
        /**
         * the thickness of the line.
         * This option will be ignored if you are
         * using a custom paint via [.setCustomPaint]
         */
        var thickness: Int = 5

        /**
         * flag whether the area under the line to the bottom
         * of the viewport will be filled with a
         * specific background color.
         *
         * @see .backgroundColor
         */
        var drawBackground = false

        /**
         * flag whether the data points are highlighted as
         * a visible point.
         *
         * @see .dataPointsRadius
         */
        var drawDataPoints = false

        /**
         * the radius for the data points.
         *
         * @see .drawDataPoints
         */
        var dataPointsRadius = 10f

        /**
         * the background color for the filling under
         * the line.
         *
         * @see .drawBackground
         */
        var backgroundColor = Color.argb(100, 172, 218, 255)
    }

    companion object {
        private const val ANIMATION_DURATION: Long = 333
    }
}