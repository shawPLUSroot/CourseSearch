/**
 * @author: Luqiao Dai
 */
package com.example.coursesearch;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

//Show details of item clicked
public class FullInfoActivity extends AppCompatActivity {

    TextView courseCode;
    TextView college;
    TextView career;
    TextView rate;
    TextView tuition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info);

        courseCode = findViewById(R.id.course);
        college = findViewById(R.id.college);
        career = findViewById(R.id.career);
        rate = findViewById(R.id.rate);
        tuition = findViewById(R.id.tuition);

        String course = getIntent().getStringExtra("Course");
        courseCode.setText("Course: " + course);
        String c = getIntent().getStringExtra("College");
        college.setText("College: " + c);
        String care = getIntent().getStringExtra("Career");
        career.setText("Career: " + care);
        String r = getIntent().getStringExtra("Rate");
        rate.setText("Rate: " + r);
        String tuit = getIntent().getStringExtra("Tuition");
        tuition.setText("Tuition: " + tuit);

        int count = Log.codeLogMap.containsKey(course) ? Log.codeLogMap.get(course) : 0;
        Log.codeLogMap.put(course, count + 1);
        int countCollge = Log.collegeLogMap.containsKey(c) ? Log.collegeLogMap.get(c) : 0;
        Log.collegeLogMap.put(c, countCollge + 1);
        int countCareer= Log.careerLogMap.containsKey(care) ? Log.careerLogMap.get(care) : 0;
        Log.careerLogMap.put(care, countCareer+ 1);
        int countTuition = Log.tuitionLogMap.containsKey(tuit)? Log.tuitionLogMap.get(tuit):0;
        Log.tuitionLogMap.put(tuit,countTuition+1);
        Log.printAll();


    }
}