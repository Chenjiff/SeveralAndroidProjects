package com.code.chenjifff.storage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{
    EditText passwordEditView;
    EditText confirmEditView;
    public static int MODE = MODE_PRIVATE;
    public static final String PREFERENCE_NAME = "PasswordStorage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Button okButton = (Button) this.findViewById(R.id.OK_but);
        okButton.setOnClickListener(this);
        Button clearButton = (Button) this.findViewById(R.id.clear_but);
        clearButton.setOnClickListener(this);
        passwordEditView = (EditText) this.findViewById(R.id.password_text);
        confirmEditView = (EditText) this.findViewById(R.id.confirm_text);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);
        String password = new String();
        String defaultPassword = String.valueOf("");
        password = sharedPreferences.getString("password", defaultPassword);
        if(!password.equals(defaultPassword)) {
            passwordEditView.setVisibility(View.INVISIBLE);
            confirmEditView.setHint("Password");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.OK_but:
                if(passwordEditView.getVisibility() == View.VISIBLE) {
                    String password = passwordEditView.getText().toString();
                    String confirmPassword = confirmEditView.getText().toString();
                    if (password.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    } else if (!password.equals(confirmPassword)) {
                        Toast.makeText(this, "Password mismatches.", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("password", password);
                        editor.commit();
                        Intent intent = new Intent(this, Detail.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);
                    String password = sharedPreferences.getString("password", "");
                    String inputPassword = confirmEditView.getText().toString();
                    if(password.equals(inputPassword)) {
                        Intent intent = new Intent(this, Detail.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.clear_but:
                passwordEditView.setText("");
                confirmEditView.setText("");
            default:
                break;
        }
    }
}
