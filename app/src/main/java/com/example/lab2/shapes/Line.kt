package com.example.lab2.shapes

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import com.example.lab2.DrawingView
import com.example.lab2.Shape

class Line (startX: Float, startY: Float, endX: Float, endY: Float, drawingView: DrawingView) : Shape(startX, startY, endX, endY, false, drawingView) {

    override fun onActionMove(drawPath: Path) {
        super.onActionMove(drawPath)
        drawPath.moveTo(getStartX(), getStartY())
        drawPath.lineTo(getEndX(), getEndY())
    }

    override fun onActionUp(drawPath: Path, drawCanvas: Canvas?, drawPaint: Paint) {
        super.onActionUp(drawPath, drawCanvas, drawPaint)
        drawPaint.pathEffect = null
        drawPaint.color = color
        drawCanvas!!.drawLine(getStartX(), getStartY(), getEndX(), getEndY(), drawPaint)
        drawPaint.pathEffect = DashPathEffect(floatArrayOf(25f, 25f), 0f)
    }

}