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
    private ArrayList<Job> jobs = new ArrayList<>();
    private ArrayAdapter<Job> adapter;
    private ListView jobsListView;
    private JobRepository jobRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, jobs);
        //adapter setup
        if(findViewById(R.id.jobsListView)!=null) {
            jobsListView = findViewById(R.id.jobsListView);

            jobsListView.setAdapter(adapter);
            jobsListView.setOnItemClickListener(this);

        }

        if(savedInstanceState != null){
            for (Parcelable job : savedInstanceState.getParcelableArrayList("jobs")){
                jobs.add((Job) job);
            }


        jobRepository = new JobRepository(this);
        jobRepository.getAllJobs().observe(this, new Observer<List<Job>>() {
            @Override
            public void onChanged(List<Job> updatedJobs) {
// update the jobs list when the database changes
                adapter.clear();
                adapter.addAll(updatedJobs);
            }
        });

    }

    public void saveJob(View view){
        String status;
        EditText nameField = findViewById(R.id.name);
        String name = nameField.getText().toString();
        EditText emailField = findViewById(R.id.email);
        String email = emailField.getText().toString();
        EditText phoneField = findViewById(R.id.phone);
        String phone = phoneField.getText().toString();
        Job job=new Job(name,email,phone);

        //see if job is in the ArrayList
        if (jobs.contains(job)){
            Job existingJob = job.get(jobs.indexOf(job));
            job.id = existingJob.id;
            jobRepository.update(job);
            status="Updated";
        } else {
            jobRepository.insert(job);
            status="Saved";
        }

        adapter.notifyDataSetChanged();
        Toast.makeText(this,status + " job details for \n" + job.name + "\nEmail: " + job.email + "\nMobile: " + job.mobile,Toast.LENGTH_SHORT).show();
    }

    public void deleteJob(View view){
        EditText nameField = findViewById(R.id.name);
        String name = nameField.getText().toString();
        EditText emailField = findViewById(R.id.email);
        String email = emailField.getText().toString();
        EditText phoneField = findViewById(R.id.phone);
        String phone = phoneField.getText().toString();
        Job job=new Job(name,email,phone);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
 builder.setTitle("Confirm delete Job?!");
 builder.setMessage("You are about to delete this job from your records. Do you really want to proceed?");
 builder.setCancelable(false);
 builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
     @Override
     public void onClick(DialogInterface dialog, int which) {
         if (jobs.contains(job)){
            Job existingJob = jobs.get(jobs.indexOf(job));
            job.id = existingJob.id;
            jobRepository.delete(Job);
        } else {
            jobRepository.insert(job);
        }

        adapter.notifyDataSetChanged();
        Toast.makeText(this,"Deleted job details for \n" + job.name,Toast.LENGTH_SHORT).show();
     }
 });
 
 builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
     @Override
     public void onClick(DialogInterface dialog, int which) {
         Toast.makeText(getApplicationContext(), "You've changed your mind to delete", Toast.LENGTH_SHORT).show();
     }
 });
 
 builder.show();
        
        //see if job is in the ArrayList
//         if (jobs.contains(job)){
//             Job existingJob = jobs.get(jobs.indexOf(job));
//             job.id = existingJob.id;
//             jobRepository.delete(Job);
//         } else {
//             jobRepository.insert(job);
//         }

//         adapter.notifyDataSetChanged();
//         Toast.makeText(this,"Deleted job details for \n" + job.name,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedState) {
        savedState.putParcelableArrayList("jobs",jobs);
        super.onSaveInstanceState(savedState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Job job = (Job) parent.getAdapter().getItem(position);
        EditText nameField = findViewById(R.id.name);
        nameField.setText(job.name);
        EditText emailField = findViewById(R.id.email);
        emailField.setText(job.email);
        EditText phoneField = findViewById(R.id.phone);
        phoneField.setText(job.mobile);

        Toast.makeText(parent.getContext(), "Clicked " + job,
                Toast.LENGTH_SHORT).show();

    }
}
