package com.example.finalproject0;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class NewCheck extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ListView listView;
    private Button finishOrderButton;
    private Map<Integer, Integer> itemQuantities = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcheck);

        databaseHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listViewChecks);
        finishOrderButton = findViewById(R.id.buttonFinishOrder);

        loadMenuItems();

        finishOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCheck();
            }
        });
    }

    private void loadMenuItems() {
        Cursor cursor = databaseHelper.getAllItems();
        listView.setAdapter(new ItemAdapter(this, cursor));
    }

    private void createNewCheck() {
        long checkId = databaseHelper.createNewCheck();
        if (checkId == -1) {
            Toast.makeText(this, "Error creating order. Check DB Logs!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Map.Entry<Integer, Integer> entry : itemQuantities.entrySet()) {
            if (entry.getValue() > 0) {
                long result = databaseHelper.addItemToCheck((int) checkId, entry.getKey(), entry.getValue());
                if (result == -1) {
                    Toast.makeText(this, "Error adding item to check!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        Toast.makeText(this, "Order Completed!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(NewCheck.this, Checksummary.class);
        intent.putExtra("CHECK_ID", (int) checkId);
        startActivity(intent);
        finish();

    }


    private class ItemAdapter extends BaseAdapter {
        private Context context;
        private Cursor cursor;

        public ItemAdapter(Context context, Cursor cursor) {
            this.context = context;
            this.cursor = cursor;
        }

        @Override
        public int getCount() {
            return cursor.getCount();
        }

        @Override
        public Object getItem(int position) {
            cursor.moveToPosition(position);
            return cursor;
        }

        @Override
        public long getItemId(int position) {
            cursor.moveToPosition(position);
            return cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_row_check, parent, false);
            }

            cursor.moveToPosition(position);
            int itemId = cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            double itemPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));

            TextView itemNameText = convertView.findViewById(R.id.textItemName);
            TextView itemPriceText = convertView.findViewById(R.id.textItemPrice);
            TextView quantityText = convertView.findViewById(R.id.textQuantity);
            Button increaseButton = convertView.findViewById(R.id.buttonIncrease);
            Button decreaseButton = convertView.findViewById(R.id.buttonDecrease);

            itemNameText.setText(itemName);
            itemPriceText.setText("$" + itemPrice);
            quantityText.setText(String.valueOf(itemQuantities.getOrDefault(itemId, 0)));

            increaseButton.setOnClickListener(v -> {
                int currentQuantity = itemQuantities.getOrDefault(itemId, 0);
                itemQuantities.put(itemId, currentQuantity + 1);
                quantityText.setText(String.valueOf(currentQuantity + 1));
            });

            decreaseButton.setOnClickListener(v -> {
                int currentQuantity = itemQuantities.getOrDefault(itemId, 0);
                if (currentQuantity > 0) {
                    itemQuantities.put(itemId, currentQuantity - 1);
                    quantityText.setText(String.valueOf(currentQuantity - 1));
                }
            });

            return convertView;
        }
    }
}
