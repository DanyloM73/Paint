package com.example.lab2.shapes

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.example.lab2.DrawingView
import com.example.lab2.Shape

class LineOO(startX: Float, startY: Float, endX: Float, endY: Float, drawingView: DrawingView) : Shape(startX, startY, endX, endY, false, drawingView) {
    private fun createShapes(): Triple<Ellipse, Line, Ellipse> {
        val startCircle = Ellipse(getStartX() - 30, getStartY() - 30, getStartX() + 30, getStartY() + 30, true, drawingView)
        val endCircle = Ellipse(getEndX() - 30, getEndY() - 30, getEndX() + 30, getEndY() + 30, true, drawingView)
        val line = Line(getStartX(), getStartY(), getEndX(), getEndY(), drawingView)

        return Triple(startCircle, line, endCircle)
    }


    override fun onActionMove(drawPath: Path) {
        super.onActionMove(drawPath)
        val (startCircle, line, endCircle) = createShapes()

        line.onActionMove(drawPath)
        startCircle.onActionMove(drawPath)
        endCircle.onActionMove(drawPath)
    }

    override fun onActionUp(drawPath: Path, drawCanvas: Canvas?, drawPaint: Paint) {
        super.onActionUp(drawPath, drawCanvas, drawPaint)
        val (startCircle, line, endCircle) = createShapes()

        startCircle.changeColor(color)
        line.changeColor(color)
        endCircle.changeColor(color)

        line.onActionUp(drawPath, drawCanvas, drawPaint)
        startCircle.onActionUp(drawPath, drawCanvas, drawPaint)
        endCircle.onActionUp(drawPath, drawCanvas, drawPaint)
    }
}