package com.example.q.faultsreportingapp;

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


public class Employees extends AppCompatActivity {

    Session session = null;
    Context context = null;
    String rec, subject,email;
    ConnectionClass conClass;
    EditText txtName, txtSurname, txtContact, reciep;
    Button btnSave,btnSend;
    Spinner spnGender,spnRole,spnDepart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
        context = this;
        subject = "Successfully Registered";

        conClass = new ConnectionClass();
        txtName = (EditText) findViewById(R.id.txtName);
        txtSurname = (EditText) findViewById(R.id.txtSurname);
        spnGender = (Spinner) findViewById(R.id.spnGender);
        txtContact = (EditText) findViewById(R.id.txtContact);
        reciep = (EditText) findViewById(R.id.txtEmail);
        btnSave = (Button) findViewById(R.id.btnAddEmp);
        //btnSend=(Button) findViewById(R.id.btnSend);

        spnDepart =(Spinner) findViewById(R.id.spnDepartment);
        spnRole =(Spinner) findViewById(R.id.spnUserRole);
        //group for email body
        //Save button with listener event.
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Writting person information into Database
                WriteToDB rw = new WriteToDB();
                rw.execute("");
                notification();
            }
        });
    }
    public void notification(){
        rec =reciep.getText().toString();
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
        Employees.RetreiveFeedTask task = new Employees.RetreiveFeedTask();
        task.execute();

    }
    //Class to
    public class WriteToDB extends AsyncTask<String, String, String> {
        String message = "";
        Boolean isSuccess = false;

        String name = txtName.getText().toString();
        String surname = txtSurname.getText().toString();
        String gender = spnGender.getSelectedItem().toString();
        String contact = txtContact.getText().toString();
        String email =  reciep.getText().toString();
        String pswd = name.substring(0,2) +""+ surname.substring(0,2)+name.length();

       // String verifyPswd = txtVerifyPswd.getText().toString();
       //toString String modifiedDate = "2018-09-18";
        //Date date = new Date();
        //String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);

        protected void onPostExecute(String r) {
            //Pop message display unit for Toast Method
            Toast.makeText(Employees.this, r, Toast.LENGTH_LONG).show();
        }
        @Override
        protected String doInBackground(String... strings) {
            /*if (name.trim().equals("") || surname.trim().equals("") || gender.trim().equals("") || contact.trim().equals("")
                    || email.trim().equals("")  )
                message = "Please enter all fields";
            else{*/
                try {

                    //Connecting to DB
                    Connection conn = conClass.CONN();
                    CallableStatement callStatement = null;

                    if (conn == null) {
                        message = "Error in connection!";
                    } else {

                        //The Stored Procedure to Save into DB
                        String insertSPQuery = "{call Mob_Emp_UserProfile(?,?,?,?,?,?,?,?,?)}";
                        callStatement = conn.prepareCall(insertSPQuery);   //For more info on Prepared statement https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
                        callStatement.setInt(1, spnRole.getSelectedItemPosition()+2);//to match the value on the database table for user role
                        callStatement.setString(2, name.trim());
                        callStatement.setString(3, surname.trim());
                        callStatement.setString(4,spnGender.getSelectedItem().toString());//gender.trim());
                        callStatement.setString(5, contact.trim());
                        callStatement.setString(6, email.trim());
                        callStatement.setString(7, pswd.trim());
                        callStatement.setString(8,  java.time.LocalDate.now().toString());
                        callStatement.setInt(9, spnDepart.getSelectedItemPosition() );
                        callStatement.executeUpdate();
                        isSuccess = true;
                        message = "Successfully Saved";
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    Log.e("Error Found", ex.getMessage());
                    message = "Exception";
                }
            //}
            //}
            return message;
        }
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String>
    {
        String name = txtName.getText().toString();
        String surname = txtSurname.getText().toString();
        String email = reciep.getText().toString();
        String pswd = name.substring(0,2)+ surname.substring(0,2)+name.length();
        String textMessage =   "Dear "+name+" "+surname+ " .You've been registered on the incident report system. Your credentials are, username: "+email+"   ,password: " +pswd;


        @Override
        protected String doInBackground(String... params) {

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
            //     pdialog.dismiss();
            reciep.setText("");
            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Employees.this, AdminMainPage.class));
        }
    }
}