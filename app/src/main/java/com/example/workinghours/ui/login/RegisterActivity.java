package com.example.workinghours.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workinghours.BaseApp;
import com.example.workinghours.R;
import com.example.workinghours.database.entity.UserEntity;
import com.example.workinghours.database.repository.UserRepository;
import com.example.workinghours.ui.BaseActivity;
import com.example.workinghours.ui.MainActivity;
import com.example.workinghours.util.OnAsyncEventListener;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private UserRepository repository;

    private Toast toast;

    private EditText etEmail;
    private EditText etPwd1;
    private EditText etPwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        repository = ((BaseApp)getApplication()).getUserRepository();

        initializeForm();

        toast = Toast.makeText(this, getString(R.string.user_created), Toast.LENGTH_LONG);
    }

    private void initializeForm() {

        etEmail = findViewById(R.id.email);
        etPwd1 = findViewById(R.id.password);
        etPwd2 = findViewById(R.id.passwordRep);
        Button saveBtn = findViewById(R.id.editButton);
        saveBtn.setOnClickListener(view -> saveChanges(
                etEmail.getText().toString(),
                etPwd1.getText().toString(),
                etPwd2.getText().toString()
        ));
    }

    private void saveChanges(String email, String pwd, String pwd2) {
        if (!pwd.equals(pwd2) || pwd.length() < 5) {
            etPwd1.setError(getString(R.string.error_invalid_password));
            etPwd1.requestFocus();
            etPwd1.setText("");
            etPwd2.setText("");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.error_invalid_email));
            etEmail.requestFocus();
            return;
        }
        UserEntity newUser = new UserEntity(email, pwd);

        repository.register(newUser, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "createUserWithEmail: success");
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "createUserWithEmail: failure", e);
                setResponse(false);
            }
        });
    }

    private void setResponse(Boolean response) {
        if (response) {
            toast.show();
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            etEmail.setError(getString(R.string.error_used_email));
            etEmail.requestFocus();
        }
    }
}
