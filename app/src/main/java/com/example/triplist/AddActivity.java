package com.example.triplist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class AddActivity extends AppCompatActivity {
    ImageView picture;
    TextView place;
    TextView description;
    Button save;
    /*public static final String file_name = "trip.txt";
    FileOutputStream outputStream;
    ByteArrayOutputStream byteArrayOuyputStream;
    byte[] imageByte;
    ImageView getim;*/
    DataBaseHandler dataBaseHandler;
    int MenuFlg;

    /**
     * On load of Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent intent = getIntent();
        MenuFlg = Integer.parseInt(intent.getStringExtra(TripActivity.MenuFlag));
        picture = (ImageView)findViewById(R.id.edtPicture);
        place = (TextView)findViewById(R.id.edtPlace);
        description = (TextView)findViewById(R.id.edtSubject);
        save = (Button)findViewById(R.id.edtSave);
        dataBaseHandler = new DataBaseHandler(this);
        if(MenuFlg == 1){
            String editTitle = intent.getStringExtra(TripActivity.titleExtra);
            String editDescription = intent.getStringExtra(TripActivity.descriptionExtra);
            Bitmap getImage = dataBaseHandler.getImage(editTitle);
            place.setText(editTitle);
            description.setText(editDescription);
            picture.setImageBitmap(getImage);
        }

        /**
         * when click on imageview
         */
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,1);*/
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galleryIntent,1);
            }
        });
        /**
         * When click on save button
         */
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uploadAction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        }

    /**
     * while selcting image from storage
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
                Uri image = data.getData();
                Bitmap im = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                picture.setImageBitmap(im);
                /*picture.setImageURI(image);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save Operation
     */
    public void uploadAction(){
        String placeName = place.getText().toString();
        String descriptionDetails = description.getText().toString();
        BitmapDrawable draw = (BitmapDrawable)picture.getDrawable();
        Bitmap bitmapImage = draw.getBitmap();
        String result = dataBaseHandler.saveData(new ImgSrc(placeName,bitmapImage,descriptionDetails),MenuFlg);
        if(MenuFlg == 0){
            if(result.equals("duplicate")){
                Toast.makeText(this,"Duplicate value",Toast.LENGTH_LONG).show();
            }else if(result.equals("inserted")){
                Toast.makeText(this,"Added Successfully",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"Add Operation failed",Toast.LENGTH_LONG).show();
            }
        }else{
            if(result.equals("updated")){
                Toast.makeText(this,"Update Successfully",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"update failed",Toast.LENGTH_LONG).show();
            }
        }

        Bitmap emptyImage = null;
        place.setText("");
        description.setText("");
        picture.setImageBitmap(emptyImage);
        /*FileInputStream fis = null;
        try {
            fis = openFileInput(file_name);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while((text = br.readLine()) != null){
                sb.append(text).append("\n");
            }
            placeName.setText(sb.toString()s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*SharedPreferences sp = getPreferences(MODE_PRIVATE);*/
      /*  String key = placeName.getText().toString();*/
        /*String val = sp.getString(key,null);*/
        /*StorageOperation so1 = new StorageOperation(AddActivity.this);
        String val = so1.getValue();
        subject.setText(val);
        placeName.setText(key);*/
        /*if(val != null){
            subject.setText(val);
            placeName.setText(key);
        }*/
        /*BitmapDrawable draw = (BitmapDrawable)picture.getDrawable();
        Bitmap bitmappic = draw.getBitmap();
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath()+"/trip");
        directory.mkdir();
        File outFile = new File(directory,"chennai"+".jpg");
        try {
            outputStream = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmappic.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outFile));
            sendBroadcast(intent);*/
        /*String name = placeName.getText().toString();
        byteArrayOuyputStream = new ByteArrayOutputStream();
        BitmapDrawable draw = (BitmapDrawable)picture.getDrawable();
        Bitmap bitmappic = draw.getBitmap();
        bitmappic.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOuyputStream);
        imageByte = byteArrayOuyputStream.toByteArray();
        boolean check = dataBaseHandler.storeImage(name, imageByte);
        if(check){
            Toast.makeText(this,"success",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"fail",Toast.LENGTH_LONG).show();
        }*/
    }
        public void saveAction() throws IOException {
            /*String place = placeName.getText().toString();
            String sub = subject.getText().toString();
            StorageOperation so = new StorageOperation(AddActivity.this);
            so.setValue(place,sub);
            Toast.makeText(this,"Saved to "+getFilesDir()+"/"+file_name,Toast.LENGTH_LONG).show();
            placeName.setText("");
            subject.setText("");*/
           /* ArrayList<ImgSrc> a = dataBaseHandler.getAllImages();*/
            /*Toast.makeText(this,a.size(),Toast.LENGTH_LONG).show();*/
            /*Bitmap b = a.get(0).getImg();
            getim.setImageBitmap(b);*/
        }
    }

