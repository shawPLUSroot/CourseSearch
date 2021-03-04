/**
 * Tokenizer Skeleton code from Lab
 * method distance and similarity (line32-57)
 * Reference:
 * http://rosettacode.org/wiki/Levenshtein_distance#Java
 * https://rltk.readthedocs.io/en/latest/mod_similarity.html
 * @author Ruonan Zhang
 */
package com.example.coursesearch;
import java.util.ArrayList;

public class QueryTokenizer {
    private boolean invalid;
    private String query;
    Token currentToken;
    static final char symbol[] = {'=', '<', '>', ';'};
    static final char inValidsymbol[] = {'=','<', '>',';',':','-','_','~','#'};
    /**
     * Initialize tokenizer for an invalid query
     */
    public QueryTokenizer(boolean invalid, String query) {
        this.query = query;
        invalidNext();
    }

   /**
     * Initialize tokenizer for a valid query
     */
    public QueryTokenizer(String query) {

        this.query = query;
        next();
    }
    /**
     * find the number of times to convert a word to another
     * @param a a string
     * @param b a string
     */
    public static int distance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
    /**
     * use formula damerau_levenshtein_distance / max(len(s1), len(s2))
     * to calculate the similarity between two strings
     * @param a a string
     * @param b a string
     */
    public static double similarity(String a, String b){
        int distance = distance(a,b);
        double max = Math.max(a.length(), b.length());
        return 1-distance/max;
    }
    // check if a char is an element of the array
    public static boolean inArray(Character a,char[] b){
        for (int i = 0; i < b.length; i++) {
            if(b[i]==a){return true;}
        }return false;
    }
    //find next token in a valid query and update it in currentToken
    public void next() {
        query = query.replaceAll(" ", "");
        if (query.isEmpty()) {
            currentToken = null;
            return;
        }
        char first = query.charAt(0);
        if(first=='='){
            currentToken = new Token("=", Token.Type.tok_equal);
        }else
        if(first=='<'){
            currentToken = new Token("<", Token.Type.tok_lower);
        }else
        if(first=='>'){
            currentToken = new Token(">", Token.Type.tok_greater);
        }else
        if(first==';'){
            currentToken = new Token(";", Token.Type.tok_semicolon);
        }else {// the token is not a symbol
            String word = "" + first;
            int i = 1;

            while (( i < query.length()&&(!inArray(query.charAt(i), symbol)))) {
                word = word + query.charAt(i);
                i++;
            }
            boolean isInt = true;
            // check it is a word or an integer
            for (int i1 = 0; i1 < word.length(); i1++) {
                if(!Character.isDigit(word.charAt(i1)))isInt=false;
            }
            if(isInt){currentToken = new Token(word, Token.Type.tok_int);}
            else currentToken = new Token(word, Token.Type.tok_string);
        }
        int remove = currentToken.value.length();
        query = query.substring(remove);

    }
    //find next token in an invalid query and update it in currentToken
    public void invalidNext() {
        query = query.replaceAll(" ", "");
        if (query.isEmpty()) {
            currentToken = null;
            return;
        }
        char first = query.charAt(0);
        if(first=='='){
            currentToken = new Token("=", Token.Type.tok_equal);
        }else
        if(first=='<'){
            currentToken = new Token("<", Token.Type.tok_lower);
        }else
        if(first=='>'){
            currentToken = new Token(">", Token.Type.tok_greater);
        }else
        if(first==';'){
            currentToken = new Token(";", Token.Type.tok_semicolon);
        }else
        if(inArray(first,inValidsymbol)){
            currentToken = new Token(first+"", Token.Type.tok_equal);
        }else {
            String word = "" + first;
            int i = 1;
            while (( i < query.length()&&(!inArray(query.charAt(i), inValidsymbol)))) {
                word = word + query.charAt(i);
                i++;
            }
            //split integer and word in the string
            String w = "";
            String in = "";
            for (int i1 = 0; i1 < word.length(); i1++) {
                if(Character.isLetter(word.charAt(i1))){
                    w += word.charAt(i1);
                }else if(Character.isDigit(word.charAt(i1))){
                    in+= word.charAt(i1);
                }
            }
            if(w.length()!=0&&in.length()!=0){
                currentToken = new Token(in+" "+w, Token.Type.tok_intword);
                int remove = currentToken.value.length()-1;
                query = query.substring(remove);
                return;}
            else if(w.length()==0)currentToken = new Token(in, Token.Type.tok_int);
            else currentToken = new Token(w, Token.Type.tok_string);
        }
        int remove = currentToken.value.length();
        query = query.substring(remove);
    }
    /**
     * a recursive method to change an invalid query to a valid one
     * @param a a string to be corrected
     * @return String a valid query
     */
    public static String changeTerms(String a ){
        if(a.isEmpty())return "";
        QueryTokenizer queryTokenizer = new QueryTokenizer(true,a);
        ArrayList<Token> tokens = new ArrayList<>();

        while (queryTokenizer.currentToken!=null&&!queryTokenizer.currentToken.value.equals(";")){
            if (exist(tokens,queryTokenizer.currentToken)){
                queryTokenizer.invalidNext();
                continue;}
            tokens.add(queryTokenizer.currentToken);
            queryTokenizer.invalidNext();}
        String changed = change(tokens);
        return changed+";"+changeTerms(queryTokenizer.query);
    }
    /**
     * helper function in method changeTerms:
     * correct positions of tokens
     */
    private static String change(ArrayList<Token> tokens) {
        String t = "";
        if(tokens.size()==1&&tokens.get(0).type.equals(Token.Type.tok_intword)){
            String[] ss = tokens.get(0).value.split(" ");
            t+=ss[1];
            t+="=";
            t+=ss[0];
        }
        if(tokens.size()==3){
            if(tokens.get(0).type.equals(Token.Type.tok_string)){
                t+=(correct(tokens.get(0).value));
                if(tokens.get(1).type.equals(Token.Type.tok_equal)){
                    tokens.get(1).value="=";}
                t+=(tokens.get(1).value);
                if(tokens.get(2).type.equals(Token.Type.tok_string))
                    t+=correct(tokens.get(2).value);
                else t+= tokens.get(2).value;
            }else if(tokens.get(0).type.equals(Token.Type.tok_int)){
                t+=correct(tokens.get(2).value);
                if(tokens.get(1).type.equals(Token.Type.tok_greater)){
                    tokens.get(1).type= Token.Type.tok_lower;
                    tokens.get(1).value="<";
                }
                else if(tokens.get(1).type.equals(Token.Type.tok_lower)){
                    tokens.get(1).type= Token.Type.tok_greater;
                    tokens.get(1).value=">";
                }
                t+=tokens.get(1).value;
                t+=tokens.get(0).value;
            }
        }
        return t;
    }
    /**
     * helper function in method changeTerms:
     * correct words
     */
    private static String correct(String token) {
        // set a number greater than 0.5 to guarantee the similarity
        if(similarity(token,"college")>=0.5)return "college";
        if(similarity(token,"career")>=0.5)return "career";
        if(similarity(token,"rate")>=0.5)return "rate";
        if(similarity(token,"tuitionfee")>=0.5)return "tuitionfee";
        if(similarity(token,"undergraduate")>=0.5)return "undergraduate";
        if(similarity(token,"postgraduate")>=0.5)return "postgraduate";
        if(similarity(token,"cbe")>0.5)return "cbe";
        if(similarity(token,"cecs ")>0.5)return "cecs";
        if(similarity(token,"col")>0.5)return "col";
        if(similarity(token,"cass")>0.5)return "cass";
        if(similarity(token,"post")>0.5)return "postgraduate";
        if(similarity(token,"under")>0.5)return "undergraduate";
        return token;
    }
    /**
     * check if any tokens in list has the same value with input token
     */
    public static boolean exist(ArrayList<Token> orders,Token t){
        for (Token token : orders) {
            if(token.value.equals(t.value))
                return true;
        }return false;
    }
}
