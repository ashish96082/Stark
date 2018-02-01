package com.example.piyush.magicalmethods;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by USER
 */

import com.example.piyush.magicalmethods.databinding.ActivityCourseDetailBinding;

public class CourseDetail extends AppCompatActivity {
    Button button;
    TextView tocHeading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        tocHeading=findViewById(R.id.tocHeading);
        button=findViewById(R.id.button);
        MyData myData = getIntent().getParcelableExtra("Card");
        ActivityCourseDetailBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_course_detail);
        binding.setMyData(myData);
    }
}
