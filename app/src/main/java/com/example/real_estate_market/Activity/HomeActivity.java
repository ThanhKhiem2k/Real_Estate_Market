package com.example.real_estate_market.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.real_estate_market.Adapter.ItemAdapter;
import com.example.real_estate_market.Class.Class.ClassPost;
import com.example.real_estate_market.Class.Class.DataPost;
import com.example.real_estate_market.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.Collections;

public class HomeActivity extends AppCompatActivity {
    ListView lvHomePost;
    ArrayList<DataPost> arrayDataPost;
    ArrayList<ClassPost> arrayClassPost;
    //    int SELECT_PICTURE = 200;
    ItemAdapter adapter;
    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    ArrayList<String> uriItem;
    private ProgressDialog progressDialog;
    PullToRefreshView mPullToRefreshView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addControls();
        addEvents();
//        ReloadData();
    }
    private void addControls() {
        lvHomePost = findViewById(R.id.listView_Home);
        progressDialog = new ProgressDialog(this);
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
    }
    private void addEvents() {
//        ReloadData();
        clickListView();
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
    private void toItem(String idPost) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("idPost", idPost);
        startActivity(intent);
    }
    private void clickListView() {
        lvHomePost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataPost post = arrayDataPost.get(position);
//                Log.i("i=", arrayDataPost.get(position).getNameProject());
                toItem(post.getIdPost());
//
            }
        });
    }
    private void ReloadData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }
        String UID = user.getUid();
        arrayClassPost = new ArrayList<>();
        uriItem = new ArrayList<String>();
        Query allCatePost = FirebaseDatabase.getInstance().getReference().child("Posts");
        allCatePost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@org.jetbrains.annotations.NotNull @NotNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (final DataSnapshot item : dataSnapshot.getChildren())
                {
                    progressDialog.show();
                    ClassPost post = item.getValue(ClassPost.class);
                    arrayClassPost.add(post);
                    myRef.child("ImagePosts").child(post.getKey()).child(post.getKey()+"ID1").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (final DataSnapshot item : snapshot.getChildren()) {
                                uriItem.add(item.getValue().toString());
                                if (uriItem.size() == arrayClassPost.size()) {
                                    arrayDataPost = new ArrayList<>();
                                    for (int i = 0; i < arrayClassPost.size(); i++) {
                                        arrayDataPost.add(new DataPost(arrayClassPost.get(i).getName(),
                                                arrayClassPost.get(i).getPhoneNumber(),
                                                    arrayClassPost.get(i).getPrice(), arrayClassPost.get(i).getKey(),
                                                uriItem.get(i), arrayClassPost.get(i).getAddress()));
                                    }
                                    //              Log.i("array:", arrayDataPost.get(0).nameProject);
                                    Collections.reverse(arrayDataPost);
                                    adapter = new ItemAdapter(HomeActivity.this, R.layout.item_listview, arrayDataPost);
                                    progressDialog.dismiss();
                                    lvHomePost.setAdapter(adapter);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
            }
            @Override
            public void onCancelled(@org.jetbrains.annotations.NotNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
    @Override
    public void onResume()
    {  // After a pause OR at startup

        ReloadData();
        super.onResume();
    }
}