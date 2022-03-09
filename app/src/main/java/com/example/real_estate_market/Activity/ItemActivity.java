package com.example.real_estate_market.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.real_estate_market.Class.Class.ClassPost;
import com.example.real_estate_market.Class.Class.DataPost;
import com.example.real_estate_market.Class.Class.Profile;
import com.example.real_estate_market.Class.Class.UserData;
import com.example.real_estate_market.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {
    SliderLayout sliderShow;
    ArrayList<DataPost> arrayDataPost;
    ArrayList<ClassPost> arrayClassPost;
    ArrayList<String> itemName;
    //    int SELECT_PICTURE = 200;
    private DatabaseReference mDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    ArrayList<Uri> uriItem;
    TextView NameProject, Price, Type, Address, PhoneNumber, NameTT, EmailTT, Detail;
    ImageView Avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        addControls();
        addEvents();
    }



    private void addControls() {
        sliderShow  = findViewById(R.id.Slider);
        Avatar = findViewById(R.id.imageView);
        NameProject = findViewById(R.id.textViewNameProject_item);
        Price = findViewById(R.id.textViewPrice_item);
        Type = findViewById(R.id.textViewType_item);
        Address = findViewById(R.id.textViewAddress_item);
        PhoneNumber = findViewById(R.id.textViewPhoneNumber_item);
        Detail = findViewById(R.id.textViewDetail_item);
        NameTT = findViewById(R.id.textViewNameTT);
        EmailTT = findViewById(R.id.textView_EmailTT);
    }
    private void addEvents() {
        ReloadData();
    }

    private void sliderBar(String id) {
        TextSliderView textSliderView = new TextSliderView(this);
        textSliderView.image(id);
        sliderShow.addSlider(textSliderView);
    }
    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }
    private void ReloadData() {
//
        Intent intent = getIntent();
        final String idPost = intent.getStringExtra("idPost");
//        arrayClassPost = new ArrayList<>();
//        uriItem = new ArrayList<>();
        final DatabaseReference nDatabase = FirebaseDatabase.getInstance().getReference();

        assert idPost != null;
        nDatabase.child("Posts").child(idPost).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClassPost classPost = snapshot.getValue(ClassPost.class);
                if (classPost != null) {
                    nDatabase.child("Auth").child(classPost.getUserID()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserData userData = snapshot.getValue(UserData.class);
                            if (userData != null) {
                                Glide.with(ItemActivity.this).load(userData.getUriAvater()).error(R.drawable.avatar_default).into(Avatar);
                            }
                            //                        Log.i("Name:",userData.getUriAvater());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    nDatabase.child("users").child(classPost.getUserID()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Profile profile = snapshot.getValue(Profile.class);
                            if (profile == null) {
                            } else {
                                NameTT.setText(profile.getName());
                                EmailTT.setText(profile.getEmail());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        itemName = new ArrayList<>();
        final StorageReference storageRef = FirebaseStorage.getInstance("gs://real-estate-market-8fcf8.appspot.com/").getReference();
        storageRef.child(idPost).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    // All the items under listRef.
                    itemName.add(item.getName());
                }
                for(int i = 1; i <= itemName.size(); i++){
                    storageRef.child(idPost).child(idPost+"ID"+i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            sliderBar(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors

                        }
                    });
                }
//                onStop();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").child(idPost);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClassPost post = snapshot.getValue(ClassPost.class);
                if (post != null) {
                    setPost(post);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void setPost(ClassPost post) {
        NameProject.setText(post.getName());
        double price = Double.parseDouble (post.getPrice());
        DecimalFormat f = new DecimalFormat("##.00");
        if(price>=1000000000){
            price = price/1000000000;
            Price.setText(f.format(price) + " tỷ VNĐ");
        }else if(price>=1000000){
            price = price/1000000;
            Price.setText(f.format(price) + " triệu VNĐ");
        }else {
            Price.setText(post.getPrice() + "VNĐ");
        }
        Type.setText("Loại: " + post.getType());
        Address.setText("Địa chỉ: " + post.getAddress());
        PhoneNumber.setText("Liên hệ: " +post.getPhoneNumber());
        Detail.setText("Chi tiết:\n" + post.getDetail());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}