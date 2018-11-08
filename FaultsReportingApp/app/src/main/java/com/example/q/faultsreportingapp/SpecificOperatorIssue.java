package com.example.q.faultsreportingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidRuntimeException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOError;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SpecificOperatorIssue extends AppCompatActivity
{
    ConnectionClass connectionClass;
    CallableStatement callableStatement;
    Connection con = connectionClass.CONN();
  //  int inc;
    ArrayList EmpIncident = new ArrayList();
    Button btnUpdt,btnClose;
    EditText body,empID;
    TextView txtdescription,department,inc_ID,txtstatus,txtdate,adress;
    Spinner spStatusControl;
    ArrayAdapter<String> adapter;
    String message="";
    int incidentID;
    int inc;
    ImageView imageViewer;

    //Function array to return list of issues per operator
    public ArrayList getIncidentByOperator()
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
                String query = "call Mob_GetSpecificIssueforOperator(?)";
                callableStatement = con.prepareCall(query);
                callableStatement.setInt(1,incidentID);
                ResultSet rs = callableStatement.executeQuery();

                while (rs.next())
                {
                    //arr.add(rs.getInt(1)+" "+(rs.getString(2))+" "+(rs.getString(3))+" "+(rs.getString(4))+" "+(rs.getDate(5))+" "+(rs.getString(6)))  //Adding issues to string array then return it
                    arr.add(rs.getInt(1));
                    arr.add(rs.getString(2));
                    arr.add(rs.getString(3));
                    arr.add(rs.getString(4));
                    arr.add(rs.getDate(5));
                    arr.add(rs.getString(6));
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
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_operator_issue);

        imageViewer = (ImageView) findViewById(R.id.imageView2);

        incidentID= getIntent().getIntExtra("operatorIncIDfrmList",1);  //Incident ID for searching an issue
        btnUpdt = (Button)findViewById(R.id.btnShow);
        btnClose=(Button)findViewById(R.id.button2);
        txtdescription =(TextView)findViewById(R.id.txtDescription);
        department =(TextView)findViewById(R.id.txtDepartment);
        inc_ID =(TextView)findViewById(R.id.txtincID);
        txtstatus=(TextView)findViewById(R.id.txtStatus);
        txtdate=(TextView)findViewById(R.id.txtDate);
        adress=(TextView)findViewById(R.id.txtAddress);
        body=(EditText)findViewById(R.id.txtIncBody);
        // empID =(EditText)findViewById(R.id.txtEmployeeID);
        int empID =3;
       // spStatusControl=(Spinner)findViewById(R.id.spnStatus);
        //Array object for specific issue
        EmpIncident = getIncidentByOperator();
        //Displaying specific issue
        inc_ID.setText("incident_No:  "+EmpIncident.get(0).toString());
        txtdescription.setText("Description: "+EmpIncident.get(1).toString());
        department.setText("Department:   "+EmpIncident.get(2).toString());
        txtstatus.setText("Status:      "+EmpIncident.get(3).toString());
        txtdate.setText("Date:         "+EmpIncident.get(4).toString());
        adress.setText("Location:     "+EmpIncident.get(5).toString());
        inc= Integer.parseInt(EmpIncident.get(0).toString());
        SpecificOperatorIssue.DownloadImage doin = new SpecificOperatorIssue.DownloadImage();
        doin.execute();
        //Updating specific issue
        btnUpdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                updateIncident(2);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                updateIncident(4);
            }
        });
    }
    public void updateIncident(int i){
        try {

            Connection con = connectionClass.CONN();
            callableStatement = null;
            String updt = "call Mob_UpdateIncidentbyOperator(?,?,?,?,?)";
            callableStatement = con.prepareCall(updt);
            callableStatement.setInt(1, 19);
            callableStatement.setInt(2,inc );
            Log.i("MyActivity", "MyClass.getView() — before the update " + i+" "+inc);
            callableStatement.setInt(3,i);//status of the issue is now closed
            callableStatement.setString(4,body.getText().toString() );
            callableStatement.setString(5, java.time.LocalDate.now().toString());
            Log.i("MyActivity", "MyClass.getView() — before the update " + i);
            callableStatement.executeUpdate();

            message = "Successfully Saved";
            Toast t = Toast.makeText(SpecificOperatorIssue.this,message,Toast.LENGTH_LONG);
            t.show();
            body.setText("");
            spStatusControl.setSelection(0);
        }
        catch (Exception ex)
        {
            message = "Exception";
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
