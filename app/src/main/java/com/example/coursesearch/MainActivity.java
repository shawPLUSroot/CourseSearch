/**
 * @author Luqiao Dai
 * multiple buttons implements based on Youtube video
 * Reference: https://www.youtube.com/watch?v=GtxVILjLcw8&feature=share
 */
package com.example.coursesearch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etInput;
    Button instruction;
    Button btnSearch;
    Button btnCBE;
    Button btnCECS;
    Button btnLaw;
    Button btnCASS;

    static ArrayList<Course> res = new ArrayList<>();
    ArrayList<String> cbe = new ArrayList<>();
    ArrayList<String> cecs = new ArrayList<>();
    ArrayList<String> law = new ArrayList<>();
    ArrayList<String> cass = new ArrayList<>();

    static HashMap<String, Integer> cbeMap = new HashMap<>();
    static HashMap<String, Integer> cecsMap = new HashMap<>();
    static HashMap<String, Integer> lawMap = new HashMap<>();
    static HashMap<String, Integer> cassMap = new HashMap<>();

    /**
     * Given a input string like "comp", it will return a ArrayList<Course> of all comp courses. Each Course type in this
     * arrayList contains CourseCode, college that this course belongs to, career(i.e Undergraduate/ Postgraduate), rate that
     * is evaluated by students, and course's tuition fee. For instance, res = [Course COMP1597 College of Engineering and Computer
     * Science Postgraduate 5760 98, Course COMP1202 ...].
     * @param inputSt a string of input that users type in the EditText
     * @return a ArrayList<Course> that contains all relative information about the input string
     */
    public ArrayList<Course> getRes (String inputSt){

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
            res = new Parser(mathTokenizer,c).parseQuery();

            //if a query is invalid, change the invalid query to a valid query
            if(res.get(0).college.college.equals("invaild_query")){
                /* change(getInputText) */
                inputSt = QueryTokenizer.changeTerms(inputSt);
                mathTokenizer = new QueryTokenizer(inputSt);
                res = new Parser(mathTokenizer,c).parseQuery();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(res.size()>0){ // if res is not empty, start to rank our res ArrayList
            inputSt=inputSt.toLowerCase();
            QueryTokenizer tokenizer = new QueryTokenizer(inputSt);
            System.out.println("query = "+inputSt);
            System.out.println("Log.codeLogMap.size() = "+Log.codeLogMap.size());
            if(tokenizer.currentToken.value.equals("rate")){
                res = SearchActivity.rankWithRate(res);
            }else if(tokenizer.currentToken.value.equals("college")){
                if(Log.careerLogMap.size()!=0) res = SearchActivity.rankWithCareer(res,Log.careerLogMap);
            }else if(tokenizer.currentToken.value.equals("career")){
                if(Log.careerLogMap.size()!=0) res = SearchActivity.rankWithCollege(res,Log.collegeLogMap);
            }
            else if(tokenizer.currentToken.value.charAt(0)=='t'){
                res = SearchActivity.rankWithTuition(res);
            }
            else {res = SearchActivity.rankWithRate(res);}
            if(Log.codeLogMap.size()!=0)res = SearchActivity.rankWithCode(res,Log.codeLogMap);

        }
        return res;
    }

    public ArrayList<String> getCbe(){ //get courses information of CBE College, and rank these CBE courses with rate
        String cbeStr = "college=cbe";
        ArrayList<Course> cbeCourses = getRes(cbeStr);
        cbeCourses = SearchActivity.rankWithRate(cbeCourses);
        for(Course c:cbeCourses){
            cbe.add(c.getCode()+" - Rate: "+c.getRate()+"/100");
        }
        return cbe;
    }

    public ArrayList<String> getCecs(){ //get courses information of CECS College, and rank these CECS courses with rate
        String cecsStr = "college=cecs";
        ArrayList<Course> cecsCourses = getRes(cecsStr);
        cecsCourses = SearchActivity.rankWithRate(cecsCourses);
        for(Course c:cecsCourses){
            cecs.add(c.getCode()+" - Rate: "+c.getRate()+"/100");
        }
        return cecs;
    }

    public ArrayList<String> getLaw(){ //get courses information of Law College, and rank these LAW courses with rate
        String lawStr = "college=col";
        ArrayList<Course> lawCourses = getRes(lawStr);
        lawCourses = SearchActivity.rankWithRate(lawCourses);
        for(Course c:lawCourses){
            law.add(c.getCode()+" - Rate: "+c.getRate()+"/100");
        }
        return law;
    }

    public ArrayList<String> getCass(){ //get courses information of CASS College, and rank these cass courses with rate
        String cassStr = "college=cass";
        ArrayList<Course> cassCourses = getRes(cassStr);
        cassCourses = SearchActivity.rankWithRate(cassCourses);
        for(Course c:cassCourses){
            cass.add(c.getCode()+" - Rate: "+c.getRate()+"/100");
        }
        return cass;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instruction = findViewById(R.id.instruction);
        etInput = findViewById(R.id.etInput);
        btnSearch = findViewById(R.id.btnSearch);
        btnCBE = findViewById(R.id.btnCBE);
        btnCECS = findViewById(R.id.btnCECS);
        btnLaw = findViewById(R.id.btnLAW);
        btnCASS = findViewById(R.id.btnCASS);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String msg = intent.getStringExtra("UserName");
        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.tv_user);
        textView.setText(msg); // so that TextView to say: Welcome, username

        instruction.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnCBE.setOnClickListener(this);
        btnCECS.setOnClickListener(this);
        btnLaw.setOnClickListener(this);
        btnCASS.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.instruction: //when click the instruction button, it will give users a clear and briefly instruction of how to search a course
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Instructions on how to search");
                builder.setMessage("We can handle both valid and invalid queries.\n" +
                        "Your input format should be as this: cass;rate>10;tuitionFee = 5700");
                builder.setPositiveButton("I know", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
                break;
            case R.id.btnSearch: //when users click the search button, they will get the list of outputs
                String input = etInput.getText().toString();
                Log.recordInLog(input);
                Log.printAll();
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("Input", input);
                startActivity(intent);
                break;
            case R.id.btnCBE: //when users click the CBE button, it will bring them into a list of all CBE courses with rates interface
                getCbe();

                for (int j=0; j<res.size(); j++){ // Map course Code with its position in the original list
                    cbeMap.put(res.get(j).getCode(),j);
                }

                Intent cbeListShow = new Intent(MainActivity.this, CbeActivity.class);
                cbeListShow.putStringArrayListExtra("CBEList", cbe);
                startActivity(cbeListShow);
                break;
            case R.id.btnCECS: //when users click the CECS button, it will bring them into a list of all CECS courses with rates interface
                getCecs();

                for (int k=0; k<res.size(); k++){
                    cecsMap.put(res.get(k).getCode(),k);
                }

                Intent cecsListShow = new Intent(MainActivity.this, CecsActivity.class);
                cecsListShow.putStringArrayListExtra("CECSList", cecs);
                startActivity(cecsListShow);
                break;
            case R.id.btnLAW: //when users click the COL button, it will bring them into a list of all LAW courses with rates interface
                getLaw();

                for (int m=0; m<res.size(); m++){
                    lawMap.put(res.get(m).getCode(),m);
                }

                Intent lawListShow = new Intent(MainActivity.this, LawActivity.class);
                lawListShow.putStringArrayListExtra("LAWList", law);
                startActivity(lawListShow);
                break;
            case R.id.btnCASS: //when users click the CASS button, it will bring them into a list of all CASS courses with rates interface
                getCass();

                for (int n=0; n<res.size(); n++){
                    cassMap.put(res.get(n).getCode(),n);
                }

                Intent cassListShow = new Intent(MainActivity.this, CassActivity.class);
                cassListShow.putStringArrayListExtra("CASSList", cass);
                startActivity(cassListShow);
                break;
        }
    }
}