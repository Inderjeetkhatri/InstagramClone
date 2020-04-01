package com.example.instagramclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {
    private EditText edtProfileName,edtProfileBio,edtProfileWorkplace,edtProfileEducation;
    private Button btnProfileUpdateInfo;

    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_profile_tab, container, false);

        edtProfileName=view.findViewById(R.id.edtProfileName);
        edtProfileBio=view.findViewById(R.id.edtProfileBio);
        edtProfileEducation=view.findViewById(R.id.edtProfileEducation);
        edtProfileWorkplace=view.findViewById(R.id.edtProfileWorkPlace);
        btnProfileUpdateInfo=view.findViewById(R.id.btnProfileUpdateInfo);



        final ParseUser parseUser=ParseUser.getCurrentUser();
        if(parseUser.get("ProfileName")==null){
            parseUser.put("ProfileName",parseUser.getUsername());
            edtProfileName.setText(parseUser.get("ProfileName")+"");
        }
        else{
            edtProfileName.setText(parseUser.get("ProfileName")+"");
        }
        if(parseUser.get("ProfileBio")==null){
            edtProfileBio.setText("");
        }
        else{
            edtProfileBio.setText(parseUser.get("ProfileBio")+"");
        }
        if(parseUser.get("ProfileEducation")==null){
            edtProfileEducation.setText("");
        }
        else {
            edtProfileEducation.setText(parseUser.get("ProfileEducation") + "");
        }
        if(parseUser.get("ProfileWorkPlace")==null){
            edtProfileWorkplace.setText("");
        }
        else {
            edtProfileWorkplace.setText(parseUser.get("ProfileWorkPlace") + "");
        }


        btnProfileUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                parseUser.put("ProfileName",edtProfileName.getText().toString());

                parseUser.put("ProfileBio",edtProfileBio.getText().toString());

                parseUser.put("ProfileWorkPlace",edtProfileWorkplace.getText().toString());

                parseUser.put("ProfileEducation",edtProfileEducation.getText().toString());
                final ProgressDialog parseDialog = new ProgressDialog(getContext());
                parseDialog.setMessage(" Loading...");
                parseDialog.show();
                parseDialog.setCancelable(false);

                parseUser.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            FancyToast.makeText(getContext(),"Profile Updated",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                        }
                        else{
                            FancyToast.makeText(getContext(),e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                        }
                        parseDialog.dismiss();
                    }
                });
            }
        });

        return view;

    }
}
