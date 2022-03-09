package com.example.real_estate_market.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.real_estate_market.Class.Class.Profile;
import com.example.real_estate_market.Class.Class.UserData;
import com.example.real_estate_market.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class MyProfileActivity extends AppCompatActivity {
    int SELECT_PICTURE = 3;
    Button btn_signOut, updatePass;
    ImageView imageView;
    TextView txtName, txtSex,txtEmail,txtAddress,txtPhoneNumber;
    FirebaseUser user;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btn_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MyProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ReloadProfile();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toUpdatePassword();
            }
        });
    }

    private void toUpdatePassword() {
        Intent intent = new Intent(this, UpdatePasswordActivity.class);
        startActivity(intent);
    }

    private void ReloadProfile() {
        progressDialog.show();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }

        String UID = user.getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Auth").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                if (userData != null) {
                    Glide.with(MyProfileActivity.this).load(userData.getUriAvater()).error(R.drawable.avatar_default).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabase.child("users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profile = snapshot.getValue(Profile.class);
                progressDialog.dismiss();
                if(profile == null){
                }
                else {
                    setProfile(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
            private void setProfile(Profile profile) {
                txtName.setText(profile.getName());
                txtSex.setText(profile.getSex());
                txtEmail.setText(profile.getEmail());
                txtAddress.setText(profile.getAddress());
                txtPhoneNumber.setText(profile.getNPhone());
            }
        });
    }

    private void addControls() {
        btn_signOut = findViewById(R.id.button_signOut);
        imageView = findViewById(R.id.imageView);
        txtName = findViewById(R.id.textView_Name_Output);
        txtSex = findViewById(R.id.textView_Sex_Output);
        txtEmail = findViewById(R.id.textView_Email_Output);
        txtAddress = findViewById(R.id.textView_Address_Output);
        txtPhoneNumber = findViewById(R.id.textView_NPhone_Output);
        progressDialog = new ProgressDialog(this);
        updatePass = findViewById(R.id.button_changePassword);
    }

    private void UploadProfile(Uri uri) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });
    }

    void imageChooser() {
        // create an instance of the
        // intent of the type image
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(intent, SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                final Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    imageView.setImageURI(selectedImageUri);
                    final String IdUser = user.getUid();
                    UploadProfile(selectedImageUri);
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference riversRef = storageRef.child("Avatar/"+selectedImageUri.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(selectedImageUri);
                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
//                                    Log.i("url", uri.toString());
                                    final UserData userData = new UserData(uri.toString());
                                    final DatabaseReference nDatabase = FirebaseDatabase.getInstance().getReference().child("Auth").child(IdUser);
                                    nDatabase.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            nDatabase.setValue(userData);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(MyProfileActivity.this, "Thay đổi thất bại vui lòng thử lại sau!!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            // ...
                        }
                    });



                }
            }
        }
    }

    public void ToEditProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

}