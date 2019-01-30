package com.example.samy.firebaseuploadimage;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    private static final int GET_GALLERY_CODE = 100;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText title,desc;
    ProgressBar progressBar;
    ImageButton imageButton;
    private StorageReference storageReference;
    Uri mImageUri=null;
    private Uri uploadSessionUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title=findViewById(R.id.title_id);
        desc=findViewById(R.id.desc_id);
        progressBar=new ProgressBar(this);
        imageButton=findViewById(R.id.image_button);
        storageReference=FirebaseStorage.getInstance().getReference();


    }

    public void sendTitle(View view) {

        startUpload();

    }
    private void startUpload() {
        final String titleText = title.getText().toString().trim();
        final String descText=desc.getText().toString().trim();
        if(!TextUtils.isEmpty(titleText)&&!TextUtils.isEmpty(descText)){
           progressBar=new ProgressBar(this);
           progressBar.setVisibility(View.VISIBLE);
            final StorageReference pathFile = storageReference.child("images").child(mImageUri.getLastPathSegment());


            pathFile.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return pathFile.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Post post=new Post(titleText,descText,task.getResult().toString());
                    Log.d("Myimage1",task.getResult().toString());
                    Log.d("Myimage2",task.getResult().toString());
                    sendData(post);
                    title.setText("");
                   desc.setText("");
                  imageButton.setImageURI(null);
                }
            });
            // another solution
//            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            Log.d(TAG, "onSuccess: uri= "+ uri.toString());
//                        }
//                    });
//                }
//            });

        }

    }
    public void sendData(Post post){
       progressBar.setVisibility(View.VISIBLE);
     db.collection("post").add(post)
             .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                 @Override
                 public void onSuccess(DocumentReference documentReference) {
                     Toast.makeText(getApplicationContext(),"new data is added : "+documentReference.getPath(),Toast.LENGTH_LONG).show();
                     progressBar.setVisibility(View.GONE);
                 }
             }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
             Toast.makeText(getApplicationContext(),"new data is added : "+e.getMessage(),Toast.LENGTH_LONG).show();
              progressBar.setVisibility(View.GONE);
         }
     });


    }

    public void addImage(View view) {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent,GET_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_GALLERY_CODE&&resultCode==RESULT_OK){
              mImageUri = data.getData();
             imageButton.setImageURI(mImageUri);
        }
    }

    public void showPosts(View view) {
      Intent intent=new Intent(this,RescyclerActivity.class);
      startActivity(intent);
    }
}
