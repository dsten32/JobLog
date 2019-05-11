package com.comp575.myapplication;

import android.os.Parcelable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ArrayList<Contact> contacts = new ArrayList<>();
    private ArrayAdapter<Contact> adapter;
    private ListView contactsListView;
    private ContactRepository contactRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        //adapter setup
        if(findViewById(R.id.contactsListView)!=null) {
            contactsListView = findViewById(R.id.contactsListView);

            contactsListView.setAdapter(adapter);
            contactsListView.setOnItemClickListener(this);

        }

        if(savedInstanceState != null){
            for (Parcelable contact : savedInstanceState.getParcelableArrayList("contacts")){
                contacts.add((Contact) contact);
            }
        } /*else {
            //hardwired contacts for testing
            contacts.add(new Contact("random Name", "email@place.com", "09856466"));
            contacts.add(new Contact("cooler Name", "email@place.com", "09856466"));
        }*/

        contactRepository = new ContactRepository(this);
        contactRepository.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> updatedContacts) {
// update the contacts list when the database changes
                adapter.clear();
                adapter.addAll(updatedContacts);
            }
        });

    }

    public void saveContact(View view){
        String status;
        EditText nameField = findViewById(R.id.name);
        String name = nameField.getText().toString();
        EditText emailField = findViewById(R.id.email);
        String email = emailField.getText().toString();
        EditText phoneField = findViewById(R.id.phone);
        String phone = phoneField.getText().toString();
        Contact contact=new Contact(name,email,phone);

        //see if contact is in the ArrayList
        if (contacts.contains(contact)){
            Contact existingContact = contacts.get(contacts.indexOf(contact));
            contact.id = existingContact.id;
            contactRepository.update(contact);
            status="Updated";
//            contacts.set(contacts.indexOf(contact),contact);
        } else {
            contactRepository.insert(contact);
            status="Saved";
//            contacts.add(contact);
        }

        adapter.notifyDataSetChanged();
        Toast.makeText(this,status + " contact details for \n" + contact.name + "\nEmail: " + contact.email + "\nMobile: " + contact.mobile,Toast.LENGTH_SHORT).show();
    }

    public void deleteContact(View view){
        EditText nameField = findViewById(R.id.name);
        String name = nameField.getText().toString();
        EditText emailField = findViewById(R.id.email);
        String email = emailField.getText().toString();
        EditText phoneField = findViewById(R.id.phone);
        String phone = phoneField.getText().toString();
        Contact contact=new Contact(name,email,phone);

        //see if contact is in the ArrayList
        if (contacts.contains(contact)){
            Contact existingContact = contacts.get(contacts.indexOf(contact));
            contact.id = existingContact.id;
            contactRepository.delete(contact);
//            contacts.set(contacts.indexOf(contact),contact);
        } else {
            contactRepository.insert(contact);
//            contacts.add(contact);
        }

        adapter.notifyDataSetChanged();
        Toast.makeText(this,"Deleted contact details for \n" + contact.name,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedState) {
        savedState.putParcelableArrayList("contacts",contacts);
        super.onSaveInstanceState(savedState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contact contact = (Contact) parent.getAdapter().getItem(position);
        EditText nameField = findViewById(R.id.name);
        nameField.setText(contact.name);
        EditText emailField = findViewById(R.id.email);
        emailField.setText(contact.email);
        EditText phoneField = findViewById(R.id.phone);
        phoneField.setText(contact.mobile);

        Toast.makeText(parent.getContext(), "Clicked " + contact,
                Toast.LENGTH_SHORT).show();

    }
}
