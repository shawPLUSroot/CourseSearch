/**
 * @author Xueting Ren
 * Reference:
 * https://stackoverflow.com/questions/4898590/generating-xml-using-sax-and-java
 * http://zetcode.com/java/sax/
 */
package com.example.coursesearch;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;


public class GenerateData {
    /**
     * Generate XML file with given data
     * @param courses A List of data instances of Course objects
     */
    public static void generateXML(List<Course> courses){
        SAXTransformerFactory tff = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

        try {
            TransformerHandler handler = tff.newTransformerHandler();
            Transformer tr = handler.getTransformer();
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            File f = new File("src/main/java/com/example/coursesearch/courses.xml");
            if(!f.exists()){
                f.createNewFile();
            }
            Result result = new StreamResult(new FileOutputStream(f));
            handler.setResult(result);

            handler.startDocument();
            AttributesImpl attr = new AttributesImpl();
            handler.startElement("", "", "list", attr);
            attr.clear();

            for (Course course : courses) {
                attr.clear();
                handler.startElement("", "", "course", attr);

                attr.clear();
                handler.startElement("", "", "code", attr);
                handler.characters(course.getCode().toCharArray(), 0, course.getCode().length());
                handler.endElement("", "", "code");


                attr.clear();
                handler.startElement("", "", "rate", attr);
                handler.characters(course.getRate().toCharArray(), 0, course.getRate().length());
                handler.endElement("", "", "rate");

                attr.clear();
                handler.startElement("", "", "college", attr);
                handler.characters(course.getCollege().toCharArray(), 0, course.getCollege().length());
                handler.endElement("", "", "college");

                attr.clear();
                handler.startElement("", "", "career", attr);
                handler.characters(course.getCareer().toCharArray(), 0, course.getCareer().length());
                handler.endElement("", "", "career");

                attr.clear();
                handler.startElement("", "", "tuition_fee", attr);
                handler.characters(course.getTuitionFee().toCharArray(), 0, course.getTuitionFee().length());
                handler.endElement("", "", "tuition_fee");


                handler.endElement("", "", "course");
            }

            handler.endElement("", "", "list");
            handler.endDocument();
            System.out.println("courses.xml generated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to generate courses.xml");
        }

    }
    public static void saveToJSONFile(String file,List<Course> courseList) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try(FileWriter fw = new FileWriter(file)){
            gson.toJson(courseList, fw);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void saveToBespokeFile(String file,List<Course> courseList) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file, true)))
        {
            for(Course c:courseList)
            {

                String record = c.code.toString()+"-"+c.college.toString()+"-"+c.career+"-"+c.tuition_fee+"-"+c.rate.toString();

                bw.write(record);
                bw.newLine();
            }

        }catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Generate data with the data constraints for Course codes
     * @param startNum The start code of Course object
     * @param len The length of the generated list of data
     */
    public static List<Course> generateData(int startNum,int len){
        String[] courseType = new String[]{"COMP","MATH","DESA","ENGN","BIOL","PSYC","LAWS","STAT","MGMT","BUSN"};
        String[] collegeList = new String[]{"College Of Law","College of Engineering and Computer Science","College of Arts and Social Sciences","College of Business and Economics"};
        String[] careerList = new String[]{"Undergraduate","Postgraduate"};
        List<Course> courseList = new ArrayList<>();
        for(int i=startNum;i<startNum+len;i++){
            int index =(int)(Math.random()*(courseType.length-1));
            int rate,tuitionFee;
            String coursePre=courseType[index];
            index = (int) (Math.random()*1.2);
            String careerPre = careerList[index];
            String college;
            rate=(int)(Math.random()*100);
            if(coursePre.equals("COMP")|| coursePre.equals("ENGN")){
                college = collegeList[1];
                tuitionFee = 5760;
            }else if(coursePre.equals("BIOL") || coursePre.equals("PSYC")||coursePre.equals("DESA")||coursePre.equals("MATH")){
                college = collegeList[2];
                tuitionFee=5400;
            }else if(coursePre.equals("LAWS")){
                college = collegeList[0];
                tuitionFee=5200;
            }else{
                college = collegeList[3];
                tuitionFee=5700;
            }
            String code = coursePre+Integer.toString(i);
            Course generatedCourse = new Course(code,college,careerPre,tuitionFee,rate);
            System.out.println("new Course("+'"'+code+'"'+','+'"'+college+'"'+','+'"'+careerPre+'"'+','+tuitionFee+","+rate+')'+';');
            courseList.add(generatedCourse);
        }
        return courseList;
    }

}
