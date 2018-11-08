package com.example.q.faultsreportingapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.sql.CallableStatement;
import java.sql.Connection;


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

public class LocationPage extends AppCompatActivity {
    Session session = null;
    Context context = null;
    String rec, subject,email;

    ConnectionClass conClass;
    EditText txtStrt, txtCty,txtCode;
    Button btnSave;
    String desr,userEmail,userName;
    int depart;
    int userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_page);

        context = this;
        //Getting the values using the intent from Report incident activity
        desr=getIntent().getStringExtra("description");
        depart = getIntent().getIntExtra("dept",1);
        userID= getIntent().getIntExtra("userID",1);
        userEmail =getIntent().getStringExtra("userEmail");
        userName =getIntent().getStringExtra("userName");

        conClass = new ConnectionClass();
        txtStrt = (EditText) findViewById(R.id.txtStreet);
        txtCty = (EditText) findViewById(R.id.txtCity);
        txtCode = (EditText) findViewById(R.id.txtPCode);
        btnSave = (Button) findViewById(R.id.btnSubmt);

        //Save button with listener event.
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Writting person information into Database
                WriteToDB rw = new WriteToDB();
                rw.execute("");

                rec =userEmail;
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
                LocationPage.RetreiveFeedTask task = new LocationPage.RetreiveFeedTask();
                task.execute();
            }
        });
    }

    //Class to
    public class WriteToDB extends AsyncTask<String,String,String>
    {
        String message="";
        Boolean isSuccess=false;

        String city =  txtCty.getText().toString();
        String street =txtStrt.getText().toString();
        int code = Integer.parseInt(txtCode.getText().toString());



        protected void onPostExecute(String r)
        {
            Toast.makeText(LocationPage.this,r,Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            if (city.trim().equals("") || street.trim().equals(""))
                message = "Please enter all fields";
            else
            {
                try {
                    //Connecting to DB
                    Connection conn = conClass.CONN();
                    CallableStatement callStatement = null;
                    if (conn == null) {
                        message = "Error in connection!";
                    } else {
                        String insertSPQuery = "{call Mob_New_Create_Incident(?,?,?,?,?,?,?,?,?) }";
                       callStatement = conn.prepareCall(insertSPQuery);   //For more info on Prepared statement https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
                        callStatement.setString(1, street.trim());
                        callStatement.setString(2, city.trim());
                        callStatement.setInt(3, code);
                        callStatement.setString(4, java.time.LocalDate.now().toString());
                        callStatement.setInt(5, depart);
                        callStatement.setInt(6, userID);
                        callStatement.setInt(7, 1);
                        callStatement.setString(8, desr);
                        callStatement.setString(9,java.time.LocalDate.now().toString());
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
        String textMessage =   "Dear "+userName+"\n"+", We have recieved your issue we will attend it as soon as we can " ;
        @Override
        protected String doInBackground(String... params) {
            subject = "Issue Received";
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
            startActivity(new Intent(LocationPage.this, Customer_MainPage.class));
        }
    }
}
