package com.example.piyush.magicalmethods.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.piyush.magicalmethods.R;
import com.example.piyush.magicalmethods.lib.Util;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;

public class DevelopersActivity extends AppCompatActivity {
        Toolbar toolbar;
        Drawer result=null;
        AccountHeader headerResult=null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_developers);
            toolbar=(Toolbar)findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("DevelopersActivity");

            kotlin.Pair<Drawer, AccountHeader> temp = Util.Companion.initDrawer(this);
            result = temp.component1();
            result.setSelection(5L, false);
            headerResult = temp.component2();
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

