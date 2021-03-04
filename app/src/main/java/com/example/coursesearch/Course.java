/**
 * @author Xueting Ren
 * @author Ruonan Zhang
 */
package com.example.coursesearch;
public class Course implements Comparable<Course> {
    public College college;
    public Career career;
    public Code code;
    public TuitionFee tuition_fee;
    public Rate rate;
    /**
     * Initialize Course with corresponding data
     */
    public Course(String code,String college,String career,int tuition_fee,int rate){
        this.code = new Code(code);
        this.college=new College(college);
        this.career=new Career(career);
        this.tuition_fee= new TuitionFee(tuition_fee);
        this.rate=new Rate(rate);
    }
    /**
     * Initialize empty Course
     */
    public Course(){
    }


    @Override
    public String toString(){
        return "Course "+this.code.toString()+" "+college.toString()+" "+career.toString()+" "+tuition_fee.toString()+" "+rate.toString();
    }

    /**
     * Set the code field for the constructor of course
     * @param code
     */
    public void setCode(String code){
        this.code=new Code(code);
    }
    /**
     * Set the tuition_fee field for the constructor of course
     * @param tuition_fee
     */
    public void setTuition_fee(int tuition_fee){
        this.tuition_fee = new TuitionFee(tuition_fee);
    }

    /**
     * Set the rate field for the constructor of course
     * @param rate
     */
    public void setRate(int rate) {
        this.rate = new Rate(rate);
    }
    /**
     * Set the college field for the constructor of course
     * @param college
     */
    public void setCollege(String college){
        this.college=new College(college);
    }

    /**
     * Set the career field for the constructor of course
     * @param career
     */
    public void setCareer(String career) {
        this.career = new Career(career);
    }

    /**
     * @return The value of tuition_fee for the constructor of course
     */
    public String getTuitionFee(){
        return this.tuition_fee.toString();
    }
    /**
     * @return The value of code for the constructor of course
     */
    public String getCode(){
        return this.code.toString();
    }
    /**
     * @return The value of college for the constructor of course
     */
    public String getCollege(){
        return this.college.toString();
    }
    /**
     * @return The value of career for the constructor of course
     */
    public String getCareer(){
        return this.career.toString();
    }
    /**
     * @return The value of rate for the constructor of course
     */
    public String getRate(){
        return this.rate.toString();
    }

    public class Code{
        String code;
        public Code(String code){
            this.code = code;
        }
        @Override
        public String toString() {
            return code;
        }
    }

    public class Rate{
        int rate;
        public Rate(int rate){
            this.rate=rate;
        }

        @Override
        public String toString() {
            return Integer.toString(rate);
        }
    }

    public class TuitionFee{
        int tuitionfee;
        public TuitionFee(int tuitionfee){
            this.tuitionfee=tuitionfee;
        }
        @Override
        public String toString() {
            return Integer.toString(tuitionfee);
        }
    }
    public class Career{
        public String career;
        public Career(String career){
            this.career=career;
        }
        @Override
        public String toString() {
            return career;
        }
    }

    public class College{
        String college;
        public College(String college){
            this.college = college;
        }
        @Override
        public String toString() {
            return college;
        }
    }

    /**
     *
     * @param c Course we need to comprare
     * @return The value of difference between these two of number part of code
     */
    @Override
    public int compareTo(Course c){
        String c1=this.getCode();
        String c2 = c.getCode();
        int c_a = Integer.valueOf(c1.substring(4));
        int c_b = Integer.valueOf(c2.substring(4));
        return c_a - c_b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        boolean gh= college.college.equals(course.college.college);
        boolean gh1= career.career.equals(course.career.career);
        boolean gh2= code.code.equals(course.code.code);
        boolean gh3= tuition_fee.tuitionfee ==course.tuition_fee.tuitionfee;
        boolean gh6 = rate.rate==course.rate.rate;
        return gh&&gh1&&gh2&&gh3&&gh6;

    }

}
