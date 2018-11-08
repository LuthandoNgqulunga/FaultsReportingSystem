package com.example.q.faultsreportingapp;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.*;
import android.widget.ListView;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
public class  TrackIncident extends ListActivity
{
    ArrayList arr;
    Intent incID;
    int incidentIDfromList;
    ConnectionClass connectionClass;
    Button btnlogin;
    int empID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        arr = getList();
        if(arr.size()==0){ Toast.makeText(getApplicationContext(), "Currently there are no issues reported", Toast.LENGTH_LONG).show();}

        setListAdapter(new ArrayAdapter<String>(this,R.layout.activity_track_incident ,R.id.IssuesAlreadyAssigned,arr));
    }

    protected void onListItemClick(ListView i, View v, int position, long id)
    {
        String incListPosition = arr.get(position).toString(); //getting item via position clicked on listview
        String idR=incListPosition.substring(0,2).trim();//substring of incident id from a string of listview row
        incidentIDfromList =Integer.parseInt(idR);

        incID =(new Intent(TrackIncident.this,ViewProgressAndClosed.class));
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
                callableStatement.setInt(1,4);//return all issue=4 which are closed issues
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
