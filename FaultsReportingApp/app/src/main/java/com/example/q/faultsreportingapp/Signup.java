package com.example.q.faultsreportingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
//import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Properties;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.content.Context;

public class Signup extends AppCompatActivity {
    ConnectionClass conClass;
    EditText txtName, txtSurname,txtContact,txtEmail,txtPswd,txtVerifyPswd;
    Button btnSignin,btnSignup;
    Spinner spnGender;
    Intent userIDIntent;

    Session session = null;
    Context context = null;
    String rec, subject,emai, userName,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setContentView(R.layout.activity_signup);

        // Now get a handle to any View contained
        // within the main layout you are using
        View someView = findViewById(R.id.relativeLayout);
        // Find the root view
        View root = someView.getRootView();
        // Set the color
        //root.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        //Initialization of the GUI controls
        conClass = new  ConnectionClass();
        txtName = (EditText) findViewById(R.id.txtName1);
        txtSurname = (EditText) findViewById(R.id.txtSurname1);
        spnGender =(Spinner)findViewById(R.id.spnGender1);
        txtContact = (EditText) findViewById(R.id.txtContact1);
        txtEmail = (EditText) findViewById(R.id.txtEmail1 );
        txtPswd = (EditText) findViewById(R.id.txtPswd1);
        txtVerifyPswd = (EditText) findViewById(R.id.txtPswd2);
        btnSignup =(Button)findViewById(R.id.btnSignup1);
        btnSignin =(Button)findViewById(R.id.btnLogin1);

        // userName=name.trim();
        // rec=email.trim();
        // pass=pswd.trim();


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Signup.this,Login.class);
                startActivity(intent);
            }
        });
        //Save button with listener event.
        btnSignup .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Writting person information into Database
                WriteToDB rw = new WriteToDB();
                rw.execute("");
                userName=txtName.getText().toString();
                rec=txtEmail.getText().toString();
                pass=txtPswd.getText().toString();

                // rec =userEmail;
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                session = Session.getDefaultInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("clungsile@gmail.com", "Cesile15");
                    }
                });// pdialog = ProgressDialog.show(context, "", "Processing payment...", true);
                Signup.RetreiveFeedTask task = new Signup.RetreiveFeedTask();
                task.execute();



                //userIDIntent = (new Intent(Signup.this,Login.class));
                //  startActivity(userIDIntent);
            }
        });
    }
    //Class to
    public class WriteToDB extends AsyncTask<String,String,String>
    {
        String message="";
        Boolean isSuccess=false;

        String name =  txtName.getText().toString();
        String surname =txtSurname.getText().toString();
        String gender = spnGender.getSelectedItem().toString();
        String contact = txtContact.getText().toString();
        String email = txtEmail.getText().toString();
        String pswd = txtPswd.getText().toString();
        String verifyPswd = txtVerifyPswd.getText().toString();

        //  String modifiedDate = "2018-09-18";
        //Date date = new Date();
        //String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);

        protected void onPostExecute(String r)
        {
            //Pop message display unit for Toast Method
            Toast.makeText(Signup.this,r,Toast.LENGTH_LONG).show();
            /*
             txtName.setText("");
             txtSurname.setText("");
             spnGender.setSelection(0);
             txtContact.setText("");
             txtEmail.setText("");
             txtPswd.setText("");
             txtVerifyPswd.setText("");
             */
        }

        @Override
        protected String doInBackground(String... strings)
        {
            if (name.trim().equals("") || surname.trim().equals("") || gender.trim().equals("")|| contact.trim().equals("")
                    || email.trim().equals("")|| pswd.trim().equals("")|| verifyPswd.trim().equals(""))
                message = "Please enter all fields";
            else
            {
                //   if(pswd != verifyPswd){
                //     message = "password does not match";
                // }

                // else {
                try {

                    //Connecting to DB
                    Connection conn = conClass.CONN();
                    CallableStatement callStatement = null;

                    if (conn == null) {
                        message = "Error in connection!";
                    } else {

                        //The Stored Procedure to Save into DB
                        String insertSPQuery = "{call Mob_Citizen_User_Profile(?,?,?,?,?,?,?,?)}";
                        callStatement = conn.prepareCall(insertSPQuery);   //For more info on Prepared statement https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
                        callStatement.setInt(1,  1);
                        callStatement.setString(2, name.trim());
                        callStatement.setString(3, surname.trim());
                        callStatement.setString(4, "F");//gender.trim());
                        callStatement.setString(5, contact.trim());
                        callStatement.setString(6, email.trim());
                        callStatement.setString(7, pswd.trim());
                        callStatement.setString(8, java.time.LocalDate.now().toString());
                        callStatement.executeUpdate();



                        isSuccess = true;
                        message = "Successfully Saved";
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    Log.e("Error Found", ex.getMessage());
                    message = "Exception";
                }
            }
            //}
            return message;
        }
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String>
    {
        String textMessage =   "Dear "+userName+"\n"+", You have successfully created you account. Your username is:  "+rec+ "  .Password:  " +pass ;
        @Override
        protected String doInBackground(String... params) {
            subject = "Successfully registered";
            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("clungsile@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Successfully submitted", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Signup.this, Login.class));
        }
    }

}















