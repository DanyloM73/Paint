package com.example.lab2.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.example.lab2.DrawingView
import com.example.lab2.Shape

class Cube(startX: Float, startY: Float, endX: Float, endY: Float, drawingView: DrawingView) : Shape(startX, startY, endX, endY, false, drawingView) {
    private fun createShapes(): Triple<Rectangle, Rectangle, Array<Line>> {
        val front = Rectangle(getStartX(), getStartY(), getEndX(), getEndY(), false, drawingView)
        val back = Rectangle(getStartX() - 70, getStartY() - 70, getEndX() - 70, getEndY() - 70, false, drawingView)
        val sides = arrayOf(
            Line(back.calculateLeft(), back.calculateTop(), front.calculateLeft(), front.calculateTop(), drawingView),
            Line(back.calculateRight(), back.calculateTop(), front.calculateRight(), front.calculateTop(), drawingView),
            Line(back.calculateLeft(), back.calculateBottom(), front.calculateLeft(), front.calculateBottom(), drawingView),
            Line(back.calculateRight(), back.calculateBottom(), front.calculateRight(), front.calculateBottom(), drawingView)
        )

        return Triple(front, back, sides)
    }

    override fun onActionMove(drawPath: Path) {
        super.onActionMove(drawPath)
        val (front, back, sides) = createShapes()

        front.onActionMove(drawPath)
        back.onActionMove(drawPath)
        sides.forEach { it.onActionMove(drawPath) }
    }

    override fun onActionUp(drawPath: Path, drawCanvas: Canvas?, drawPaint: Paint) {
        super.onActionUp(drawPath, drawCanvas, drawPaint)
        val (front, back, sides) = createShapes()

        front.changeColor(color)
        back.changeColor(color)
        sides.forEach { it.changeColor(color) }

        front.onActionUp(drawPath, drawCanvas, drawPaint)
        back.onActionUp(drawPath, drawCanvas, drawPaint)
        sides.forEach { it.onActionUp(drawPath, drawCanvas, drawPaint) }
    }
}
