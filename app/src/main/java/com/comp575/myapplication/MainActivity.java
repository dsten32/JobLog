package com.comp575.myapplication;

import android.content.Context;
import android.os.Parcelable;
import androidx.appcompat.app.AppCompatActivity;


import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {
    //contacts variables
    private ArrayList<Contact> contacts = new ArrayList<>();
    private ArrayAdapter<Contact> adapter;
    private ListView contactsListView;
    private ContactRepository contactRepository;

    //search variables
    private SearchView editSearch;

    private Context context;

    private int screenOrientation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        screenOrientation = ((WindowManager)  context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        setContentView(R.layout.activity_main);
        if(screenOrientation==3){
            View listbutton = findViewById(R.id.listbutton);
            listbutton.setVisibility(View.GONE);
            View contactbutton = findViewById(R.id.editbutton);
            contactbutton.setVisibility(View.GONE);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        //adapter setup
        if (findViewById(R.id.contactsListView) != null) {
            contactsListView = findViewById(R.id.contactsListView);
            contactsListView.setAdapter(adapter);
            contactsListView.setOnItemClickListener(this);
        }


        // Locate the EditText in listview_main.xml, or not id portrait view
        editSearch = findViewById(R.id.search);
        if (editSearch != null) {
            editSearch.setOnQueryTextListener(this);
        }

        if (savedInstanceState != null) {
            for (Parcelable contact : savedInstanceState.getParcelableArrayList("contacts")) {
                contacts.add((Contact) contact);
            }
        }

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

    public void saveContact(View view) {
        String status;
        EditText nameField = findViewById(R.id.name);
        String name = nameField.getText().toString();
        EditText emailField = findViewById(R.id.email);
        String email = emailField.getText().toString();
        EditText phoneField = findViewById(R.id.phone);
        String phone = phoneField.getText().toString();
        Contact contact = new Contact(name, email, phone);

        //see if contact is in the ArrayList
        if (contacts.contains(contact)) {
            Contact existingContact = contacts.get(contacts.indexOf(contact));
            contact.id = existingContact.id;
            contactRepository.update(contact);
            status = "Updated";
//            contacts.set(contacts.indexOf(contact),contact);
        } else {
            contactRepository.insert(contact);
            status = "Saved";
//            contacts.add(contact);
        }

        adapter.notifyDataSetChanged();
        Toast.makeText(this, status + " contact details for \n" + contact.name + "\nEmail: " + contact.email + "\nMobile: " + contact.mobile, Toast.LENGTH_SHORT).show();
    }

    public void deleteContact(View view) {
        EditText nameField = findViewById(R.id.name);
        String name = nameField.getText().toString();
        EditText emailField = findViewById(R.id.email);
        String email = emailField.getText().toString();
        EditText phoneField = findViewById(R.id.phone);
        String phone = phoneField.getText().toString();
        Contact contact = new Contact(name, email, phone);

        //see if contact is in the ArrayList
        if (contacts.contains(contact)) {
            Contact existingContact = contacts.get(contacts.indexOf(contact));
            contact.id = existingContact.id;
            contactRepository.delete(contact);
//            contacts.set(contacts.indexOf(contact),contact);
        } else {
            contactRepository.insert(contact);
//            contacts.add(contact);
        }

        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Deleted contact details for \n" + contact.name, Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        Filter filter = adapter.getFilter();
        filter.filter(text);
        return false;
    }

    public void flipView(View view) {
        ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
        viewFlipper.setInAnimation(in);
        viewFlipper.setOutAnimation(out);
        // you can switch between next and previous layout and display it
        viewFlipper.showNext();
    }


    public void selectImage(View view) {
    }

}
