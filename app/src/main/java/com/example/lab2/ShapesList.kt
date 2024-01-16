package com.example.lab2

import android.graphics.Color
import com.example.lab2.shapes.Cube
import com.example.lab2.shapes.Ellipse
import com.example.lab2.shapes.Line
import com.example.lab2.shapes.LineOO
import com.example.lab2.shapes.Point
import com.example.lab2.shapes.Rectangle
import org.json.JSONArray
import org.json.JSONObject

class ShapesList () {

    val shapes = mutableListOf<Shape>()
    var json = JSONObject()

    fun addShape(shape: Shape) {
        shapes.add(shape)

        val type = when (shape) {
            is Point -> "КРАПКА"
            is Line -> "ЛІНІЯ"
            is Rectangle -> "ПРЯМОКУТНИК"
            is Ellipse -> "ЕЛІПС"
            is LineOO -> "ГАНТЕЛЯ"
            is Cube -> "КУБ"
            else -> ""
        }

        val shapeJson = JSONObject().apply {
            put("start", JSONObject().apply {
                put("x1", shape.getStartX())
                put("y1", shape.getStartY())
            })
            put("end", JSONObject().apply {
                put("x2", shape.getEndX())
                put("y2", shape.getEndY())
            })
        }

        if (!json.has(type)) {
            json.put(type, JSONArray())
        }

        json.getJSONArray(type).put(shapeJson)
    }


    fun removeShape(shape: Shape) {
        val type = when (shape) {
            is Point -> "КРАПКА"
            is Line -> "ЛІНІЯ"
            is Rectangle -> "ПРЯМОКУТНИК"
            is Ellipse -> "ЕЛІПС"
            is LineOO -> "ГАНТЕЛЯ"
            is Cube -> "КУБ"
            else -> ""
        }

        if (json.has(type)) {
            val jsonArray = json.getJSONArray(type)
            val newJsonArray = JSONArray()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val startX = jsonObject.getJSONObject("start").getDouble("x1").toFloat()
                val startY = jsonObject.getJSONObject("start").getDouble("y1").toFloat()
                val endX = jsonObject.getJSONObject("end").getDouble("x2").toFloat()
                val endY = jsonObject.getJSONObject("end").getDouble("y2").toFloat()
                if (startX != shape.getStartX() && startY != shape.getStartY()
                    && endX != shape.getEndX() && endY != shape.getEndY()) {
                    newJsonArray.put(jsonObject)
                }
            }
            json.put(type, newJsonArray)
        }
        shapes.remove(shape)
    }

    fun addShapeToTable(table: Table) {
        val shape = shapes[shapes.lastIndex]

        val type = when (shape) {
            is Point -> "КРАПКА"
            is Line -> "ЛІНІЯ"
            is Rectangle -> "ПРЯМОК."
            is Ellipse -> "ЕЛІПС"
            is LineOO -> "ГАНТЕЛЯ"
            is Cube -> "КУБ"
            else -> ""
        }

        table.addRowWithCells(
            shapes, type, String.format("%.1f", shape.getStartX()), String.format("%.1f", shape.getEndX()),
            String.format("%.1f", shape.getStartY()), String.format("%.1f", shape.getEndY())
        )
    }

    fun removeSelection() {
        shapes.forEach { it.changeColor(Color.BLACK) }
    }

    fun clearList() {
        shapes.clear()
        json = JSONObject()
    }
}