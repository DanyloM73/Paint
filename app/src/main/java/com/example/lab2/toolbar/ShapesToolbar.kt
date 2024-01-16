package com.example.lab2.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.lab2.R


class ShapesToolbar(context: Context, attrs: AttributeSet?) : Toolbar(context, attrs) {

    private var onShapeSelectedListener: OnShapeSelectedListener? = null

    fun setOnShapeSelectedListener(listener: OnShapeSelectedListener) {
        onShapeSelectedListener = listener
    }

    val shapes = listOf(
        Pair(R.drawable.point_icon, "КРАПКА"),
        Pair(R.drawable.line_icon, "ЛІНІЯ"),
        Pair(R.drawable.rect_icon, "ПРЯМОКУТНИК"),
        Pair(R.drawable.circle_icon, "ЕЛІПС"),
        Pair(R.drawable.cube_icon, "КУБ"),
        Pair(R.drawable.lineoo_icon, "ГАНТЕЛЯ")
    )

    private val buttonContainer = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER_HORIZONTAL
        }
    }

    init {
        val buttonSize = context.resources.getDimensionPixelSize(R.dimen.toolbar_button_size)
        val buttonMargin = context.resources.getDimensionPixelSize(R.dimen.toolbar_button_margin)

        for ((shape, name) in shapes) {
            val button = ImageButton(context).apply {
                layoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize).apply {
                    marginStart = buttonMargin
                    marginEnd = buttonMargin
                }
                setImageResource(shape)
                setBackgroundResource(R.color.btn)
                setOnClickListener { view ->
                    onShapeSelected(view as ImageButton, name)
                }
                setOnLongClickListener {
                    onNotify(name)
                }
            }
            buttonContainer.addView(button)
        }

        addView(buttonContainer)
    }

    private fun onShapeSelected(button: ImageButton, shapeName: String) {
        for (i in 0 until buttonContainer.childCount) {
            val child = buttonContainer.getChildAt(i)
            if (child is ImageButton) {
                child.setBackgroundColor(ContextCompat.getColor(context, R.color.btn))
            }
        }

        button.setBackgroundColor(ContextCompat.getColor(context, R.color.btn_selected))

        Toast.makeText(context, "Обрано фігуру $shapeName", Toast.LENGTH_LONG).show()

        onShapeSelectedListener?.onShapeSelected(shapeName)
    }

    fun selectShapeButton(shapeName: String) {
        for (i in 0 until buttonContainer.childCount) {
            val button = buttonContainer.getChildAt(i) as ImageButton
            val shapeType = shapes[i].second
            if (shapeType == shapeName) {
                button.setBackgroundColor(ContextCompat.getColor(context, R.color.btn_selected))
            } else {
                button.setBackgroundColor(ContextCompat.getColor(context, R.color.btn))
            }
        }
    }

    private fun onNotify(shapeName: String): Boolean{
        Toast.makeText(context, shapeName, Toast.LENGTH_LONG).show()
        return true
    }
}
