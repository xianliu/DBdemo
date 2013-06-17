package com.sicau.dbdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kevin on 13-6-14.
 */
public class ListActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;

    private ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.navbar_layout);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
            String itemName = parent.getItemAtPosition(position).toString();

            if(itemName == "Create Contact") {
                finish();
                startActivity(new Intent().setClass(ListActivity.this, CreateContact.class));
            } else if(itemName == "Update Contact") {
                finish();
                startActivity(new Intent().setClass(ListActivity.this, UpdateContact.class));
            }

            Toast.makeText(getApplicationContext(), itemName, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private List<String> getData(){

        List<String> data = new ArrayList<String>();
        data.add("Create Contact");
        data.add("Update Contact");

        return data;
    }

}