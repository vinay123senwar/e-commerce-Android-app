package com.example.ecommerce.buyers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameText, userPhoneEditText, addressEditText;
    private TextView profilChangeTextBttn,CloseTextBttn,UpdateTextBttn;
    private Uri imageUri;
    private String myurl;
    private StorageReference StorageProfilePictureRef;
    private StorageTask uploadTask;
    private String checker="";
    private String Phone;
    private Button set_security_bttn;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        set_security_bttn=(Button)findViewById(R.id.settings_set_security);
        profileImageView=(CircleImageView)findViewById(R.id.settings_profile_image);
        fullNameText =(EditText)findViewById(R.id.settings_full_name);
        userPhoneEditText =(EditText)findViewById(R.id.settings_phone_number);
        addressEditText =(EditText)findViewById(R.id.settings_address);
        profilChangeTextBttn=(TextView)findViewById(R.id.profile_image_change_button);
        CloseTextBttn=(TextView)findViewById(R.id.close_account_settings);
        UpdateTextBttn=(TextView)findViewById(R.id.update_account_settings);
        Phone = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("Phone")).toString();
        StorageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        UserInfoDisplay( profileImageView, fullNameText,userPhoneEditText,addressEditText);

        CloseTextBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        UpdateTextBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             if(checker.equals("clicked"))
             {
                 UserInfoSet();
             }
             else
             {
                 UpdateOnlyUserInfo();
             }
            }
        });

        profilChangeTextBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               checker="clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });

        set_security_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             Intent resetIntent =new Intent(SettingsActivity.this, ResetPasswordActivity.class);
             resetIntent.putExtra("check","settings");
             resetIntent.putExtra("Phone",Phone);
             startActivity(resetIntent);
            }
        });
    }

    private void UpdateOnlyUserInfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap =new HashMap<>();
        userMap.put("Name",fullNameText.getText().toString());
        userMap.put("PhoneOrder",userPhoneEditText.getText().toString());
        userMap.put("address",addressEditText.getText().toString());
        ref.child(Phone).updateChildren(userMap);


        startActivity(new Intent(SettingsActivity.this, HomeActivity.class).putExtra("Phone",Phone)
        .putExtra("type",""));
        toast("Profile Info Updated Successfully");
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri=result.getUri();
            profileImageView.setImageURI(imageUri);
        }
        else
        {
            toast("Error Try Again");
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class).putExtra("Phone",Phone));

            finish();
        }
    }

    private void toast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void UserInfoSet()
    {
        if(TextUtils.isEmpty(fullNameText.getText().toString()))
        {
            toast("NAme Is MAntatory");
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            toast("address Is MAntatory");
        }
        else if(TextUtils.isEmpty(userPhoneEditText.getText().toString()))
        {
            toast("phone Is MAntatory");
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage()
    {
        final ProgressDialog loadingBar =new ProgressDialog(this);
        loadingBar.setTitle("Updating Profile Image");
        loadingBar.setMessage("Please Wait While We Are Updating Profile Image");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        if(imageUri !=null)
        {
            final StorageReference fileRef = StorageProfilePictureRef.child(Phone +".jpg");
            uploadTask =fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                 if(task.isSuccessful())
                 {
                     Uri downloadURl =task.getResult();
                     myurl =downloadURl.toString();
                     DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                     HashMap<String, Object> userMap =new HashMap<>();
                     userMap.put("Name",fullNameText.getText().toString());
                     userMap.put("PhoneOrder",userPhoneEditText.getText().toString());
                     userMap.put("address",addressEditText.getText().toString());
                     userMap.put("image",myurl);
                     ref.child(Phone).updateChildren(userMap);

                     loadingBar.dismiss();

                     startActivity(new Intent(SettingsActivity.this,HomeActivity.class).putExtra("Phone",Phone)
                         .putExtra("type",""));
                     toast("Profile Info Updated Successfully");
                     finish();
                 }
                 else
                 {
                     loadingBar.dismiss();
                     toast("Error");
                 }
                }
            });

        }
        else
        {
            toast("IMage IS not Selected");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void UserInfoDisplay(final CircleImageView profileImageView, final EditText fullNameText, final EditText userPhoneEditText, final EditText addressEditText)
    {

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Phone);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("image").exists())
                    {
                        String image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();
                        String name = Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString();
                        String phone = Objects.requireNonNull(dataSnapshot.child("Phone").getValue()).toString();
                        String address = Objects.requireNonNull(dataSnapshot.child("address").getValue()).toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameText.setText(name);
                        userPhoneEditText.setText(phone);
                        addressEditText.setText(address);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
