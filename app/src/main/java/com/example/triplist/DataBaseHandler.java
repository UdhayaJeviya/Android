package com.example.triplist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DataBaseHandler extends SQLiteOpenHelper {
    Context context;
    private static String Query = "create table tripList (image BLOB,place TEXT,description TEXT)";
    ByteArrayOutputStream byteArrayOuyputStream;
    byte[] imageByte;

    public DataBaseHandler(Context context) {
        super(context, "myDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(Query);
        }catch (Exception e){

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Return Particular image for given place
     * @param place
     * @return image
     */
    public Bitmap getImage(String place){
        byte[] blobImage;
        Bitmap img = null;
        SQLiteDatabase getImageDb = null;
        try{
            getImageDb = this.getWritableDatabase();
            Cursor dataObject = getImageDb.rawQuery("select image from tripList WHERE place = ? ",new String[] {place});
            if(dataObject.getCount() != 0){
                while (dataObject.moveToNext()){
                    blobImage = dataObject.getBlob(0);
                    img = BitmapFactory.decodeByteArray(blobImage,0,blobImage.length);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            getImageDb.close();
        }
        return img;
    }

    /**
     * Perform Insert and Update Operation
     * @param object
     * @param action
     * @return boolean value
     */
    public String saveData(ImgSrc object,int action){
        String success = "";
        long insert = 0;
        int update = 0;
        SQLiteDatabase saveDataDb = null;
        try{
            byteArrayOuyputStream = new ByteArrayOutputStream();
            saveDataDb = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            Bitmap image = object.getImg();
            image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOuyputStream);
            imageByte = byteArrayOuyputStream.toByteArray();
            String place = object.getPlace();
            contentValues.put("place",object.getPlace());
            contentValues.put("image",imageByte);
            contentValues.put("description",object.getDescription());
            int duplicateCount = Duplicate(place,saveDataDb);
            if(action == 0){
                if(duplicateCount != 0){
                    return success = "duplicate";
                }else{
                    insert = saveDataDb.insert("tripList",null,contentValues);
                }
            }else{
                update = saveDataDb.update("tripList",contentValues,"place = ?",new String[]{place});
            }
            if(insert != 0){
                success = "inserted";
            }
            if(update != 0){
                success = "updated";
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            saveDataDb.close();
        }
        return success;
    }

    /**
     * Perform the operation of retrieving all data
     * @return source
     */
    public ArrayList<ImgSrc> getAllData(){
        ArrayList<ImgSrc> source = new ArrayList<ImgSrc>();
        String place;
        String description;
        byte[] byteImg;
        Bitmap image;
        SQLiteDatabase getAllDb = null;
        try{
            getAllDb = this.getReadableDatabase();
            Cursor dataObject = getAllDb.rawQuery("select * from tripList",null);
            if(dataObject.getCount() != 0){
                while (dataObject.moveToNext()){
                    byteImg = dataObject.getBlob(0);
                    place = dataObject.getString(1);
                    description = dataObject.getString(2);
                    image = BitmapFactory.decodeByteArray(byteImg,0,byteImg.length);
                    source.add(new ImgSrc(place,image,description));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            getAllDb.close();
        }
        return source;
    }

    /**
     * Delete All the data from Table
     * @return int value
     */
    public int DeleteAll(){
        SQLiteDatabase deleteAllDb = null;
        int result = 0;
        try{
            deleteAllDb = this.getWritableDatabase();
            result = deleteAllDb.delete("tripList",null,null);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            deleteAllDb.close();
        }
        return result;
    }

    /**
     * Delete Particular Data in table
     * @param place
     * @return
     */
    public int Delete(String place){
        SQLiteDatabase deleteDb = null;
        int result = 0;
        try{
            deleteDb = this.getWritableDatabase();
            result = deleteDb.delete("tripList","place = ?",new String[] {place});
        }catch (Exception e){

        }finally {
            deleteDb.close();
        }
        return  result;
    }

    public int Duplicate(String place,SQLiteDatabase db){
        int duplicate = 0;
        try{
            Cursor dataObject = db.rawQuery("select * from tripList WHERE place = ? ",new String[] {place});
            if(dataObject.getCount() != 0){
                duplicate++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return duplicate;
    }
/* Delete Table*/
    /*public void delete(){
        try{
            SQLiteDatabase dele = this.getWritableDatabase();
            int a = dele.delete("tripList",null,null);
            dele.close();
        }catch (Exception e){

        }
    }*/
}
