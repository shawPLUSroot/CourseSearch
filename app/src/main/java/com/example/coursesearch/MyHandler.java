/**
 * @author: Xueting Ren
 * Reference:
 * https://www.journaldev.com/1198/java-sax-parser-example
 * https://howtodoinjava.com/java/xml/sax-parser-read-xml-example/
 */
package com.example.coursesearch;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.LinkedList;

public class MyHandler extends DefaultHandler{
    LinkedList<Course> courseLinkedList = new LinkedList<Course>();
    Course c = null;

    StringBuilder sb = null;

    @Override
    public void startElement(String uri, String localName, String tagName,
                             Attributes attributes) throws SAXException {
        if ("course".equals(tagName)) {
            c = new Course();
        }

        sb = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String text = new String(ch, start, length);
        if (sb != null) {
            sb.append(text);
        }
    }


    /**
     * Parse the xml file with corresponding name of tag
     *
     * @param uri Inherit from DefaultHandler
     * @param localName Inherit from DefaultHandler
     * @param tagName Tag that we are interpreting
     */
    @Override
    public void endElement(String uri, String localName, String tagName) {
        if ("code".equals(tagName)) {
            String code = sb.toString();
            c.setCode(code);
        } else if ("college".equals(tagName)) {
            String college = sb.toString();
            c.setCollege(college);
        } else if ("career".equals(tagName)) {
            String career = sb.toString();
            c.setCareer(career);
        } else if ("rate".equals(tagName)) {
            int rate = Integer.valueOf(sb.toString());
            c.setRate(rate);
        } else if ("tuition_fee".equals(tagName)){
            int tuitionFee = Integer.valueOf(sb.toString());
            c.setTuition_fee(tuitionFee);
        } else if ("course".equals(tagName)) {
            courseLinkedList.add(c);

        }
        sb = null;
    }


}
