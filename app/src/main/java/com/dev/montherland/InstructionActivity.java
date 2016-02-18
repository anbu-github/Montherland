package com.dev.montherland;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class InstructionActivity extends AppCompatActivity {
    Bundle extras;
    Activity thisActivity=this;
    EditText in;
    String str_instr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
      in=(EditText)findViewById(R.id.instruction);

        try {
            extras=getIntent().getExtras();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Create Order");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action barenu
        getMenuInflater().inflate(R.menu.menu_next, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                return true;
            case R.id.next_button:

                str_instr=in.getText().toString();
                
                Intent in=new Intent(thisActivity,OrderConfirmDetails.class);
                in.putExtra("bundle",extras);
                in.putExtra("instr",str_instr);
                startActivity(in);



        }
        return true;
    }

        }