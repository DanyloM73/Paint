package com.example.lab2.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.example.lab2.DrawingView
import com.example.lab2.Shape

class Point (startX: Float, startY: Float, drawingView: DrawingView) : Shape(startX, startY, startX, startY, false, drawingView) {
    override fun onActionMove (drawPath: Path) {
        super.onActionMove(drawPath)
        drawPath.addCircle(super.getEndX(), super.getEndY(), 3f, Path.Direction.CW)
    }

    override fun onActionUp(drawPath: Path, drawCanvas: Canvas?, drawPaint: Paint) {
        super.onActionUp(drawPath, drawCanvas, drawPaint)
        drawPaint.color = color
        drawCanvas!!.drawPoint(super.getEndX(), super.getEndY(), drawPaint)
    }
}