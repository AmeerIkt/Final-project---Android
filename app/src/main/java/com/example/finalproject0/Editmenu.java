package com.example.finalproject0;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Editmenu extends AppCompatActivity {

    RecyclerView recyclerView ;
    FloatingActionButton add_button ;
    DatabaseHelper db ;

    ArrayList<String> item_id , item_name , item_price ;
    CustomAdapter customAdapter ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editmenu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(Editmenu.this);
        item_id = new ArrayList<>();
        item_name = new ArrayList<>();
        item_price = new ArrayList<>();
        recyclerView = findViewById(R.id.editmenu_recyclerView);
        add_button = findViewById(R.id.editmenu_addbutton);
        fetchdata();

        customAdapter = new CustomAdapter(Editmenu.this ,  this , item_id , item_name , item_price) ;
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Editmenu.this));

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Editmenu.this, additem.class);
                Editmenu.this.startActivityForResult(intent ,1);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode , int resultCode , @Nullable Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void fetchdata (){
        Cursor cursor = db.getAllItems();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "Nothing to read", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()){
                item_id.add(cursor.getString(0));
                item_name.add(cursor.getString(1));
                item_price.add(cursor.getString(2));

            }
        }
    }
}