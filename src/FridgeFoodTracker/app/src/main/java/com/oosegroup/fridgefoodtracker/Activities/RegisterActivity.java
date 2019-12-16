package com.oosegroup.fridgefoodtracker.Activities;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.oosegroup.fridgefoodtracker.R;
import com.oosegroup.fridgefoodtracker.models.FridgeAccountAuthenticator;

/**
 * Activity for account registration
 */
public class RegisterActivity extends AppCompatActivity {
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
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        usernameStr = username.getText().toString();
        passwordStr = password.getText().toString();
        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            // passwords do not match
            new AlertDialog.Builder(this)
                    .setTitle("Error!")
                    .setMessage("Passwords do not match")
                    .setNegativeButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        if (usernameStr.isEmpty() || passwordStr.isEmpty()) {
            // either username or password is empty, prompt error
            new AlertDialog.Builder(this)
                    .setTitle("Error!")
                    .setMessage("Username or password is empty")
                    .setNegativeButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

            // try create account
            FridgeAccountAuthenticator.createAccount(usernameStr, passwordStr);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // check if account creation was successful
                    if (pref.getBoolean("registered", false)) {
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("Account")
                                .setMessage("Account was successfully registered!")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // got to LoginActivity
                                        Intent loginActivityIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(loginActivityIntent);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    } else {
                        // account registration failed, prompt user
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("Error!")
                                .setMessage("Registration failed!")
                                .setNegativeButton(android.R.string.ok, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            }, 500);
    }
}
