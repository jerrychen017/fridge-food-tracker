package com.oosegroup.fridgefoodtracker.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.oosegroup.fridgefoodtracker.R;
import com.oosegroup.fridgefoodtracker.models.FridgeAccountAuthenticator;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    // TODO:
    /*
     * 1. link LoginActivity to SplashActivity when user is successfully logged in
     * 2. link RegisterActivity to LoginActivity when user is successfully registered
     * 3. add a buttons to switch between login/register
     * 4. RegisterActivity: if user already exists, prompt dialog
     * 5. A Signout button on MainActivity
     * 6.
     * */

    RequestQueue queue;
    String usernameStr;
    String passwordStr;
    SharedPreferences pref;

    private View.OnClickListener registerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            register(v);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.queue = Volley.newRequestQueue(this);
        this.pref = getSharedPreferences("fridge-food-tracker", MODE_PRIVATE);
        FridgeAccountAuthenticator.init(queue, pref);

        CardView register = (CardView) findViewById(R.id.login);
        register.setOnClickListener(registerOnClickListener);

    }

    private void register(View view) {
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        EditText confirmPassword = (EditText) findViewById(R.id.confirm);

        if (password.getText().toString().equals(confirmPassword.getText().toString())) {
            usernameStr = username.getText().toString();
            passwordStr = password.getText().toString();
        } else {
            // passwords do not match
            new AlertDialog.Builder(this)
                    .setTitle("Error!")
                    .setMessage("Passwords do not match")
                    .setNegativeButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if (usernameStr != null && passwordStr != null) {
            FridgeAccountAuthenticator.createAccount(usernameStr, passwordStr);
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


}
