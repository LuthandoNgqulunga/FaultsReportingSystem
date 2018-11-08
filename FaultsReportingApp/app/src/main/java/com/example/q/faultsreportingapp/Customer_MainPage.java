package com.example.q.faultsreportingapp;


import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Customer_MainPage extends ListActivity {
    int personID;
    Intent custIntent;
    String name,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_customer__main_page);
        personID=getIntent().getIntExtra("personIDLogin",1);
        email = getIntent().getStringExtra("email");
        name=getIntent().getStringExtra("name");

        Log.i("MyActivity", "MyClass.getView() — on customer main: " + personID);

        String[]attraction={"Report Issue","My In-progress Issues","My Closed Issues"};
        setListAdapter(new ArrayAdapter<String>(this,R.layout.activity_customer__main_page,R.id.report,attraction));
    }

    protected void onListItemClick(ListView i, View v, int position, long id){
        switch(position){
            case 0:

                custIntent = new Intent(Customer_MainPage.this,ReportIncident.class);
                custIntent.putExtra("personIDLogin",personID);
                custIntent.putExtra("email",email);
                custIntent.putExtra("name",name);
                startActivity(custIntent);
                break;
            case 1:
                custIntent = new Intent(Customer_MainPage.this,CustomerInprogressIssues.class);
                custIntent.putExtra("personIDLogin",personID);
                startActivity(custIntent);
                break;
            case 2:
                custIntent = new Intent(Customer_MainPage.this,myClosedIssues.class);
                custIntent.putExtra("personIDLogin",personID);
                startActivity(custIntent);
                break;

        }
    }
}
