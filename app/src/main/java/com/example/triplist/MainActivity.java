package com.example.triplist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

     TextInputLayout userName;
     TextInputLayout password;
     Button loginButton;
     TextView authoriseText;
    TextInputEditText userNameInput;
    TextInputEditText passwordInput;
     StorageOperation storage;

    /**
     * On load of Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storage = new StorageOperation(this);
        setLoginStatus(0);
        userName = findViewById(R.id.edtUser);
        userNameInput = findViewById(R.id.edtUserInput);
        passwordInput = findViewById(R.id.passwordInput);
        password = findViewById(R.id.edtPassword);
        loginButton = findViewById(R.id.edtButton);
        authoriseText  = findViewById(R.id.edtText);
        authoriseText.setEnabled(false);
        if((storage.getLogin()) != 0 ){
            Intent intent = new Intent(MainActivity.this,TripActivity.class);
            startActivity(intent);
        }
       /* else{
            userNameInput.setText("");
            passwordInput.setText("");
        }*/
        /**
         * call validation function when click login button
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authoriseText.setEnabled(false);
                validate(userName.getEditText().getText().toString(),password.getEditText().getText().toString());
            }
        });
    }

    /**
     * when click back button
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * set Login status
     * @param login
     */
    public void setLoginStatus(int login){
        storage.setLogin(login);
    }

    /**
     * Validation when click login
     * @param userName
     * @param password
     */
    public void validate(String userName,String password){
        if(userName.equals("") || password.equals("")) {
            Toast.makeText(getApplicationContext(), "Provide required data", Toast.LENGTH_LONG).show();
        }else if((userName.equalsIgnoreCase("Udhaya")&& password.equals("trip$007")) || (userName.equalsIgnoreCase("Dhiva")&& password.equals("trip$007"))){
            Intent intent = new Intent(MainActivity.this,TripActivity.class);
            startActivity(intent);
            storage.setLogin(1);
            userNameInput.setText("");
            passwordInput.setText("");
        }else{
            authoriseText.setEnabled(true);
        }
    }
}
