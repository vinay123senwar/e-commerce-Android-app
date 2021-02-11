package com.example.ecommerce.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName,InputPhoneNumber,InputPassword;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton=(Button)findViewById(R.id.register_button);
        InputName =(EditText)findViewById(R.id.register_username_input);
        InputPhoneNumber =(EditText)findViewById(R.id.register_phone_number_input);
        InputPassword = (EditText)findViewById(R.id.register_password_input);
        loadingBar =new ProgressDialog(this);
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount()
    {
      String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"please write name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"please write phone number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"please write password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Create Acoount");
            loadingBar.setMessage("Please wait while we checking Credentials" );
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            ValidatePhoneNumber(name, phone, password);
        }

    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
               if(!(dataSnapshot.child("Users").child(phone).exists()))
               {
                   HashMap<String, Object> UserdataMap =new HashMap<>();
                   UserdataMap.put("Phone",phone);
                   UserdataMap.put("Name",name);
                   UserdataMap.put("Password",password);
                   RootRef.child("Users").child(phone).updateChildren(UserdataMap)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task)
                               {
                                if(task.isSuccessful())
                                {
                                    loadingBar.setTitle("Create Acoount");
                                    loadingBar.setMessage("Account Created Successfully" );
                                    loadingBar.setCanceledOnTouchOutside(true);
                                    loadingBar.show();
                                    Intent login =new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(login);
                                }
                                else
                                {
                                    loadingBar.setTitle("Create Acoount");
                                    loadingBar.setMessage("Error" );
                                    loadingBar.setCanceledOnTouchOutside(true);
                                    loadingBar.show();
                                }
                               }
                           });
               }
               else
               {
                   Toast.makeText(RegisterActivity.this,"User Already Exists Try With Other Number",Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
