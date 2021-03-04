/**
 * Code similar to lab login system content
 * @author: Luqiao Dai
 */
package com.example.coursesearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {

    private EditText userName;
    private EditText passWord;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        readFile();

        userName = (EditText)findViewById(R.id.etUsername);
        passWord = (EditText)findViewById(R.id.etPassword);
        login = (Button)findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userName.getText().toString(); //convert the input username into string
                String password = User.getPassword(username); //get user's corresponding correct password

                if (password.isEmpty()){ //In this case, a user is not found in the 'userDetails.csv' file
                    Toast.makeText(getApplicationContext(),"invalid user", Toast.LENGTH_SHORT).show();
                }else if (!password.equals(passWord.getText().toString())){
                    Toast.makeText(getApplicationContext(),"incorrect password",Toast.LENGTH_SHORT).show();
                }else {
                    String id = User.getID(username);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USER",id);
                    EditText editText = (EditText)findViewById(R.id.etUsername);
                    String msg = editText.getText().toString();
                    intent.putExtra("UserName", msg); // pass username to MainActivity
                    startActivity(intent);
                }
            }
        });
    }

    public void readFile(){
        BufferedReader reader = null;
        try{//note that Try-with-resources requires API level 19
            reader = new BufferedReader(new InputStreamReader(getAssets().open("userDetails.csv"),"UTF-8")); // encoding

            String line;
            while ((line = reader.readLine())!=null){ //read each line until end of file
                String[] tokens = line.split(","); //beak each line into tokens (note that we are reading a csv file (comma-separated values))
                User users = new User(tokens[0],tokens[1],tokens[2]);
                users.addUser(); // If reading user information, you may want to store it in a User class
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("--- File End ---");
        }
    }
}