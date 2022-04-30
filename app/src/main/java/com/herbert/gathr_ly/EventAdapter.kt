package com.herbert.gathr_ly

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.herbert.gathr_ly.fragments.EventsFragment

class EventAdapter(val context: Context, val events: MutableList<Event>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Specify the layout file to use for this item

        val view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = events[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    // Clean all elements of the recycler
    fun clear() {
        events.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(eventList: List<Event>) {
        events.addAll(eventList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvDetails: TextView = itemView.findViewById(R.id.tvDetails)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(event: Event) {
            tvName.text = event.getName()
            tvDetails.text = event.getDetails()
            // figure out time
            val bestTimesList = event.bestTimes()
            var result = ""
            for (time in bestTimesList!!) {
                result += if (result == "") {
                    time
                } else {
                    "\n$time"
                }
            }
            if (result == "") {
                result = "No overlapping times."
            }
            tvTime.text = result
        }

        override fun onClick(p0: View?) {
            Log.i(EventsFragment.TAG, "Clicked on position: $adapterPosition")
            val event = events[adapterPosition]
            val intent = Intent(context, EventDetailActivity::class.java)
            intent.putExtra(KEY_OBJECT_ID, event.objectId)
            context.startActivity(intent)
        }
    }

    companion object {
        const val KEY_OBJECT_ID = "objectId"
    }
}
