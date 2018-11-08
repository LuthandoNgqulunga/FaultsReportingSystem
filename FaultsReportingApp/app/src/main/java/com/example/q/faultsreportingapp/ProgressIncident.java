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
import android.widget.Spinner;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
/*
public class ProgressIncident extends ListActivity {
    ArrayList arr;
    Intent incID;
    int incList;
    ConnectionClass connectionClass;
    Spinner spnStatus;
    int statusID;
    //  Button btnlogin;
    //  int empID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_track_incident);
       // spnStatus=(Spinner)findViewById(R.id.spnStatus);
        statusID =spnStatus.getSelectedItemPosition();
        arr = getList();
        setListAdapter(new ArrayAdapter<String>(this,R.layout.activity_progress_incident ,R.id.IssueInprogress,arr));
    }

    protected void onListItemClick(ListView i, View v, int position, long id)
    {
        String incListPosition = arr.get(position).toString(); //getting item via position clicked on listview
        String idR=incListPosition.substring(0,1).trim();//substring of incident id from a string of listview row
        incList =Integer.parseInt(idR);

       incID =(new Intent(ProgressIncident.this,SpecificIssue.class));
        // Log.i("MyActivity", "MyClass.getView() — you dont have a role! " + incList);
        // incID.putExtra("specInc", incList);
        //  incID.putExtra("specificIncident",incidentIDfromList);
        //incID.putIntExtra("incidentIDfrmList",incidentIDfromList);
        startActivity(incID);
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
                String query = "call Mob_GetListOfIssues(?)";
                callableStatement = con.prepareCall(query);
                callableStatement.setInt(1,2);//return all issue=1 which are open issues
                ResultSet rs = callableStatement.executeQuery();
                while (rs.next())
                {
                    arr.add(rs.getInt("incident_ID")+" \n"+(rs.getString("inc_Desc"))+" \n"+(rs.getDate("inc_Date")));
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
}*/
public class  ProgressIncident extends ListActivity
{
    ArrayList arr = getList();
    Intent incID;
    int incidentIDfromList;
    ConnectionClass connectionClass;
    Button btnlogin;
    int empID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_list_of_issues);
        setListAdapter(new ArrayAdapter<String>(this,R.layout.activity_progress_incident ,R.id.IssuesAlreadyAssigned,arr));
    }

    protected void onListItemClick(ListView i, View v, int position, long id)
    {
        String incListPosition = arr.get(position).toString(); //getting item via position clicked on listview
        String idR=incListPosition.substring(0,2).trim();//substring of incident id from a string of listview row
        incidentIDfromList =Integer.parseInt(idR);

        incID =(new Intent(ProgressIncident.this,ViewProgressAndClosed.class));
      //  Log.i("MyActivity", "MyClass.getView() — you dont have a role! " + incidentIDfromList);
        incID.putExtra("incID",incidentIDfromList);
        startActivity(incID);
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
                //String query = "select mi.incident_ID,mi.inc_Desc,mi.inc_Date from Mob_Incident mi where mi.status_ID = '" + statusID + "'";
                String query = "call Mob_GetListOfIssues(?)";
                callableStatement = con.prepareCall(query);
                callableStatement.setInt(1,2);//return all issue=4 which are closed issues
                ResultSet rs = callableStatement.executeQuery();
                while (rs.next())
                {
                    arr.add(rs.getInt("incident_ID")+" \n"+(rs.getString("inc_Desc"))+" \n"+(rs.getDate("inc_Date")));
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


