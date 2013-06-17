package com.sicau.dbdemo;

import android.app.Activity;
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
public class UpdateContact extends Activity {
    private ListView listView;

    private DBManager manager;

    private EditText nameView;
    private EditText phoneView;

    private int contactId;
    private List<Integer> ids;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(), "back button pressed!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(UpdateContact.this, ListActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Note:manager will be used in getData, so need to initialized ahead of this function
        this.manager = new DBManager(this);

        setContentView(R.layout.update_contact);

        listView = (ListView) findViewById(R.id.updateListView);
        listView.setAdapter(new SimpleAdapter(this, getData(), android.R.layout.simple_expandable_list_item_2, new String[]{"name", "phone"}, new int[]{android.R.id.text1, android.R.id.text2}));

        nameView = (EditText) findViewById(R.id.updateNameText);
        phoneView = (EditText) findViewById(R.id.updatePhoneText);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Map<String, String> contactMap = getData().get(position);

                UpdateContact.this.contactId = ids.get(position);

                nameView.setText(contactMap.get("name"));
                phoneView.setText(contactMap.get("phone"));

                Toast.makeText(getApplicationContext(), contactMap.get("name") ,Toast.LENGTH_SHORT).show();
            }

        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
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
                    contact.set_id(UpdateContact.this.contactId);
                    contact.setName(name);
                    contact.setPhone(phone);
                    Toast.makeText(getApplicationContext(), name + " " + phone  ,Toast.LENGTH_SHORT).show();

                    manager.update(contact);

                    listView.setAdapter(new SimpleAdapter(UpdateContact.this, getData(), android.R.layout.simple_expandable_list_item_2, new String[]{"name", "phone"}, new int[]{android.R.id.text1, android.R.id.text2}));
                }

            }
        });

    }

    private List<Map<String, String>> getData() {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        List<Contact> contactList = this.manager.query();

        this.ids = new ArrayList<Integer>();

        for(Contact contact : contactList) {
            Map<String, String> contactMap = new HashMap<String, String>();

            contactMap.put("name", contact.getName());
            contactMap.put("phone", contact.getPhone());

            this.ids.add(contact.get_id());

            data.add(contactMap);
        }

        return data;
    }
}