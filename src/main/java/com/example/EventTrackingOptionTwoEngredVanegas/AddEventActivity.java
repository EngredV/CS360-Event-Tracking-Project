package com.example.EventTrackingOptionTwoEngredVanegas;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AddEventActivity extends AppCompatActivity {

    private EditText titleEditText, dateEditText, timeEditText, descriptionEditText;
    private Button saveButton;
    private DatabaseHelper dbHelper;
    private BottomNavigationView bottomNavigationView;

    private boolean editMode = false;
    private int eventId = -1;

    // Method to set edit mode and event ID
    public void setEditMode(boolean editMode, int eventId) {
        this.editMode = editMode;
        this.eventId = eventId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        titleEditText = findViewById(R.id.event_title);
        dateEditText = findViewById(R.id.event_date);
        timeEditText = findViewById(R.id.event_time);
        descriptionEditText = findViewById(R.id.event_description);
        saveButton = findViewById(R.id.save_event);

        dbHelper = new DatabaseHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addEvent();
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Activity selectedActivity = null;

                if (item.getItemId() == R.id.home) {
                    startActivity(new Intent(AddEventActivity.this, HomeActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.add_event) {
                    // No need to change this since we're already on the add event screen
                    return true;
                } else if (item.getItemId() == R.id.settings) {
                    startActivity(new Intent(AddEventActivity.this, SettingsActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

        Intent intent = getIntent();
        boolean editMode = intent.getBooleanExtra("editMode", false);
        if (editMode) {
            int eventId = intent.getIntExtra("eventId", -1);
            String title = intent.getStringExtra("title");
            String date = intent.getStringExtra("date");
            String time = intent.getStringExtra("time");
            String description = intent.getStringExtra("description");

            if (eventId != -1) {
                // Populate the form fields with the event details
                titleEditText.setText(title);
                dateEditText.setText(date);
                timeEditText.setText(time);
                descriptionEditText.setText(description);
            }
        }
    }

    private void addEvent() {
        String title = titleEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedEventId", eventId);
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("date", date);
        resultIntent.putExtra("time", time);
        resultIntent.putExtra("description", description);

        try {
            if (editMode) {
                // Updates existing event
                boolean isUpdated = dbHelper.updateEvent(eventId, title, date, time, description);
                if (isUpdated) {
                    setResult(Activity.RESULT_OK, resultIntent);
                    Toast.makeText(AddEventActivity.this, "Event updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddEventActivity.this, "Failed to update event", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Adds new event
                boolean isInserted = dbHelper.insertEvent(title, date, time, description);
                if (isInserted) {
                    setResult(Activity.RESULT_OK, resultIntent);
                    Toast.makeText(this, "Event successfully added!", Toast.LENGTH_SHORT).show();
                    titleEditText.setText("");
                    dateEditText.setText("");
                    timeEditText.setText("");
                    descriptionEditText.setText("");
                    // Navigates back to HomeActivity after saving
                    startActivity(new Intent(AddEventActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Failed to add event", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
                Log.e("AddEventActivity", "Error saving event", e);
                Toast.makeText(this, "Error saving event: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.EVENT_TITLE, title);
        values.put(DatabaseHelper.EVENT_DATE, date);
        values.put(DatabaseHelper.EVENT_TIME, time);
        values.put(DatabaseHelper.EVENT_DESCRIPTION, description);

        long newRowId = db.insert(DatabaseHelper.EVENT_TABLE, null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error adding event", Toast.LENGTH_SHORT).show();
        }
    }
}