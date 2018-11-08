package com.example.q.faultsreportingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class Login extends AppCompatActivity {

   // Button buttn = findViewById(R.id.btnSignup);
    ConnectionClass connectionClass;
    EditText edtuserid,edtpass;
    Button btnlogin,btnsign;
    Integer roleId=0;
    Integer personID=0;
    String message = "";
    Intent userIDIntent;
    String personName;

    String userid,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connectionClass = new ConnectionClass();//the class file
        edtuserid = (EditText) findViewById(R.id.txtName1);
        edtpass = (EditText) findViewById(R.id.txtPswd1);
        btnlogin = (Button) findViewById(R.id.btnLogin);
        btnsign = (Button) findViewById(R.id.btnSignup);
        //The login button
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //search person information into Database
                DoLogin rw = new DoLogin();
                rw.execute("");
            }
        });

        //The signup button
        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Login.this,ListOfIssues.class);
               Intent intent = new Intent(Login.this,Signup.class);
               startActivity(intent);
            }
        });
    }
    public void direct(){
        if (roleId != null)
        {
            if(roleId == 1)
            {
                userIDIntent = (new Intent(Login.this,Customer_MainPage.class));
                userIDIntent.putExtra("personIDLogin",personID);
                userIDIntent.putExtra("email",userid);
                userIDIntent.putExtra("name",personName);
                startActivity(userIDIntent);
            }
            else if(roleId == 2 || roleId == 4)
            {
                userIDIntent = (new Intent(Login.this,AdminMainPage.class));
                userIDIntent.putExtra("personIDLogin",personID);
                userIDIntent.putExtra("email",userid);
                startActivity(userIDIntent);
            }
            else if(roleId == 3)
            {
                userIDIntent = (new Intent(Login.this,OperatorPage.class));
                userIDIntent.putExtra("personIDLogin",personID);
                userIDIntent.putExtra("email",userid);
                startActivity(userIDIntent);
            }
            else
            {
                Log.i("MyActivity", "MyClass.getView() — you dont have a role! " + roleId);
            }
        }

    }

    public class DoLogin extends AsyncTask<String,String,String> {
        Boolean isSuccess = false;

      //  String userid = edtuserid.getText().toString();
      //  String password = edtpass.getText().toString();
      //  String roleName = name.getText().toString();

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(Login.this, r, Toast.LENGTH_SHORT).show();

            if (isSuccess) {
                Toast.makeText(Login.this, r, Toast.LENGTH_SHORT).show();
            }
            direct();
        }
        @Override
        protected String doInBackground(String... params) {

            userid = edtuserid.getText().toString();
            password = edtpass.getText().toString();
            Log.i("MyActivity", "MyClass.getView() — get item number " + userid +" "+password);

            if (userid.trim().equals("") || password.trim().equals(""))
                message = "Please enter User Id and Password";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    CallableStatement callableStatement = null;

                    if (con == null) {
                        message = "Error in connection with SQL server";
                    } else
                     {
                         String query = "call Mob_User_Login(?,?)";
                         callableStatement=con.prepareCall(query);
                         callableStatement.setString(1,userid);
                         callableStatement.setString(2,password);
                         ResultSet rs = callableStatement.executeQuery();
                         if (rs.next())
                         {
                            roleId = rs.getInt(2); //Role ID used as a session to redirect page accordingly
                            personID =  rs.getInt(3);
                            // Log.i("MyActivity", "MyClass.getView() — get item number " + rs.getString(1) +" "+rs.getInt(2));
                             personName=rs.getString(1);
                             message = " Welcome " +personName;  //+" Role "+roleId+"ID "+personID;
                          //  message = " Welcome " + rs.getString(1) +"Role "+rs.getInt(2)+"ID "+rs.getInt(3); //Perfect one
                            isSuccess = true;
                         }
                         else
                         {
                            message = "Invalid Credentials";
                            isSuccess = false;
                         }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    message = "Exceptions";
                }


            }
            return message;
        }
    }
}
