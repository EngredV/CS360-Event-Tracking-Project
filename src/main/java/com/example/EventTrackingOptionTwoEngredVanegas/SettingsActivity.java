package com.example.EventTrackingOptionTwoEngredVanegas;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Activity selectedActivity = null;

                if (item.getItemId() == R.id.home) {
                    startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                    finish();
                    return true;
                } else // No need to change this since we're already on the settings screen
                    if (item.getItemId() == R.id.add_event) {
                    startActivity(new Intent(SettingsActivity.this, AddEventActivity.class));
                    finish();
                    return true;
                } else return item.getItemId() == R.id.settings;
            }
        });

        LinearLayout editProfile = findViewById(R.id.edit_profile_layout);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class));
            }
        });

        LinearLayout pushNotifications = findViewById(R.id.push_notifications_layout);
        pushNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, PushNotificationsActivity.class));
            }
        });
    }
}