package com.example.EventTrackingOptionTwoEngredVanegas;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private DatabaseHelper databaseHelper;
    private BottomNavigationView bottomNavigationView;
    public static final int REQUEST_CODE_EDIT_EVENT = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler_view_events);
        databaseHelper = new DatabaseHelper(this);

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(this, eventList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Activity selectedActivity = null;

                if (item.getItemId() == R.id.home) {
                    // No need to change this since we're already on the home screen
                    return true;
                } else if (item.getItemId() == R.id.add_event) {
                    startActivity(new Intent(HomeActivity.this, AddEventActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.settings) {
                    startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("HomeActivity", "onResume called, clearing eventList and loading events");
        eventList.clear(); // Prevents duplication by Clearing the list
        loadEvents(); // Refreshes the list when coming back to HomeActivity
    }

    private void loadEvents() {
        Log.d("HomeActivity", "Loading events...");
        Cursor cursor = null;
        try {
            cursor = databaseHelper.getAllEvents();
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No events found", Toast.LENGTH_SHORT).show();
            } else {
                eventList.clear(); // The list is cleared before adding new items
                Log.d("HomeActivity", "eventList cleared, cursor count: " + cursor.getCount());
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.EVENT_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EVENT_TITLE));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EVENT_DATE));
                    String time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EVENT_TIME));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EVENT_DESCRIPTION));

                    // Checks if the event with this ID already exists in the eventList to avoid duplicates
                    boolean eventExists = false;
                    for (Event event : eventList) {
                        if (event.getId() == id) {
                            eventExists = true;
                            break;
                        }
                    }

                    if (!eventExists) {
                        eventList.add(new Event(id, title, date, time, description));
                        Log.d("HomeActivity", "Event loaded: " + id + ", " + title + ", " + date + ", " + time + ", " + description);
                    } else {
                        Log.d("HomeActivity", "Event with ID " + id + " already exists in eventList, skipping.");
                    }
                }
                Log.d("HomeActivity", "eventList size after loading: " + eventList.size());
                // Notifies the adapter that an event item has been inserted at the last position
                eventAdapter.notifyItemInserted(eventList.size() - 1);
            }
        }catch (Exception e)  {
            Log.e("HomeActivity", "Error loading events", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_EVENT && resultCode == Activity.RESULT_OK && data != null) {
            int updatedEventId = data.getIntExtra("updatedEventId", -1);
            String title = data.getStringExtra("title");
            String date = data.getStringExtra("date");
            String time = data.getStringExtra("time");
            String description = data.getStringExtra("description");

            if (updatedEventId != -1) {
                for (int i = 0; i < eventList.size(); i++) {
                    if (eventList.get(i).getId() == updatedEventId) {
                        eventList.get(i).setTitle(title);
                        eventList.get(i).setDate(date);
                        eventList.get(i).setTime(time);
                        eventList.get(i).setDescription(description);
                        eventAdapter.updateEvent(i, eventList.get(i));
                        break;
                    }
                }
            }
        }
    }
}