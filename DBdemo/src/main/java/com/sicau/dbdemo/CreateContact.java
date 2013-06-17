package com.sicau.dbdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.sicau.Dao.Contact;
import com.sicau.Dao.DBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kevin on 13-6-15.
 */
public class CreateContact extends Activity {
    private ListView listView;

    private DBManager manager;

    private EditText nameView;
    private EditText phoneView;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(), "back button pressed!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(CreateContact.this, ListActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.manager = new DBManager(this);

        setContentView(R.layout.create_contact);
        listView = (ListView) findViewById(R.id.createListView);
        listView.setAdapter(new SimpleAdapter(this, getData(), android.R.layout.simple_expandable_list_item_2, new String[]{"name", "phone"}, new int[]{android.R.id.text1, android.R.id.text2}));

        nameView = (EditText) findViewById(R.id.name);
        phoneView = (EditText) findViewById(R.id.phone);


        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameView.getText().toString();
                String phone = phoneView.getText().toString();


                View focusView = null;
                Boolean cancel = false;
                // Check for a valid password.
                if (TextUtils.isEmpty(name)) {
                    nameView.setError(getString(R.string.error_field_required));
                    focusView = nameView;
                    cancel = true;
                } else if (TextUtils.isEmpty(phone)) {
                    phoneView.setError(getString(R.string.error_field_required));
                    focusView = phoneView;
                    cancel = true;
                }

                if(cancel) {
                    focusView.requestFocus();
                } else {
                    Contact contact = new Contact();
                    contact.setName(name);
                    contact.setPhone(phone);
                    createContact(contact);

                    listView.setAdapter(new SimpleAdapter(CreateContact.this, getData(), android.R.layout.simple_expandable_list_item_2, new String[]{"name", "phone"}, new int[]{android.R.id.text1, android.R.id.text2}));

                    Toast.makeText(getApplicationContext(), "Create Contact!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void createContact(Contact contact) {
        List<Contact> contactList = new ArrayList<Contact>();
        contactList.add(contact);
        this.manager.create(contactList);
    }

    private List<Map<String, String>> getData() {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        List<Contact> contactList = this.manager.query();

        for(Contact contact : contactList) {
            Map<String, String> contactMap = new HashMap<String, String>();

            contactMap.put("name", contact.getName());
            contactMap.put("phone", contact.getPhone());

            data.add(contactMap);
        }

        return data;
    }

}