package com.example.ecommerce.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Admin.AdminCategoryActivity;
import com.example.ecommerce.Admin.AdminHomeActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText InputPhoneNumber,InputPassword;
    private Button LoginButton;
    private String parentDBname;
    private ProgressDialog loadingBar;
    private CheckBox chckRememberMe;
    private TextView AdminLink,NotAdminLink,ForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton=(Button)findViewById(R.id.login_button);
        InputPhoneNumber =(EditText)findViewById(R.id.login_phone_number_input);
        InputPassword = (EditText)findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);
        AdminLink=(TextView)findViewById(R.id.admin_panel_link);
        NotAdminLink=(TextView)findViewById(R.id.not_admin_panel_link);
        ForgetPassword=(TextView)findViewById(R.id.forget_password);
        parentDBname="Users";

        //chckRememberMe =(CheckBox)findViewById(R.id.remember_me_chbx);
        //Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                LoginButton.setText("LogIn as Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDBname="Admins";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                LoginButton.setText("LogIn");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDBname="Users";
            }
        });

        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent resetIntent =new Intent(LoginActivity.this, ResetPasswordActivity.class);
                resetIntent.putExtra("check","login").putExtra("Phone","");;
                startActivity(resetIntent);
            }
        });
    }

    private void LoginUser()
    {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone))
       {
        Toast.makeText(this,"please write phone number",Toast.LENGTH_SHORT).show();
       }
        else if(TextUtils.isEmpty(password))
       {
        Toast.makeText(this,"please write password",Toast.LENGTH_SHORT).show();
       }
        else
        {
            loadingBar.setTitle("Log in Acoount");
            loadingBar.setMessage("Please wait while we checking Credentials" );
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            AllowAccesToAccount(phone,password);
        }
    }

    private void AllowAccesToAccount(final String phone, final String password)
    {
      /*  if(chckRememberMe.isChecked())
        {
            Paper.book().write(prevalent.UserPhoneKey,phone);
            Paper.book().write(prevalent.UserPasswordKey,password);
        }*/
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(parentDBname).child(phone).exists())
                {
                    Users usersdata =dataSnapshot.child(parentDBname).child(phone).getValue(Users.class);
                   assert usersdata != null;
                    if(usersdata.getPhone().equals(phone))
                    {
                        if(usersdata.getPassword().equals(password))
                        {
                            if(parentDBname.equals("Admins")) {
                                LoggedIN();
                                loadingBar.dismiss();

                                Intent homIntent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                startActivity(homIntent);
                            }
                            else if(parentDBname.equals("Users"))
                            {
                                LoggedIN();
                                loadingBar.dismiss();

                                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                homeIntent.putExtra("Phone",""+phone);
                                homeIntent.putExtra("type","");
                                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(homeIntent);
                            }
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
}
