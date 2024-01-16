package com.example.lab2

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.lab2.toolbar.OnShapeSelectedListener
import com.example.lab2.toolbar.ShapesToolbar
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    private val openFileLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            val inputStream = contentResolver.openInputStream(it)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = bufferedReader.readLine()
            }

            val jsonString = stringBuilder.toString()
            parseShapes(jsonString)
        }
    }

    private lateinit var drawingView: DrawingView
    private lateinit var toolbar: ShapesToolbar
    private lateinit var tableView: ScrollView
    private lateinit var table: Table
    private lateinit var buttonsPanel: LinearLayout
    private lateinit var clearButton: ImageButton
    private lateinit var tableButton: ImageButton
    private lateinit var downloadButton: ImageButton
    private lateinit var uploadButton: ImageButton
    private lateinit var closeTableButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawing_view)
        toolbar = findViewById(R.id.shapes_toolbar)
        tableView = findViewById(R.id.table_view)
        table = findViewById(R.id.table)
        buttonsPanel = findViewById(R.id.btns_panel)
        clearButton = findViewById(R.id.clear_btn)
        tableButton = findViewById(R.id.table_btn)
        downloadButton = findViewById(R.id.download_btn)
        uploadButton = findViewById(R.id.upload_btn)
        closeTableButton = findViewById(R.id.close_table)

        drawingView.setTable(table)

        clearButton.setOnClickListener {
            drawingView.clearView()
        }

        tableButton.setOnClickListener {
            buttonsPanel.visibility = View.GONE
            tableView.visibility = View.VISIBLE
        }

        closeTableButton.setOnClickListener {
            tableView.visibility = View.GONE
            buttonsPanel.visibility = View.VISIBLE
            drawingView.removeSelection()
            table.removeSelection()
        }

        downloadButton.setOnClickListener { createFileButtonClicked() }

        uploadButton.setOnClickListener { openFilePicker() }

        toolbar.setOnShapeSelectedListener(object : OnShapeSelectedListener {
            override fun onShapeSelected(shapeName: String) {
                drawingView.setCurrentShape(shapeName)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        drawingView.clearView()
    }

    private fun openFilePicker() {
        openFileLauncher.launch(arrayOf("*/*"))
    }

    private fun createFileButtonClicked() {
        val editText = EditText(this)
        val container = FrameLayout(this)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(50, 0, 50, 0)
        editText.layoutParams = params

        container.addView(editText)
        AlertDialog.Builder(this, R.style.AlertDialogCustom)
            .setTitle("Введіть назву файлу")
            .setView(container)
            .setPositiveButton("OK") { _, _ ->
                val filename = editText.text.toString()
                val data = drawingView.shapes.json
                saveData(this, data, filename)
            }
            .setNegativeButton("ВІДМІНА", null)
            .show()
    }

    private fun saveData(context: Context, data: JSONObject, filename: String) {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, filename)
        val outputStreamWriter = OutputStreamWriter(file.outputStream())
        outputStreamWriter.use { it.write(data.toString()) }
        Toast.makeText(context, "Файл $filename збережено", Toast.LENGTH_LONG).show()
    }

    private fun parseShapes(jsonString: String) {
        drawingView.clearView()
        val json = JSONObject(jsonString)

        val shapeTypes = json.keys()
        while (shapeTypes.hasNext()) {
            val shapeType = shapeTypes.next()
            val shapeArray = json.getJSONArray(shapeType)

            for (i in 0 until shapeArray.length()) {
                val shapeJson = shapeArray.getJSONObject(i)
                val startX = shapeJson.getJSONObject("start").getDouble("x1").toFloat()
                val startY = shapeJson.getJSONObject("start").getDouble("y1").toFloat()
                val endX = shapeJson.getJSONObject("end").getDouble("x2").toFloat()
                val endY = shapeJson.getJSONObject("end").getDouble("y2").toFloat()

                drawingView.createShapeByCoords(shapeType, startX, startY, endX, endY)
            }
        }
        toolbar.selectShapeButton(drawingView.currentShapeName)
    }
}
