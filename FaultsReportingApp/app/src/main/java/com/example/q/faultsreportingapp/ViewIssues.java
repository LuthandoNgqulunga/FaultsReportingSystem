package com.example.q.faultsreportingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.AndroidRuntimeException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.IOError;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewIssues extends AppCompatActivity {

    // Declaring layout widgets
    ConnectionClass connectionClass;
    ImageView imageView;
    ProgressBar progressBar;
    Button btnDownload;
    TextView errorMsg;

    // Declaring connection variables
    Connection conn;
    String un,password,db,ip;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_issues);

        // Initializing the layouts on oncreate
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        errorMsg = (TextView) findViewById(R.id.errorMsg);
        btnDownload = (Button) findViewById(R.id.button);

        //Stopping progressbar first
        progressBar.setVisibility(View.GONE);

        ip = "*.*.*.*";
        db = "*";
        un = "*";
        password = "*";
        // Initializing Connection Variables
        //ip = "111.111.11.111:1111/"; //Change this ip with your Ip, and also don't forget to add the port
        //at the end with a slash
        //db = "database"; //Change this Database name with yours
        //un = "username"; // Change this username with your database username
       // password = "password"; // Change this password with your database password
    }

    // function to download image from the server
    public void downloadImage(View view)
    {
        // Setting an Async Task so that main thread does not through exception
        DownloadImage doin = new DownloadImage();
        doin.execute();
    }

    // Async task ; a background method
    private class DownloadImage extends AsyncTask<String, Void, String>
    {
        String image="";
        String msg =  "";
        ResultSet rs;

        @Override
        protected void onPreExecute()
        {
            errorMsg.setText("Downloading Please Wait...");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params)
        {
            String msg =  "";
            try
            {
                //Connecting to DB
                //conn = conClass.CONN();
               // CallableStatement callStatement = null;
                Connection con = connectionClass.CONN();
                //Connecting
              //  con = ConnectionHelper(un, password, db, ip);

                // Lets suppose the image which existed in the database has an ID=1 and we want to retrieve that image.
			            /*
                      	   Here column picture is that column which contains the picture
                     	*/
                String commands = "SELECT picture From Mob_Picture WHERE picture_ID='1' ";
                Statement stmt = con.createStatement();
                rs = stmt.executeQuery(commands);
                if(rs.next())
                {
                    image = rs.getString("picture");
                    msg = "Retrieved Successfully";
                }
                else
                {
                    msg = "Image not Found in the Database";
                }
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
            //Stoping the progress bar and showing the message
            progressBar.setVisibility(View.GONE);
            errorMsg.setText(msg);

            //Checking if image we got is empty or we have success
            if(resultSet.matches(""))
            {

            }
            else
            {
                //Decoding and Setting Image in the imageview
                byte[] decodeString = Base64.decode(resultSet, Base64.DEFAULT);
                Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                imageView.setImageBitmap(decodebitmap);
            }
        }
    }

    /*/ This function is used to connect to the database. It uses the library that we included
    @SuppressLint("NewApi")
    public Connection ConnectionHelper(String user, String password, String database, String server)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + "/" +";db="  +db+ ";user=" + un + ";password=" + password + ";";
            //ConnectionURL = "jdbc:jtds:sqlserver://" + server + database + ";user=" + user + ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("ERRO1", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO2", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO3", e.getMessage());
        }
        return connection;
    }*/

}
