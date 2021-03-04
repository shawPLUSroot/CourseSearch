/**
 * @author: Luqiao Dai
 * new search method (search automatically) is based on Youtube
 * method distance and similarity (line69-92)
 * Reference: https://www.youtube.com/watch?v=rdu1ZqM9rSE&feature=share
 */
package com.example.coursesearch;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import java.util.ArrayList;

public class LawActivity extends AppCompatActivity {

    ListView lawListView;
    ArrayAdapter<String> lawAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law);

        lawListView = findViewById(R.id.law_listView);

        ArrayList<String> lawArrayList = getIntent().getStringArrayListExtra("LAWList");

        lawAdapter = new ArrayAdapter(LawActivity.this, android.R.layout.simple_list_item_1, lawArrayList);
        lawListView.setAdapter(lawAdapter);

        lawListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Integer coursePos = -1; //initialize course position

                Intent intent = new Intent(LawActivity.this, FullInfoActivity.class);
                String course = lawAdapter.getItem(position); //get clicked item CourseCode and rates, e.g. "LAWS1225 - Rate: 95/100"
                String courseCode = course.substring(0,8); //get clicked item CourseCode, e.g. "LAWS1225"
                if (MainActivity.lawMap.containsKey(courseCode)){ //Use HashMap function to get the clicked course corresponding position of its original list
                    coursePos = MainActivity.lawMap.get(courseCode);
                }

                //after get the position in its original list, then get relative corresponding information about this course
                String college = MainActivity.res.get(coursePos).getCollege();
                String career = MainActivity.res.get(coursePos).getCareer();
                String rate = MainActivity.res.get(coursePos).getRate();
                String tuition = MainActivity.res.get(coursePos).getTuitionFee();

                intent.putExtra("Course",courseCode);
                intent.putExtra("College", college);
                intent.putExtra("Career", career);
                intent.putExtra("Rate", rate);
                intent.putExtra("Tuition", tuition);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                lawAdapter.getFilter().filter(s);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}