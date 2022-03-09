package com.example.real_estate_market.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.real_estate_market.Adapter.ItemAdapter;
import com.example.real_estate_market.Class.Class.ClassPost;
import com.example.real_estate_market.Class.Class.DataPost;
import com.example.real_estate_market.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.Collections;

public class MyPostsActivity extends AppCompatActivity {
    ListView lvMyPost;
    ArrayList<DataPost> arrayDataPost;
    private int countofimages;
    ArrayList<ClassPost> arrayClassPost;
//    int SELECT_PICTURE = 200;
    ItemAdapter adapter;
    FirebaseUser user;
    private DatabaseReference mDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    ArrayList<String> uriItem;
    private ProgressDialog progressDialog;
    PullToRefreshView mPullToRefreshView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
        addControls();
        addEvents();

    }

    private void addControls() {
        lvMyPost = findViewById(R.id.listView_MyPosts);
        progressDialog = new ProgressDialog(this);
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
    }

    private void addEvents() {
//        ReloadData();
        clickListView();
        DeleteListView();
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                long REFRESH_DELAY = 500;
                ReloadData();
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    private void DeleteListView() {
        lvMyPost.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyPostsActivity.this);
                alertDialogBuilder.setMessage("Bán có muốn xóa sản phẩm này!");
                alertDialogBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataPost dataPost = arrayDataPost.get(position);
                        // xóa sp đang nhấn giữ
                        arrayDataPost.remove(position);
                        Log.i("deleteid",dataPost.getIdPost());
                        myRef.child("Posts").child(dataPost.getIdPost()).removeValue();
                        myRef.child("ImagePosts").child(dataPost.getIdPost()).removeValue();
                        //cập nhật lại listview
                        adapter.notifyDataSetChanged();
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        // Create a storage reference from our app
                        // Delete the file

                        final StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(dataPost.getIdPost());
                        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                            @Override
                            public void onSuccess(ListResult listResult) {

                                for (StorageReference item : listResult.getItems()) {
                                    storageRef.child(item.getName()).delete();
                                    Toast.makeText(MyPostsActivity.this,"Xóa tin thành công!!!",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                });
                alertDialogBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //không làm gì
                    }
                });
                alertDialogBuilder.show();
                return true;
//                return false;
            }

        });
    }

    private void clickListView() {
        lvMyPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataPost post = arrayDataPost.get(position);
                toItem(post.getIdPost());
            }
        });
    }

    private void toItem(String idPost) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("idPost", idPost);
        startActivity(intent);
    }

    private void ReloadData() {

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }
        String UID = user.getUid();
        arrayClassPost = new ArrayList<>();
        uriItem = new ArrayList<String>();
        Query allCatePost = FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("userID").equalTo(UID);
        allCatePost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (final DataSnapshot item : dataSnapshot.getChildren())
                {
                    progressDialog.show();
                    ClassPost post = item.getValue(ClassPost.class);
                    arrayClassPost.add(post);
                    myRef.child("ImagePosts").child(post.getKey()).child(post.getKey()+"ID1").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (final DataSnapshot item : snapshot.getChildren()){
//                                UriIMG uriIMG = item.getValue(UriIMG.class);
                                uriItem.add(item.getValue().toString());
//                                Log.i("uri",uriIMG.getUriImg());
                                if(uriItem.size() == arrayClassPost.size()){
                                    arrayDataPost = new ArrayList<>();
                                    for (int i = 0; i < arrayClassPost.size();i++){
                                        arrayDataPost.add(new DataPost(arrayClassPost.get(i).getName(),
                                                arrayClassPost.get(i).getPhoneNumber(),arrayClassPost.get(i).getPrice(), arrayClassPost.get(i).getKey(),uriItem.get(i), arrayClassPost.get(i).getAddress()));
                                    }
                                    //              Log.i("array:", arrayDataPost.get(0).nameProject);
                                    Collections.reverse(arrayDataPost);
                                    adapter = new ItemAdapter(MyPostsActivity.this,R.layout.item_listview,arrayDataPost);
                                    progressDialog.dismiss();
                                    lvMyPost.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        ReloadData();
    }
    public void MoveToAddPost(View view) {
        Intent intent = new Intent(this, AddPostsActivity.class);
        startActivity(intent);
//        finish();
    }
    @Override
    public void onResume()
    {  // After a pause OR at startup
        ReloadData();
        super.onResume();
    }
}