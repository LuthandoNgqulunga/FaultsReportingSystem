package com.example.q.faultsreportingapp;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Button btn,btnEmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btnContinue);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Intent intent = new Intent(MainActivity.this,Login.class);
                    startActivity(intent);
                }
                catch (Exception ex)
                {
                    Log.e("Lungi", ex.getMessage());
                }
            }
        });


    }
}
