package com.example.registration;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import androidx.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {


    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int IMAGE_PICK_CODE = 1002;

    //firebase objs
    FirebaseDatabase database;
    FirebaseStorage storage;
//    private Bitmap thumbnail;

    Button mUploadBtn;
    Button mCaptureBtn;
    Button mUploadFirebase;
    ImageView mImageView;
    ImageView temp;
    ActivityResultLauncher<String> mGetContent;
    Uri image_uri;
    Uri firebaseUri;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mImageView = findViewById(R.id.image_view);
        mCaptureBtn = findViewById(R.id.capture_image_btn);
        mUploadBtn = findViewById(R.id.upload_image_btn);
        mUploadFirebase = findViewById(R.id.upload_firebase);


        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                firebaseUri = result;
                mImageView.setImageURI(result);
                

            }
        });
        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }

        });
        mUploadFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firebaseUri!=null){
                    uploadToFirebase(firebaseUri);
                }else{
                    Toast.makeText(ProfileActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });








        //capture button click
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if system os is >= marshmallow, request runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED){
                        //permission not enabled, request it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show popup to request permissions
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        //permission already granted
                        openCamera();
                    }
                }
                else {
                    //system os < marshmallow
                    openCamera();
                }
            }
        });
    }

    private void uploadImage() {
        Toast toast=Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();
    }

//    private void pickImageFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, IMAGE_PICK_CODE);
//    }
//reial push
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        System.out.println("hiiiihiii" + image_uri);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called, when user presses Allow or Deny from Permission Request Popup
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted
                    openCamera();
//                    pickImageFromGallery();
                } else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //PICK IMAGE GALLERY
//        if(resultCode == RESULT_OK && requestCode==IMAGE_PICK_CODE){
//            mImageView.setImageURI(data.getData());
//        }

        //called when image was captured from camera


        if (resultCode == RESULT_OK) {

            mImageView.setImageURI(image_uri);

        }
    }

    private void uploadToFirebase(Uri uri){

        System.out.println(uri.toString());
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ArrayList Dis_sum = new ArrayList();
                        ArrayList cure = new ArrayList();
                        Model model = new Model(uri.toString(),"","","", Dis_sum , cure);
                        String modelId =  root.push().getKey();
                        root.child(modelId).setValue(model);
//                        System.out.println(uri.toString());
//                        Toast.makeText(ProfileActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                        // put api upload code here
                        callPUTDataMethod(uri.toString());



                    }

                });
            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                Toast.makeText(ProfileActivity.this, "Uploading file to firebase", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Uploading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    public void callPUTDataMethod(String url) {
        System.out.println(url);
        String baseurl = "https://farmers-assistant-backend.herokuapp.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl).addConverterFactory(GsonConverterFactory.create()).build();
        UploadApis retrofitAPI = retrofit.create(UploadApis.class);
        ArrayList cause = new ArrayList();
        ArrayList cure = new ArrayList();
        Model model = new Model(url,"","","", cause, cure);


        Call<Model> call = retrofitAPI.updateData(model);
        call.enqueue(new Callback<Model>() {


            @Override
            public void onResponse(Call<Model> call, Response <Model> response) {
//url, crop, disease, disease_summary, cause, cure
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Model responseFromAPI = response.body();
//                System.out.println(response.body().getCause());
//                System.out.println(response.body().getCure());
//
//                String crop =  response.body().getCrop()==null ? "Not Available" :  response.body().getCrop();
//
//                String cause =  response.body().getCause().toString()==null ? "Not Available" :  response.body().getCause().toString();
//
//                String cure =  response.body().getCure()==null ? "Not Available" :  response.body().getCure().toString();
//                String disease = response.body().getDisease();
                String crop = response.body().getCrop();

                String cause = response.body().getCause().toString();

                String cure =   response.body().getCure().toString();
                String disease =  response.body().getDisease();
//                String disease_summary =  response.body().get().toString().isEmpty() ? "Not Available" :  response.body().getDisease();

                String image = firebaseUri.toString();
                System.out.println(crop + cause + disease + cure);

                Intent intent =  new Intent(ProfileActivity.this, ResultActivity.class);
                intent.putExtra("cause", cause);
                intent.putExtra("cure", cure);
                intent.putExtra("disease", disease);
                intent.putExtra("crop", crop);
                intent.putExtra("image", image);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {

                System.out.println(t.getMessage());

            }
        });


    }

}







//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationManager;
//import android.location.LocationRequest;
//import android.os.Bundle;
//import android.text.Html;
//import android.text.TextUtils;
//import android.util.Patterns;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import android.os.Bundle;
//import android.view.View;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//public class ProfileActivity extends AppCompatActivity {
//    Button bt_location;
//    TextView textView1, textView2, textView3, textView4, textView5;
//    FusedLocationProviderClient fusedLocationProviderClient;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//        // Assign values
//        bt_location = findViewById(R.id.bt_location);
//        textView1 = findViewById(R.id.text_view1);
//        textView2 = findViewById(R.id.text_view2);
//        textView3 = findViewById(R.id.text_view3);
//        textView4 = findViewById(R.id.text_view4);
//        textView5 = findViewById(R.id.text_view5);
//
//        // Initialize fusedLocationProvider
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        bt_location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//                    getLocation();
//
//                } else {
//                    ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//                }
//
//            }
//        });
//    }
//
//    @SuppressLint("MissingPermission")
//    private void getLocation() {
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                Location location = task.getResult();
//                if (location != null) {
//                      try {
//                          Geocoder geocoder = new Geocoder(ProfileActivity.this, Locale.getDefault());
//                          List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//
//                          textView1.setText(Html.fromHtml("<font color='black><b>Latitude</b><br></font>"+addresses.get(0).getLatitude()
//
//                          ));
//                          //set longitude
//                          textView2.setText(Html.fromHtml("<font color='black><b>Longitude</b><br></font>"+addresses.get(0).getLongitude()
//                          ));
//
//                          //set country name
//                          textView3.setText(Html.fromHtml("<font color='black><b>Country</b><br></font>"+addresses.get(0).getCountryName()
//                          ));
//                          //set Locality
//                          textView4.setText(Html.fromHtml("<font color='black><b>Locality</b><br></font>"+addresses.get(0).getLocality()
//                          ));
//                          //set address
//                          textView5.setText(Html.fromHtml("<font color='black><b>Address</b><br></font>"+addresses.get(0).getAddressLine(0)
//                          ));
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });
//
//
//    }
//
//
//
//}