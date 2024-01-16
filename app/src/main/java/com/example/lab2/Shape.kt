package com.example.lab2

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path

open class Shape (private var startX: Float, private var startY: Float,
                  private var endX: Float, private var endY: Float, var isFilled: Boolean, var drawingView: DrawingView) {

    var color: Int = Color.BLACK

    fun changeColor(newColor: Int) {
        color = newColor
        onActionUp(drawingView.drawPath, drawingView.drawCanvas, drawingView.drawPaint)
        drawingView.invalidate()
        drawingView.drawPaint.color = Color.BLACK
    }

    fun setStartX(value: Float) { startX = value }
    fun setStartY(value: Float) { startY = value }
    fun setEndX(value: Float) { endX = value }
    fun setEndY(value: Float) { endY = value }

    fun getStartX(): Float { return startX }
    fun getStartY(): Float { return startY }
    fun getEndX(): Float { return endX }
    fun getEndY(): Float { return endY }

    fun removeSelf() {
        drawingView.removeShape(this)
    }

    open fun onActionDown(drawPath: Path) {
        drawPath.moveTo(startX, startY)
    }
    
    open fun onActionMove(drawPath: Path) {}
    
    open fun onActionUp(drawPath: Path, drawCanvas: Canvas?,
                         drawPaint: Paint) {}
}