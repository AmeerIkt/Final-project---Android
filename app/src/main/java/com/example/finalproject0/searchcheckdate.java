package com.example.finalproject0;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class searchcheckdate extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private EditText inputDate;
    private Button searchButton;
    private TextView checkResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcheckdate);

        databaseHelper = new DatabaseHelper(this);
        inputDate = findViewById(R.id.inputDate);
        searchButton = findViewById(R.id.searchButton);
        checkResults = findViewById(R.id.checkResults);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCheckByDate();
            }
        });
    }

    private void searchCheckByDate() {
        String date = inputDate.getText().toString().trim();
        if (date.isEmpty()) {
            Toast.makeText(this, "Please enter a date (YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getChecksByDate(date);
        if (cursor != null && cursor.moveToFirst()) {
            StringBuilder results = new StringBuilder();
            do {
                results.append("Check ID: ").append(cursor.getInt(0))
                        .append("\nTimestamp: ").append(cursor.getString(1))
                        .append("\n\n");
            } while (cursor.moveToNext());
            checkResults.setText(results.toString());
        } else {
            checkResults.setText("No checks found for this date.");
        }
    }
}
