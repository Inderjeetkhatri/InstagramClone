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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsernameLogin,edtPasswordLogin;
    private TextView txtLoginToSignUp;
    private Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        edtUsernameLogin=findViewById(R.id.edtUsernameLogin);
        edtPasswordLogin=findViewById(R.id.edtPasswordLogin);
        edtPasswordLogin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnLogin);
                }
                return false;
            }
        });
        btnLogin=findViewById(R.id.btnLogin);
        txtLoginToSignUp=findViewById(R.id.txtLoginTosignUp);
        setTitle("Log In");

        btnLogin.setOnClickListener(this);
        txtLoginToSignUp.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null){
            //ParseUser.getCurrentUser().logOut();
            transitionToSocialMediaActivity();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:

                if(edtPasswordLogin.getText().toString().equals("") ||
                        edtUsernameLogin.getText().toString().equals("")){
                    FancyToast.makeText(LoginActivity.this,"Enter Valid Username or Password",
                            FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();

                }
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Logging In");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    ParseUser.logInInBackground(edtUsernameLogin.getText().toString(),
                            edtPasswordLogin.getText().toString(),
                            new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {
                                //FancyToast.makeText(LoginActivity.this, user.getUsername() + " is Logged In Successfully",FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();
                                transitionToSocialMediaActivity();
                            }
                            else {
                                FancyToast.makeText(LoginActivity.this, "Enter Valid Username or Password",
                                        FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }

                break;
            case R.id.txtLoginTosignUp:
                Intent intent=new Intent(LoginActivity.this,SignUpLoginActivity.class);
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
        Intent intent=new Intent(LoginActivity.this,SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }
}
