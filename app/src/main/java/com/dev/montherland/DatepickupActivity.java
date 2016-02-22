package com.dev.montherland;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class DatepickupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datepickup);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action barenu
        getMenuInflater().inflate(R.menu.menu_next, menu);
        MenuItem item = menu.findItem(R.id.next_button);
        item.setTitle("Done");
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

                super.onBackPressed();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);



                return true;

            default:
                return true;
        }
    }

}
