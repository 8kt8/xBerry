package com.xberry.common.recyclerview

import android.view.View
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class XBerrySnapHelper(
    private val rows: Int,
    private val columns: Int,
) : PagerSnapHelper() {

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager,
        velocityX: Int,
        velocityY: Int
    ): Int {
        val lm = layoutManager as? XBerryGridLayoutManager ?: return RecyclerView.NO_POSITION

        val itemsPerPage = rows * columns
        val currentPage = lm.horizontalOffset / lm.width
        val totalPages = lm.totalPages

        val forward = velocityX > 0
        val targetPage = when {
            forward -> (currentPage + 1).coerceAtMost(totalPages - 1)
            else -> (currentPage - 1).coerceAtLeast(0)
        }

        return targetPage * itemsPerPage
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        val lm = layoutManager as? XBerryGridLayoutManager
        if (lm == null || lm.width == 0) return null
        val itemsPerPage = rows * columns
        val page = lm.horizontalOffset / lm.width
        val position = page * itemsPerPage
        return lm.findViewByPosition(position)
    }
}