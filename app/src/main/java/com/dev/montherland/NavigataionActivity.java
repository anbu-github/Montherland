package com.dev.montherland;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dev.montherland.adapter.DrawerItemCustomAdapter;
import com.dev.montherland.model.ObjectDrawerItem;
import com.dev.montherland.util.StaticVariables;

public class NavigataionActivity extends Activity {

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


        // set a custom shadow that overlays the main content when the drawer opens
        try {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[6];

        drawerItem[0] = new ObjectDrawerItem(R.drawable.school_icon1, "Customers");
        drawerItem[1] = new ObjectDrawerItem(R.drawable.communication_icon, "Order");
        drawerItem[2] = new ObjectDrawerItem(R.drawable.profile_icon1, "View/Edit Profile");
        drawerItem[3] = new ObjectDrawerItem(R.drawable.password_icon2, "Change Password");
        drawerItem[4] = new ObjectDrawerItem(R.drawable.logout_icon1, "Logout");
        drawerItem[5] = new ObjectDrawerItem(R.drawable.exit_icon1, "Exit");




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
                case "Order": {
                    android.app.Fragment test;
                    android.app.FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new PurchaseOrderFragment();
                    Bundle args = new Bundle();

                    fragmentTransaction.setCustomAnimations(
                            R.animator.fragment_slide_left_enter,
                            R.animator.fragment_slide_left_exit,
                            R.animator.fragment_slide_right_enter,
                            R.animator.fragment_slide_right_exit);
                    fragmentTransaction.replace(R.id.content_frame, test);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("Order");
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
                case "Order": {
                    android.app.Fragment test;
                    android.app.FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new PurchaseOrderFragment();
                    Bundle args = new Bundle();

                    fragmentTransaction.setCustomAnimations(
                            R.animator.fragment_slide_left_enter,
                            R.animator.fragment_slide_left_exit,
                            R.animator.fragment_slide_right_enter,
                            R.animator.fragment_slide_right_exit);
                    fragmentTransaction.replace(R.id.content_frame, test);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    setTitle("Order");
                    break;
                }
                case "Exit": {
                    Log.v("exit", "It is working");
                    System.exit(0);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    break;
                }
                case "View/Edit Profile": {
                    Log.v("exit","It is working");

                    Intent intent=new Intent(NavigataionActivity.this,ViewProfile.class);
                    startActivity(intent);
                    break;
                }
                case "Change Password":{

                    Intent intent=new Intent(NavigataionActivity.this,Change_Password.class);
                    startActivity(intent);

                 //   Toast.makeText(NavigataionActivity.this,getResources().getString(R.string.under_construction),Toast.LENGTH_SHORT).show();
                break;
                }

                case "Logout": {

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(new ContextThemeWrapper(NavigataionActivity.this,R.style.myDialog));
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
        android.app.Fragment test;
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        test = new CustomerHomeFragment();

        fragmentTransaction.setCustomAnimations(
                R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
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
        super.onBackPressed();
        NavigataionActivity.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}