/**
 * @author: Xueting Ren
 * Reference: https://www.journaldev.com/1198/java-sax-parser-example
 */
package com.example.coursesearch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.LinkedList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
public class RetrieveData {
    RBTree<Course> codeTree = new RBTree<>();
    RBTree<Pair<Integer>> rateTree = new RBTree<>();
    RBTree<Pair<Integer>> tuitionfeeTree = new RBTree<>();
    RBTree<Pair<String>> careerTree = new RBTree<>();
    RBTree<Pair<String>> collegeTree = new RBTree<>();
    RBTree<Pair<String>> codePreTree = new RBTree<>();

    public RetrieveData(LinkedList<Course> courseLinkedList) throws Exception {
        for(Course c:courseLinkedList){
            codeTree.insert(c);
            insertOtherTrees(c,rateTree,tuitionfeeTree,careerTree,collegeTree,codePreTree);
        }
    }

    public static LinkedList<Course> loadFromXMLFile(Object filename) throws Exception{
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        MyHandler handler = new MyHandler();
        if(filename instanceof String) saxParser.parse(new File((String)filename), handler);
        else saxParser.parse((InputStream) filename, handler);
        return handler.courseLinkedList;
    }

    public static LinkedList<Course> loadFromBespokeFile(Object filename) throws Exception {
        LinkedList<Course> courseLinkedList = new LinkedList<>();
        BufferedReader br;
        if(filename instanceof String) br = new BufferedReader(new FileReader((String) filename));
        else br = new BufferedReader(new InputStreamReader((InputStream) filename));
        String record;

        while((record = br.readLine()) != null)
        {
            String[] tokens = record.split("-");
            Course c = new Course(tokens[0],tokens[1],tokens[2],Integer.valueOf(tokens[3]),Integer.valueOf(tokens[4]));
            courseLinkedList.add(c);
        }

        return courseLinkedList;
    }

    public static LinkedList<Course> loadFromJSONFile(Object filename) throws Exception {
        Gson gson = new Gson();
        JsonReader jsonReader = null;

        final Type CUS_LIST_TYPE = TypeToken.getParameterized(LinkedList.class, Course.class).getType();
        //or TypeToken.getParameterized(ArrayList.class, PersonJSON.class).getType();

        try{
            if(filename instanceof String) jsonReader = new JsonReader(new FileReader((String) filename));
            else jsonReader = new JsonReader(new InputStreamReader((InputStream) filename));
        }catch (Exception e) {
            e.printStackTrace();
        }
        LinkedList<Course> courseList = gson.fromJson(jsonReader, CUS_LIST_TYPE);


        return courseList;
    }


    public static void insertOtherTrees(Course c,RBTree<Pair<Integer>> rateTree,RBTree<Pair<Integer>> tuitionfeeTree,RBTree<Pair<String>> careerTree,RBTree<Pair<String>> collegeTree,RBTree<Pair<String>> codePreTree){
        Pair<String> p_college = new Pair<>();
        Pair<String>  p_career =new Pair<>();
        Pair<String> p_code_pre = new Pair<>();
        Pair<Integer>  p_rate =new Pair<>();
        Pair<Integer>  p_tuition_fee =new Pair<>();
        p_code_pre.setKey(c.getCode().substring(0,4));
        p_code_pre.codes.add(c.getCode());
        Node<Pair<String>> codePrePair = codePreTree.search(p_code_pre);
        if(codePrePair != null){
            codePrePair.value.codes.add(c.getCode());
        }else{
            codePreTree.insert(p_code_pre);
        }
        p_college.setKey(c.getCollege());
        p_college.codes.add(c.getCode());
        Node<Pair<String>> collegePair = collegeTree.search(p_college);
        if(collegePair != null){
            collegePair.value.codes.add(c.getCode());
        }else{
            collegeTree.insert(p_college);
        }
        p_career.setKey(c.getCareer());
        p_career.codes.add(c.getCode());
        Node<Pair<String>> careerPair = careerTree.search(p_career);
        if(careerPair != null){
            careerPair.value.codes.add(c.getCode());
        }else{
            careerTree.insert(p_career);
        }
        p_rate.setKey(Integer.valueOf(c.getRate()));
        p_rate.codes.add(c.getCode());
        Node<Pair<Integer>> ratePair = rateTree.search(p_rate);
        if( ratePair != null){
            ratePair.value.codes.add(c.getCode());
        }else{
            rateTree.insert(p_rate);
        }

        p_tuition_fee.setKey(Integer.valueOf(c.getTuitionFee()));
        p_tuition_fee.codes.add(c.getCode());
        Node<Pair<Integer>> tuitionFeePair = tuitionfeeTree.search(p_tuition_fee);
        if( tuitionFeePair != null){
            tuitionFeePair.value.codes.add(c.getCode());
        }else{
            tuitionfeeTree.insert(p_tuition_fee);
        }

    }

}
