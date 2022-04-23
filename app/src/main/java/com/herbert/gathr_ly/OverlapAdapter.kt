package com.herbert.gathr_ly

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.round

class OverlapAdapter(val context: Context, val counts: IntArray, val maxCount: Int): RecyclerView.Adapter<OverlapAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_hour, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val count = counts[position]
        holder.bind(count)
    }

    override fun getItemCount(): Int {
        return counts.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHour: TextView = itemView.findViewById(R.id.tvHour)
        val frame: FrameLayout = itemView.findViewById(R.id.frame)
        val tvCount: TextView = itemView.findViewById(R.id.tvCount)

        fun bind(count: Int) {
            tvHour.text = "$adapterPosition:00"
            tvCount.text = count.toString()
            val opaquePercentInAlphaRange: Int = round(((count * 1.0) / maxCount) * 255).toInt()
            if (opaquePercentInAlphaRange == 0) {
                frame.background = context.getDrawable(R.drawable.border)
            } else {
                val color = Color.argb(opaquePercentInAlphaRange, 80, 80, 80)
                frame.setBackgroundColor(color)
            }
        }
    }
}