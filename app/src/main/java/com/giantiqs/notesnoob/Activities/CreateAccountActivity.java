package com.giantiqs.notesnoob.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.giantiqs.notesnoob.R;
import com.giantiqs.notesnoob.Utilities.Utility;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText, confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        createAccountBtn = findViewById(R.id.create_account);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);

        createAccountBtn.setOnClickListener(v -> createAccount());
        loginBtnTextView.setOnClickListener(v -> finish());
    }

    private void createAccount() {

        String email = emailEditText.getText().toString(),
                password = passwordEditText.getText().toString(),
                confirmPassword = confirmPasswordEditText.getText().toString();

        if (!validateData(email, password, confirmPassword)) return;

        addFirebaseAccount(email, password);
    }

    private void addFirebaseAccount(String email, String password) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(CreateAccountActivity.this, task -> {
                    changeInProgress(false);

                    if (task.isSuccessful()) {
                        Utility.showToast(
                                CreateAccountActivity.this,
                                "Account Created"
                        );

                        firebaseAuth.getCurrentUser()
                                .sendEmailVerification();
                        firebaseAuth.signOut();
                        finish();
                    } else {
                        Utility.showToast(
                                CreateAccountActivity.this,
                                task.getException().getLocalizedMessage()
                        );
                    }
                });
    }

    private void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateData(String email, String password, String confirmPassword) {

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid.");
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password length must be greater than 5");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return false;
        }

        return true;
    }
}