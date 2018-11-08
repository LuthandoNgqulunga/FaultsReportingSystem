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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;




public class CitizenViewIssue extends ListActivity
{
    ArrayList arrEmp;
    Intent operatorIncID;
    Integer empID;
    ConnectionClass connectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_operator_page);
        empID=getIntent().getIntExtra("personIDLogin",1);
        arrEmp = getMyList();
        setListAdapter(new ArrayAdapter<String>(this,R.layout.activity_citizen_view_issue,R.id.CitizenListOfIssue,arrEmp));
    }

    protected void onListItemClick(ListView i, View v, int position, long id)
    {
        String incListPosition = arrEmp.get(position).toString(); //getting item via position clicked on listview
        Integer incidentIDfromList =Integer.parseInt(incListPosition.substring(0,2).trim()); //substring of incident id from a string of listview row
        //Log.i("MyActivity", "MyClass.getView() — incident num " + incidentIDfromList);
        operatorIncID =(new Intent(CitizenViewIssue.this,SpecificOperatorIssue.class));
        operatorIncID.putExtra("operatorIncIDfrmList",incidentIDfromList);
        startActivity(operatorIncID);
    }
    public ArrayList getMyList()
    {
        String message = "";
        Boolean isSuccess = false;

        //Integer empID = 3;  //Status ID of Incident, from Spinner control

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
                String query = "call Mob_GetListOfIssuesForOperator(?)";
                callableStatement = con.prepareCall(query);
                callableStatement.setInt(1,3);
                ResultSet rs = callableStatement.executeQuery();
               // Log.i("MyActivity", "MyClass.getView() — get item number " + empID);
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
