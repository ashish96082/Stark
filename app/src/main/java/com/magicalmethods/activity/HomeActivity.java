package com.magicalmethods.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.beust.klaxon.JsonArray;
import com.beust.klaxon.JsonObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.magicalmethods.R;
import com.magicalmethods.adapters.CourseDetailsSmallAdapter;
import com.magicalmethods.entity.CourseDetails;
import com.magicalmethods.lib.MMfromServer;
import com.magicalmethods.lib.Util;
import com.magicalmethods.listeners.MMfromServerListener;
import com.magicalmethods.views.LandscapeCarouselView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    Context context = this;
    Toolbar toolbar;
    Drawer result = null;
    AccountHeader headerResult = null;

    LandscapeCarouselView carouselView;
    int[] sampleImages = {R.drawable.poster, R.drawable.poster, R.drawable.poster, R.drawable.poster, R.drawable.poster, R.drawable.poster, R.drawable.poster, R.drawable.poster, R.drawable.poster};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Magical Methods");

        Util.Companion.saveUserData(this);

        kotlin.Pair<Drawer, AccountHeader> temp = Util.Companion.initDrawer(this);
        result = temp.component1();
        result.setSelection(1L, false);
        headerResult = temp.component2();


        /*
          Loading courses list
         */
        loadCourseList(
                (RecyclerView) findViewById(R.id.home_all_courses_list),
                (ProgressBar) findViewById(R.id.all_courses_progress_bar),
                "course_list"
        );
        loadCourseList(
                (RecyclerView) findViewById(R.id.home_trending_courses_list),
                (ProgressBar) findViewById(R.id.trending_courses_progress_bar),
                "course_list"
        );
        loadCourseList(
                (RecyclerView) findViewById(R.id.home_user_courses_list),
                (ProgressBar) findViewById(R.id.user_courses_progress_bar),
                "user_course_list"
        );

        /*
        On click listener for opening website
         */
//        Button knowMore = findViewById(R.id.home_know_more);
//        knowMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://magicalmethods.com/")));
//            }
//        });

        /*
        Listener to open course lists activity
         */
        findViewById(R.id.home_all_courses_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ExploreActivity.class));
            }
        });
        findViewById(R.id.home_trending_courses_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ExploreActivity.class));
            }
        });
        findViewById(R.id.home_user_courses_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, PurchasedCoursesActivity.class));
            }
        });


        carouselView = findViewById(R.id.carousel_view);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Log.d("Home", "existing image listener");
                imageView.setImageResource(sampleImages[position]);
            }
        });

        MMfromServer mMfromServer = new MMfromServer(this, new MMfromServerListener() {
            @Override
            public void onDownloadComplete(@NotNull JsonObject json) {
                if ((json.get("result")).equals("success")) {
                    JsonArray<JsonObject> courseList = (JsonArray<JsonObject>) json.get("data");
                    final ArrayList<CourseDetails> courseDetails = new ArrayList<CourseDetails>();
                    for (int i = 0; i < courseList.size(); i++) {
                        JsonObject courseInfo = courseList.get(i);
                        courseDetails.add(new CourseDetails(
                                (String) courseInfo.get("name"),
                                (String) courseInfo.get("description"),
                                (String) courseInfo.get("image"),
                                Integer.parseInt(((String) courseInfo.get("price"))),
                                (String) courseInfo.get("course_key")
                        ));
                    }


//                    Log.d("Home", "creating image listener");
                    carouselView.setImageListener(new ImageListener() {
                        @Override
                        public void setImageForPosition(int position, ImageView imageView) {
//                            Log.d("Home", "inside image listener");
                            Glide
                                    .with(getApplicationContext())
                                    .load(courseDetails.get(position).getImage())
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.poster)
//                                            .crossFade()
                                            .centerCrop())
                                    .into(imageView);
                        }
                    });
                    carouselView.setImageClickListener(new ImageClickListener() {
                        @Override
                        public void onClick(int position) {
                            context.startActivity(new Intent(context, CourseDetailsActivity.class)
                                    .putExtra("courseKey", courseDetails.get(position).getCourseKey()));
                        }
                    });
                    carouselView.setPageCount(courseDetails.size());
                }
            }
        });
        mMfromServer.get(MMfromServer.REQUEST_COURSE_CAROUSEL_VIEW_LIST, "asaouetsfnvpsq");
        mMfromServer.setPage(1, 6);
        mMfromServer.execute();
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Exit Application?");
            alertDialogBuilder
                    .setMessage("Click yes to exit!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveTaskToBack(true);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(1);
                                }
                            })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void loadCourseList(final RecyclerView recyclerView, final ProgressBar progressBar, String type) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.hasFixedSize();
        recyclerView.setNestedScrollingEnabled(true);


        MMfromServer mMfromServer = new MMfromServer(this, new MMfromServerListener() {
            @Override
            public void onDownloadComplete(@NotNull JsonObject json) {
                System.out.println(json.toJsonString(true));

                if ((json.get("result")).equals("success")) {
                    JsonArray<JsonObject> courseList = (JsonArray<JsonObject>) json.get("data");
                    final ArrayList<CourseDetails> courseDetails = new ArrayList<CourseDetails>(6);
                    for (int i = 0; i < courseList.size(); i++) {
                        JsonObject courseInfo = courseList.get(i);
                        courseDetails.add(new CourseDetails(
                                (String) courseInfo.get("name"),
                                (String) courseInfo.get("description"),
                                (String) courseInfo.get("image"),
                                Integer.parseInt(((String) courseInfo.get("price"))),
                                (String) courseInfo.get("course_key")
                        ));
                    }
                    CourseDetailsSmallAdapter adapter = new CourseDetailsSmallAdapter(HomeActivity.this, courseDetails);
                    recyclerView.setAdapter(adapter);

                    hideProgressBar(recyclerView, progressBar);
                }
            }
        });

        Integer get = 0;
        if (type.equals("course_list"))
            get = MMfromServer.REQUEST_COURSE_LIST;
        else if (type.equals("user_course_list"))
            get = MMfromServer.REQUEST_USER_COURSE_LIST;

        mMfromServer.get(get, "asaouetsfnvpsq");
        mMfromServer.setPage(1, 6);
        mMfromServer.execute();
    }


    private void hideProgressBar(RecyclerView recyclerView, ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
