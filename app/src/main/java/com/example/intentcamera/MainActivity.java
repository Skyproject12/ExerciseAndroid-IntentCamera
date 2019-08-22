package com.example.intentcamera;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

    Button buttonTakePic;
    ImageView imageTake;
    String pathToFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonTakePic= findViewById(R.id.buttonTakePic);
        imageTake= findViewById(R.id.image);
        if(Build.VERSION.SDK_INT>=23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        buttonTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPictureTakerTakerAction();
            }
        });
    }

    private void dispatchPictureTakerTakerAction() {
        // intent to camera
        Intent takePic= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePic.resolveActivity(getPackageManager())!=null){
            File photoFile= null;
            try{
                photoFile=createPhotoFile();
            }
            catch (Exception e){

            }
            if(photoFile!=null) {
                Toast.makeText(this, "hasil", Toast.LENGTH_SHORT).show();
                pathToFile = photoFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(MainActivity.this, "com.example.intentcamera.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePic,1);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "te"+requestCode+"result code"+resultCode, Toast.LENGTH_SHORT).show();
        if(resultCode==RESULT_OK){
            if(requestCode==1){
                Toast.makeText(this, "message", Toast.LENGTH_SHORT).show();
                Bitmap bitmap= BitmapFactory.decodeFile(pathToFile);
                imageTake.setImageBitmap(bitmap);
                ByteArrayOutputStream stream= new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100, stream);
                byte[] buffer= stream.toByteArray();
            }
        }
    }

    private File createPhotoFile() {
        String name= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir= getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=null;
        try {
            image= File.createTempFile(name,".jpg",storageDir);
        } catch (IOException e) {
            Log.d("mylog","Excep :"+e.toString());
        }
        return image;
    }
}
