package com.example.real_estate_market.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.real_estate_market.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePasswordActivity extends AppCompatActivity {
    FragmentManager mFragmentManager = getSupportFragmentManager();
    EditText Pass1, Pass2;
    Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        addControls();
        addEvents();
    }

    private void addControls() {
        Pass1 = findViewById(R.id.editTextPass1);
        Pass2 = findViewById(R.id.editTextPass2);
        confirm = findViewById(R.id.buttonConfirm);
    }

    private void addEvents() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Pass1.getText().toString().trim().equals(Pass2.getText().toString().trim())){
                    UpdatePassword();
                }else {
                    Toast.makeText(UpdatePasswordActivity.this, "Mật khẩu không trùng khớp",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void UpdatePassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        user.updatePassword(Pass1.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdatePasswordActivity.this,
                                    "Thay đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }else {
                            Toast.makeText(UpdatePasswordActivity.this,
                                    "Thay đổi mật khẩu thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (getParent() instanceof MainActivity) {
            int count = mFragmentManager.getBackStackEntryCount();
            if(count == 0){
                // TODO: Call your exist alert
            }
            else{
                super.onBackPressed();
            }
        }
        else{
            super.onBackPressed();
        }
    }

    public void BackProfile(View view) {
        super.onBackPressed();
    }
}