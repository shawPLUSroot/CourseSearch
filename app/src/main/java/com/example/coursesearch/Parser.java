/**
 * @author Ruonan Zhang
 */
package com.example.coursesearch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Parser {

    QueryTokenizer queryTokenizer;
    LinkedList<Course> file;
    RetrieveData r ;
    /**
     * initialize rb trees to store data
     */
    static RBTree<Course> codeTree;
    static RBTree<Pair<Integer>> rateTree;
    static RBTree<Pair<Integer>> tuitionfeeTree;
    static RBTree<Pair<String>> careerTree;
    static RBTree<Pair<String>> collegeTree;
    static RBTree<Pair<String>> codePreTree;
    /**
     * constructor
     */
    public Parser(QueryTokenizer queryTokenizer, LinkedList<Course> file) throws Exception {
        this.queryTokenizer = queryTokenizer;this.file = file;
        this.r = new RetrieveData(file);
        collegeTree = r.collegeTree;
        codeTree = r.codeTree;
        tuitionfeeTree = r.tuitionfeeTree;
        careerTree = r.careerTree;
        rateTree = r.rateTree;
        codePreTree = r.codePreTree;
    }
    /**
     * check if a course code in an input query is valid("comp1100" or "comp")
     */
    public static boolean isVaildCode(String code){
        String[] courseType = new String[]{"COMP","MATH","DESA","ENGN","BIOL","PSYC","LAWS","STAT","MGMT","BUSN"};
        List<String> list = Arrays.asList(courseType);
        if(code.length()==8 && list.contains(code.substring(0,4).toUpperCase())){
            for (int i = 4; i < code.length(); i++) {
                if(!Character.isDigit(code.charAt(i))) return false;
            }
            return true;
        }
        return code.length()==4 && list.contains(code.toUpperCase());
    }
    /**
     *check if a term is invalid
     */
    public static boolean isInvaild(ArrayList<Course> condition){
        boolean lengthIsOne = condition.size()==1;
        if(!lengthIsOne) return false;
        boolean invaild = condition.get(0).college.college.equals("invaild_query");
        return invaild;

    }
    /**
     *get join of lists of two terms
     */
    public static ArrayList<Course> addCondition(ArrayList<Course> firstCondition,ArrayList<Course> secondCondition){
        if(isInvaild(firstCondition)){
            return secondCondition;
        }
        if(isInvaild(secondCondition)){
            return firstCondition;
        }
        else {
        ArrayList<Course> inCommon = new ArrayList<>();
        for (int i = 0; i < secondCondition.size(); i++) {
            Course course = secondCondition.get(i);
            if(firstCondition.contains(course)){inCommon.add(course);}
        }
        return inCommon;
    }
    }
    /**
     * Grammar
     * S::= T | T; | T ; S
     */
    public ArrayList<Course> parseQuery() throws Exception{
        ArrayList<Course> T = parseT();
        if(queryTokenizer.currentToken==null)
            return T;
        if(queryTokenizer.currentToken.value==";") {
            queryTokenizer.next();
            if(queryTokenizer.currentToken==null)return T;
            ArrayList<Course> courses = parseQuery();
            return addCondition(T, courses);
        }
        else return T;
    }
    /**
     * Grammar
     * T → courseName | C=M | U<N | U>N | U=N | P | H
     */
    public ArrayList<Course> parseT() throws Exception{
        // check the token in query is a valid word
        boolean notCollege = !queryTokenizer.currentToken.value.toLowerCase().equals("college");
        boolean notCareer = !queryTokenizer.currentToken.value.toLowerCase().equals("career");
        boolean notRate = !queryTokenizer.currentToken.value.toLowerCase().equals("rate");
        boolean notCourseName = queryTokenizer.currentToken.value.length()!=8;
        boolean notCoursePre = queryTokenizer.currentToken.value.length()!=4;
        boolean notTuitionFee = queryTokenizer.currentToken.value.toLowerCase().charAt(0)!='t';
        ArrayList<Course> special = new ArrayList<>();
        Course c0 = new Course("invaild_query","invaild_query","invaild_query",5700,43);
        special.add(c0);
        if(notCollege && notCareer && notRate && notCourseName && notCoursePre && notTuitionFee){
            while (!queryTokenizer.currentToken.value.equals(";"))
            {
                queryTokenizer.next();
                if(queryTokenizer.currentToken==null){return special;}
            }
            return special;}
        if(queryTokenizer.currentToken.value.toLowerCase().equals("college")){
            queryTokenizer.next();
            if(!queryTokenizer.currentToken.value.equals("=")){
               while (!queryTokenizer.currentToken.value.equals(";"))
                {queryTokenizer.next();
                    if(queryTokenizer.currentToken==null){return special;}
                }
                return special;
            }
            queryTokenizer.next();
            String college = queryTokenizer.currentToken.value;
            // check the token in query is a valid college
            boolean notcbe = !college.toLowerCase().equals("cbe");
            boolean notcass = !college.toLowerCase().equals("cass");
            boolean notcol = !college.toLowerCase().equals("col");
            boolean notcecs = !college.toLowerCase().equals("cecs");
           if(notcbe&&notcass&&notcecs&&notcol){
                while (!queryTokenizer.currentToken.value.equals(";"))
                {queryTokenizer.next();
                    if(queryTokenizer.currentToken==null){return special;}
                }
                return special;
            }
            ArrayList<Course> courses = findCollege(college);
            queryTokenizer.next();
            return courses;

        }else if(queryTokenizer.currentToken.value.toLowerCase().equals("career")){
            queryTokenizer.next();
            if(!queryTokenizer.currentToken.value.equals("=")){
               while (!queryTokenizer.currentToken.value.equals(";"))
                {queryTokenizer.next();
                    if(queryTokenizer.currentToken==null){return special;}
                }
                return special;
            }
            queryTokenizer.next();
            String career = queryTokenizer.currentToken.value;
            boolean notUnder = !career.toLowerCase().equals("undergraduate")&&  !career.toLowerCase().equals("ug");
            boolean notPost = !career.toLowerCase().equals("postgraduate")&& !career.toLowerCase().equals("pg");
            if(notUnder && notPost  ){
                while (!queryTokenizer.currentToken.value.equals(";"))
                {queryTokenizer.next();
                    if(queryTokenizer.currentToken==null){return special;}
                }
                return special;}
            if(!notPost){career = "Postgraduate";}
            if(!notUnder){career = "Undergraduate";}
            ArrayList<Course> courses = findCareer(career);
            queryTokenizer.next();
            return courses;
        }else if(!queryTokenizer.currentToken.value.toLowerCase().equals("rate")&&(queryTokenizer.currentToken.value.length()==4||queryTokenizer.currentToken.value.length()==8)){
            String code =  queryTokenizer.currentToken.value;
            ArrayList<Course> courses = findCode(code);
            queryTokenizer.next();
            return courses;
        }
        else {
            String U = parseU();
            boolean greater1 = queryTokenizer.currentToken.type.equals(Token.Type.tok_greater);
            boolean less1 = queryTokenizer.currentToken.type.equals(Token.Type.tok_lower);
            boolean equal1 = queryTokenizer.currentToken.type.equals(Token.Type.tok_equal);
            if(!greater1 && !less1 && !equal1  ){
                while (!queryTokenizer.currentToken.value.equals(";"))
                {queryTokenizer.next();
                    if(queryTokenizer.currentToken==null){return special;}
                }
                return special;}

            if (queryTokenizer.currentToken.type.equals(Token.Type.tok_greater)) {
                queryTokenizer.next();
                if(!queryTokenizer.currentToken.type.equals(Token.Type.tok_int)){
                    while (!queryTokenizer.currentToken.value.equals(";"))
                    {queryTokenizer.next();
                        if(queryTokenizer.currentToken==null){return special;}
                    }
                    return special;}

                int num = parseN();
                ArrayList<Course> greater ;
                if(U.charAt(0)=='t'||U.charAt(0)=='T'){
                    greater = findTuitionGreater(num);}
                else {
                    greater = findRateGreater(num);
                }

                return greater;
            } else
            if (queryTokenizer.currentToken.type.equals(Token.Type.tok_lower)) {
                queryTokenizer.next();
                if(!queryTokenizer.currentToken.type.equals(Token.Type.tok_int)){
                    while (!queryTokenizer.currentToken.value.equals(";"))
                    {queryTokenizer.next();
                        if(queryTokenizer.currentToken==null){return special;}
                    }
                    return special;}
                int num = parseN();
                ArrayList<Course> lower ;
                if(U.charAt(0)=='t'|| U.charAt(0)=='T'){
                    lower = findTuitionLower(num);}
                else {
                    lower = findRateLower(num);
                }
                return lower;
            } else {
                queryTokenizer.next();
                if(!queryTokenizer.currentToken.type.equals(Token.Type.tok_int)){
                    while (!queryTokenizer.currentToken.value.equals(";"))
                    {queryTokenizer.next();
                        if(queryTokenizer.currentToken==null){return special;}
                    }
                    return special;}
                int num = parseN();
                ArrayList<Course> equal ;
                if(U.charAt(0)=='t'|| U.charAt(0)=='T'){
                    equal = findTuitionEqual(num);}
                else {
                    equal = findRateEqual(num);
                }
                return equal;
            }
        }
    }

    private ArrayList<Course> findCode(String code) throws Exception{
        ArrayList<Course> special = new ArrayList<>();
        ArrayList<Course> result = new ArrayList<>();
        Course c0 = new Course("invaild_query","invaild_query","invaild_query",5700,43);
        special.add(c0);
        if(!isVaildCode(code))return special;
        if(code.length()==4) {//"COMP"
            // getCodePreTree(file);
            Pair<String> codes = new Pair<>(code.toUpperCase(), new HashSet<String>());
            Node<Pair<String>> temp = codePreTree.search(codes);
            if (temp == null) return result;
            HashSet<String> res = temp.value.codes;
            for (String r : res) {
                Course c = new Course();
                c.setCode(r);
                result.add(codeTree.search(c).value);
            }
            return result;
        }else{//"COMP1100"
            //  getCodePreTree(file);
            Pair<String> codes = new Pair<>(code.substring(0,4).toUpperCase(), new HashSet<String>());
            Node<Pair<String>> temp = codePreTree.search(codes);
            if (temp == null) return result;
            HashSet<String> res = temp.value.codes;
            for (String r : res) {
                if(r.equals(code)){
                    Course c = new Course();
                    c.setCode(r);
                    result.add(codeTree.search(c).value);}
            }
            return result;
        }
    }
    private ArrayList<Course> findCareer(String career) throws Exception{
        ArrayList<Course> result = new ArrayList<>();
        // getCareerTree(file);
        Pair<String> careers = new Pair<>(career,new HashSet<String>());
        HashSet<String> res = careerTree.search(careers).value.codes;
        for (String r : res) {
            Course c = new Course();
            c. setCode(r);
            result.add(codeTree.search(c).value);
        }

        return result;
    }
    private ArrayList<Course> findCollege(String college) throws Exception{
        ArrayList<Course> result = new ArrayList<>();
        // getCollegeTree(file);
        if(college.toLowerCase().equals("cbe")){college = "College of Business and Economics";}
        if (college.toLowerCase().equals("cecs")){college = "College of Engineering and Computer Science";}
        if (college.toLowerCase().equals("cass")){college ="College of Arts and Social Sciences";}
        if (college.toLowerCase().equals("col")){college ="College Of Law";}
        Pair<String> careers = new Pair<>(college,new HashSet<String>());
        HashSet<String> res = collegeTree.search(careers).value.codes;
        for (String r : res) {
            Course c = new Course();
            c.setCode(r);
            result.add(codeTree.search(c).value);
        }
        return result;
    }

    private ArrayList<Course> findTuitionGreater(int num) throws Exception{
        ArrayList<Course> result = new ArrayList<>();
        //  gettuitionTree(file);
        HashSet<String> res = tuitionfeeTree.searchGreater(num);
        for (String r : res) {
            Course c = new Course();
            c. setCode(r);
            result.add(codeTree.search(c).value);
        }
        return result;
    }
    private ArrayList<Course> findRateGreater(int num) throws Exception{
        ArrayList<Course> result = new ArrayList<>();
        //  getRateTree(file);

        HashSet<String> res = rateTree.searchGreater(num);
        for (String r : res) {
            Course c = new Course();
            c. setCode(r);
            result.add(codeTree.search(c).value);
        }
        return result;
    }
    private ArrayList<Course> findTuitionLower(int num) throws Exception{
        ArrayList<Course> result = new ArrayList<>();
        //  gettuitionTree(file);
        HashSet<String> res = tuitionfeeTree.searchLower(num);
        for (String r : res) {
            Course c = new Course();
            c. setCode(r);
            result.add(codeTree.search(c).value);
        }
        return result;
    }
    private ArrayList<Course> findTuitionEqual(int num) throws Exception {
        ArrayList<Course> result = new ArrayList<>();
        Pair<Integer> careers = new Pair<>();
        careers.setKey(num);
        Node<Pair<Integer>> temp = tuitionfeeTree.search(careers);
        if (temp == null) return result;
        HashSet<String> res = temp.value.codes;
         for (String r : res) {
            Course c = new Course();
            c. setCode(r);
            result.add(codeTree.search(c).value);
        }
        return result;
    }

    private ArrayList<Course> findRateEqual(int num) throws Exception{
        ArrayList<Course> result = new ArrayList<>();
        //  getRateTree(file);
        Pair<Integer> careers = new Pair<>(num,new HashSet<String>());
        Node<Pair<Integer>> temp = rateTree.search(careers);
        if (temp == null) return result;
        HashSet<String> res = temp.value.codes;
        for (String r : res) {
            Course c = new Course();
            c. setCode(r);
            result.add(codeTree.search(c).value);
        }
        return result;
    }
    private ArrayList<Course> findRateLower(int num) throws Exception{
        ArrayList<Course> result = new ArrayList<>();
        //   getRateTree(file);
        HashSet<String> res = rateTree.searchLower(num);
        for (String r : res) {
            Course c = new Course();
            c. setCode(r);
            result.add(codeTree.search(c).value);
        }
        return result;
    }

    public int parseN() {
        int num = Integer.parseInt(queryTokenizer.currentToken.value);
        queryTokenizer.next();
        return num;
    }
    private String parseU() {
        String U = queryTokenizer.currentToken.value;
        queryTokenizer.next();
        return U;
    }


}
