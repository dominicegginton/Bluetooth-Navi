package com.example.navi_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class registration extends AppCompatActivity {

    EditText edit_email;
    EditText edit_password;
    EditText edit_confirm_password;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // INIT UI
        edit_email = (EditText) findViewById(R.id.edit_username);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_confirm_password = (EditText) findViewById(R.id.edit_confirm_password);
        btn_register = (Button) findViewById(R.id.btn_register);
    }

    /**
     * btn_register_clicked
     * when register button is clicked the user will be registered
     * @param view view
     */
    public void btn_register_clicked(View view) {

        // Get new users email from UI
        String newUserEmail = edit_email.getText().toString();
        // Get new users password from UI
        String newUserPassword = edit_password.getText().toString();
        // Get new users confirmation password from UI
        String newUserConfirmPassword = edit_confirm_password.getText().toString();

        // Check if all the inputted data is not empty
        if (!newUserEmail.equals("") || !newUserPassword.equals("") || !newUserConfirmPassword.equals("")) {

            // Check if the new users password and the confirmation password match
            if (newUserPassword.equals(newUserConfirmPassword)){

                // Create new database helper object
                DatabaseHelp databaseHelper = new DatabaseHelp(this);

                // Check if the database registation worked by calling .registerUser()
                if (databaseHelper.registerUser(newUserEmail, newUserPassword, "Student")) {

                    // Create new alert
                    AlertDialog alertDialog = new AlertDialog.Builder(registration.this).create();
                    alertDialog.setTitle("Registration");
                    alertDialog.setMessage("Your registration was successful");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Login", new DialogInterface.OnClickListener() {

                        // Exit button on click event
                        public void onClick(DialogInterface dialog, int which) {

                            // Exit this dialog
                            dialog.dismiss();
                            // Create new intent to open new page
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            // Open page
                            startActivity(intent);
                        }

                    });

                    // Show Alert Dialog
                    alertDialog.show();

                }
            }else {

                // Create new alert
                AlertDialog alertDialog = new AlertDialog.Builder(registration.this).create();
                alertDialog.setTitle("Password Error");
                alertDialog.setMessage("Sorry your passwords do not match");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Try Again", new DialogInterface.OnClickListener() {

                    // Exit button on click event
                    public void onClick(DialogInterface dialog, int which) {

                        // Exit this dialog
                        dialog.dismiss();

                        // Clear the password UI objects for retry
                        edit_password.getText().clear();
                        edit_confirm_password.getText().clear();

                    }

                });

                // Show Alert Dialog
                alertDialog.show();

            }

        }else {

            // Create new alert
            AlertDialog alertDialog = new AlertDialog.Builder(registration.this).create();
            alertDialog.setTitle("Registration Error");
            alertDialog.setMessage("Sorry there was an error while trying to register your details to the databse");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Try Again", new DialogInterface.OnClickListener() {

                // Exit button on click event
                public void onClick(DialogInterface dialog, int which) {

                    // Exit this dialog
                    dialog.dismiss();

                    // Clear UI
                    edit_email.getText().clear();
                    edit_password.getText().clear();
                    edit_confirm_password.getText().clear();

                }

            });

            // Show Alert Dialog
            alertDialog.show();

        }
    }
}
