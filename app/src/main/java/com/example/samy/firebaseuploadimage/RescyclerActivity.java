package com.example.samy.firebaseuploadimage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RescyclerActivity extends AppCompatActivity {
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference notebookRef=db.collection("post");
    private RecyclerView recyclerView;
    FirePostRecyclerAdapter firePostRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescycler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView=findViewById(R.id.recycler_view_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(notebookRef, Post.class)
                .build();
        firePostRecyclerAdapter=new FirePostRecyclerAdapter(options,getApplicationContext());
        recyclerView.setAdapter(firePostRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firePostRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firePostRecyclerAdapter.stopListening();
    }
}
