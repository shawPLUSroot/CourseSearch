package com.example.coursesearch;
/**
 * @author Xueting Ren
 */
import org.junit.Test;

import org.junit.Assert;
import org.junit.Before;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class ParserTest {
    String file = "src/main/assets/courseTest.xml";
    String jsonfile = "src/main/assets/courseTest.json";
    String binfile = "src/main/assets/courseTest.bin";
    ArrayList<Course> res;
    Course c0,c1,c2,c3,c4,c5,c6,c7,c8,c9;
    @Before
    public void setUp() throws Exception {
        c0 = new Course("ENGN1000","College of Engineering and Computer Science","Undergraduate",5760,43);
        c1 = new Course("COMP1001","College of Engineering and Computer Science","Undergraduate",5760,1);
        c2 =new Course("MATH1002","College of Arts and Social Sciences","Undergraduate",5400,37);
        c3 =new Course("COMP1003","College of Engineering and Computer Science","Undergraduate",5760,2);
        c4=new Course("ENGN1004","College of Engineering and Computer Science","Postgraduate",5760,5);
        c5=new Course("MGMT1005","College of Business and Economics","Undergraduate",5700,73);
        c6=new Course("BIOL1006","College of Arts and Social Sciences","Undergraduate",5400,68);
        c7=new Course("COMP1007","College of Engineering and Computer Science","Undergraduate",5760,0);
        c8=new Course("BIOL1008","College of Arts and Social Sciences","Postgraduate",5400,93);
        c9=new Course("ENGN1009","College of Engineering and Computer Science","Undergraduate",5760,99);
    }

    @Test(timeout=1000)
    public void testSingleQueryForCollege() throws Exception {
        QueryTokenizer mathTokenizer = new QueryTokenizer("college=cass");
        LinkedList<Course> c = RetrieveData.loadFromXMLFile(file);
        res = new Parser(mathTokenizer,c).parseQuery();
        res.sort(Course::compareTo);
        ArrayList<Course> trueRes = new ArrayList<>();
        trueRes.add(c2);
        trueRes.add(c6);
        trueRes.add(c8);
        trueRes.sort(Course::compareTo);
        Assert.assertArrayEquals(res.toArray(),trueRes.toArray());
    }

    @Test(timeout=1000)
    public void testSingleQueryForRate() throws Exception {
        QueryTokenizer mathTokenizer = new QueryTokenizer("rate=2");
        LinkedList<Course> c = RetrieveData.loadFromXMLFile(file);

        res = new Parser(mathTokenizer,c).parseQuery();
        res.sort(Course::compareTo);
        ArrayList<Course> trueRes = new ArrayList<>();
        trueRes.add(c3);
        trueRes.sort(Course::compareTo);
        Assert.assertArrayEquals(res.toArray(),trueRes.toArray());
    }
    @Test(timeout=1000)
    public void testSingleQueryForTuitionFee() throws Exception {
        QueryTokenizer mathTokenizer = new QueryTokenizer("tuitionFee=5760");
        LinkedList<Course> c = RetrieveData.loadFromXMLFile(file);
        res = new Parser(mathTokenizer,c).parseQuery();
        res.sort(Course::compareTo);
        ArrayList<Course> trueRes = new ArrayList<>();
        trueRes.add(c0);
        trueRes.add(c1);
        trueRes.add(c3);
        trueRes.add(c4);
        trueRes.add(c7);
        trueRes.add(c9);
        trueRes.sort(Course::compareTo);
        Assert.assertArrayEquals(res.toArray(),trueRes.toArray());
    }
    @Test(timeout=1000)
    public void testSingleQueryForCareer() throws Exception {
        QueryTokenizer mathTokenizer = new QueryTokenizer("career=Postgraduate");
        LinkedList<Course> c = RetrieveData.loadFromXMLFile(file);
        res = new Parser(mathTokenizer,c).parseQuery();
        res.sort(Course::compareTo);
        ArrayList<Course> trueRes = new ArrayList<>();
        trueRes.add(c4);
        trueRes.add(c8);
        trueRes.sort(Course::compareTo);
        Assert.assertArrayEquals(res.toArray(),trueRes.toArray());
    }

    @Test(timeout=1000)
    public void testSingleQueryForCodePre() throws Exception {
        QueryTokenizer mathTokenizer = new QueryTokenizer("BioL");
        LinkedList<Course> c = RetrieveData.loadFromJSONFile(jsonfile);
        res = new Parser(mathTokenizer,c).parseQuery(); res.sort(Course::compareTo);
        ArrayList<Course> temp = new ArrayList<>();
        assertEquals(res,temp);
    }

    @Test(timeout=1000)
    public void testSingleQueryForCode() throws Exception {
        QueryTokenizer mathTokenizer = new QueryTokenizer("COMP1011");
        LinkedList<Course> c = RetrieveData.loadFromBespokeFile(binfile);
        res = new Parser(mathTokenizer,c).parseQuery(); res.sort(Course::compareTo);
        ArrayList<Course> trueRes = new ArrayList<>();
        assertEquals(res,trueRes);
    }

    @Test(timeout=1000)
    public void testMultipleQuery() throws Exception {
        QueryTokenizer mathTokenizer = new QueryTokenizer("college=cass;rate>50");
        LinkedList<Course> c = RetrieveData.loadFromXMLFile(file);
        res = new Parser(mathTokenizer,c).parseQuery();
        res.sort(Course::compareTo);
        ArrayList<Course> trueRes = new ArrayList<>();
        trueRes.add(c6);
        trueRes.add(c8);
        trueRes.sort(Course::compareTo);
        Assert.assertArrayEquals(res.toArray(),trueRes.toArray());
    }

    @Test(timeout=1000)
    public void testPartiallyvaild() throws Exception {
        QueryTokenizer mathTokenizer = new QueryTokenizer(" sdfgh;rate>70fgh;colleg=cbe;college=cbee;tuitionfee>5760;rate<5;tuitionfee<40;rate:50;career=up;career<70 ");
        LinkedList<Course> c = RetrieveData.loadFromXMLFile(file);
        res = new Parser(mathTokenizer,c).parseQuery();
        res.sort(Course::compareTo);
        ArrayList<Course> trueRes = new ArrayList<>();
        Assert.assertArrayEquals(res.toArray(),trueRes.toArray());
    }
    @Test(timeout=1000)
    public void testInvaild() throws Exception {
        String getInputText="93 rate;colle:cas;carer=postgradu;5200<tuitionfee";
        QueryTokenizer mathTokenizer = new QueryTokenizer(getInputText);
        LinkedList<Course> c = RetrieveData.loadFromXMLFile(file);
        res = new Parser(mathTokenizer,c).parseQuery();
        if(res.get(0).college.college.equals("invaild_query")){
            getInputText = QueryTokenizer.changeTerms(getInputText);
            mathTokenizer = new QueryTokenizer(getInputText);
            res = new Parser(mathTokenizer,c).parseQuery();
        }
        res.sort(Course::compareTo);
        ArrayList<Course> trueRes = new ArrayList<>();
        trueRes.add(c8);
        Assert.assertArrayEquals(res.toArray(),trueRes.toArray());
    }


}