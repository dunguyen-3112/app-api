package com.test.cdcn_appmobile.ui.main.statistical.graph

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.View
import com.test.cdcn_appmobile.R
import com.test.cdcn_appmobile.data.models.DrawerObject
import com.test.cdcn_appmobile.extension.getHeight
import com.test.cdcn_appmobile.extension.toStringNumber


/*
 * Created by tuyen.dang on 12/4/2022
 */

@SuppressLint("ViewConstructor")
class DrawView(
    context: Context,
    private val listDrawerObject: List<DrawerObject>,
    private val onSuccessDraw: () -> Unit
) : View(context) {

    private val initX = 150f
    private val initY = context.getHeight() * 7 / 10 - 100

    private var textTitlePaint: Paint = Paint()
    private var textValueOXYPaint: Paint = Paint()
    private var textValueOXPaint: Paint = Paint()
    private var redPaintBrushFill: Paint = Paint()
    private var greenPaintBrushFill: Paint = Paint()
    private var grayPaintBrushStroke: Paint = Paint()
    private var graphPath = Path()
    private var xDraw = initX
    private var yDraw = initY
    private var widthDrawMax = 0f
    private var heightDrawMax = 20
    private var listPointUnitLine = ArrayList<Float>()
    private var listPointDraw = ArrayList<Float>()
    private var rangeOfOne = 0f
    private var speedStep = 5f
    private var coefficient = 1
    private var maxRangeOY = 0L
    private var stepRange = 0L

    init {
        var temp = 0L

        for (i in listDrawerObject) {
            if (i.getMaxMoney() > temp) {
                temp = i.getMaxMoney()
            }
        }

        if (temp != 0L) {
            while (maxRangeOY < temp) {
                maxRangeOY += 100
            }
        } else {
            maxRangeOY = 500
        }

        stepRange = maxRangeOY / 10

        rangeOfOne = 250f
        widthDrawMax = rangeOfOne * listDrawerObject.size + initX

        textTitlePaint.apply {
            color = Color.BLACK
            textSize = 70f
        }

        textValueOXYPaint.apply {
            color = Color.BLACK
            textSize = 30f
            textAlign = Paint.Align.RIGHT
            initGraphPath()
        }

        textValueOXPaint.run {
            color = Color.BLACK
            textSize = 30f
            textAlign = Paint.Align.CENTER
        }

        redPaintBrushFill.apply {
            color = context.getColor(R.color.colorVenetianRed)
            style = Paint.Style.FILL
        }

        greenPaintBrushFill.apply {
            color = context.getColor(R.color.colorMiddleGreenYellow)
            style = Paint.Style.FILL
        }

        grayPaintBrushStroke.apply {
            color = context.getColor(R.color.colorChineseSilver)
            style = Paint.Style.STROKE
            strokeWidth = 5f
            textSize = 30f
        }

        for (i in (0..10)) {
            listPointUnitLine.add(
                (initY - heightDrawMax) / maxRangeOY * stepRange * i + heightDrawMax
            )
        }
        listPointUnitLine.reverse()

        for (i in (listDrawerObject.indices)) {
            listPointDraw.add(
                (widthDrawMax - initX) / listDrawerObject.size * i
            )
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        layoutParams.width = widthDrawMax.toInt()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        initGraphPath()

        canvas?.run {

            drawColor(context.getColor(R.color.white))

            drawPath(graphPath, grayPaintBrushStroke)

            listPointUnitLine.forEachIndexed { index, element ->
                drawPath(pathUnitLine(element), grayPaintBrushStroke)
                if (yDraw < element) {
                    drawText(
                        (stepRange * index * coefficient).toStringNumber(),
                        initX - 50,
                        element + 10f,
                        textValueOXYPaint
                    )
                }
            }

            listPointDraw.forEachIndexed { index, element ->
                val rectTurnover =
                    rectValue(element.toInt(), listDrawerObject[index].receivedMoney, 0)
                val rectCost = rectValue(element.toInt(), listDrawerObject[index].spendMoney, 1)

                drawRect(rectTurnover, greenPaintBrushFill)
                drawRect(rectCost, redPaintBrushFill)

                if (xDraw > element + (rangeOfOne / 6 * 1) + (rangeOfOne / 6 * 2) + initX) {
                    drawText(
                        listDrawerObject[index].time,
                        element + (rangeOfOne / 6 * 1) + (rangeOfOne / 6 * 2) + initX,
                        initY + 50f,
                        textValueOXPaint.apply {
                            textAlign = Paint.Align.CENTER
                        }
                    )
                }
            }

            if (xDraw < widthDrawMax)
                xDraw += speedStep * 3
            if (yDraw > heightDrawMax)
                yDraw -= speedStep

            invalidate()
            onSuccessDraw()
        }

    }

    private fun initGraphPath() {
        graphPath.moveTo(initX, initY)
        graphPath.lineTo(initX, yDraw)
    }

    private fun pathUnitLine(startY: Float): Path {
        val path = Path()
        path.moveTo(initX, startY)
        path.lineTo(xDraw, startY)
        return path
    }

    private fun rectValue(startX: Int, value: Long, index: Int): Rect {
        val rect = Rect()
        rect.set(
            startX + (rangeOfOne / 6 * 1).toInt() + (rangeOfOne / 6 * 2).toInt() * index + initX.toInt(),
            if (yDraw > initY.toInt() - fromValueToOY(value).toInt()) {
                yDraw.toInt()
            } else {
                initY.toInt() - fromValueToOY(value).toInt()
            },
            startX + (rangeOfOne / 6 * 1).toInt() + (rangeOfOne / 6 * 2).toInt() * (index + 1) + initX.toInt(),
            initY.toInt()
        )
        return rect
    }

    private fun fromValueToOY(value: Long): Float = (initY - heightDrawMax) / maxRangeOY * value

}