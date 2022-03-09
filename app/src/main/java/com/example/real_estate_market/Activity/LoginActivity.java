package com.example.real_estate_market.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.real_estate_market.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView linkToSignup;
    EditText edt_mail_login, edt_password_login;
    Button btn_Login;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        initListener();
    }

    private void initUI(){
        progressDialog = new ProgressDialog(this);
        linkToSignup = findViewById(R.id.textView_linkToSignup);
        edt_mail_login = findViewById(R.id.editText_loginEmail);
        edt_password_login = findViewById(R.id.editText_loginPassword);
        btn_Login = findViewById(R.id.button_Login);
    }
    private void initListener() {
        linkToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin();
            }
        });
    }

    private void onClickLogin() {
        String strEmail = edt_mail_login.getText().toString().trim();
        String strPassword = edt_password_login.getText().toString().trim();
        progressDialog.show();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!!! ",
                                    Toast.LENGTH_SHORT).show();
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại, hãy thử lại sau!!!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}