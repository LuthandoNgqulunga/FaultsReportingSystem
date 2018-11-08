package com.example.q.faultsreportingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
//import android.se.omapi.Session;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AndroidRuntimeException;
import android.util.Base64;
import java.io.IOError;
import java.sql.SQLException;
import java.sql.Statement;
import android.widget.ImageView;

public class SpecificIssue extends AppCompatActivity
{

    Session session = null;
    Context context = null;
    String subject, textMessage,email;

    ConnectionClass connectionClass;
    Connection con = connectionClass.CONN();
    CallableStatement callableStatement;

    int incidentID,employeeID;//getIntent().getIntExtra("specificIncident",12);
    ArrayList incident = new ArrayList();
    Button btnAssign;
    EditText body,empID,reciep;
    TextView txtdescription,department,inc_ID,txtstatus,txtdate,adress;
    String message="";

    ImageView imageViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_issue);

        context = this;
        imageViewer = (ImageView) findViewById(R.id.specificImage);
        incidentID=getIntent().getIntExtra("specInc",12);
        btnAssign = (Button)findViewById(R.id.btnUpdate);
        txtdescription =(TextView)findViewById(R.id.txtDescription);
        department =(TextView)findViewById(R.id.txtDepartment);
        inc_ID =(TextView)findViewById(R.id.txtincID);
        txtstatus=(TextView)findViewById(R.id.txtStatus);
        txtdate=(TextView)findViewById(R.id.txtDate);
        adress=(TextView)findViewById(R.id.txtAddress);
        body=(EditText)findViewById(R.id.txtIncBody);
        empID =(EditText)findViewById(R.id.txtEmployeeID);
        //reciep =(EditText)findViewById(R.id.txtEmpEmail);
        subject = "Issue assignment";
      //  textMessage =   "Incident No: "+ incidentID +" has been assigned to you, and requires your attention";

        incident = getIncident();
        inc_ID.setText(incident.get(0).toString());
        txtdescription.setText(incident.get(1).toString());
        department.setText(incident.get(2).toString());
        txtstatus.setText(incident.get(3).toString());
        txtdate.setText(incident.get(4).toString());
        adress.setText(incident.get(5).toString());

        SpecificIssue.DownloadImage doin = new SpecificIssue.DownloadImage();
        doin.execute();

        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.i("MyActivity", "MyClass.getView() — get item number " + incidentID);
                textMessage =   "Incident No: "+ incidentID+"   "+body.getText().toString();

                insertTOAudit_Trail();
                //Log.i("MyActivity", "MyClass.getView() — i assign this issue to " + employeeID);
                //rec =reciep.getText().toString();
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
                RetreiveFeedTask task = new RetreiveFeedTask();
                task.execute();

            }
        });
    }

    public void insertTOAudit_Trail(){

        try {
            //Insert into au
            callableStatement = null;

            String updt = "call Mob_InsertTOAudit_Trail(?,?,?,?,?,?)";
            callableStatement = con.prepareCall(updt);
            employeeID=Integer.parseInt(empID.getText().toString());
            callableStatement.setInt(1,employeeID);
            callableStatement.setInt(2,incidentID);
            callableStatement.setInt(3,2); //2=In-progress: Status id is always gona be 2, when assigning meaning its in progress
            callableStatement.setString(4,body.getText().toString());
            callableStatement.setString(5,subject+": "+textMessage);
            callableStatement.setString(6, java.time.LocalDate.now().toString());
            callableStatement.executeQuery();

            Log.i("MyActivity", "MyClass.getView() — successfully " + incidentID);
            message = "Successfully Assigned";
            Toast t = Toast.makeText(SpecificIssue.this,message,Toast.LENGTH_LONG);
            t.show();
            body.setText("");
        }
        catch (Exception ex)
        {
            message = "Successfully Assigned";
            Toast t = Toast.makeText(SpecificIssue.this,message,Toast.LENGTH_LONG);
            t.show();
         //   body.setText("");
          //  empID.setText("");
        }
    }

    public ArrayList getIncident()
    {
        String message = "";
        Boolean isSuccess = false;
        ArrayList arr = new ArrayList();
        try {
            Connection con = connectionClass.CONN();
            CallableStatement callableStatement = null;
            if (con == null)
            {
                message = "Error in connection with SQL server";
            } else
            {
                ResultSet rs ;
                String query = "call Mob_GetSpecificIssue(?)";
                callableStatement = con.prepareCall(query);
                callableStatement.setInt(1,incidentID);
                rs = callableStatement.executeQuery();

                while (rs.next())
                {
                    //arr.add(rs.getInt(1)+" "+(rs.getString(2))+" "+(rs.getString(3))+" "+(rs.getString(4))+" "+(rs.getDate(5))+" "+(rs.getString(6)))
                    arr.add("Inc_ID     :"+rs.getInt(1));
                    arr.add("Description:"+rs.getString(2));
                    arr.add("Department :"+rs.getString(3));
                    arr.add("Status     :"+rs.getString(4));
                    arr.add("Date       :"+rs.getDate(5));
                    arr.add("Location   :"+rs.getString(6));
                }
            }
        }
        catch (Exception ex)
        {
            isSuccess = false;
            message = "Exceptions";
        }
        return arr;
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{

                //retrieving employee email
                ResultSet rs1 ;
                String emailQuery ="Call Mob_GetEmpEmail(?)";
                callableStatement = con.prepareCall(emailQuery);
                callableStatement.setInt(1,employeeID);
                rs1 = callableStatement.executeQuery();
                if (rs1.next()){ email=rs1.getString("email");}

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("clungsile@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
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
    }

    // Async task ; a background method
    private class DownloadImage extends AsyncTask<String, Void, String>
    {
        String image="";
        String msg =  "";
        ResultSet rs;

        @Override
        protected String doInBackground(String... params)
        {
            String msg =  "";
            try
            {
                ResultSet rs ;
                String emailQuery ="Call Mob_GetAnImage(?)";
                callableStatement = con.prepareCall(emailQuery);
                callableStatement.setInt(1,incidentID);
                rs = callableStatement.executeQuery();
                if (rs.next()){ image = rs.getString("picture");}
                else { msg = "Image not Found in the Database"; }
            }
            // Catching all the exceptions
            catch (SQLException ex)
            {
                msg = ex.getMessage().toString();
                Log.d("seotoolzz", msg);
            }
            catch (IOError ex)
            {
                // TODO: handle exception
                msg = ex.getMessage().toString();
                Log.d("seotoolzz", msg);
            }
            catch (AndroidRuntimeException ex)
            {
                msg = ex.getMessage().toString();
                Log.d("seotoolzz", msg);
            }
            catch (NullPointerException ex)
            {
                msg = ex.getMessage().toString();
                Log.d("seotoolzz", msg);
            }
            catch (Exception ex)
            {
                msg = ex.getMessage().toString();
                Log.d("seotoolzz", msg);
            }
            return image;
        }

        @Override
        protected void onPostExecute(String resultSet)
        {
            //Checking if image we got is empty or we have success
            if(resultSet.matches(""))
            {
                byte[] decodeString = Base64.decode(resultSet, Base64.DEFAULT);
                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                imageViewer.setImageBitmap(decodebitmap);
            }
            else
            {//Decoding and Setting Image in the imageview
                byte[] decodeString = Base64.decode(resultSet, Base64.DEFAULT);
                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                imageViewer.setImageBitmap(decodebitmap);
            }
        }
    }
}
