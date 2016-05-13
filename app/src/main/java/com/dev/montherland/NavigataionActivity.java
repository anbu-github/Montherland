package com.dev.montherland;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dev.montherland.adapter.DrawerItemCustomAdapter;
import com.dev.montherland.model.ObjectDrawerItem;
import com.dev.montherland.util.PDialog;
import com.dev.montherland.util.StaticVariables;

public class NavigataionActivity extends FragmentActivity {
    private boolean doubleBackToExitPressedOnce = false;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private android.support.v4.app.ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigataion);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        StaticVariables.mode1="";


        // set a custom shadow that overlays the main content when the drawer opens
        try {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[5];

        drawerItem[0] = new ObjectDrawerItem(R.drawable.school_icon1, "Customers");
        drawerItem[1] = new ObjectDrawerItem(R.drawable.communication_icon, "Orders");
        drawerItem[2] = new ObjectDrawerItem(R.drawable.communication_icon, "Reports");
        drawerItem[3] = new ObjectDrawerItem(R.drawable.password_icon2, "Settings");
       // drawerItem[4] = new ObjectDrawerItem(R.drawable.logout_icon1, "Logout");
        drawerItem[4] = new ObjectDrawerItem(R.drawable.exit_icon1, "Exit");




        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.drawer_list_item, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
       getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new android.support.v4.app.ActionBarDrawerToggle(
                NavigataionActivity.this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.mipmap.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        )

        {

            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Redirecting user to page requested in intent after clicking on menu drawer activity
        String redirection = "";
        try {
            redirection = getIntent().getExtras().getString("redirection");
            switch (redirection) {
                case "Customers": {
                    selectItem(0);
                    break;
                }

                case "Orders": {
                    Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new Order_View();

                  /*  fragmentTransaction.setCustomAnimations(
                            R.anim.fragment_slide_left_enter,
                            R.anim.fragment_slide_left_exit,
                            R.anim.fragment_slide_right_enter,
                            R.anim.fragment_slide_right_exit);*/
                    fragmentTransaction.replace(R.id.content_frame, test);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("Orders");
                    break;
                }

                case "Settings":{

                    Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new Settings();

                   /* fragmentTransaction.setCustomAnimations(
                            R.anim.fragment_slide_left_enter,
                            R.anim.fragment_slide_left_exit,
                            R.anim.fragment_slide_right_enter,
                            R.anim.fragment_slide_right_exit);*/
                    fragmentTransaction.replace(R.id.content_frame, test);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("Settings");
/*
                    Intent intent=new Intent(NavigataionActivity.this,Settings.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);*/

                    //   Toast.makeText(NavigataionActivity.this,getResources().getString(R.string.under_construction),Toast.LENGTH_SHORT).show();
                    break;
                }
                
                default: {
                    selectItem(0);
                }
            }
        }

        catch(Exception ignored)
        {
            ignored.printStackTrace();
            if (savedInstanceState == null) {
                selectItem(0);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        /*case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String planet = getResources().getStringArray(R.array.planets_array)[position];
//            Log.d("planet",planet);
            //        selectItem(position);
            mDrawerLayout.closeDrawer(mDrawerList);
            switch (planet) {
                case "Customers":
                    selectItem(0);

                    break;
                case "Orders": {
                    Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new Order_View();

                  /*  fragmentTransaction.setCustomAnimations(
                            R.anim.fragment_slide_left_enter,
                            R.anim.fragment_slide_left_exit,
                            R.anim.fragment_slide_right_enter,
                            R.anim.fragment_slide_right_exit);*/
                    fragmentTransaction.replace(R.id.content_frame, test);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("Orders");
                    break;
                }

                case "Reports": {
                    Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new Order_View_Report();

                  /*  fragmentTransaction.setCustomAnimations(
                            R.anim.fragment_slide_left_enter,
                            R.anim.fragment_slide_left_exit,
                            R.anim.fragment_slide_right_enter,
                            R.anim.fragment_slide_right_exit);*/
                    fragmentTransaction.replace(R.id.content_frame, test);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("Orders Report");
                    break;
                }

                case "Orders History": {
                    Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new HistoryHomeFragment();

                  /*  fragmentTransaction.setCustomAnimations(
                            R.anim.fragment_slide_left_enter,
                            R.anim.fragment_slide_left_exit,
                            R.anim.fragment_slide_right_enter,
                            R.anim.fragment_slide_right_exit);*/
                    fragmentTransaction.replace(R.id.content_frame, test);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("Order History");
                    break;
                }


                case "Exit": {
                    Log.v("exit", "It is working");
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NavigataionActivity.this);
                    builder.setTitle(getResources().getString(R.string.exit))
                            .setCancelable(true)
                            .setMessage(getResources().getString(R.string.exit_warning))
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    System.exit(0);
                                    android.os.Process.killProcess(android.os.Process.myPid());

                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }

                            });
                    builder.show();



                    break;
                }
                case "View/Edit Profile": {
                    Log.v("exit","It is working");

                    Intent intent=new Intent(NavigataionActivity.this,ViewProfile.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    break;
                }

                case "Settings":{

                    Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new Settings();

                   /* fragmentTransaction.setCustomAnimations(
                            R.anim.fragment_slide_left_enter,
                            R.anim.fragment_slide_left_exit,
                            R.anim.fragment_slide_right_enter,
                            R.anim.fragment_slide_right_exit);*/
                    fragmentTransaction.replace(R.id.content_frame, test);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("Settings");
/*
                    Intent intent=new Intent(NavigataionActivity.this,Settings.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);*/

                    //   Toast.makeText(NavigataionActivity.this,getResources().getString(R.string.under_construction),Toast.LENGTH_SHORT).show();
                break;
                }


                case "Logout": {

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NavigataionActivity.this);
                    builder.setTitle(getResources().getString(R.string.logout))
                            .setCancelable(true)
                            .setMessage(getResources().getString(R.string.logout_warning))
                            .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbhelp entry = new dbhelp(NavigataionActivity.this);
                                    entry.open();
                                    entry.logoout();
                                    entry.close();

                                    Intent intent2 = new Intent(NavigataionActivity.this, Login.class);
                                    NavigataionActivity.this.finish();
                                    startActivity(intent2);
                                    NavigataionActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                                }

                            });
                    builder.show();


                    break;
                }
                default: {
                    selectItem(0);
                }
            }
        }
    }

    private void selectItem(int position) {

        // update the main content by replacing fragments
        Fragment test;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        test = new CustomerHomeFragment();

       /* fragmentTransaction.setCustomAnimations(
                R.animator.fragment_slide_left_enter,
                R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_right_exit);*/
        fragmentTransaction.replace(R.id.content_frame, test);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            //NavigataionActivity.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
            NavigataionActivity.this.overridePendingTransition(R.anim.bottom_in, R.anim.top_out);

            return;
        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.exit_press_back_twice_message, Toast.LENGTH_SHORT).show();
    }
}