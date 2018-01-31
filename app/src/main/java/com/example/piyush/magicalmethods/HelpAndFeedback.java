package com.example.piyush.magicalmethods;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;

public class HelpAndFeedback extends AppCompatActivity {

    Toolbar toolbar;
    Drawer result=null;
    AccountHeader headerResult=null;
    FloatingActionButton compose;
    FloatingActionButton call;
    private static final int REQUEST_CALL = 1;

   // private DatabaseReference mDatabase;
   // private ListView mList;
  //  private ArrayList<String> mQAList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_feedback);
        compose = (FloatingActionButton) findViewById(R.id.compose);
        call = (FloatingActionButton) findViewById(R.id.call);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Help and Feedback");

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Tony Stark").withEmail("ironman@jarvis.com").withIcon(getResources().getDrawable(R.drawable.tony_stark))

                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();


        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)

                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("Home").withIcon(getResources().getDrawable(R.drawable.home)),
                        new PrimaryDrawerItem().withIdentifier(2).withName("Explore").withIcon(getResources().getDrawable(R.drawable.explore)),

                        new ExpandableDrawerItem().withName("Your courses").withIcon(getResources().getDrawable(R.drawable.your_courses)).withIdentifier(3).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName("Course 1").withLevel(2).withIdentifier(31).withSelectable(false),
                                new SecondaryDrawerItem().withName("Course 2").withLevel(2).withIdentifier(32).withSelectable(false)
                        ),

                        new PrimaryDrawerItem().withIdentifier(4).withName("Help and Feedback").withIcon(getResources().getDrawable(R.drawable.help_and_feedback)),
                        new PrimaryDrawerItem().withIdentifier(5).withName("Developers").withIcon(getResources().getDrawable(R.drawable.developers))


                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(HelpAndFeedback.this, HomeActivity.class);
                            } else if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(HelpAndFeedback.this, Explore.class);
                                // } else if (drawerItem.getIdentifier() ==4) {
                                //    intent = new Intent(HelpAndFeedback.this, HelpAndFeedback.class);
                            } else if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(HelpAndFeedback.this, Developers.class);
                            }
                            if (intent != null) {
                                HelpAndFeedback.this.startActivity(intent);
                            }
                        }

                        return false;
                    }

                })
                // .withSavedInstance(savedInstanceState)
                // .withShowDrawerOnFirstLaunch(true)
                .build();

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
    }

    /*    mDatabase = FirebaseDatabase.getInstance().getReference();
        mList = (ListView) findViewById(R.id.help_answer);

       final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mQAList);
        mList.setAdapter(arrayAdapter);

        mDatabase.addChildEventListener(new ChildEventListener() {
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
    }  */
    public void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(HelpAndFeedback.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HelpAndFeedback.this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL);
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
