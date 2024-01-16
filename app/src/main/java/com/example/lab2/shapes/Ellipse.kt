package com.example.lab2.shapes

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import com.example.lab2.DrawingView
import com.example.lab2.Shape

class Ellipse (startX: Float, startY: Float, endX: Float, endY: Float, isFilled: Boolean, drawingView: DrawingView) : Shape(startX, startY, endX, endY, isFilled, drawingView) {
    override fun onActionMove(drawPath: Path) {
        super.onActionMove(drawPath)
        drawPath.addOval(getStartX(), getStartY(), getEndX(), getEndY(), Path.Direction.CW)
    }

    override fun onActionUp(drawPath: Path, drawCanvas: Canvas?, drawPaint: Paint) {
        super.onActionUp(drawPath, drawCanvas, drawPaint)
        drawPaint.pathEffect = null

        if(isFilled) {
            drawPaint.color = Color.WHITE
            drawPaint.style = Paint.Style.FILL_AND_STROKE
            drawCanvas!!.drawOval(getStartX(), getStartY(), getEndX(), getEndY(), drawPaint)
        }

        drawPaint.color = color
        drawPaint.style = Paint.Style.STROKE
        drawCanvas!!.drawOval(getStartX(), getStartY(), getEndX(), getEndY(), drawPaint)

        drawPaint.pathEffect = DashPathEffect(floatArrayOf(25f, 25f), 0f)
    }
}