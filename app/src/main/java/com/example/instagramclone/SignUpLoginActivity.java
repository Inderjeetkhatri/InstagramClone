package com.example.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmailSignUp,edtUsernameSignUp,edtPasswordSignUp;
    private TextView txtSignUpToLogin;
    private Button btnSignUp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_login_activity);

        edtEmailSignUp=findViewById(R.id.edtEmailSignUp);
        edtUsernameSignUp=findViewById(R.id.edtUsernameSignUp);
        edtPasswordSignUp=findViewById(R.id.edtPasswordSignUp);
        edtPasswordSignUp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnSignUp);
                }

                return false;
            }
        });
        btnSignUp=findViewById(R.id.btnSignUp);
        txtSignUpToLogin=findViewById(R.id.txtSignUpToLogin);

        if(ParseUser.getCurrentUser()!=null) {
            //ParseUser.getCurrentUser().logOut();
            transitionToSocialMediaActivity();
        }

        setTitle("Sign Up");

        btnSignUp.setOnClickListener(this);
        txtSignUpToLogin.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnSignUp:

                if(edtEmailSignUp.getText().toString().equals("") ||
                        edtUsernameSignUp.getText().toString().equals("") ||
                        edtPasswordSignUp.getText().toString().equals("")){
                    FancyToast.makeText(SignUpLoginActivity.this,"Enter Valid Username, Email or Password",
                            FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();

                }
                else {
                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtEmailSignUp.getText().toString());
                    appUser.setUsername(edtUsernameSignUp.getText().toString());
                    appUser.setPassword(edtPasswordSignUp.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing Up");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                //FancyToast.makeText(SignUpLoginActivity.this, appUser.getUsername() + " is signed up",FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                               transitionToSocialMediaActivity();
                            } else {
                                FancyToast.makeText(SignUpLoginActivity.this, "Enter Valid Username, Email or Password",
                                        FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }

                break;
            case R.id.txtSignUpToLogin:
                Intent intent=new Intent(SignUpLoginActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void rootLayoutTapped(View view){
        try {
            InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void transitionToSocialMediaActivity(){
        Intent intent=new Intent(SignUpLoginActivity.this,SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }

}

