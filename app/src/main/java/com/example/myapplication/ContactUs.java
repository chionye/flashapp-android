package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ContactUs extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        imageView = findViewById(R.id.vector);
        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(ContactUs.this, MainActivity.class);
            startActivity(intent);
        });
    }
}