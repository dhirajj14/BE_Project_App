package com.example.smartdustbin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Worker_menu extends AppCompatActivity {

    LinearLayout ds,fd,da,lo;
    TextView head_txt_bg;
    Animation head_fade_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_menu);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        checkLocationPermission();

        //animate heading
        head_txt_bg = findViewById(R.id.heading_text_menu);
        head_fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in_heading_menu);
        head_txt_bg.startAnimation(head_fade_in);
        //end

        ds = findViewById(R.id.dustbin_status_layout);
        fd = findViewById(R.id.find_dustbin_layout);
        da = findViewById(R.id.dustbin_analysis_layout);
        lo = findViewById(R.id.dustbin_logout_layout);

        ds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ds.setBackground(getResources().getDrawable(R.drawable.menu_card_rectangle_bg));
                Intent dust_status = new Intent(Worker_menu.this, Dustbin_status.class);
                startActivity(dust_status);
            }
        });

        fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ds.setBackground(getResources().getDrawable(R.drawable.menu_card_rectangle_bg));
                Intent map = new Intent(Worker_menu.this, MapsActivity.class);
                startActivity(map);
            }
        });

        da.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ds.setBackground(getResources().getDrawable(R.drawable.menu_card_rectangle_bg));
                Intent analysis = new Intent(Worker_menu.this, Line_chart.class);
                startActivity(analysis);

            }
        });

        lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ds.setBackground(getResources().getDrawable(R.drawable.menu_card_rectangle_bg));
                Intent log_out = new Intent(Worker_menu.this, MainActivity.class);
                log_out.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(log_out);
                FirebaseAuth.getInstance().signOut();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Worker_menu.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }
}
