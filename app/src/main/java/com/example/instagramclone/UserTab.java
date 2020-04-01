package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    public UserTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_user_tab, container, false);

        listView= view.findViewById(R.id.userListView);
        arrayList=new ArrayList();
        arrayAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setOnItemClickListener(UserTab.this);
        listView.setOnItemLongClickListener(UserTab.this);
        final TextView txtLoadingUsers=view.findViewById(R.id.txtLoadingUsers);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e==null){
                    if(users.size()>0){
                        for(ParseUser user : users){
                            arrayList.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                        txtLoadingUsers.animate().alpha(0).setDuration(500);
                        listView.setVisibility(view.VISIBLE);
                    }
                }
            }
        });


        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(),UsersPosts.class);
        intent.putExtra("username",arrayList.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
/*
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
        parseQuery.whereEqualTo("username",arrayList.get(position));
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user!=null && e==null){
                    new GenericDialog.Builder(getContext())
                            .setDialogFont(R.font.quicksand_regular)
                            .setDialogTheme(R.style.AppTheme)
                            .setIcon(android.R.drawable.checkbox_on_background)
                            .setTitle(user.get("ProfileName")+"\n"+user.get("ProfileBio")+"\n"+user.get("ProfileWorkPlace")+"\n"+user.get("ProfileEducation")).setTitleAppearance(R.color.colorPrimaryDark, 16)
                            .setMessage("Data Collected Successfully")
                            .addNewButton(R.style.,new GenericDialogOnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setButtonOrientation(LinearLayout.HORIZONTAL)
                            .setCancelable(true)
                            .generate();
                }

            }

        });

        return true;
    }     */
}
