package com.example.ecommerce.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ecommerce.R;
import com.example.ecommerce.buyers.HomeActivity;
import com.example.ecommerce.buyers.MainActivity;

public class AdminHomeActivity extends AppCompatActivity {
    private Button LogoutBttn,CheckOrderbttn,maintainProductsBttn,adminCheckApprove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        LogoutBttn=(Button)findViewById(R.id.admin_logout_bttn);
        CheckOrderbttn=(Button)findViewById(R.id.admin_check_order_bttn);
        maintainProductsBttn=(Button)findViewById(R.id.admin_maintain_bttn);
        adminCheckApprove=(Button)findViewById(R.id.admin_check_approve_bttn);
        maintainProductsBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent maintainIntent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                maintainIntent.putExtra("type","Admin");
                startActivity(maintainIntent);
                finish();

            }
        });


         LogoutBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             Intent logIntent = new Intent(AdminHomeActivity.this, MainActivity.class);
             logIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             startActivity(logIntent);
             finish();
            }
        });

       CheckOrderbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent CheckIntent = new Intent(AdminHomeActivity.this, AdminNewOrderActivity.class);
                startActivity(CheckIntent);
            }
        });

       adminCheckApprove.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               Intent CheckApproveIntent = new Intent(AdminHomeActivity.this, AdminCheckApproveActivity.class);
               startActivity(CheckApproveIntent);
           }
       });
    }
}
