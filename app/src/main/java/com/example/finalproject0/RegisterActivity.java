package com.example.finalproject0;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    Button register_button ;
    TextView username_input , password_input ,passwordcheck_input ;
    private DatabaseHelper databaseHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        register_button = findViewById(R.id.register_register_button);
        username_input = findViewById(R.id.register_name_input);
        password_input = findViewById(R.id.register_password_input);
        passwordcheck_input = findViewById(R.id.register_passwordcheck_input);
        databaseHelper = new DatabaseHelper(this);



        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernamedata = username_input.getText().toString().trim();
                String passworddata = password_input.getText().toString().trim();
                String passworddatacheck = passwordcheck_input.getText().toString().trim();


                if (!passworddatacheck.equals(passworddata)){
                    passwordcheck_input.setError("The two passwords you enterd dont match ");

                }
                if (databaseHelper.isUsernameTaken(usernamedata)){
                    username_input.setError("This username is already in use");

                }

                if (!databaseHelper.isUsernameTaken(usernamedata) && passworddatacheck.equals(passworddata)) {
                    databaseHelper.addUser(usernamedata , passworddata);
                    finish();
                }

            }
        });
    }
}