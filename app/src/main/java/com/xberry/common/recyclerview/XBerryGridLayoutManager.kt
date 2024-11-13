package com.xberry.common.recyclerview

import android.graphics.PointF
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil

class XBerryGridLayoutManager(
    private val rows: Int = 2,
    private val columns: Int = 5,
) : RecyclerView.LayoutManager(), RecyclerView.SmoothScroller.ScrollVectorProvider {

    private var itemWidth = 0
    private var itemHeight = 0
    private var totalWidth = 0
    private var offsetX = 0

    val horizontalOffset: Int
        get() = offsetX

    val totalPages: Int
        get() = ceil(itemCount / (rows * columns).toDouble()).toInt()

    override fun isAutoMeasureEnabled(): Boolean = true

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun canScrollHorizontally(): Boolean = true

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (itemCount == 0) {
            detachAndScrapAttachedViews(recycler)
            return
        }

        detachAndScrapAttachedViews(recycler)

        val parentWidth = width
        itemWidth = parentWidth / columns
        itemHeight = height / rows

        // Calculate the total width, accounting for all pages
        totalWidth = itemWidth * columns * ceil(itemCount / (rows * columns).toDouble()).toInt()

        layoutChunk(recycler)
    }

    private fun layoutChunk(recycler: RecyclerView.Recycler) {
        val visibleRect = Rect(offsetX, 0, offsetX + width, height)

        for (i in 0 until itemCount) {
            val page = i / (rows * columns)
            val positionInPage = i % (rows * columns)
            val row = positionInPage / columns
            val column = positionInPage % columns

            // Adjust left position
            val left = (page * columns + column) * itemWidth - offsetX

            val top = row * itemHeight
            val right = left + itemWidth
            val bottom = top + itemHeight

            // Only layout items that are in the visible area
            if (Rect.intersects(visibleRect, Rect(left, top, right, bottom))) {
                val view = recycler.getViewForPosition(i)
                addView(view)
                measureChildWithMargins(view, 0, 0)
                layoutDecorated(view, left, top, right, bottom)
            }
        }
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        val newOffsetX = offsetX + dx

        // Calculate the maximum offset to prevent overscroll
        val maxOffsetX = totalWidth - width

        // Constrain offsetX within the valid range
        val constrainedOffsetX = newOffsetX.coerceIn(0, maxOffsetX)

        // Calculate the actual scroll distance
        val actualDx = constrainedOffsetX - offsetX

        // Update offsetX to the new constrained value
        offsetX = constrainedOffsetX

        // Offset children and re-layout
        offsetChildrenHorizontal(-actualDx)
        layoutChunk(recycler)

        return actualDx
    }

    fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        position: Int
    ) {
        val page = position / (rows * columns)
        val targetOffsetX = page * width
        recyclerView.smoothScrollBy(targetOffsetX - offsetX, 0)
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        smoothScrollToPosition(recyclerView, position)
    }

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF {
        val itemsPerPage = rows * columns
        val targetPage = targetPosition / itemsPerPage

        val direction = if (targetPage > offsetX / width) 1 else -1

        return PointF(direction.toFloat(), 0f)
    }
}
