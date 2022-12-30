package com.example.ev_sc.Profile.Register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ev_sc.Login.LoginScreen;
import com.example.ev_sc.User.UserDB;
import com.example.ev_sc.User.UserObj;
import com.example.ev_sc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterScreen extends Activity {

    EditText line_first_name_register;
    EditText line_last_name_register;
    EditText line_email_register;
    EditText line_username_register;
    EditText line_enter_password_register;
    EditText line_confirm_password_register;

    Button register_button;

    FirebaseAuth fAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.register);

        // init widgets //
        init_widgets();
        // init listeners //
        OnClickRegisterButton();
    }

    /**
     * init widgets method
     */
    private void init_widgets() {
        line_first_name_register = (EditText) (findViewById(R.id.line_first_name_register));
        line_last_name_register = (EditText) (findViewById(R.id.line_last_name_register));
        line_email_register = (EditText) (findViewById(R.id.line_email_register));
        line_username_register = (EditText) (findViewById(R.id.line_uasername_register));
        line_enter_password_register = (EditText) (findViewById(R.id.line_enter_password_register));
        line_confirm_password_register = (EditText) (findViewById(R.id.line_confirm_password_register));
        register_button = (Button) findViewById(R.id.register_button);
    }

    /**
     * Adding the listener click to move from Register screen to login screen after registration Via REGISTER BUTTON.
     */
    private void OnClickRegisterButton() {
        register_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = line_email_register.getText().toString().trim();
                String password = line_enter_password_register.getText().toString().trim();
                String confirm_password = line_confirm_password_register.getText().toString().trim();

                // data to be moved into the database user layer //
                String first_name = line_first_name_register.getText().toString().trim();
                String last_name = line_last_name_register.getText().toString().trim();
                String username = line_username_register.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    line_email_register.setError("Email Is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    line_enter_password_register.setError("Password Is Required");
                    return;
                }
                if (TextUtils.isEmpty(confirm_password)) {
                    line_confirm_password_register.setError("Confirm Password Is Required");
                    return;
                }
                if (!(password.equals(confirm_password))) {
                    line_confirm_password_register.setError("Passwords are not identical");
                    return;
                }
                /*TODO: add more exceptions*/


                // register user in firebase //
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterScreen.this, "User Created.", Toast.LENGTH_SHORT).show();

                            //extract data and add it to database//
                            UserObj newUser = new UserObj(first_name, last_name, username, "0", fAuth.getCurrentUser().getUid(), 0);
                            UserDB newUserDB = new UserDB();
                            newUserDB.AddUserToDatabase(newUser);

                            startActivity(new Intent(getApplicationContext(), LoginScreen.class));

                        } else {
                            Toast.makeText(RegisterScreen.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Intent register_to_login = new Intent(view.getContext(), LoginScreen.class);
                startActivityForResult(register_to_login, 0);
                finish();
            }
        });
    }
}
