
package com.example.q.faultsreportingapp;

        import android.app.ListActivity;
        import android.content.Intent;
        import android.database.Cursor;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.app.ListActivity;
        import android.content.Intent;
        import android.net.Uri;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;
        import java.sql.CallableStatement;
        import java.sql.Connection;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.Statement;
        import java.util.ArrayList;
        import java.util.List;
/**
public class getArray extends AppCompatActivity {

    ConnectionClass connectionClass;
    Button btnlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_issues);

        btnlogin = (Button) findViewById(R.id.button2);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                com.example.q.faultsreportingapp.ListOfIssues.GetIssuesList rw = new com.example.q.faultsreportingapp.ListOfIssues.GetIssuesList();
                rw.execute("");
            }
        });
    }



    protected void onPostExecute(String r) {

        Toast.makeText(com.example.q.faultsreportingapp.ListOfIssues.this, r, Toast.LENGTH_SHORT).show();


        // Toast.makeText(ListOfIssues.this, r, Toast.LENGTH_SHORT).show();

    }

    public class GetIssuesList extends AsyncTask<String,String,String>
    {
        String message = "";
        Boolean isSuccess = false;
        Integer statusID = 3;

        ArrayList<String> arr = new ArrayList<String>();

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(com.example.q.faultsreportingapp.ListOfIssues.this, r, Toast.LENGTH_SHORT).show();

            if (isSuccess) {
                Toast.makeText(com.example.q.faultsreportingapp.ListOfIssues.this, r, Toast.LENGTH_SHORT).show();

            }
        }
        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                CallableStatement callableStatement = null;
                if (con == null) {
                    message = "Error in connection with SQL server";
                } else
                {

                    String query = "select mi.incident_ID,mi.inc_Desc,mi.inc_Date from Mob_Incident mi where mi.status_ID = '" + statusID + "'";
                    PreparedStatement st = con.prepareStatement(query);
                    ResultSet rs = st.executeQuery();
                    while (rs.next())
                    {
                        arr.add(rs.getInt("incident_ID")+","+(rs.getString("inc_Desc"))+","+(rs.getDate("inc_Date")));

                    }
                    isSuccess = true;
                    message = arr.get(1);

                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                message = "Exceptions";
            }

            return message;
        }
    }


}
**/