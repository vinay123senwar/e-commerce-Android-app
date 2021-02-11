package com.example.ecommerce.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.model.Users;
import com.example.ecommerce.prevalent.prevalent;
import com.example.ecommerce.sellers.SellerHomeActivity;
import com.example.ecommerce.sellers.SellerRegistration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton,loginButton;
    private TextView sellerBttn;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton=(Button)findViewById(R.id.main_join_now_button);
        loginButton=(Button)findViewById(R.id.main_login_button);
        loadingBar =new ProgressDialog(this);

        sellerBttn=(TextView)findViewById(R.id.main_seller_text);
        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLoginActivity();
            }
        });
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterActivity();
            }
        });

        String UserPhoneKey =Paper.book().read(prevalent.UserPhoneKey);
        String UserPasswordKey =Paper.book().read(prevalent.UserPasswordKey);
      /*  if(!UserPasswordKey.equals("") && !UserPhoneKey.equals(""))
        {
            if(!TextUtils.isEmpty(UserPhoneKey)  && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey,UserPasswordKey);
            }
            loadingBar.setTitle("Logging IN");
            loadingBar.setMessage("Please wait while we verify");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        }*/

      sellerBttn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v)
          {
             Intent sellrIntent =new Intent(MainActivity.this, SellerRegistration.class);
             startActivity(sellrIntent);
          }
      });
    }

    private void AllowAccess(final String phone, final String password)
    {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("Users").child(phone).exists())
                {
                    Users usersdata =dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    assert usersdata != null;
                    if(usersdata.getPhone().equals(phone))
                    {
                        if(usersdata.getPassword().equals(password))
                        {
                            LoggedIN();
                            loadingBar.dismiss();

                            Intent homeIntent =new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(homeIntent);
                        }
                        else
                        {
                            PasswordIncorrect();
                        }

                    }
                }
                else
                {
                    Accountnotfound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void PasswordIncorrect()
    {
        Toast.makeText(this,"Password Incorrect",Toast.LENGTH_SHORT).show();
        loadingBar.dismiss();
    }

    private void LoggedIN()
    {
        Toast.makeText(this,"Logged In Successfully",Toast.LENGTH_SHORT).show();
    }

    private void Accountnotfound()
    {
        Toast.makeText(this,"Account not found",Toast.LENGTH_SHORT).show();
        loadingBar.dismiss();
    }

    private void SendUserToRegisterActivity() {
        Intent registerIntent =new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent =new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null)
        {
            Intent loIntent = new Intent(MainActivity.this, SellerHomeActivity.class);
            loIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loIntent);
            finish();
        }
    }
}
