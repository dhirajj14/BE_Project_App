package com.example.smartdustbin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Dustbin_status extends AppCompatActivity {

    TextView head_txt_bg;
    Animation head_fade_in;
    ViewDialog viewDialog;
    String detail_area;

    //firebase data fetch items

    FirebaseDatabase database;
    DatabaseReference myRef ;
    List<Dustbin_status_items> list;
    RecyclerView recycle;
    Button cb;

    //end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dustbin_status);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        viewDialog = new ViewDialog(this);
       // animate heading
       head_txt_bg = findViewById(R.id.heading_text_menu1);
       head_fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in_heading_menu);
       head_txt_bg.startAnimation(head_fade_in);
        //end

        //fetching dustbin status database
        viewDialog.showDialog();
        recycle = (RecyclerView) findViewById(R.id.recycle);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Dustbin_status");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                list = new ArrayList<Dustbin_status_items>();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    Dustbin_status_items value = dataSnapshot1.getValue(Dustbin_status_items.class);
                    Dustbin_status_items fire = new Dustbin_status_items();
                    String id = dataSnapshot1.getKey();
                    String area = value.getArea();
                    String distance = value.getDistance();
                    fire.setId(id);
                    fire.setArea(area);
                    fire.setDistance(distance);
                    list.add(fire);

                }
                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(list,Dustbin_status.this);
                // RecyclerView.LayoutManager recyce = new GridLayoutManager(Dustbin_status.this,2);
                RecyclerView.LayoutManager recyce = new LinearLayoutManager(Dustbin_status.this);
                // recycle.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                recycle.setLayoutManager(recyce);
                recycle.setItemAnimator( new DefaultItemAnimator());
                recycle.setAdapter(recyclerAdapter);
                viewDialog.hideDialog();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

        //en

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }


}