/*
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
//import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Signup extends AppCompatActivity {
    ConnectionClass conClass;
    EditText txtName, txtSurname,txtContact,txtEmail,txtPswd,txtVerifyPswd;
    Button btnSignin,btnSignup;
    Spinner spnGender;
    Intent userIDIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setContentView(R.layout.activity_signup);

        // Now get a handle to any View contained
        // within the main layout you are using
        View someView = findViewById(R.id.relativeLayout);
        // Find the root view
        View root = someView.getRootView();
        // Set the color
        //root.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        //Initialization of the GUI controls
        conClass = new  ConnectionClass();
        txtName = (EditText) findViewById(R.id.txtName1);
        txtSurname = (EditText) findViewById(R.id.txtSurname1);
        spnGender =(Spinner)findViewById(R.id.spnGender1);
        txtContact = (EditText) findViewById(R.id.txtContact1);
        txtEmail = (EditText) findViewById(R.id.txtEmail1 );
        txtPswd = (EditText) findViewById(R.id.txtPswd1);
        txtVerifyPswd = (EditText) findViewById(R.id.txtPswd2);
        btnSignup =(Button)findViewById(R.id.btnSignup1);
        btnSignin =(Button)findViewById(R.id.btnLogin1);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Signup.this,Login.class);
                startActivity(intent);
            }
        });
        //Save button with listener event.
        btnSignup .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Writting person information into Database
                WriteToDB rw = new WriteToDB();
                rw.execute("");

                userIDIntent = (new Intent(Signup.this,Login.class));
                startActivity(userIDIntent);
            }
        });
    }
    //Class to
    public class WriteToDB extends AsyncTask<String,String,String>
    {
        String message="";
        Boolean isSuccess=false;

        String name =  txtName.getText().toString();
        String surname =txtSurname.getText().toString();
        String gender = spnGender.getSelectedItem().toString();
        String contact = txtContact.getText().toString();
        String email = txtEmail.getText().toString();
        String pswd = txtPswd.getText().toString();
        String verifyPswd = txtVerifyPswd.getText().toString();
      //  String modifiedDate = "2018-09-18";
        //Date date = new Date();
        //String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);

        protected void onPostExecute(String r)
        {
            //Pop message display unit for Toast Method

             txtName.setText("");
             txtSurname.setText("");
             spnGender.setSelection(0);
             txtContact.setText("");
             txtEmail.setText("");
             txtPswd.setText("");
             txtVerifyPswd.setText("");

        }


        protected String doInBackground(String... strings)
        {
            if (name.trim().equals("") || surname.trim().equals("") || gender.trim().equals("")|| contact.trim().equals("")
                    || email.trim().equals("")|| pswd.trim().equals("")|| verifyPswd.trim().equals(""))
                message = "Please enter all fields";
            else
            {
             //   if(pswd != verifyPswd){
               //     message = "password does not match";
               // }

               // else {
                    try {

                        //Connecting to DB
                        Connection conn = conClass.CONN();
                        CallableStatement callStatement = null;

                        if (conn == null) {
                            message = "Error in connection!";
                        } else {

                            //The Stored Procedure to Save into DB
                            String insertSPQuery = "{call Mob_Citizen_User_Profile(?,?,?,?,?,?,?,?)}";
                            callStatement = conn.prepareCall(insertSPQuery);   //For more info on Prepared statement https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
                            callStatement.setInt(1,  1);
                            callStatement.setString(2, name.trim());
                            callStatement.setString(3, surname.trim());
                            callStatement.setString(4, "F");//gender.trim());
                            callStatement.setString(5, contact.trim());
                            callStatement.setString(6, email.trim());
                            callStatement.setString(7, pswd.trim());
                            callStatement.setString(8, java.time.LocalDate.now().toString());
                            callStatement.executeUpdate();
                            isSuccess = true;
                            message = "Successfully Saved";
                        }
                    } catch (Exception ex) {
                        isSuccess = false;
                        Log.e("Error Found", ex.getMessage());
                        message = "Exception";
                    }
                }
            //}
            return message;
        }
    }

}
*/