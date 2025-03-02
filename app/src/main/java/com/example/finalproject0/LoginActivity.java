package com.example.finalproject0;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    Button login_button;
    TextView username_input , password_input ;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login_button = findViewById(R.id.login_login_button);
        username_input = findViewById(R.id.login_name_input);
        password_input = findViewById(R.id.login_password_input);
        databaseHelper = new DatabaseHelper(this);



        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernamedata = username_input.getText().toString().trim();
                String passworddata = password_input.getText().toString().trim();

                if (databaseHelper.validateUser(usernamedata, passworddata)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //intent.putExtra("username", usernamedata);
                    startActivity(intent);
                    //finish();
                }
                else {
                    username_input.setError("Wrong username or password");
                }



            }
        });
    }
}