package com.example.finalproject0;

import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.List;

public class searchcheckid extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private EditText inputCheckId;
    private Button searchButton;
    private ListView checkListView;
    private List<String> checkList;
    private List<Integer> checkIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcheckid);

        databaseHelper = new DatabaseHelper(this);
        inputCheckId = findViewById(R.id.inputCheckId);
        searchButton = findViewById(R.id.searchButton);
        checkListView = findViewById(R.id.listViewChecks);

        checkList = new ArrayList<>();
        checkIds = new ArrayList<>();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCheck();
            }
        });

        checkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedCheckId = checkIds.get(position);
                Intent intent = new Intent(searchcheckid.this, Checksummary.class);
                intent.putExtra("CHECK_ID", selectedCheckId);
                startActivity(intent);
            }
        });
    }

    private void searchCheck() {
        String checkIdStr = inputCheckId.getText().toString().trim();
        if (checkIdStr.isEmpty()) {
            Toast.makeText(this, "Please enter a check ID", Toast.LENGTH_SHORT).show();
            return;
        }

        checkList.clear();
        checkIds.clear();

        int checkId = Integer.parseInt(checkIdStr);
        Cursor cursor = databaseHelper.getCheckById(checkId);
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
            Toast.makeText(this, "No check found with this ID.", Toast.LENGTH_SHORT).show();
        }
    }
}
