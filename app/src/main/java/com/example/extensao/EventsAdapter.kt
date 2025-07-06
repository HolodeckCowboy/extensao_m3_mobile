package com.example.extensao

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.DateTimeFormatter

class EventsAdapter(
    private val events: List<Event>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.eventTitle)
        val date: TextView = view.findViewById(R.id.eventDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.title.text = event.title
        holder.date.text = event.dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)
        holder.itemView.setOnClickListener { onItemClick(event.id) }
    }

    override fun getItemCount() = events.size
}