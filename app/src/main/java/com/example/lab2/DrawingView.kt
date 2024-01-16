package com.example.lab2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.lab2.shapes.Cube
import com.example.lab2.shapes.Ellipse
import com.example.lab2.shapes.Line
import com.example.lab2.shapes.LineOO
import com.example.lab2.shapes.Point
import com.example.lab2.shapes.Rectangle

class DrawingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var currentShape: Shape
    private lateinit var shapesTable: Table
    var currentShapeName: String = ""
    val shapes = ShapesList()
    private var startX = 0f
    private var startY = 0f

    var drawPath: Path = Path()
    var drawPaint: Paint = Paint()
    private var canvasPaint: Paint = Paint(Paint.DITHER_FLAG)
    var drawCanvas: Canvas? = null
    private var canvasBitmap: Bitmap? = null

    init {
        setupDrawing()
    }

    fun setCurrentShape(shape: String) {
        currentShapeName = shape
    }

    private fun setupDrawing() {
        drawPaint.color = Color.BLACK
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = 15f
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND
        drawPaint.pathEffect = DashPathEffect(floatArrayOf(25f, 25f), 0f)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(canvasBitmap!!, 0f, 0f, canvasPaint)
        canvas.drawPath(drawPath, drawPaint)
    }

    fun createShapeByCoords(shapeType: String, startCoordX: Float, startCoordY: Float, endCoordX: Float, endCoordY: Float) {
        currentShapeName = shapeType
        val shape = createNewShape()

        shape.setStartX(startCoordX)
        shape.setStartY(startCoordY)
        shape.setEndX(endCoordX)
        shape.setEndY(endCoordY)

        shape.onActionUp(drawPath, drawCanvas, drawPaint)
        shapes.addShape(shape)
        shapes.addShapeToTable(shapesTable)
    }

    private fun createNewShape(): Shape {
        return when (currentShapeName) {
            "КРАПКА" -> Point(startX, startY, this)
            "ЛІНІЯ" -> Line(startX, startY, startX, startY, this)
            "ПРЯМОКУТНИК" -> Rectangle(startX, startY, startX, startY, true, this)
            "ЕЛІПС" -> Ellipse(startX, startY, startX, startY, false, this)
            "КУБ" -> Cube(startX, startY, startX, startY, this)
            "ГАНТЕЛЯ" -> LineOO(startX, startY, startX, startY, this)
            else -> Shape(startX, startY, startX, startY, false, this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (currentShapeName.isNotEmpty()) {
            val touchX = event.x
            val touchY = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = touchX
                    startY = touchY
                    currentShape = createNewShape()
                    currentShape.onActionDown(drawPath)
                }

                MotionEvent.ACTION_MOVE -> {
                    drawPath.reset()
                    currentShape.setEndX(touchX)
                    currentShape.setEndY(touchY)
                    currentShape.onActionMove(drawPath)
                }

                MotionEvent.ACTION_UP -> {
                    drawPath.reset()
                    currentShape.setEndX(touchX)
                    currentShape.setEndY(touchY)
                    currentShape.onActionUp(drawPath, drawCanvas, drawPaint)
                    shapes.addShape(currentShape)
                    shapes.addShapeToTable(shapesTable)
                }

                else -> return super.onTouchEvent(event)
            }
            invalidate()
            return true
        }
        return true
    }

    fun removeShape(shape: Shape) {
        shapes.removeShape(shape)
        canvasBitmap = Bitmap.createBitmap(
            canvasBitmap!!.width,
            canvasBitmap!!.height, Bitmap.Config.ARGB_8888
        )
        drawCanvas = Canvas(canvasBitmap!!)
        shapes.shapes.forEach{ it.onActionUp(drawPath, drawCanvas, drawPaint) }
        invalidate()
    }

    fun removeSelection() { shapes.removeSelection() }

    fun setTable(table: Table) { shapesTable = table }

    fun clearView() {
        canvasBitmap = Bitmap.createBitmap(
            canvasBitmap!!.width,
            canvasBitmap!!.height, Bitmap.Config.ARGB_8888
        )
        drawCanvas = Canvas(canvasBitmap!!)
        shapes.clearList()
        shapesTable.clearTable()
        invalidate()
    }
}