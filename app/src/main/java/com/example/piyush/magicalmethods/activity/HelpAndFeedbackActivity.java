package com.example.piyush.magicalmethods.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.piyush.magicalmethods.R;
import com.example.piyush.magicalmethods.lib.Util;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;

public class HelpAndFeedbackActivity extends AppCompatActivity {

    Toolbar toolbar;
    Drawer result=null;
    AccountHeader headerResult=null;
    FloatingActionButton compose;
    FloatingActionButton call;
    private static final int REQUEST_CALL = 1;

    private DatabaseReference mDatabase;
    private ListView mList;
    private ArrayList<String> mQAList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_feedback);
        compose = (FloatingActionButton) findViewById(R.id.compose);
        call = (FloatingActionButton) findViewById(R.id.call);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Help and Feedback");

        kotlin.Pair<Drawer, AccountHeader> temp = Util.Companion.initDrawer(this);
        result = temp.component1();
        result.setSelection(4L, false);
        headerResult = temp.component2();

        compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "feedback@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "User Feedback");
                startActivity(Intent.createChooser(emailIntent, "Complete action using"));
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mList = (ListView) findViewById(R.id.help_answer);




        /**
         * Created by PR
         */

       final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mQAList);
        mList.setAdapter(arrayAdapter);

        mDatabase.child("help").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String value = dataSnapshot.getValue(String.class);
                mQAList.add(value);
                arrayAdapter.notifyDataSetChanged();


            }
         final

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(HelpAndFeedbackActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HelpAndFeedbackActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:1234567890")));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = result.saveInstanceState(outState);
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }

    }

}
