package com.oosegroup.fridgefoodtracker.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.oosegroup.fridgefoodtracker.R;
import com.oosegroup.fridgefoodtracker.models.FridgeAccountAuthenticator;

public class LoginActivity extends AppCompatActivity {

    RequestQueue queue;
    String usernameStr;
    String passwordStr;
    SharedPreferences pref;

    private View.OnClickListener loginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            login(v);
        }
    };
    private View.OnClickListener registerActivityOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent registerActivityIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerActivityIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.queue = Volley.newRequestQueue(this);
        this.pref = getSharedPreferences("fridge-food-tracker", MODE_PRIVATE);
        FridgeAccountAuthenticator.init(queue, pref);

        CardView login = (CardView) findViewById(R.id.login);
        login.setOnClickListener(loginOnClickListener);
        TextView register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(registerActivityOnClickListener);
    }

    private void login(View view) {
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        usernameStr = username.getText().toString();
        passwordStr = password.getText().toString();

        if (usernameStr != null && passwordStr != null) {
            FridgeAccountAuthenticator.login(usernameStr, passwordStr);
//            FridgeAccountAuthenticator.auth();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (pref.getBoolean("loggedIn", true)) { // go to SplashActivity
                        Intent splashActivityIntent = new Intent(LoginActivity.this, SplashActivity.class);
                        startActivity(splashActivityIntent);
                    } else {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Error!")
                                .setMessage("Username and password do not match")
                                .setNegativeButton(android.R.string.ok, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            }, 500);

        } else {
            // prompt error
            new AlertDialog.Builder(this)
                    .setTitle("Error!")
                    .setMessage("Username or password is empty")
                    .setNegativeButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    // TODO:
    /*
    * 1. link LoginActivity to SplashActivity when user is successfully logged in
    * 2. link RegisterActivity to LoginActivity when user is successfully registered
    * 3. add a buttons to switch between login/register
    * 4. RegisterActivity: if user already exists, prompt dialog
    * 5. A Signout button on MainActivity
    * 6.
    * */
}
