package com.dev.montherland;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

public class InstructionActivity extends Activity {
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
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if (Build.VERSION.SDK_INT > 19) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeAsUpIndicator(R.drawable.pf);
            } else {
                getActionBar().setHomeButtonEnabled(true);
                getActionBar().setIcon(R.drawable.pf);
            }
            getActionBar().setTitle("Create Order");

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
