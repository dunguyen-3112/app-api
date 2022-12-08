package com.test.cdcn_appmobile.ui.main.transactions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.View
import com.test.cdcn_appmobile.R
import com.test.cdcn_appmobile.data.models.DrawerObject
import com.test.cdcn_appmobile.extension.getWidth

/*
 * Created by tuyen.dang on 10/15/2022
 */

@SuppressLint("ViewConstructor")
class DrawView(
    context: Context,
    private val listDrawerObject: MutableList<DrawerObject>,
) : View(context) {

    private val initX = 120f
    private val initY = context.getWidth() * 9 / 10 - 20

    private var textTitlePaint: Paint = Paint()
    private var textValueOXYPaint: Paint = Paint()
    private var textValueOXPaint: Paint = Paint()
    private var redPaintBrushFill: Paint = Paint()
    private var bluePaintBrushFill: Paint = Paint()
    private var greenPaintBrushFill: Paint = Paint()
    private var grayPaintBrushStroke: Paint = Paint()
    private var graphPath = Path()
    private var xDraw = initX
    private var yDraw = initY
    private var widthDrawMax = context.getWidth() * 7 / 10 - 20
    private var heightDrawMax = context.getWidth() * 9 / 10 * 1 / 10
    private var listPointUnitLine = ArrayList<Float>()
    private var listPointDraw = ArrayList<Float>()
    private var rangeOfOne = 0f
    private val speedStep = 5f
    private val coefficient = 1
    private var maxRangeOY = 0
    private var stepRange = 0

    init {
        rangeOfOne = (widthDrawMax - initX) / listDrawerObject.size

        val temp =
            if (listDrawerObject[0].receivedMoney >= listDrawerObject[0].spendMoney)
                listDrawerObject[0].receivedMoney
            else listDrawerObject[0].spendMoney

        if(temp != 0L) {
            while (maxRangeOY < temp) {
                maxRangeOY += 100
            }
        } else {
            maxRangeOY = 500
        }

        stepRange = maxRangeOY / 10

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

        textValueOXPaint.apply {
            color = Color.BLACK
            textSize = 30f
            textAlign = Paint.Align.CENTER
        }

        redPaintBrushFill.apply {
            color = context.getColor(R.color.colorVenetianRed)
            style = Paint.Style.FILL
        }

        bluePaintBrushFill.apply {
            color = context.getColor(R.color.colorCyanAzure)
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

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        initGraphPath()

        canvas?.run {
            drawPath(graphPath, grayPaintBrushStroke)

            listPointUnitLine.forEachIndexed { index, element ->
                drawPath(pathUnitLine(element), grayPaintBrushStroke)
                if (yDraw < element) {
                    drawText(
                        (stepRange * index * coefficient).toString(),
                        initX - 20f,
                        element + 10f,
                        textValueOXYPaint
                    )
                }
            }

            listPointDraw.forEachIndexed { index, element ->
                val rectTurnover = rectValue(element.toInt(), listDrawerObject[index].spendMoney, 0)
                val rectCost = rectValue(element.toInt(), listDrawerObject[index].receivedMoney, 1)

                drawRect(rectTurnover, redPaintBrushFill)
                drawRect(rectCost, greenPaintBrushFill)
            }

            drawRectNote(0, 60, "Chi", canvas, redPaintBrushFill)
            drawRectNote(1, 60, "Thu", canvas, greenPaintBrushFill)

            if (xDraw < widthDrawMax)
                xDraw += speedStep
            if (yDraw > heightDrawMax)
                yDraw -= speedStep

            invalidate()
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

    private fun drawRectNote(
        index: Int,
        noteLength: Int,
        title: String,
        canvas: Canvas?,
        paint: Paint,
    ) {
        val rect = Rect()
        rect.set(
            widthDrawMax.toInt() + 20,
            initY.toInt() / 2 + (noteLength + 50) * index,
            widthDrawMax.toInt() + 20 + noteLength,
            initY.toInt() / 2 + 100 * index + noteLength,
        )
        canvas?.drawRect(rect, paint)
        canvas?.drawText(
            title,
            widthDrawMax + 20f + noteLength + 20f,
            initY / 2 + (noteLength + 50) * index + noteLength * 1 / 2 + 10f,
            textValueOXPaint.apply {
                textAlign = Paint.Align.LEFT
            }
        )
    }

    private fun rectValue(startX: Int, value: Long, index: Int): Rect {
        val rect = Rect()
        rect.set(
            startX + (rangeOfOne / 4).toInt() + (rangeOfOne / 4).toInt() * index + initX.toInt(),
            if (yDraw > initY.toInt() - fromValueToOY(value).toInt()) {
                yDraw.toInt()
            } else {
                initY.toInt() - fromValueToOY(value).toInt()
            },
            startX + (rangeOfOne / 4).toInt() + (rangeOfOne / 4).toInt() * (index + 1) + initX.toInt(),
            initY.toInt()
        )
        return rect
    }

    private fun fromValueToOY(value: Long): Float = (initY - heightDrawMax) / maxRangeOY * value

}
