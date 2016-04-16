package com.dev.montherland;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.montherland.model.Response_Model;

import java.util.ArrayList;
import java.util.List;

public class Edit_Contact_detail extends Activity {

    String name, email, mobile, titleId;
    EditText et_name, et_email, et_mobile;
    Activity thisActivity = this;
    List<Response_Model> person;
    ArrayList<String> titleIdList = new ArrayList<>();
    ArrayList<String> titleList = new ArrayList<>();
    Spinner spinner2;
    ArrayAdapter<String> dataAdapter;
    List<Response_Model> respones;
    String id="";
    Bundle bundle;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_customer_contact);

        et_name = (EditText) findViewById(R.id.viewedit_name_edit);
        et_email = (EditText) findViewById(R.id.create_email);
        et_mobile = (EditText) findViewById(R.id.viewedit_phone_edit);
        try {
        bundle=getIntent().getExtras();
        name=bundle.getString("name");
        mobile=bundle.getString("mobile");
    }

    catch (Exception e){
        e.printStackTrace();

    }}
}
