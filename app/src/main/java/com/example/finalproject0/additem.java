package com.example.finalproject0;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class additem extends AppCompatActivity {

    Button add_item_button ;
    EditText name_input , price_input ;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_additem);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        add_item_button = findViewById(R.id.item_add_button);
        name_input = findViewById(R.id.item_name_input);
        price_input = findViewById(R.id.item_price_input);
        databaseHelper = new DatabaseHelper(this);

        add_item_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namedata = name_input.getText().toString().trim();
                String pricedata = price_input.getText().toString().trim();

                if (!databaseHelper.isItemNameTaken(namedata)){
                    databaseHelper.addItem(namedata , Double.parseDouble(pricedata));
                    finish();
                }
                else {
                    name_input.setError("The Item name is already being used ");

                }
            }
        });

    }



}