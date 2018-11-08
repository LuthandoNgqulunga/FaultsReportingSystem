package com.example.q.faultsreportingapp;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
public class AdminMainPage extends ListActivity {
    Integer personID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_admin_main_page);
        personID=getIntent().getIntExtra("personIDLogin",1);
        String[]admin={"Issues need to be assigned","View Closed Issues","View In Progress Issues","Add and maintain employees"};

        setListAdapter(new ArrayAdapter<String>(this,R.layout.activity_admin_main_page,R.id.admin,admin));
    }

    protected void onListItemClick(ListView i, View v, int position, long id){

        switch(position){

            case 0:
                startActivity(new Intent(AdminMainPage.this,ListOfIssues.class));
                break;
            case 1:
                startActivity(new Intent(AdminMainPage.this,TrackIncident.class));
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gatewayworld.co.za")));
                break;
            case 2:
                startActivity(new Intent(AdminMainPage.this,ProgressIncident.class));
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gatewayworld.co.za")));
                break;
            case 3:
                startActivity(new Intent(AdminMainPage.this,Employees.class));
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gatewayworld.co.za")));
                break;


        }
    }
}
