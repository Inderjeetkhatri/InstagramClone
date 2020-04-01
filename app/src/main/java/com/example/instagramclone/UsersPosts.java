package com.example.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPosts extends AppCompatActivity {
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);
        linearLayout=findViewById(R.id.linearLayout);
        Intent receivedIntentObject=getIntent();
        final String receivedUserName=receivedIntentObject.getStringExtra("username");
        //FancyToast.makeText(UsersPosts.this,receivedUserName,FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();

        setTitle(receivedUserName+",s Posts");

        ParseQuery<ParseObject> parseQuery= new ParseQuery<>("Photo");
        parseQuery.whereEqualTo("username",receivedUserName);
        parseQuery.orderByDescending("createdAt");
        final ProgressDialog parseDialog = new ProgressDialog(UsersPosts.this);
        parseDialog.setMessage(" Loading...");
        parseDialog.show();



        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()>0 && e==null){
                    for(ParseObject post : objects){
                        final TextView postDescription=new TextView(UsersPosts.this);
                        postDescription.setText(post.get("pic_dis")+"");


                        ParseFile postPicture=(ParseFile) post.get("picture");
                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                if(data!=null && e==null){
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(data,
                                            0,data.length);
                                    ImageView postImageView=new ImageView(UsersPosts.this);
                                    LinearLayout.LayoutParams imageviews_params=new LinearLayout.
                                            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageviews_params.setMargins(10,10,10,10);
                                    postImageView.setLayoutParams(imageviews_params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams description_params=new LinearLayout.
                                            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    description_params.setMargins(10,10,10,20);
                                    postDescription.setLayoutParams(description_params);
                                    //postDescription.setGravity(Gravity.CENTER);
                                    postDescription.setBackgroundColor(Color.WHITE);
                                    postDescription.setTextColor(Color.BLACK);
                                    postDescription.setTextSize(15f);


                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(postDescription);


                                }

                            }
                        });

                    }
                }else{
                    FancyToast.makeText(UsersPosts.this,receivedUserName+"'s doesn't have any post",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                    //finish();
                }

                parseDialog.dismiss();

            }
        });

    }
}
