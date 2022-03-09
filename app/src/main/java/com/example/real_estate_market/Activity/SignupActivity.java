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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    TextView linkToLogin;
    EditText edtEmail,edtPassword,edtConfirmPassword;
    Button btnSignup;
    private ProgressDialog progressDialog;
//    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
// ...
        initUI();
        initListener();
    }

    private void initUI() {
        linkToLogin = findViewById(R.id.textView_linkToLogin);
        edtConfirmPassword = findViewById(R.id.editText_confirmSignupPassword);
        edtEmail = findViewById(R.id.editText_emailSignup);
        edtPassword = findViewById(R.id.editText_signupPassword);
        btnSignup = findViewById(R.id.button_Signup);
        progressDialog = new ProgressDialog(this);
    }

    private void initListener() {
        linkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignUp();
            }
        });
    }

    private void onClickSignUp() {
        final String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();
        String strConfirmPassword = edtConfirmPassword.getText().toString().trim();

        if((strPassword.equals("")) || (strConfirmPassword.equals(""))){
            Toast.makeText(SignupActivity.this, "Mời bạn nhập đầy đủ thông tin",
                    Toast.LENGTH_SHORT).show();
        }
        else if(!isEmailValid(strEmail)){
            Toast.makeText(SignupActivity.this, "Email chưa đúng mời bạn nhập email",
                    Toast.LENGTH_SHORT).show();
        }else if(!strPassword.equals(strConfirmPassword)){
            Toast.makeText(SignupActivity.this, "Xác nhận mật khẩu chưa chính xác",
                    Toast.LENGTH_SHORT).show();
        }else if(strPassword.length() < 6){
            Toast.makeText(SignupActivity.this, "Yêu càu mật khẩu nhiều hơn 5 kí tự",
                    Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(SignupActivity.this, "Đăng kí thành công!!! ",
                                        Toast.LENGTH_SHORT).show();
//                                UpLoadEmailToRealtime();
                                finishAffinity();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignupActivity.this, "Email đã được đăng kí hãy thử lại!!!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}