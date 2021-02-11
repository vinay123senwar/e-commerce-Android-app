package com.example.ecommerce.sellers;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellerLogin extends AppCompatActivity {

    private EditText emailInput,passwordInput;
    private Button loginbttn;
    private ProgressDialog loadingbar;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        emailInput=(EditText)findViewById(R.id.seller_login_email);
        passwordInput=(EditText)findViewById(R.id.seller_login_password);
        loginbttn=(Button)findViewById(R.id.seller_login_bttn);
        mAuth=FirebaseAuth.getInstance();
        loadingbar=new ProgressDialog(this);

        loginbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginSeller();
            }
        });


    }
    private void LoginSeller()
    {
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please write your email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please write your password...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Logging in");
            loadingbar.setMessage("Please Wait While We are Checking Credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                Intent Signin = new Intent(SellerLogin.this, SellerHomeActivity.class);
                                Signin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               startActivity(Signin);
                                                           }
                            else
                            {
                                toast(task.getException().toString());
                            }

                        }
                    });

        }
//        else
//        {
//            toast("Please Write All Details");
//        }

    }
    private void toast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
