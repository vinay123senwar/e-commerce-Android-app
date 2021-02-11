package com.example.ecommerce.buyers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check="";
    private TextView pageTitle;
    private EditText phoneNumber,q1,q2;
    private Button verifyBttn;
    private String Phone;
    int a=1;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Phone = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("Phone")).toString();
        pageTitle=(TextView)findViewById(R.id.reset_reset_password);
        phoneNumber=(EditText)findViewById(R.id.reset_phone_number);
        q1=(EditText)findViewById(R.id.reset_q1);
        q2=(EditText)findViewById(R.id.reset_q2);
        verifyBttn=(Button)findViewById(R.id.reset_bttn);

        check= getIntent().getStringExtra("check");


    }

    private void setAnswers()
    {
        String aq1=q1.getText().toString().toLowerCase();
        String aq2=q2.getText().toString().toLowerCase();
        if(q1==null  && q2==null)
        {
            toast("Please answer all question");
        }
        else
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(Phone);
            HashMap<String, Object> UserdataMap =new HashMap<>();
            UserdataMap.put("answer1",aq1);
            UserdataMap.put("answer2",aq2);
            ref.child("Security Question").updateChildren(UserdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        toast("Answers Set Successfully");
                        Intent bIntent =new Intent(ResetPasswordActivity.this, SettingsActivity.class);
                        startActivity(bIntent);
                    }
                }
            });
        }
    }

    private void toast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart()
    {
        super.onStart();
        phoneNumber.setVisibility(View.INVISIBLE);
        if(check.equals("settings"))
        {
         pageTitle.setText("Set Question");

         displayPreviousData();
         verifyBttn.setText("SET");
            verifyBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    setAnswers();
                }
            });
        }
        if(check.equals("login"))
        {
            phoneNumber.setVisibility(View.VISIBLE);
            verifyBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                 VerifyUser();
                }
            });
        }
    }

    private void VerifyUser()
    {
        final String lPhone=phoneNumber.getText().toString();
        final String aq1=q1.getText().toString().toLowerCase();
        final String aq2=q2.getText().toString().toLowerCase();

        if(!lPhone.equals("") && !aq1.equals("") && !aq2.equals("")) {
            //   DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
            //         .child("Users");

            //ref.addValueEventListener(new ValueEventListener() {
            //  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            // @Override
            //public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //  if(dataSnapshot.exists())//(dataSnapshot.hasChild(lPhone))
            // {
            final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(lPhone);

            ref1.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("Security Question")) {
                        String mmanswer1 = Objects.requireNonNull(dataSnapshot.child("Security Question").child("answer1").getValue()).toString();
                        String mmanswer2 = Objects.requireNonNull(dataSnapshot.child("Security Question").child("answer2").getValue()).toString();
                        if (!mmanswer1.equals(aq1)) {
                            toast("First Answer is Wrong");
                        } else if (!mmanswer2.equals(aq2)) {
                            toast("Second ANswer Is wrong");
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                            builder.setTitle("New Password");
                            final EditText newpsswrdv = new EditText(ResetPasswordActivity.this);
                            newpsswrdv.setHint("New Password");
                            builder.setView(newpsswrdv);

                            builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!newpsswrdv.getText().toString().equals("")) {
                                        ref1.child("Password").setValue(newpsswrdv.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            toast("Password Changed Successfully");
                                                            Intent lointent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                            lointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(lointent);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });

                            builder.setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            //} else {
            //  toast("Phone Number Not Exixt");
            //}
            // }

            // @Override
            //public void onCancelled(@NonNull DatabaseError databaseError) {

            //}
            //});
            // }
            //else
            //{
            //  toast("Please Write all Details");
        }
    }

    private void displayPreviousData()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(Phone);

        ref.child("Security Question").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String ans1 = Objects.requireNonNull(dataSnapshot.child("answer1").getValue()).toString();
                    String ans2 = Objects.requireNonNull(dataSnapshot.child("answer2").getValue()).toString();

                    q1.setText(ans1);
                    q2.setText(ans2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
