/**
 * @author: Luqiao Dai & Xueting Ren & Ruonan Zhang
 */
package com.example.coursesearch;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    EditText newInput;
    Button btnGo;
    ListView listView;
    Parser res ;
    ArrayAdapter<String> aa;
    ArrayList<Course> resultArray;

    public ArrayList<Course> getRes (String inputSt){
        inputSt = inputSt.toUpperCase();
        QueryTokenizer mathTokenizer = new QueryTokenizer(inputSt);
        try {
            InputStream initialStream  = getAssets().open("courses.xml");
            LinkedList<Course> c = RetrieveData.loadFromXMLFile(initialStream);
            InputStream s2  = getAssets().open("courses.bin");
            LinkedList<Course> c2 = RetrieveData.loadFromBespokeFile(s2);
            InputStream s3 = getAssets().open("courses.json");
            LinkedList<Course> c3 = RetrieveData.loadFromJSONFile(s3);
            c.addAll(c2);
            c.addAll(c3);
            res = new Parser(mathTokenizer,c);
            resultArray = res.parseQuery();

            //if the query is invalid
            if(resultArray.get(0).college.college.equals("invaild_query")){
                /* change(getInputText) */
                inputSt = QueryTokenizer.changeTerms(inputSt);
                mathTokenizer = new QueryTokenizer(inputSt);
                resultArray = new Parser(mathTokenizer,c).parseQuery();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(resultArray.size()>0){
            inputSt=inputSt.toLowerCase();
            QueryTokenizer tokenizer = new QueryTokenizer(inputSt);
            System.out.println("query = "+inputSt);
            System.out.println("Log.codeLogMap.size() = "+Log.codeLogMap.size());
            if(tokenizer.currentToken.value.equals("rate")){
                resultArray = rankWithRate(resultArray);
            }else if(tokenizer.currentToken.value.equals("college")){
                if(Log.careerLogMap.size()!=0) resultArray = rankWithCareer(resultArray,Log.careerLogMap);
            }else if(tokenizer.currentToken.value.equals("career")){
                if(Log.careerLogMap.size()!=0) resultArray = rankWithCollege(resultArray,Log.collegeLogMap);
            }
            else if(tokenizer.currentToken.value.charAt(0)=='t'){
                resultArray = rankWithTuition(resultArray);
            }
            else {resultArray = rankWithRate(resultArray);}
            if(Log.codeLogMap.size()!=0)resultArray = rankWithCode(resultArray,Log.codeLogMap);

        }
        return resultArray;
    }

    public static ArrayList<Course> rankWithCollege(ArrayList<Course> resultArray, HashMap<String, Integer> collegeLogMap) {
        List<Map.Entry<String,Integer>> careerLog = new ArrayList<>(collegeLogMap.entrySet());
        Comparator<Map.Entry<String,Integer>> timesComparator = new Comparator<Map.Entry<String,Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> stringIntegerEntry, Map.Entry<String, Integer> t1) {
                return stringIntegerEntry.getValue()-t1.getValue();
            }
        };
        Collections.sort(careerLog,timesComparator);
        resultArray = rankCollege(resultArray, careerLog.get(careerLog.size()-1).getKey());
        return  resultArray;
    }


    public static ArrayList<Course> rankWithTuition(ArrayList<Course> res) {
        Comparator<Course> tuitionFeeComparator = new Comparator<Course>() {
            @Override
            public int compare(Course course, Course t1) {
                return course.tuition_fee.tuitionfee-t1.tuition_fee.tuitionfee;
            }
        };
        Collections.sort(res,tuitionFeeComparator);
        return res;
    }

    public static ArrayList<Course> rankWithCareer(ArrayList<Course> res, HashMap<String,Integer> careerLogMap) {
        List<Map.Entry<String,Integer>> careerLog = new ArrayList<>(careerLogMap.entrySet());
        if(careerLog.size()==1){ res = rankCareer(res, careerLog.get(0).getKey());}
        if(careerLog.size()==2) {
            Comparator<Map.Entry<String,Integer>> timesComparator = new Comparator<Map.Entry<String,Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> stringIntegerEntry, Map.Entry<String, Integer> t1) {
                    return stringIntegerEntry.getValue()-t1.getValue();
                }
            };
            Collections.sort(careerLog,timesComparator);
            res = rankCareer(res, careerLog.get(1).getKey());
        }
        return  res;
    }

    public static ArrayList<Course> rankWithCode(ArrayList<Course> res, HashMap<String,Integer> codeLogMap) {
        List<Map.Entry<String,Integer>> codeLog = new ArrayList<>(codeLogMap.entrySet());
        Comparator<Map.Entry<String,Integer>> timesComparator = new Comparator<Map.Entry<String,Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> stringIntegerEntry, Map.Entry<String, Integer> t1) {
                return stringIntegerEntry.getValue()-t1.getValue();
            }
        };
        Collections.sort(codeLog,timesComparator);
        int swapPosition = 0;
        for (int i = codeLog.size()-1; i >= 0; i--) {
            int index= containCodeString(res,codeLog.get(i).getKey());
            if(index==-1)continue;
            Collections.swap(res,swapPosition,index);
            swapPosition++;
        }
        return res;
    }

    public static ArrayList<Course> rankWithRate(ArrayList<Course> resultArrayList) {
        Comparator<Course> rateComparator = new Comparator<Course>() {
            @Override
            public int compare(Course course, Course t1) {
                return t1.rate.rate-course.rate.rate;
            }
        };
        Collections.sort(resultArrayList,rateComparator);
        return resultArrayList;
    }

    public static int containCodeString(ArrayList<Course> courses ,String course){
        for (int i = 0; i < courses.size(); i++) {
            if(courses.get(i).code.code.equals(course)){
                return i;
            }
        }
        return -1;
    }
    public static ArrayList<Course> rankCareer(ArrayList<Course> courses,String string){
        ArrayList<Course> result = new ArrayList<>() ;
        ArrayList<Course> post = new ArrayList<>() ;
        ArrayList<Course> under = new ArrayList<>() ;
        for (Course cours : courses) {
            if(cours.career.career.equals("Postgraduate")){post.add(cours);}
            else under.add(cours);
        }
        if(string.equals("Postgraduate")){
            post = rankWithRate(post);
            under = rankWithRate(under);
            result.addAll(post);
            result.addAll(under);
        }else {//string.equals("under")
            post = rankWithRate(post);
            under = rankWithRate(under);
            result.addAll(under);
            result.addAll(post);
        }
        return result;
    }

    private static ArrayList<Course> rankCollege(ArrayList<Course> courses, String key) {
        ArrayList<Course> result = new ArrayList<>() ;
        ArrayList<Course> college = new ArrayList<>() ;
        for (Course cours : courses) {
            if(cours.college.college.equals(key)){college.add(cours);}
            else result.add(cours);
        }
        college = rankWithRate(college);
        result = rankWithRate(result);
        college.addAll(result);
        return college;
    }
    public ArrayList<String> getCourseCode(ArrayList<Course> courses){
        ArrayList<String> courseCode = new ArrayList<>();
        for(Course c:courses){
            courseCode.add(c.getCode()+" - Rate: "+c.getRate()+"/100");
        }
        return courseCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        newInput = findViewById(R.id.newInput);
        btnGo = findViewById(R.id.btnGo);
        listView = findViewById(R.id.lv_listView);

        Intent intent = getIntent();
        String receivedInputStr = intent.getStringExtra("Input"); //get the input string from the 'MainActivity'
        String cr = intent.getStringExtra("cr");
        String orignial = newInput.getText().toString(); //get original string (Initially it's empty "")

        // if input string is empty, show "No input query" msg
        if (orignial.equals("")&&receivedInputStr.equals("")){
            Toast.makeText(getApplicationContext(),"No input query",Toast.LENGTH_SHORT).show();
        }else {
            newInput.setText(receivedInputStr); //Every time users go into the searchActivity, set the EditText in 'SearchActivity'
            // same as the original EditText in the MainActivity
            getRes(receivedInputStr); //get the resultArray
            if (resultArray.size()==0){ //if the resultArray is empty, the searched courses does not exist
                Toast.makeText(getApplicationContext(),"This course does not exist.",Toast.LENGTH_SHORT).show();
            }
            search();
        }

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newStr = newInput.getText().toString(); //get the input string from EditText in SearchActivity
                Log.recordInLog(newStr);
                Log.printAll();

                if (newStr.equals("")) {
                    Toast.makeText(getApplicationContext(),"No input query",Toast.LENGTH_SHORT).show();
                }else {
                    getRes(newStr);
                    //If the new output is empty, then says this course doesn't exist.
                    if (resultArray.size()==0) Toast.makeText(getApplicationContext(),"This course does not exist.",Toast.LENGTH_SHORT).show();
                    search();
                }

            }
        });
    }

    public void search(){

        ArrayList<String> courses = getCourseCode(resultArray);

        aa = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_list_item_1,courses); // to link the Array you created and ListView
        listView.setAdapter(aa); //Set adapter on list view
        aa.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent1 = new Intent(SearchActivity.this, FullInfoActivity.class);
                Course c = resultArray.get(position);
                String course = c.getCode();
                String college = c.getCollege();
                String career = c.getCareer();
                String rate = c.getRate();
                String tuition = c.getTuitionFee();

                intent1.putExtra("Course",course);
                intent1.putExtra("College", college);
                intent1.putExtra("Career", career);
                intent1.putExtra("Rate", rate);
                intent1.putExtra("Tuition", tuition);
                startActivity(intent1);

            }
        });
    }
}
