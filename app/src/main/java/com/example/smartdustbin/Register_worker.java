package com.example.smartdustbin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Register_worker extends AppCompatActivity {

    RelativeLayout register_form_layout;
    Animation register_fade_in;
    private FirebaseAuth mAuth;
    private TextView r_name, r_last_name, r_email,r_password, r_authorise_code, r_retype_password;
    private Button r_register_btn;
    private static final String TAG = "";
    private  String name, last_name, email, password, re_password,code;
    ViewDialog viewDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        viewDialog = new ViewDialog(this);
        setContentView(R.layout.activity_register_worker);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //form animation
        register_form_layout = findViewById(R.id.register_form_relativelayout);
        register_fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in_register_form);
        register_form_layout.startAnimation(register_fade_in);
        //end


        //getting register form data
        r_name = findViewById(R.id.rw_first_name);
        r_last_name = findViewById(R.id.rw_last_name);
        r_email = findViewById(R.id.rw_emai_id);
        r_password = findViewById(R.id.rw_password);
        r_retype_password = findViewById(R.id.rw_re_type_password);
        r_authorise_code = findViewById(R.id.rw_code);
        r_register_btn = findViewById(R.id.register_btn);
        //end

        //registering
        r_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(r_email.getText().toString(), r_password.getText().toString());

            }
        });
        //end
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }


    private void createAccount(final String email, final String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            viewDialog.hideDialog();
            return;
        }else{
            if(r_authorise_code.getText().toString().equals("223359")) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(Register_worker.this, "success.",
                                            Toast.LENGTH_SHORT).show();

                                    //write data to database
                                    User user = new User(name, last_name, email, code);
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user);
                                    FirebaseAuth.getInstance().signOut();
                                    //end
                                    Intent main_login = new Intent(Register_worker.this, MainActivity.class);
                                    main_login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(main_login);
                                    viewDialog.hideDialog();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Register_worker.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    viewDialog.hideDialog();
                                }

                            }
                        });
            }else {
                viewDialog.hideDialog();
                Toast.makeText(Register_worker.this, "Invalid Authorised code.",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }


        private boolean validateForm() {
            boolean valid = true;
            viewDialog.showDialog();
            name = r_name.getText().toString();
            if (TextUtils.isEmpty(name)) {
                r_name.setError(Html.fromHtml("Reqired"));
                valid = false;
            } else {
                r_name.setError(null);
            }

            last_name = r_last_name.getText().toString();
            if (TextUtils.isEmpty(last_name)) {
                r_last_name.setError(Html.fromHtml("Reqired"));
                valid = false;
            } else {
                r_last_name.setError(null);
            }

            email = r_email.getText().toString();
            if (TextUtils.isEmpty(email)) {
                r_email.setError(Html.fromHtml("Reqired"));
                valid = false;
            } else {
                r_email.setError(null);
            }

            password = r_password.getText().toString();
            if (TextUtils.isEmpty(password)) {
                r_password.setError("Required.");
                valid = false;
            } else {
                r_password.setError(null);
            }

            re_password = r_retype_password.getText().toString();
            if (TextUtils.isEmpty(re_password)) {
                r_retype_password.setError("Required.");
                valid = false;
            }else if(!password.equals(re_password)) {
                r_password.setError("Password do not match");
                r_retype_password.setError("Password do not match");
                valid = false;
            }else {
                r_password.setError(null);
            }

            code = r_authorise_code.getText().toString();
            if (TextUtils.isEmpty(code)) {
                r_authorise_code.setError("Required.");
                valid = false;
            } else {
                r_password.setError(null);
            }

            return valid;
        }

}


