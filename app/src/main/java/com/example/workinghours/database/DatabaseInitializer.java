package com.example.workinghours.database;

import android.os.AsyncTask;
import android.util.Log;

import com.example.workinghours.database.entity.ActivityEntity;
import com.example.workinghours.database.entity.ProjectEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseInitializer {

    public static final String TAG = "DatabaseInitializer";

    public static void populateDatabase(final AppDataBase db) {
        Log.i(TAG, "Inserting demo data.");
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    private static void addProject(final AppDataBase db, final String projectName) {
        ProjectEntity project = new ProjectEntity(projectName);
        db.projectDao().insert(project);
    }

    private static void addActivity(final AppDataBase db, final String name, final Date start, final Date finish,
                                   final String owner) {
        ActivityEntity activity = new ActivityEntity(name, start, finish, owner);
        db.activityDao().insert(activity);
    }

    private static void populateWithTestData(AppDataBase db) throws ParseException {
        db.projectDao().deleteAll();

        addProject(db,
                "myFirstProject");
        addProject(db,
                "mySecondProject");
        addProject(db,
                "myThirdProject");

        try {
            // Let's ensure that the projects are already stored in the database before we continue.
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        addActivity(db,
                "Planning", simpleDateFormat.parse("28/10/2020 14:15:30") , simpleDateFormat.parse("28/10/2020 15:15:30"),
                "myFirstProject");

        addActivity(db,
                "Coding", simpleDateFormat.parse("28/10/2020 15:25:30") , simpleDateFormat.parse("28/10/2020 17:15:30"),
                "myFirstProject");
        addActivity(db,
                "Design", simpleDateFormat.parse("28/10/2020 18:25:30") , simpleDateFormat.parse("28/10/2020 19:15:30"),
                "mySecondProject");
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDataBase database;

        PopulateDbAsync(AppDataBase db) {
            database = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            try {
                populateWithTestData(database);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
