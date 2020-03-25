package com.example.triplist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class TripActivity extends AppCompatActivity {
    ListView myList;
    /*SwipeRefreshLayout swipe;
    Map<String,ArrayList> dataList = new HashMap<String,ArrayList>();
    int mImgs[]={R.drawable.manali,R.drawable.manali,R.drawable.manali,R.drawable.manali,R.drawable.manali};
    int nImgs[]={R.drawable.manali,R.drawable.manali};*/
   ArrayList<String> titleList = new ArrayList<String>();
    ArrayList<String> descritpionList = new ArrayList<String>();
    ArrayList<Bitmap> imagesList = new ArrayList<Bitmap>();
    ImageView image;
    TextView titleText;
    TextView descriptionText;
    TextView noData;
    public static final String titleExtra = "TitleExtra";
    public static final String descriptionExtra = "DescExtra";
    public static final String MenuFlag = "MenuFlag";
    StorageOperation storage;
    DataBaseHandler database;

    /**
     * OnLoad of Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        database = new DataBaseHandler(TripActivity.this);
        /*storage = new StorageOperation(TripActivity.this);*/
        myList = findViewById(R.id.myList);
        noData = findViewById(R.id.noDataMessage);
        /*View emptyview = getLayoutInflater().inflate(R.layout.activity_empty,null);
        ((ViewGroup)myList.getParent()).addView(emptyview);
        myList.setEmptyView(emptyview);*/
        myList.setEmptyView(noData);
        getAllValues();
        /**
         * while swipe upon activity
         */
        /*swipe = findViewById(R.id.swipe_layout);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                titleList.clear();
                descritpionList.clear();
                imagesList.clear();
                getAllValues();
                swipe.setRefreshing(false);
            }
        });*/

        /**
         * When click on Listview
         */
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(TripActivity.this,AddActivity.class);
                newIntent.putExtra(titleExtra,titleList.get(position));
                newIntent.putExtra(descriptionExtra,descritpionList.get(position));
                newIntent.putExtra(MenuFlag,"1");
                startActivity(newIntent);
            }
        });
    }

    /**
     * actions onRestart the activity
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        titleList.clear();
        descritpionList.clear();
        imagesList.clear();
        getAllValues();
    }

    /**
     * When click back button
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit ?");
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                /*finish();*/
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TripActivity.super.onBackPressed();
                finish();
               /* Intent backIntent = new Intent(Intent.ACTION_MAIN);
                backIntent.addCategory(Intent.CATEGORY_HOME);
                startActivity(backIntent);
                storage.setLogin(0);*/

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * getAllValues From Database
     */
    public void getAllValues(){
       /* dataList = storage.getValue();
        titleList = dataList.get("Key");
        descritpionList = dataList.get("Val");*/
        Bitmap image = null;
        ArrayList<ImgSrc> source = database.getAllData();
        for(int i =0;i<source.size();i++){
            if((source.get(i).getPlace()).equals("")){
                titleList.add("No Data");
            }else{
                titleList.add(source.get(i).getPlace());
            }
            if((source.get(i).getDescription()).equals("")){
                descritpionList.add("No Description Available");
            }else{
                descritpionList.add(source.get(i).getDescription());
            }
            if((source.get(i).getImg().equals(""))){
                imagesList.add(image);
            }else{
                imagesList.add(source.get(i).getImg());
            }
        }
        MyAdapter adapter = new MyAdapter(this,titleList,descritpionList,imagesList);
        myList.setAdapter(adapter);
        registerForContextMenu(myList);
    }

    /**
     * create context menu
     * @param menu
     * @param view
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater conInflate = getMenuInflater();
        conInflate.inflate(R.menu.menu_context,menu);
    }

    /**
     * Perform action when click delete in context menu
     * @param item
     * @return boolean value
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId() == R.id.Delete){
            String place = titleList.get(info.position);
            int deleteCheck = database.Delete(place);
            if(deleteCheck != 0){
                Toast.makeText(this,"Deleted",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"Delete Failed",Toast.LENGTH_LONG).show();
            }
            onRestart();
        }
        return true;
    }

    /**
     * set Menu option
     * @param menu
     * @return boolean value
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Perform actions when click menu item
     * @param item
     * @return boolean value
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.DeleteAll :
                int deleteAllCheck = database.DeleteAll();
                if(deleteAllCheck != 0){
                    onRestart();
                    Toast.makeText(this,"All Data Deleted",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.logout :
                storage.setLogin(0);
                Intent mainIntent = new Intent(TripActivity.this,MainActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.upload:
                Intent uploadIntent = new Intent(TripActivity.this,AddActivity.class);
                uploadIntent.putExtra(MenuFlag,"0");
                startActivity(uploadIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Adapter Settings for Listview
     */
    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        ArrayList<String> adapterTitleList;
        ArrayList<String> adapterDescriptionList;
        ArrayList<Bitmap> adapterImagesList;

        MyAdapter(Context context,ArrayList<String> titleList, ArrayList<String> descriptionList,ArrayList<Bitmap> imagesList){
            super(context,R.layout.activity_listcontent,R.id.contentTitle,titleList);
            this.context = context;
            this.adapterTitleList = titleList;
            this.adapterDescriptionList =  descriptionList;
            this.adapterImagesList = imagesList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View listLayout = inflater.inflate(R.layout.activity_listcontent,parent,false);
            image = listLayout.findViewById(R.id.contentPic);
            titleText = listLayout.findViewById(R.id.contentTitle);
            descriptionText = listLayout.findViewById(R.id.contentDescription);

            image.setImageBitmap(adapterImagesList.get(position));
            titleText.setText(adapterTitleList.get(position).toString());
            descriptionText.setText(adapterDescriptionList.get(position).toString());
            return listLayout;
        }
    }
}

