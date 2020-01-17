package com.example.smartdustbin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    Animation login_anim;
    RelativeLayout logo, login_layout;
    LinearLayout register_text;
    TextView logo_text;
    Button register_worker_btn, worker_login_btn;
    FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseAuth mAuth;
    private static final String TAG = "";
    private  String lw_email, lw_password;
    private TextView l_email, l_password;
    ViewDialog viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        viewDialog = new ViewDialog(this);


        //getting login data
        l_email = findViewById(R.id.wl_email);
        l_password = findViewById(R.id.wl_password);
        //end


        //main activity logo animation
        logo = findViewById(R.id.logo_relative);
        logo_text = findViewById(R.id.main_text);
        login_layout = findViewById(R.id.login_form_relativelayout);
        register_text = findViewById(R.id.register_user_text);


        logo.animate().translationY(-650).setDuration(800).setStartDelay(500);
        logo_text.animate().alpha(0).setDuration(800).setStartDelay(600);
        login_anim = AnimationUtils.loadAnimation(this, R.anim.fade_in_login_layout);
        login_layout.startAnimation(login_anim);
        register_text.startAnimation(login_anim);
        //animation mainactivity end

        //go to register worker layout
        register_worker_btn = findViewById(R.id.sign_up_btn);
        register_worker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(MainActivity.this, Register_worker.class);
                startActivity(reg);
            }
        });
        //end

        //worker login ui

        worker_login_btn = findViewById(R.id.wlogin_btn);
        worker_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lw_email = l_email.getText().toString();
                lw_password = l_password.getText().toString();
                if (!validateForm()) {
                    viewDialog.hideDialog();
                    return;
                }else {

                    mAuth.signInWithEmailAndPassword(lw_email, lw_password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                        Intent w_menu = new Intent(MainActivity.this, Worker_menu.class);
                                        w_menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        w_menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(w_menu);
                                        finish();
                                        viewDialog.hideDialog();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        viewDialog.hideDialog();
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                }
                            });
                }



            }
        });
        //end


    }

    //auto_login with email and password
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser !=null){
                    Intent w_menu = new Intent(MainActivity.this, Worker_menu.class);
                    updateUI(currentUser);
                    w_menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    w_menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(w_menu);
                    finish();

                }
    };
    //end
    private boolean validateForm() {
        viewDialog.showDialog();
        boolean valid = true;

        if (TextUtils.isEmpty(lw_email)) {
            l_email.setError(Html.fromHtml("Reqired"));
            valid = false;
        } else {
            l_email.setError(null);
        }

        if (TextUtils.isEmpty(lw_password)) {
            l_password.setError("Required.");
            valid = false;
        } else {
            l_password.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

        } else {

        }
    }


}

