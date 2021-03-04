/**
 * @author Xueting Ren
 */
package com.example.coursesearch;
import java.util.HashSet;

public class Pair <T extends Comparable<T>> implements Comparable<Pair<T>>{

    T val;
    HashSet<String> codes = new HashSet<>();
    /**
     * Initialize empty Pair
     */
    public Pair(){
    }
    /**
     * Initialize Pair with corresponding data
     */
    public Pair(T val,HashSet<String> codes){
        this.val  = val;
        this.codes=codes;
    }

    public void setKey(T val){
        this.val = val;
    }


    @Override
    public int compareTo(Pair p){
        return this.val.compareTo((T) p.val);
    }


}
