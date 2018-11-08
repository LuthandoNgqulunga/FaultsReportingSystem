package com.example.q.faultsreportingapp;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ViewProgressAndClosed extends ListActivity
{
    ArrayList arr;
    Intent incID;
    int incidentIDfromList;
    ConnectionClass connectionClass;
    Button btnlogin;
    int empID = 0;
    int incidID ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        incidID = getIntent().getIntExtra("incID",4);
        Log.i("MyActivity", "MyClass.getView() — you dont have a role! " + incidID);
        arr = getList();
        setListAdapter(new ArrayAdapter<String>(this,R.layout.activity_view_progress_and_closed,R.id.ViewClosed,arr));
       // setListAdapter(new ArrayAdapter<String>(this,R.layout.activity_list_of_issues,R.id.ListOfIssue1,arr));
    }


    public ArrayList getList()
    {
        String message = "";
        Boolean isSuccess = false;
        // Integer employeeID = 1;  //The employee ID session from Login



        ArrayList<String> arr = new ArrayList<String>();
        try {
            Connection con = connectionClass.CONN();
            CallableStatement callableStatement = null;
            if (con == null)
            {
                message = "Error in connection with SQL server";
            } else
            {
                String query = "call Mob_GetAuditTrailForAnIssue(?)";
                callableStatement = con.prepareCall(query);
                callableStatement.setInt(1,incidID);//return all audit which are open issues
               // String query = "call Mob_GetListOfIssues(?)";
               // callableStatement = con.prepareCall(query);
               // callableStatement.setInt(1,1);//return all issue=1 which are open issues
                ResultSet rs = callableStatement.executeQuery();
                while (rs.next())
                {
                    arr.add(rs.getString("status_Desc")+" \n"+(rs.getString("comment"))+" \n"+(rs.getDate("aud_Date")));
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
