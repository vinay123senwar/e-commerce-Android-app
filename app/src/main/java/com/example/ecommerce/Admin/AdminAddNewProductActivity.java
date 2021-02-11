package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private ImageView InputProductImage;
    private String categoryName,Description,Price,Pname,saveCurrentDate,saveCurrentTime,ProductRandomName,downloadImageUrl;
    private Button AddNewProductButton;
    private EditText InputProductName,InputProductDescription,InputProductPrice;
    private int gallery_Pick=1;
    private Uri ImageUri;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductsRef,SellerRef;
    private ProgressDialog loadingBar;
    private String sName,sAddress,sPhone,sEmail,SSID;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        InputProductImage =(ImageView)findViewById(R.id.select_product_image);
        AddNewProductButton=(Button)findViewById(R.id.add_new_product);
        InputProductName =(EditText)findViewById(R.id.product_name);
        InputProductDescription =(EditText)findViewById(R.id.product_description);
        InputProductPrice =(EditText)findViewById(R.id.product_price);

        loadingBar =new ProgressDialog(this);
        ProductImageRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef =FirebaseDatabase.getInstance().getReference().child("Produts");
        SellerRef =FirebaseDatabase.getInstance().getReference().child("Seller");
        categoryName = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("category")).toString();

        Toast.makeText(this, categoryName,Toast.LENGTH_SHORT).show();

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

        SellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            sName=dataSnapshot.child("Name").getValue().toString();
                            sPhone=dataSnapshot.child("Phone").getValue().toString();
                            sAddress=dataSnapshot.child("Address").getValue().toString();
                            sEmail=dataSnapshot.child("Email").getValue().toString();
                            SSID=dataSnapshot.child("uid").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void ValidateProductData()
    {
        Description=InputProductDescription.getText().toString();
        Price=InputProductPrice.getText().toString();
        Pname=InputProductName.getText().toString();

        if(ImageUri ==null)
        {
            Toast.makeText(this,"Product Image Is mandatory",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this,"Product Description Is mandatory",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this,"Product Price Is mandatory",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this,"Product Name Is mandatory",Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreImageInformation();
        }

    }

    private void StoreImageInformation()
    {
        Calendar calendar =Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate =new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime =new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        ProductRandomName =saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + ProductRandomName +".jpg");

        final UploadTask uploadTask =filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                loadingBar.dismiss();
                String message =e.toString();
                toast(message);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
               toast("ImageUploadedSuccessfully");
                Task<Uri> urlTask =uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if(!task.isSuccessful())
                        {
                            loadingBar.dismiss();
                            throw Objects.requireNonNull(task.getException());
                        }

                        downloadImageUrl =filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                      if(task.isSuccessful())
                      {
                          downloadImageUrl = Objects.requireNonNull(task.getResult()).toString();
                          toast("product Image Save Url");
                          SaveProducttInfoToDatabase();
                      }
                    }
                });
            }
        });


    }

    private void SaveProducttInfoToDatabase()
    {
        loadingBar.setTitle("Saving Information");
        loadingBar.setMessage("Please Wait While we are Adding New product");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();


        HashMap<String, Object> productMap =new HashMap<>();
        productMap.put("pid",ProductRandomName);
        productMap.put("Date",saveCurrentDate);
        productMap.put("Time",saveCurrentTime);
        productMap.put("Description",Description);
        productMap.put("Image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("Price",Price);
        productMap.put("Name",Pname);


        productMap.put("Seller Name",sName);
        productMap.put("Seller Address",sAddress);
        productMap.put("Seller Phone",sPhone);
        productMap.put("Seller Email",sEmail);
        productMap.put("Seller sid",SSID);

        productMap.put("Status","Not Approved");

        ProductsRef.child(ProductRandomName).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent intent =new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            toast("Product Is Added Successfuly");
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = Objects.requireNonNull(task.getException()).toString();
                            toast("Error"+message);

                        }
                    }
                });




    }

    @SuppressLint("ShowToast")
    private void toast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void openGallery()
    {
        Intent gallery =new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*");
        startActivityForResult(gallery,gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==gallery_Pick && resultCode== RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }
}
