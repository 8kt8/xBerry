package com.xberry.common.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xberry.R
import com.xberry.common.recyclerview.XBerryAdapter.XBerryViewHolder

class XBerryAdapter(private val items: List<String>) : RecyclerView.Adapter<XBerryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XBerryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return XBerryViewHolder(view)
    }

    override fun onBindViewHolder(holder: XBerryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class XBerryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String) {
            textView.text = text
            textView.setBackgroundColor(itemView.resources.getColor(R.color.teal_700, itemView.context.theme))
        }
    }
}
