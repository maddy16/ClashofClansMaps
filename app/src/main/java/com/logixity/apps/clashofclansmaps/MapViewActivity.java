package com.logixity.apps.clashofclansmaps;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MapViewActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=110;
    int imageId;
    GestureImageView expandedImageView;
    private AdView mAdView;
    static int option=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        imageId = getIntent().getIntExtra("mapImage",0);
        expandedImageView = (GestureImageView) findViewById(R.id.mapZoomImage);
        expandedImageView.setImageResource(imageId);
        mAdView = (AdView) findViewById(R.id.bannerAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    public void shareImgClicked(View v) {
        option=1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        } else{
            permissionAllowed();
        }
    }
    public void downloadImgClicked(View v) {
        option=2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        } else{
            permissionAllowed();
        }
    }
    @TargetApi(23)
    public void checkPermissions(){
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else{
            permissionAllowed();
        }
    }
    public void permissionAllowed(){
        if(option==1){
            shareImage();
        } else if(option==2) {
            downloadImage();
        }

    }
    void downloadImage(){
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),imageId);
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"map_"+System.currentTimeMillis()+".jpg";
        OutputStream out = null;
        File file=new File(path);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        path=file.getPath();


        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, path);

        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Toast.makeText(this, "Map Saved to Gallery", Toast.LENGTH_SHORT).show();
    }
    void shareImage(){
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),imageId);
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/mapShare.jpg";
        OutputStream out = null;
        File file=new File(path);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        path=file.getPath();
        Uri bmpUri = Uri.parse("file://"+path);
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Check out Clash of Clans Exclusive Maps. Live at the Playstore.\nhttps://play.google.com/store/apps/details?id="+getPackageName());
        shareIntent.setType("image/png");
        startActivity(Intent.createChooser(shareIntent,"Share with"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionAllowed();
                } else {
                    Toast.makeText(this, "Application Requires Permission for sharing", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
