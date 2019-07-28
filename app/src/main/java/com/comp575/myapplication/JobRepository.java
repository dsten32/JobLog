package com.comp575.myapplication;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class JobRepository {
    private JobDao jobDao;
    private LiveData<List<Job>> allJobs;
    JobRepository(Context context) {
        JobRoomDatabase db = JobRoomDatabase.getDatabase(context);
        jobDao = db.jobDao();
        allJobs = jobDao.getAllJobs();
    }
    public void insert(Job job) {
        new InsertAsyncTask().execute(job);
    }
    private class InsertAsyncTask extends AsyncTask<Job, Void, Void> {
        @Override
        protected Void doInBackground(final Job... params) {
            for (Job job : params) {
                jobDao.insert(job);
            }
            return null;
        }
    }
    public void update(Job job) {
        new UpdateAsyncTask().execute(job);
    }
    private class UpdateAsyncTask extends AsyncTask<Job, Void, Void> {
        @Override
        protected Void doInBackground(final Job... params) {
            for (Job job : params) {
                jobDao.update(job);
            }
            return null;
        }
    }
    public void delete(Job job) {
        new DeleteAsyncTask().execute(job);
    }
    private class DeleteAsyncTask extends AsyncTask<Job, Void, Void> {
        @Override
        protected Void doInBackground(final Job... params) {
            for (Job job : params) {
                jobDao.delete(job);
            }
            return null;
        }
    }

    public LiveData<List<Job>> getAllJobs() {
        return allJobs;
    }
}

