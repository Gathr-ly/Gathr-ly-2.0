package com.herbert.gathr_ly

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScheduleAdapter(val context: Context, val schedule: BooleanArray): RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    // Define listener member variable
    private var listener: OnItemClickListener? = null

    // Define the listener interface
    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    // Define the method that allows the parent activity or fragment to define the listener
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_hour, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hour = schedule[position]
        holder.bind(hour)
    }

    override fun getItemCount(): Int {
        return schedule.size
    }

    inner class ViewHolder(itemView: View, clickListener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView){
        val tvHour: TextView = itemView.findViewById(R.id.tvHour)
        val frame: FrameLayout = itemView.findViewById(R.id.frame)
        var hour = false

        init {
            frame.setOnClickListener{
                clickListener?.onItemClick(itemView, adapterPosition)
            }
        }

        fun bind(_hour: Boolean) {
            hour = _hour
            tvHour.text = "$adapterPosition:00"
            if (_hour) {
                frame.setBackgroundColor(context.getColor(R.color.gray))
            } else {
                frame.background = context.getDrawable(R.drawable.border)
            }
        }

        fun onClick() {
            hour = !hour
            schedule[adapterPosition] = hour
            bind(hour)
        }

    }
}