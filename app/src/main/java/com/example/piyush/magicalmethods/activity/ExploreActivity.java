package com.example.piyush.magicalmethods.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beust.klaxon.JsonArray;
import com.beust.klaxon.JsonObject;
import com.example.piyush.magicalmethods.R;
import com.example.piyush.magicalmethods.adapters.CourseDetailsAdapter;
import com.example.piyush.magicalmethods.entity.CourseDetails;
import com.example.piyush.magicalmethods.lib.Util;
import com.example.piyush.magicalmethods.listeners.MMfromServerListener;
import com.example.piyush.magicalmethods.lib.MMfromServer;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ExploreActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Toolbar toolbar;
    Drawer result = null;
    AccountHeader headerResult = null;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CourseDetailsAdapter adapter;
    private ArrayList<CourseDetails> data_list;
    ProgressBar mLoadingProgress;
    private Context context = this;

    private Boolean moreCoursesAvailable = true;
    private final Integer perPageCourses = 10;
    private Integer currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Explore Courses");
        mLoadingProgress = findViewById(R.id.pb_loading);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        data_list = new ArrayList<>();
//        load_data_from_server(0);
        loadData();


        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CourseDetailsAdapter(this, data_list);
        recyclerView.setAdapter(adapter);


        kotlin.Pair<Drawer, AccountHeader> temp = Util.Companion.initDrawer(this);
        result = temp.component1();
        result.setSelection(2L, false);
        headerResult = temp.component2();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (moreCoursesAvailable && gridLayoutManager.findLastCompletelyVisibleItemPosition() == data_list.size() - 1) {
                    loadData();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.card_list_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(ExploreActivity.this);
        return true;
    }

    private void loadData() {
        MMfromServer task = new MMfromServer(this, new MMfromServerListener() {
            @Override
            public void onDownloadComplete(@NotNull JsonObject json) {
                System.out.println(json.toJsonString(true));

//                try {
                ArrayList<CourseDetails> courses = new ArrayList<>();
                String result = (String) json.get("result");
                if (result.equals("success")) {
                    JsonArray array = (JsonArray) json.get("data");

                    if (array.size() < perPageCourses)
                        moreCoursesAvailable = false;

                    for (int i = 0; i < array.size(); i++) {
                        JsonObject object = (JsonObject) array.get(i);

                        CourseDetails data = new CourseDetails(
                                (String) object.get("name"),
                                (String) object.get("description"),
                                (String) object.get("image"),
                                Integer.parseInt((String) object.get("price")),
                                (String) object.get("course_key")
                        );
                        courses.add(data);
                    }
                }

                TextView error_msg = findViewById(R.id.error_msg);
                mLoadingProgress.setVisibility(View.INVISIBLE);
                if (data_list == null) {
                    error_msg.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                adapter.addData(courses);
            }

            @Override
            public void onPreDownload() {
                mLoadingProgress.setVisibility(View.VISIBLE);
            }
        });
        task.get(MMfromServer.Companion.getREQUEST_COURSE_LIST(), "asaouetsfnvpsq");
        task.setPage(++currentPage, perPageCourses);
        task.execute();
    }

//    private void load_data_from_server(final int id) {
//        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
//            @Override
//            protected Void doInBackground(Integer... integers) {
//                ManageSession session = new ManageSession(context);
//                Pair<String, String> sessionData = session.getSession();
//                String mcryptKey = sessionData.getFirst();
//                String sessionId = sessionData.getSecond();
//
//
//                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .url("http://mm.s-ct.asia/arrayOut.php?id=" + id + "&session_id=" + sessionId)
//                        .build();
//                try {
//                    Response response = client.newCall(request).execute();
//
//                    mcrypt mcrypt = new mcrypt(mcryptKey);
//                    String resp = new String(mcrypt.decrypt(response.body().string()));
//
//                    JSONArray array = new JSONArray(resp);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject object = array.getJSONObject(i);
//                        int tocNum = object.getJSONArray("toc").length();
//                        String[] table_of_contents = new String[tocNum];
//                        for (int j = 0; j < tocNum; j++) {
//                            table_of_contents[j] = object.getJSONArray("toc").get(j).toString();
//                        }
//
//                        System.out.println(object);
//
//                        MyData data = new MyData(object.getInt("id"), object.getString("description"), object.getString("description"),
//                                object.getString("image"), object.getString("longDesc"), table_of_contents);
//                        data_list.add(data);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                mLoadingProgress.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                TextView error_msg = findViewById(R.id.error_msg);
//                mLoadingProgress.setVisibility(View.INVISIBLE);
//                if (data_list == null) {
//                    error_msg.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.INVISIBLE);
//                } else {
//                    recyclerView.setVisibility(View.VISIBLE);
//                }
//                adapter.addData(data_list);
//
//                recyclerView.setAdapter(adapter);
//            }
//        };
//        task.execute(id);
//    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<CourseDetails> newList = new ArrayList<>();
        for (CourseDetails myData : data_list) {
            String description = myData.getDescription().toLowerCase();
            if (description.contains(newText)) {
                newList.add(myData);
            }
        }
        adapter.setFilter(newList);

        return true;
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
