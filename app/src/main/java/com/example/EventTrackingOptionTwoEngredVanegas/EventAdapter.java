package com.example.EventTrackingOptionTwoEngredVanegas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private Context context;
    private DatabaseHelper databaseHelper;

    public EventAdapter(Context context, List<Event> eventList) {

        this.eventList = eventList;
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.titleTextView.setText(event.getTitle());
        holder.dateTextView.setText(event.getDate());
        holder.timeTextView.setText(event.getTime());
        holder.descriptionTextView.setText(event.getDescription());

        // Sets OnClickListener for delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implements delete operation
                int adapterPosition = holder.getBindingAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    deleteEvent(event.getId(), adapterPosition);
                }
            }
        });

        // Sets OnClickListener for edit button
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigates to AddEventActivity for editing
                Intent editIntent = new Intent(context, AddEventActivity.class);
                editIntent.putExtra("editMode", true);
                editIntent.putExtra("eventId", event.getId());
                editIntent.putExtra("title", event.getTitle());
                editIntent.putExtra("date", event.getDate());
                editIntent.putExtra("time", event.getTime());
                editIntent.putExtra("description", event.getDescription());
                ((Activity) context).startActivityForResult(editIntent, HomeActivity.REQUEST_CODE_EDIT_EVENT);
            }
        });
    }

    @Override
    public int getItemCount() {

        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView dateTextView;
        TextView timeTextView;
        TextView descriptionTextView;
        Button deleteButton;
        Button editButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_title);
            dateTextView = itemView.findViewById(R.id.text_view_date);
            timeTextView = itemView.findViewById(R.id.text_view_time);
            descriptionTextView = itemView.findViewById(R.id.text_view_description);
            deleteButton = itemView.findViewById(R.id.delete_event);
            editButton = itemView.findViewById(R.id.edit_event);
        }
    }

    private void deleteEvent(int eventId, int position) {
        // Remove event from database
        boolean deleted = databaseHelper.deleteEvent(eventId);

        if (deleted) {
            // Removes event from list
            eventList.remove(position);
            // Notifies adapter that item is removed
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, eventList.size());
            Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to delete event", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateEvent(int position, Event updatedEvent) {
        eventList.set(position, updatedEvent);
        notifyItemChanged(position);
    }
}
