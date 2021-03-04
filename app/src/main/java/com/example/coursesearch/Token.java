/**
 * @author Ruonan Zhang
 */
package com.example.coursesearch;

public class Token {
    public enum Type {tok_empty,tok_string,tok_greater,tok_lower,tok_int,tok_semicolon,tok_equal,tok_intword}
    String value = "";
    Type type ;
    public Token(String value,Type type) {
        this.value = value;
        this.type = type;
    }


}
