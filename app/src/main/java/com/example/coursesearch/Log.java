package com.example.coursesearch;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class Log extends AppCompatActivity {

    public static HashMap<String,Integer> codeLogMap = new HashMap<>();
    public static HashMap<String,Integer> careerLogMap= new HashMap<>();
    public static HashMap<String,Integer> collegeLogMap= new HashMap<>();
    public static HashMap<String,Integer> tuitionLogMap= new HashMap<>();
    public static HashMap<String,Integer> codePreLogMap = new HashMap<>();


    public static void recordInLog(String input){
        if(input.equals("")) return;
        String inputString = input.toLowerCase();
        CharSequence comp= "comp";
        CharSequence math= "math";
        CharSequence desa= "desa";
        CharSequence engn= "engn";
        CharSequence biol= "biol";
        CharSequence psyc= "psyc";
        CharSequence laws= "laws";
        CharSequence stat= "stat";
        CharSequence mgmt= "mgmt";
        CharSequence busn= "busn";
        CharSequence cecs= "cecs";
        CharSequence cass= "cass";
        CharSequence cbe= "cbe";
        CharSequence col= "col";
        CharSequence u = "undergraduate";
        CharSequence p = "postgraduate";
        if(inputString.contains(desa)){
            updateLog(desa);
        }if(inputString.contains(math)){
            updateLog(math);
        }
        if(inputString.contains(comp)){
            updateLog(comp);
        }if(inputString.contains(engn)){
            updateLog(engn);
        }if(inputString.contains(psyc)){
            updateLog(psyc);
        }if(inputString.contains(biol)){
            updateLog(biol);
        }if(inputString.contains(laws)){
            updateLog(laws);
        }if(inputString.contains(stat)){
            updateLog(stat);
        }if(inputString.contains(mgmt)){
            updateLog(mgmt);
        }if(inputString.contains(busn)){
            updateLog(busn);
        }if(inputString.contains(cecs)){
            cecs = "College of Engineering and Computer Science";
            updateCollegeLog(cecs);
        }if(inputString.contains(cass)){
            cass = "College of Arts and Social Sciences";
            updateCollegeLog(cass);
        }if(inputString.contains(cbe)){
            cbe = "College of Business and Economics";
            updateCollegeLog(cbe);
        }if(inputString.contains(col)){
            col = "College Of Law";
            updateCollegeLog(col);
        }if(inputString.contains(p)){
            updateCareerLog(p);
        }if(inputString.contains(u)){
            updateCareerLog(u);
        }
    }

    public static void updateCareerLog(CharSequence charSequence){
        String care = charSequence.toString();
        int countCareer= Log.careerLogMap.containsKey(care) ? Log.careerLogMap.get(care) : 0;
        Log.careerLogMap.put(care, countCareer+ 1);
    }


    public static void updateLog(CharSequence charSequence){
        String comp = charSequence.toString();
        int count = Log.codePreLogMap.containsKey(comp) ? Log.codePreLogMap.get(comp) : 0;
        Log.codePreLogMap.put(comp, count + 1);
    }

    public static void updateCollegeLog(CharSequence charSequence){
        String c = charSequence.toString();
        int countCollge = Log.collegeLogMap.containsKey(c) ? Log.collegeLogMap.get(c) : 0;
        Log.collegeLogMap.put(c, countCollge + 1);
    }

    public static void printAll() {
        System.out.println("-----------------------");
        System.out.println("-------Check Log--------");
        System.out.println(careerLogMap.toString());
        System.out.println(codeLogMap.toString());
        System.out.println(codePreLogMap.toString());
        System.out.println(tuitionLogMap.toString());
        System.out.println(collegeLogMap.toString());
        System.out.println("-----------------------");
    }
}
