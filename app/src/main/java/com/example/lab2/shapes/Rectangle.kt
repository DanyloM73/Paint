package com.example.lab2.shapes

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import com.example.lab2.DrawingView
import com.example.lab2.Shape
import kotlin.math.abs

class Rectangle (startX: Float, startY: Float, endX: Float, endY: Float, isFilled: Boolean, drawingView: DrawingView) : Shape(startX, startY, endX, endY, isFilled, drawingView) {

    fun calculateLeft(): Float {
        val startX = super.getStartX()
        val endX = super.getEndX()
        return startX - abs(startX - endX)
    }
    fun calculateRight(): Float {
        val startX = super.getStartX()
        val endX = super.getEndX()
        return startX + abs(startX - endX)
    }
    fun calculateTop(): Float {
        val startY = super.getStartY()
        val endY = super.getEndY()
        return startY - abs(startY - endY)
    }
    fun calculateBottom(): Float {
        val startY = super.getStartY()
        val endY = super.getEndY()
        return startY + abs(startY - endY)
    }

    override fun onActionMove(drawPath: Path) {
        super.onActionMove(drawPath)
        drawPath.addRect(calculateLeft(), calculateTop(),
            calculateRight(), calculateBottom(), Path.Direction.CW)
    }

    override fun onActionUp(drawPath: Path, drawCanvas: Canvas?, drawPaint: Paint) {
        super.onActionUp(drawPath, drawCanvas, drawPaint)
        drawPaint.pathEffect = null

        if(isFilled) {
            drawPaint.color = Color.WHITE
            drawPaint.style = Paint.Style.FILL_AND_STROKE
            drawCanvas!!.drawRect(
                calculateLeft(), calculateTop(),
                calculateRight(), calculateBottom(), drawPaint
            )
        }

        drawPaint.color = color
        drawPaint.style = Paint.Style.STROKE
        drawCanvas!!.drawRect(calculateLeft(), calculateTop(),
            calculateRight(), calculateBottom(), drawPaint)

        drawPaint.pathEffect = DashPathEffect(floatArrayOf(25f, 25f), 0f)
    }

}