package com.example.finalproject0;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class searchcheckdate extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private EditText inputDate;
    private Button searchButton;
    private ListView checkListView;
    private List<String> checkList;
    private List<Integer> checkIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcheckdate);

        databaseHelper = new DatabaseHelper(this);
        inputDate = findViewById(R.id.inputDate);
        searchButton = findViewById(R.id.searchButton);
        checkListView = findViewById(R.id.listViewChecks);

        checkList = new ArrayList<>();
        checkIds = new ArrayList<>();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCheckByDate();
            }
        });

        checkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedCheckId = checkIds.get(position);
                Intent intent = new Intent(searchcheckdate.this, Checksummary.class);
                intent.putExtra("CHECK_ID", selectedCheckId);
                startActivity(intent);
            }
        });
    }

    private void searchCheckByDate() {
        String dateInput = inputDate.getText().toString().trim();
        if (dateInput.isEmpty()) {
            Toast.makeText(this, "Please enter a date (YYYY/MM/DD)", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Convert YYYY/MM/DD to Milliseconds since Epoch
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            long startOfDay = sdf.parse(dateInput).getTime();

            // Get end of the day by adding one day minus 1 millisecond
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startOfDay);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            long endOfDay = calendar.getTimeInMillis() - 1;

            // Query database using timestamp range
            Cursor cursor = databaseHelper.getChecksByDateRange(startOfDay, endOfDay);

            checkList.clear();
            checkIds.clear();

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String timestamp = cursor.getString(1);
                    checkList.add("Check ID: " + id + "\nTimestamp: " + timestamp);
                    checkIds.add(id);
                } while (cursor.moveToNext());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, checkList);
                checkListView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "No checks found for this date.", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid date format. Use YYYY/MM/DD.", Toast.LENGTH_SHORT).show();
        }
    }
}
