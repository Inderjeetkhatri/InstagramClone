package com.example.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePictureTab extends Fragment implements View.OnClickListener{

    private int flag=0;
    private ImageView imageSharePicture;
    private EditText edtSharePictureDescription;
    private Button btnSharePicture;
    Bitmap receivedImageBitmap;
    public SharePictureTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_share_picture_tab,
                container, false);

        imageSharePicture=view.findViewById(R.id.imageSharePicture);
        btnSharePicture=view.findViewById(R.id.btnSharePicture);
        edtSharePictureDescription= view.findViewById(R.id.edtSharePictureDescription);

        imageSharePicture.setOnClickListener(SharePictureTab.this);
        btnSharePicture.setOnClickListener(SharePictureTab.this);
        if(edtSharePictureDescription.getText().toString()==null){
            edtSharePictureDescription.setText("");
        }

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.imageSharePicture:
                if(Build.VERSION.SDK_INT>=23 &&
                        ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                }
                else{
                    getChosenImage();
                }
                break;
            case R.id.btnSharePicture:
                if(receivedImageBitmap!=null){

                        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
                        receivedImageBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile= new ParseFile("img_png",bytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture",parseFile);
                        parseObject.put("pic_dis",edtSharePictureDescription.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog parseDialog = new ProgressDialog(getContext());
                        parseDialog.setMessage(" Loading...");
                        parseDialog.show();
                        parseDialog.setCancelable(false);
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e== null){
                                    FancyToast.makeText(getContext(),"Done!!",Toast.LENGTH_LONG,
                                            FancyToast.SUCCESS,false).show();
                                }else{
                                    FancyToast.makeText(getContext(),"Unknown Error: "+ e.getMessage(),Toast.LENGTH_LONG,
                                            FancyToast.ERROR,false).show();
                                }
                                parseDialog.dismiss();
                            }
                        });


                }

               /* else if(flag==0  && edtSharePictureDescription.getText()!=null){

                    ParseObject parseObject = new ParseObject("Posts");
                    parseObject.put("pic_dis",edtSharePictureDescription.getText().toString());
                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                    final ProgressDialog parseDialog = new ProgressDialog(getContext());
                    parseDialog.setMessage(" Loading...");
                    parseDialog.show();
                    parseDialog.setCancelable(false);
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e== null){
                                FancyToast.makeText(getContext(),"Done!!",Toast.LENGTH_LONG,
                                        FancyToast.SUCCESS,false).show();
                            }else{
                                FancyToast.makeText(getContext(),"Unknown Error: "+ e.getMessage(),Toast.LENGTH_LONG,
                                        FancyToast.ERROR,false).show();
                            }
                            parseDialog.dismiss();
                        }
                    });

                }



                */



                else {
                    FancyToast.makeText(getContext(),"Select an Image First or write something to post",
                            Toast.LENGTH_LONG,FancyToast.INFO,false).show();
                }

                break;
        }
    }






    private void getChosenImage() {
        //FancyToast.makeText(getContext(),"Now we can access the images.",
        //        FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();


        Intent intent=new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2000);



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1000){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getChosenImage();
            }
        }
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== 2000) {
            if(resultCode==Activity.RESULT_OK){
                try{
                    Uri selectedImage = data.getData();
                    String[] filePathColumn ={ MediaStore.Images.Media.DATA};
                    Cursor cursor =  getActivity().getContentResolver().query(selectedImage,
                            filePathColumn,null,null,null);
                    cursor.moveToFirst();
                    int columnIndex =cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    receivedImageBitmap = BitmapFactory.decodeFile(picturePath);
                    imageSharePicture.setImageBitmap(receivedImageBitmap);
                    flag=1;

                }catch (Exception e) {
                    e.printStackTrace();
                    FancyToast.makeText(getContext(), "Select an Image First",
                            Toast.LENGTH_LONG, FancyToast.INFO, false).show();
                }


            }
        }
    }
}