package com.example.real_estate_market.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.real_estate_market.Adapter.GalleryAdapter;
import com.example.real_estate_market.Class.Class.ClassPost;
import com.example.real_estate_market.Class.Class.UriIMG;
import com.example.real_estate_market.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AddPostsActivity extends AppCompatActivity {
    TextView btnSelectIMG;
    GridView gvGallery;
    FirebaseUser user;
    int PICK_IMAGE_MULTIPLE = 3;
    String imageEncoded;
    List<String> imagesEncodedList;
    EditText edtProjectName, edtAddress, edtPrice, edtPhoneNumber,editTextMutilineDetail;
    LinearLayout LLPost;
    ArrayList<Uri> mArrayUri;
    int number_of_clicks = 0;
    int i = 0;
    AutoCompleteTextView autoCompleteTextViewType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_posts);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        addControls();
        addEvents();
    }
    private void addControls() {
        btnSelectIMG = findViewById(R.id.buttonSelectIMG);
        gvGallery = findViewById(R.id.GridViewIMG);
        edtProjectName = findViewById(R.id.editTextName);
        edtAddress = findViewById(R.id.editTextAddress);
        edtPrice = findViewById(R.id.editTextPrice);
        edtPhoneNumber = findViewById(R.id.editTextProjectPhoneNumber);
        LLPost = findViewById(R.id.linearLayoutPost);
        editTextMutilineDetail = findViewById(R.id.editTextTextMultiLineDetail);
        autoCompleteTextViewType = findViewById(R.id.autoCompleteTextViewStype);

    }
    @SuppressLint("ClickableViewAccessibility")
    private void addEvents() {
        AutoCompleteTextViewName();
        btnSelectIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);

                // create an instance of the
                // intent of the type image
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                // pass the constant to compare it
                // with the returned requestCode
                startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
            }
        });

        LLPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoCompleteTextViewType.getText().length()==0
                        || edtProjectName.getText().length()==0
                        || edtAddress.getText().length()==0
                        || edtPrice.getText().length()==0
                        || edtPhoneNumber.getText().length()==0
                        || editTextMutilineDetail.getText().length()==0) {
                    Toast.makeText(AddPostsActivity.this, "Yêu cầu nhập đầy đủ thông tin dự án" ,Toast.LENGTH_LONG).show();
                }else if(edtPhoneNumber.getText().length()!=10){
                    Toast.makeText(AddPostsActivity.this, "Yêu cầu nhập đúng SDT",Toast.LENGTH_LONG).show();
                }else if (gvGallery.getAdapter()==null){
                    Toast.makeText(AddPostsActivity.this, "Yêu cầu thêm ảnh cho dự án",Toast.LENGTH_LONG).show();
                }
                else {
                    openprogresdialog();
                }
            }
        });
        //Hiện con trỏ
//        autoCompleteTextViewType.requestFocus();
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(autoCompleteTextViewType.getWindowToken(), 0);
    }

    private void addPosts() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }
        String UID = user.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        final String key = mDatabase.push().getKey();

        ClassPost classPost = new ClassPost(autoCompleteTextViewType.getText().toString(),edtProjectName.getText().toString(),
                edtAddress.getText().toString(), edtPrice.getText().toString(), edtPhoneNumber.getText().toString(),UID,editTextMutilineDetail.getText().toString(), key);

        if (key != null) {
            mDatabase.child(key).setValue(classPost);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child(key);
            StorageReference storageRefDownload = FirebaseStorage.getInstance("gs://real-estate-market-8fcf8.appspot.com/").getReference();

            for (i = 0; i < mArrayUri.size(); i++) {
                Uri file = mArrayUri.get(i);
                StorageReference riversRef = storageRef.child(key + "ID" + (i+1) );
                riversRef.putFile(file);
            }
            for (i = 0; i < mArrayUri.size(); i++) {
                Log.i("i", String.valueOf(i));
                Log.i("uri", String.valueOf(mArrayUri.size()));
                final DatabaseReference nDatabase = FirebaseDatabase.getInstance().getReference("ImagePosts").child(key).child(key + "ID" + (i + 1));
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                storageRefDownload.child(key+"/"+key + "ID" + (i+1)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        UriIMG uriIMG = new UriIMG(uri.toString());
                        nDatabase.setValue(uriIMG);
                    }
                });
            }
            onBackPressed();
        }
    }

    private void AutoCompleteTextViewName() {
        String[] stype = getResources().getStringArray(R.array.stype);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,stype);
        autoCompleteTextViewType.setAdapter(arrayAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<>();
                GalleryAdapter galleryAdapter;
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                    mArrayUri = new ArrayList<>();
                    mArrayUri.add(mImageUri);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        mArrayUri = new ArrayList<>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                            galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                            gvGallery.setAdapter(galleryAdapter);
                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void openprogresdialog() {
        // TODO Auto-generated method stub
        final ProgressDialog progDailog = ProgressDialog.show(
                AddPostsActivity.this, "Loading....", "Please wait...", true);

        new Thread() {
            public void run() {
                try {
                    // xml parser code here put...
                    ++number_of_clicks;
                    if(number_of_clicks == 1){
                        addPosts();
                    } else {
                        ++number_of_clicks;
                    }
                } catch (Exception ignored) {
                }
                progDailog.dismiss();
            }
        }.start();
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}