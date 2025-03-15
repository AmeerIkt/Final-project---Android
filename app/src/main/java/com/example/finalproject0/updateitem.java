package com.example.finalproject0;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class updateitem extends AppCompatActivity {
    EditText name_txt  , price_txt ;
    TextView id_show ;
    Button update , delete ;
    String id;
    String name;
    String price;
    String updatedname;
    double updatedprice  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_updateitem);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name_txt = findViewById(R.id.item_nameupdate_input);
        price_txt = findViewById(R.id.item_priceupdate_input);
        id_show = findViewById(R.id.item_id_show);
        update = findViewById(R.id.item_update_button);
        delete = findViewById(R.id.item_delete_button);

        getIntentData();
        name_txt.setText(name);
        price_txt.setText(price);
        id_show.setText(id);
        DatabaseHelper db = new DatabaseHelper(updateitem.this);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatedname = String.valueOf(name_txt.getText());
                updatedprice = Double.parseDouble(String.valueOf(price_txt.getText()));

                db.updateItems(updatedname , updatedprice , Integer.parseInt(id));
                finish();




            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteItem(Integer.parseInt(id));
                finish();

            }
        });






    }

    void getIntentData (){
        if (getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("price")  ){
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            price = getIntent().getStringExtra("price");



        }else{
            Toast.makeText(this , "no data " , Toast.LENGTH_SHORT).show();
        }
    }
}