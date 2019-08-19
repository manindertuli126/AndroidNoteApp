package com.example.notesapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class newNotes extends AppCompatActivity {

    database newnotedb;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    String newNotecategoryid = "";
    String currentnotedate = "";
    private EditText usernotetitle;
    private EditText usernotedetail;
    private EditText usernotecategory;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String fullAddress;
    ImageView imageView;
    Bitmap imageBit;
    private static int RESULT_LOAD_IMG = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notes);

        Intent intent = getIntent();
        newNotecategoryid = intent.getStringExtra("passcategoryid");

        usernotecategory = findViewById(R.id.enter_category);
        usernotetitle = findViewById(R.id.enter_title);
        usernotedetail = findViewById(R.id.enter_detail);
        imageView = (ImageView)findViewById(R.id.gallery);

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

        newnotedb = new database(this);
        if (newNotecategoryid != "") {
            Cursor catresult = newnotedb.selectCategoryTableWhere(newNotecategoryid);
            if (catresult.getCount() == 0) {
                Log.i("**DB**", "FAIL");
            } else {
                while (catresult.moveToNext()) {
                    usernotecategory.setText(catresult.getString(0));
                }
            }
        }

        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.save_new_note:
                successAlert();
                break;
            case R.id.image_new_note:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                   break;
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (data != null){
            Uri imageuri = data.getData();
            try{
                Log.e("Imageselected", "imagee");
                Bitmap bit = MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                imageBit = bit;
                imageView.setImageBitmap(bit);
            }catch(IOException e){
            e.printStackTrace();
            }
        }
    }

    private void getLocation() {
        DecimalFormat df = new DecimalFormat("#.######");
        if (ActivityCompat.checkSelfPermission(newNotes.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (newNotes.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(newNotes.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

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
                Toast.makeText(this, "Unable to Trace your location", Toast.LENGTH_SHORT).show();
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

    public void successAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SUCCESS !!");
        builder.setMessage("Note saved successfully ..")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //insert note to db
                        newnotedb.insertNewNoteTable(newNotecategoryid, usernotetitle.getText().toString(), usernotedetail.getText().toString(), currentnotedate, fullAddress);
//                        String noteid = "";
//                        Cursor catresult = newnotedb.selectNoteTitleTableWhere(usernotetitle.getText().toString());
//                        if (catresult.getCount() == 0) {
//                            Log.i("**DB**", "FAIL");
//                        } else {
//                            while (catresult.moveToNext()) {
//                                noteid = catresult.getString(0);
//                            }
//                        }
//
//                        String bittostringimage = BitMapToString(imageBit);
//                        boolean insertimage = newnotedb.insertImage(bittostringimage,noteid);
//                        if (resultforinsert == true) {
//                            Toast.makeText(newNotes.this, "Data inserted", Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(newNotes.this, "Data not inserted", Toast.LENGTH_LONG).show();
//                        }
                        //navigate to note list
                        Intent mIntent = new Intent(newNotes.this, noteList.class);
                        mIntent.putExtra("passcategoryid", "");
                        mIntent.putExtra("passcategoryid", newNotecategoryid);
                        startActivity(mIntent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

//    public String saveImage(Bitmap myBitmap) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        myBitmap.createScaledBitmap(myBitmap,(int)(myBitmap.getWidth()*0.8), (int)(myBitmap.getHeight()*0.8), true);
//
//        myBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
//        File wallpaperDirectory = new File(
//                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
//        // have the object build the directory structure, if needed.
//        if (!wallpaperDirectory.exists()) {
//            wallpaperDirectory.mkdirs();
//        }
//
//        try {
//            File f = new File(wallpaperDirectory, Calendar.getInstance()
//                    .getTimeInMillis() + ".jpg");
//            f.createNewFile();
//            FileOutputStream fo = new FileOutputStream(f);
//            fo.write(bytes.toByteArray());
//            MediaScannerConnection.scanFile(this,
//                    new String[]{f.getPath()},
//                    new String[]{"image/jpeg"}, null);
//            fo.close();
//            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
//
//            return f.getAbsolutePath();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        return "";
//    }


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