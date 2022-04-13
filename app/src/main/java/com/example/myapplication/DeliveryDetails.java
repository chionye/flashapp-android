package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class DeliveryDetails extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);

        imageView = findViewById(R.id.vector);
        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(DeliveryDetails.this, MainActivity.class);
            startActivity(intent);
        });
    }
}