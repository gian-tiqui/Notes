package com.giantiqs.notesnoob.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.giantiqs.notesnoob.MainActivity;
import com.giantiqs.notesnoob.R;
import com.giantiqs.notesnoob.Utilities.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView signUpBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        signUpBtnTextView = findViewById(R.id.sign_up_view_btn);

        loginBtn.setOnClickListener(v -> loginUser());
        signUpBtnTextView.setOnClickListener(v ->
                startActivity(new Intent(
                        LoginActivity.this,
                        CreateAccountActivity.class)
                ));
    }

    private void loginUser() {

        String email = emailEditText.getText().toString(),
                password = passwordEditText.getText().toString();

        if (!validateData(email, password)) return;

        loginFirebase(email, password);
    }

    private void loginFirebase(String email, String password) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        changeInProgress(true);

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    changeInProgress(false);

                    if (task.isSuccessful()) {
                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                            startActivity(new Intent(
                                    LoginActivity.this,
                                    MainActivity.class));
                        } else {
                            Utility.showToast(
                                    LoginActivity.this,
                                    "Email not verified"
                            );
                        }
                    } else {
                        Utility.showToast(LoginActivity.this, task
                                .getException()
                                .getLocalizedMessage()
                        );
                    }
        });
    }

    private void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateData(String email, String password) {

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid.");
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password length must be greater than 5");
            return false;
        }

        return true;
    }
}