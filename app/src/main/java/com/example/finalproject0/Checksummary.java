package com.example.finalproject0;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import androidx.appcompat.app.AppCompatActivity;




public class Checksummary extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView checkIdText, timestampText, totalPriceText;
    private ListView itemList;
    private int checkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checksummary);

        databaseHelper = new DatabaseHelper(this);
        checkIdText = findViewById(R.id.textCheckId);
        timestampText = findViewById(R.id.textTimestamp);
        totalPriceText = findViewById(R.id.textTotalPrice);
        itemList = findViewById(R.id.listViewItems);

        checkId = getIntent().getIntExtra("CHECK_ID", -1);
        if (checkId == -1) {
            Toast.makeText(this, "Error: Check ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadCheckDetails();
    }

    private void loadCheckDetails() {
        Cursor checkCursor = databaseHelper.getCheckById(checkId);
        if (checkCursor != null && checkCursor.moveToFirst()) {
            checkIdText.setText("Check ID: " + checkCursor.getInt(0));
            String rawTimestamp = checkCursor.getString(1);
            String formattedTimestamp = formatTimestamp(rawTimestamp);
            timestampText.setText("Timestamp: " + formattedTimestamp);        }

        Cursor itemCursor = databaseHelper.getCheckItems(checkId);
        if (itemCursor != null) {
            List<String> items = new ArrayList<>();
            double totalPrice = 0;
            while (itemCursor.moveToNext()) {
                String itemName = itemCursor.getString(0);
                int quantity = itemCursor.getInt(1);
                double price = itemCursor.getDouble(2);
                totalPrice += price * quantity;
                items.add(itemName + " x" + quantity + " - $" + (price * quantity));
            }
            totalPriceText.setText("Total Price: $" + totalPrice);
            itemList.setAdapter(new android.widget.ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
        }
    }

    private String formatTimestamp(String rawTimestamp) {
        try {
            // Check if timestamp is in long format (milliseconds)
            if (rawTimestamp.matches("\\d+")) {
                long timestampMillis = Long.parseLong(rawTimestamp);
                return new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
                        .format(new Date(timestampMillis));
            }

            // Otherwise, parse as standard SQLite format
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
            Date date = inputFormat.parse(rawTimestamp);
            return outputFormat.format(date);
        } catch (Exception e) {
            return rawTimestamp; // Return raw value if parsing fails
        }
    }

}