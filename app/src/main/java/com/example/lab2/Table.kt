package com.example.lab2

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class Table(context: Context, attrs: AttributeSet?) : TableLayout(context, attrs) {

    init {
        addRowWithCells(cells = arrayOf("Назва", "X1", "X2", "Y1", "Y2"))
    }

    fun addRowWithCells(shapes: MutableList<Shape> = mutableListOf(), vararg cells: String) {
        val row = TableRow(context)
        for (cell in cells) {
            row.addView(createCell(cell))
        }
        this.addView(row)
        removeSelection()

        if (this.childCount > 1) {
            row.setOnClickListener {
                removeSelection()
                row.setBackgroundColor(ContextCompat.getColor(context, R.color.btn_del))
                shapes.forEach { it.changeColor(Color.BLACK) }
                val shape = shapes[this.indexOfChild(row) - 1]
                shape.changeColor(Color.RED)
            }

            row.setOnLongClickListener {
                val message = SpannableString("Ви впевнені, що хочете видалити цю фігуру?")
                message.setSpan(ForegroundColorSpan(Color.WHITE), 0, message.length, 0)
                val shape = shapes[this.indexOfChild(row) - 1]

                AlertDialog.Builder(context, R.style.AlertDialogCustom).apply {
                    setTitle("Підтвердження")
                    setMessage(message)
                    setPositiveButton("ТАК") { _, _ ->
                        shape.removeSelf()
                        this@Table.removeView(row)
                    }
                    setNegativeButton("ВІДМІНА", null)
                }.show()

                return@setOnLongClickListener true
            }

        }
    }

    fun removeSelection() {
        for (i in 0 until this.childCount) {
            val childRow = this.getChildAt(i) as TableRow
            childRow.setBackgroundColor(ContextCompat.getColor(context, R.color.btn))
        }
    }

    private fun createCell(text: String): TextView {
        val cell = TextView(context)
        cell.text = text
        cell.textSize = 18f
        cell.setTextColor(Color.WHITE)
        cell.setPadding(30, 15, 30, 15)
        return cell
    }

    fun clearTable() {
        while (this.childCount > 1) {
            this.removeViewAt(1)
        }
    }
}

