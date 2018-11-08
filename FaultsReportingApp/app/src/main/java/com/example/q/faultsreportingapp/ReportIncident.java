package com.example.q.faultsreportingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidRuntimeException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Properties;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.content.Context;



public class ReportIncident extends AppCompatActivity {

    // Declaring Layouts buttons, imageview extra
    private static final int RESULT_LOAD_IMAGE = 1;
    Button uploadpic, proceed;
    ImageView imagebox;
    ProgressBar progressBar;
    // End Layouts buttons, imageview extra
    Intent prcd ;
    // Declaring connection variables and array,string to store data in them
    byte[] byteArray;
    String encodedImage;
    Intent galleryIntent;
    Connection con;
    String un, ip, db, password;
    Spinner spnDpt;
    EditText descript;
    int persID;
    String persEemail,persName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);
        descript = (EditText) findViewById(R.id.txtDescr);
        spnDpt = (Spinner) findViewById(R.id.spnDept);
        // Finding the declared layouts
        uploadpic = (Button) findViewById(R.id.button);
        proceed = (Button) findViewById(R.id.btnProceed);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imagebox = (ImageView) findViewById(R.id.imageView);

        persID=getIntent().getIntExtra("personIDLogin",1);
        persEemail = getIntent().getStringExtra("email");
        persName=getIntent().getStringExtra("name");

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = descript.getText().toString();
                Integer depty = spnDpt.getSelectedItemPosition();
             //   String depty = spnDpt.getSelectedItem().toString();
                //Passing the values using the intent into Location page activity
                prcd= new Intent(ReportIncident.this, LocationPage.class);;
                prcd.putExtra("description", s);
                prcd.putExtra("dept",depty);

                prcd.putExtra("userID",persID);
                prcd.putExtra("userEmail",persEemail);
                prcd.putExtra("userName",persName);
                startActivity(prcd);
            }
        });

        //End  Finding the declared layouts
        // Declaring connectivity credentials

        ip = "";
        db = "";
        un = "";
        password = "";
        // End Declaring connectivity credentials
        progressBar.setVisibility(View.GONE);
        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Opening the Gallery and selecting media
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
                    galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //   startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE );
                    // onActivityResult(1,-1,galleryIntent);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                    // this will jump to onActivity Function after selecting image
                } else {
                    Toast.makeText(ReportIncident.this, "No activity found to perform this task", Toast.LENGTH_SHORT).show();
                }
                // End Opening the Gallery and selecting media
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            // getting the selected image, setting in imageview and converting it to byte and base 64
            progressBar.setVisibility(View.VISIBLE);
            Bitmap originBitmap = null;

            // Bundle extras = data.getExtras();
            // Bitmap bitmap = (Bitmap) extras.get("data");


            Uri selectedImage = data.getData();
            Toast.makeText(ReportIncident.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
            InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
                originBitmap = BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage().toString());
            }
            if (originBitmap != null) {
                this.imagebox.setImageBitmap(originBitmap);
                Log.w("Image Setted in", "Done Loading Image");
                try {
                    Bitmap image = ((BitmapDrawable) imagebox.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    // Calling the background process so that application wont slow down
                    UploadImage uploadImage = new UploadImage();
                    uploadImage.execute("");
                    //End Calling the background process so that application wont slow down
                } catch (Exception e) {
                    Log.w("OOooooooooo", "exception");
                }
                Toast.makeText(ReportIncident.this, "Conversion Done", Toast.LENGTH_SHORT).show();
            }
            // End getting the selected image, setting in imageview and converting it to byte and base 64
        } else {
            System.out.println("Error Occured");
        }
    }

    public class UploadImage extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String r) {
            // After successful insertion of image
            progressBar.setVisibility(View.GONE);
            Toast.makeText(ReportIncident.this, "Image Succesfully Uploaded", Toast.LENGTH_LONG).show();
            // End After successful insertion of image
        }

        @Override
        protected String doInBackground(String... params) {
            // Inserting in the database
            String msg = "unknown";
            try {
                con = connectionclass(un, password, db, ip);
                String commands = "Insert into Mob_Picture (picture, pdate) values ('" + encodedImage + "', '" + java.time.LocalDate.now() + "')";
                PreparedStatement preStmt = con.prepareStatement(commands);
                preStmt.executeUpdate();
                msg = "Inserted Successfully";
            } catch (SQLException ex) {
                msg = ex.getMessage().toString();
                Log.d("Error no 1:", msg);
            } catch (IOError ex) {
                msg = ex.getMessage().toString();
                Log.d("Error no 2:", msg);
            } catch (AndroidRuntimeException ex) {
                msg = ex.getMessage().toString();
                Log.d("Error no 3:", msg);
            } catch (NullPointerException ex) {
                msg = ex.getMessage().toString();
                Log.d("Error no 4:", msg);
            } catch (Exception ex) {
                msg = ex.getMessage().toString();
                Log.d("Error no 5:", msg);
            }
            System.out.println(msg);
            return "";
            //End Inserting in the database
        }
    }
    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + "/" + ";db=" + db + ";user=" + un + ";password=" + password + ";";
            // ConnectionURL = "jdbc:jtds:sqlserver://" + server+ ";user=" +database + ";user=" + user + ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("error no 1", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error no 2", e.getMessage());
        } catch (Exception e) {
            Log.e("error no 3", e.getMessage());
        }
        return connection;
    }
    public Connection getcon() {
        Connection conn = ConnectionClass.CONN();
        return conn;
    }
}

