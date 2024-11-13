package com.xberry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xberry.common.recyclerview.XBerryAdapter
import com.xberry.common.recyclerview.XBerryDragAndDropHelper
import com.xberry.common.recyclerview.XBerryGridLayoutManager
import com.xberry.common.recyclerview.XBerrySnapHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        enableEdgeToEdge()
        setupList()
    }

    private fun setupList() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val gridLayoutManager = XBerryGridLayoutManager(rows = 2, columns = 5)
        recyclerView.layoutManager = gridLayoutManager

        val adapter = XBerryAdapter(generateSampleData().toMutableList())
        recyclerView.adapter = adapter

        val dragAndDropCallback = XBerryDragAndDropHelper(adapter)
        val itemTouchHelper = ItemTouchHelper(dragAndDropCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val snapHelper = XBerrySnapHelper(rows = 2, columns = 5)
        snapHelper.attachToRecyclerView(recyclerView)
    }

    private fun generateSampleData(): List<String> {
        return List(25) { "${it + 1}" }
    }
}