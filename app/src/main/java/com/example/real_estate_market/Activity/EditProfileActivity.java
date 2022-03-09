package com.example.real_estate_market.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.real_estate_market.Class.Class.Profile;
import com.example.real_estate_market.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    FragmentManager mFragmentManager = getSupportFragmentManager();
    EditText edtName, edtSex, edtEmail, edtAddress, edtPhoneNumber;
    Button btEditProfile;
    FirebaseUser user;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        addControls();
        addEvents();
    }

    private void addEvents() {
        LoadProfile();
    }

    private void LoadProfile() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }
        String email= user.getEmail();
        edtEmail.setText(email);
        String UID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(UID);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profile = snapshot.getValue(Profile.class);
                if(profile == null){
                    return;
                }
                else {
                    setUpProfile(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
            private void setUpProfile(Profile profile) {
                edtName.setText(profile.getName());
                edtSex.setText(profile.getSex());
                edtEmail.setText(profile.getEmail());
                edtAddress.setText(profile.getAddress());
                edtPhoneNumber.setText(profile.getNPhone());
            }
        });
    }

    private void addControls() {
        edtName = findViewById(R.id.editTextPersonName);
        edtSex = findViewById(R.id.editTextSex);
        edtEmail = findViewById(R.id.editTextEmail);
        edtAddress = findViewById(R.id.editTextAddress);
        edtPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        btEditProfile = findViewById(R.id.buttonEditProfile);
    }


    public void Confirm(View view) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }
        final String UID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(UID);

        user.updateEmail(edtEmail.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this, " Update dữ liệu thành công", Toast.LENGTH_SHORT).show();
                            Profile profile = new Profile(edtAddress.getText().toString().trim(), edtEmail.getText().toString().trim(), UID, edtName.getText().toString().trim(), edtPhoneNumber.getText().toString().trim(), edtSex.getText().toString().trim());
                            mDatabase.setValue(profile);
                        }else {
                            Toast.makeText(EditProfileActivity.this, "Đăng nhập lại khi muốn thay đổi email", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        onBackPressed();
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