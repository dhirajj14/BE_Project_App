package com.example.smartdustbin;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pl.droidsonroids.gif.GifImageView;

public class Detail_status extends AppCompatActivity {
    Animation head_fade_in;
    GifImageView gif;
    String df,a,dID;
    int dust_f;
    Button empty_btn;
    FirebaseDatabase database;
    DatabaseReference myRef ;
    TextView gif_per;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_status);
        Bundle b = getIntent().getExtras();
        a = b.getString("area_name");
        df = b.getString("dustbin_filled");
        dID = b.getString("id");
        dust_f = Integer.parseInt(df);
        TextView head = (TextView) findViewById((R.id.detail_heading));
        head.setText(a);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        // animate heading
        head_fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in_heading_menu);
        head_fade_in.setStartOffset(500);
        head.startAnimation(head_fade_in);
        //end
        Toast.makeText(getApplication(), "done  " +a, Toast.LENGTH_SHORT).show();



       gif();

        empty_btn = findViewById(R.id.emptydustbin_btn);
        empty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("Dustbin_status/"+dID).child("distance");
                myRef.setValue("0");
                df = "0";
                dust_f = 0;
                gif();
            }
        });
    }

    private void gif() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gif = findViewById(R.id.dust_gif);
                if(dust_f >= 1 && dust_f<=10){
                    gif.setImageResource(R.drawable.dust_10per);
                }else if(dust_f > 10 && dust_f<=20){
                    gif.setImageResource(R.drawable.dust_20per);
                }else if(dust_f > 20 && dust_f<=30){
                    gif.setImageResource(R.drawable.dust_30per);
                }else if(dust_f > 30 && dust_f<=40){
                    gif.setImageResource(R.drawable.dust_40per);
                }else if(dust_f > 40 && dust_f<=50){
                    gif.setImageResource(R.drawable.dust_50per);
                }else if(dust_f > 50 && dust_f<=60){
                    gif.setImageResource(R.drawable.dust_60per);
                }else if(dust_f > 60 && dust_f<=70){
                    gif.setImageResource(R.drawable.dust_70per);
                }else if(dust_f > 70 && dust_f<=80){
                    gif.setImageResource(R.drawable.dust_80per);
                }else if(dust_f > 80 && dust_f<=90){
                    gif.setImageResource(R.drawable.dust_90per);
                }else{
                    gif.setImageResource(R.drawable.dust_per);
                }

                gif_per = findViewById(R.id.dust_gif_per);
                gif_per.setText(df+ "%");
            }
        }, 1000);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}
