package com.example.q.faultsreportingapp;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;


public class myClosedIssues extends ListActivity
{
    ArrayList arrEmp;
    int empID;
    ConnectionClass connectionClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_operator_page);
        //Log.i("MyActivity", "MyClass.getView() — closed issues ID: " + empID);
        arrEmp = getMyList();
        if(arrEmp.size()==0){ Toast.makeText(getApplicationContext(), "Currently there are no resolved issues on your account"+"\n"+" Try again later", Toast.LENGTH_LONG).show();}
        setListAdapter(new ArrayAdapter<String>(this,R.layout.activity_my_closed_issues,R.id.ClosedIssue,arrEmp));
    }
    public ArrayList getMyList()
    {
        String message = "";
        Boolean isSuccess = false;
        ArrayList<String> arr = new ArrayList<String>();
        try {
            Connection con = connectionClass.CONN();
            CallableStatement callableStatement = null;
            if (con == null)
            {
                message = "Error in connection with SQL server";
            } else
            {
                //String query = "select mi.incident_ID,mi.inc_Desc,mi.inc_Date from Mob_Incident mi where mi.status_ID = '" + statusID + "'";
                String query = "call Mob_CustomerClosedIssues(?)";
                callableStatement = con.prepareCall(query);
                callableStatement.setInt(1,empID);
                ResultSet rs = callableStatement.executeQuery();
                // Log.i("MyActivity", "MyClass.getView() — get item number " + empID);
                while (rs.next())
                {
                    arr.add("incident ID: "+rs.getInt("incident_ID")+" \n"+"Details: "+(rs.getString("inc_Desc")) + "\n" +"Date submitted: "+(rs.getDate("inc_Date"))+"\n"+"Status: "+ (rs.getString("status_Desc")));
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
}
