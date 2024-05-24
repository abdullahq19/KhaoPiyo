package com.example.arslicious;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    AppCompatButton btnSignUp;
    ImageView ivProfileImage;
    EditText etEmail, etPassword, etUserName;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressBar progressBar;
    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btnSignUp = findViewById(R.id.btnSignUp);
        etEmail = findViewById(R.id.etEmail);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        etPassword = findViewById(R.id.etPassword);
        etUserName = findViewById(R.id.etUserName);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

//       for Status bar colour
        getWindow().setStatusBarColor(ContextCompat.getColor(SignUpActivity.this, R.color.teal_700));

        //For SignUp
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String image = ivProfileImage.toString();
                String txt_name = etUserName.getText().toString();
                String txt_email = etEmail.getText().toString();
                String txt_Password = etPassword.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_Password)) {
                    Toast.makeText(SignUpActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                } else if (txt_Password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Password too Short", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(txt_name)) {
                    Toast.makeText(SignUpActivity.this, "Name is not Added", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_email, txt_Password);
                    currentUserImage();
                    progressBar.setVisibility(View.VISIBLE);
//                    ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
//                    progressDialog.setMessage("Processing please wait");
//                    progressDialog.show();
                }
            }
        });

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                LayoutInflater layoutInflater = getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.set_image, null);
                builder.setView(view1);
                builder.setCancelable(true);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.ass);

                AppCompatButton btnCamera = view1.findViewById(R.id.btnCamera);
                AppCompatButton btnGallery = view1.findViewById(R.id.btnGallery);

                btnCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkAndRequestPermission()) {
                            takePictureFromCamera();
                            alertDialog.dismiss();
                        }
                    }
                });

                btnGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        takePictureFromGallery();
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    //    PICTURE IN BYTES
    public void currentUserImage() {
        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child(etEmail.getText().toString());

        // [START upload_memory]
        // Get the data from an ImageView as bytes
        ivProfileImage.setDrawingCacheEnabled(true);
        ivProfileImage.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) ivProfileImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(SignUpActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //     PICTURE FROM CAMERA
    private void takePictureFromCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, 2);
        }

    }

    //    PICTURE FROM GALLERY
    private void takePictureFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    selectedImage = data.getData();
                    ivProfileImage.setImageURI(selectedImage);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    ivProfileImage.setImageBitmap(bitmapImage);
                }
        }
    }

    //    CAMERA PERMISSION
    private boolean checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ActivityCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.CAMERA}, 21);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 21 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }

    //    REGISTER USER THROUGH EMAIL,PASSWORD AND Add Name TO FIRESTORE
    private void registerUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    SEND VERIFICATION LINK
//                    FirebaseUser user = firebaseAuth.getCurrentUser();
//                    assert user != null;
//                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(SignUpActivity.this, "Verification Email has been sent", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(SignUpActivity.this, "Error! Not sent", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
//                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
//                   ADDING NAME TO FIRESTORE
                    Map<String, Object> map = new HashMap<>();
                    map.put("Name", etUserName.getText().toString());
                    firebaseFirestore.collection("UserName").document(firebaseAuth.getCurrentUser().getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {
                                Toast.makeText(SignUpActivity.this, "Name is not Added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Sign Up Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}