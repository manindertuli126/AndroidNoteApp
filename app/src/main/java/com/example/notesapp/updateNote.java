package com.example.notesapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class updateNote extends AppCompatActivity {

    database updatenotedb;
    String Notecategoryid = "";
    String Noteid = "";
    private EditText usernotetitle;
    private EditText usernotedetail;
    private EditText usernotecategory;
    String currentnotedate = "";
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String fullAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-YYYY hh:mm aaa");
        currentnotedate = df.format(c.getTime());

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        getSupportActionBar().setTitle("Update Note");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usernotecategory = findViewById(R.id.update_category);
        usernotetitle = findViewById(R.id.update_title);
        usernotedetail = findViewById(R.id.update_detail);

        Intent intent = getIntent();
        Notecategoryid = intent.getStringExtra("passcategoryid");
        Noteid = intent.getStringExtra("passnoteid");

        updatenotedb = new database(this);
        if(Notecategoryid != ""){
            Cursor catresult = updatenotedb.selectallNoteListTableWhere(Noteid);
            if (catresult.getCount() == 0) {
                Log.i("**DB**", "FAIL");
            } else {
                while (catresult.moveToNext()) {
                    usernotetitle.setText(catresult.getString(0));
                    usernotedetail.setText(catresult.getString(1));
                }
            }
        }
        if(Notecategoryid != ""){
            Cursor catresult = updatenotedb.selectCategoryTableWhere(Notecategoryid);
            if (catresult.getCount() == 0) {
                Log.i("**DB**", "FAIL");
            } else {
                while (catresult.moveToNext()) {
                    usernotecategory.setText(catresult.getString(0));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(item.getItemId()){
            case R.id.update_note:
                successAlert();
                break;

            case R.id.delete_note:
                deleteAlert();
                break;

            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void successAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SUCCESS !!");
        builder.setMessage("Note Updated successfully ..")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //insert note to db
                        updatenotedb.updatelistnoteTable(Noteid,Notecategoryid,usernotetitle.getText().toString(),usernotedetail.getText().toString(),currentnotedate,fullAddress);
                         //navigate to note list
                        Intent mIntent = new Intent(updateNote.this, noteList.class);
                        mIntent.putExtra("passcategoryid", "");
                        mIntent.putExtra("passcategoryid", Notecategoryid);
                        startActivity(mIntent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void deleteAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("DELETED !!");
        builder.setMessage("Note deleted successfully ..")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //insert note to db
                        updatenotedb.deleteFromNoteListTable(Noteid);
                        //navigate to note list
                        Intent mIntent = new Intent(updateNote.this, noteList.class);
                        mIntent.putExtra("passcategoryid", "");
                        mIntent.putExtra("passcategoryid", Notecategoryid);
                        startActivity(mIntent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void getLocation() {
        DecimalFormat df = new DecimalFormat("#.######");
        if (ActivityCompat.checkSelfPermission(updateNote.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (updateNote.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(updateNote.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = Double.parseDouble(df.format(location1.getLatitude()));
                double longi = Double.parseDouble(df.format(location1.getLongitude()));
                setAddress(latti, longi);
            } else if (location1 != null) {
                double latti = Double.parseDouble(df.format(location1.getLatitude()));
                double longi = Double.parseDouble(df.format(location1.getLongitude()));
                setAddress(latti, longi);
            } else if (location2 != null) {
                double latti = Double.parseDouble(df.format(location1.getLatitude()));
                double longi = Double.parseDouble(df.format(location1.getLongitude()));
                setAddress(latti, longi);
            } else {
                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void setAddress(Double latitude, Double longitude) {

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses.size() > 0) {
            Log.d("max", " " + addresses.get(0).getMaxAddressLineIndex());

            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            fullAddress = city+", "+state+", "+country;
        }
    }
}
