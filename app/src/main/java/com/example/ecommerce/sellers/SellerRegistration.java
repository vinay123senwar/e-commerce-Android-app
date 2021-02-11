package com.example.ecommerce.sellers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class SellerRegistration extends AppCompatActivity {

    private Button sellerLogin;
    private EditText nameInput,phoneInput,emailInput,passwordInput,addressInput;
    private Button registerbttn;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        sellerLogin=(Button)findViewById(R.id.seller_already_have_bttn);
        registerbttn=(Button)findViewById(R.id.seller_register_bttn);
        nameInput=(EditText)findViewById(R.id.seller_name);
        phoneInput=(EditText)findViewById(R.id.seller_phone);
        emailInput=(EditText)findViewById(R.id.seller_email);
        passwordInput=(EditText)findViewById(R.id.seller_password);
        addressInput=(EditText)findViewById(R.id.seller_address);
        mAuth=FirebaseAuth.getInstance();
        loadingbar= new ProgressDialog(this);
       sellerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logIntent =new Intent(SellerRegistration.this,SellerLogin.class);
                startActivity(logIntent);
            }
        });

        registerbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
              registerSeller();
            }
        });
    }

    private void registerSeller()
    {
        loadingbar.setTitle("Creating Account");
        loadingbar.setMessage("Please Wait While We Are Making Account");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        final String name = nameInput.getText().toString();
        final String phone = phoneInput.getText().toString();
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();
        final String address = addressInput.getText().toString();

        if(!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals(""))
        {

          final Task<AuthResult> authResultTask =mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        toast("Please ");
                        if(task.isSuccessful())
                        {

                            DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

                            String sid = mAuth.getCurrentUser().getUid();

                            HashMap<String,Object> sellerMap =new HashMap<>();

                            sellerMap.put("uid",sid);
                            sellerMap.put("Name",name);
                            sellerMap.put("Phone",phone);
                            sellerMap.put("Email",email);
                            sellerMap.put("Password",password);
                            sellerMap.put("Address",address);

                            RootRef.child("Seller").child(sid).updateChildren(sellerMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                toast("Account Created Successfully");
                                                loadingbar.dismiss();
                                                Intent Signin =new Intent(SellerRegistration.this,SellerHomeActivity.class);
                                                Signin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(Signin);
                                            }
                                        }
                                    });
                        }

                        else
                        {
                            String err =task.getException().toString();
                            toast(err);
                        }
                    }
                });

        }
        else
        {
            toast("Please Write provide All Details");
        }


    }

    private void CreateEmailPassword(String email, String password) {
    }

    private void toast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
