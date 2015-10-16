package com.example.dendimon.scbs;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button btnBp = (Button) findViewById(R.id.btnBackup);
        btnBp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backupIntent = new Intent(MainActivity.this,VcardActivity_All.class);
                startActivity(backupIntent);
            }
        });

        Button btnRestore = (Button) findViewById(R.id.btnRestore);
        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  Uri stuff = Uri.parse("/sdcard/SCBS/1444968617320/");
                Intent i = new Intent (Intent.ACTION_VIEW);
                i.setType("text/x-vcard");
                startActivity(i);

            }
        });

        Button btnGetSMS = (Button) findViewById(R.id.btnGetSMS);
        btnGetSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getSMS();
                cursor.moveToFirst();




                if(cursor.getCount()>0){

                    for(int i = 0;i<cursor.getCount();i++){
                        String mid = cursor.getString(cursor.getColumnIndex("_id"));
                        String mbody = cursor.getString(cursor.getColumnIndex("body"));
                        String maddress = cursor.getString(cursor.getColumnIndex("address"));
                        String mread = cursor.getString(cursor.getColumnIndex("read"));
                        String mseen = cursor.getString(cursor.getColumnIndex("seen"));

                        Log.d("mid", mid);
                        Log.d("mbody",mbody);
                        Log.d("maddress",maddress);
                        Log.d("mread",mread);
                        Log.d("mseen",mseen);

                        cursor.moveToNext();

                    }


//                    while (cursor.moveToNext()){
//                        String sender = cursor.getString(2);
//                        String content = cursor.getString(12);
//                        Log.d("sender", sender);
//                        Log.d("content",content);
//                    }
                }

            }
        });
    }

    public void displayContacts(View v){
        Cursor cursor = getContacts();
//        String[] name = new String[10];
//        String[] id = new String[10];
        cursor.moveToFirst();
       // Log.d("ID", (cursor.getCount()));
        Log.d("get count", String.valueOf(cursor.getCount()) );

        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
             //   if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0){
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String lookup = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                String raw = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID));
                Log.d("ID", id);
                Log.d("Name",name);
                Log.d("Lookup",lookup);
                Log.d("raw",raw);
                ;//}
            }
        }

//        for (int i = 0; i < cursor.getCount();i++){
//            id[i] = cursor.getString(0);
//            name[i] = cursor.getString(1);
//            cursor.moveToNext();
//            Log.d("ID", id[i]);
//            Log.d("Name",name[i]);
//        }
        cursor.close();

    }

    public Cursor getSMS (){
        ContentResolver cr = getContentResolver();

        try{
            Uri uri = Uri.parse("content://sms");
            Cursor SMSL = cr.query(uri, null, null, null, "date asc");
            return SMSL;


        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    public Cursor getContacts(){
        ContentResolver cr = getContentResolver();
        try {
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            return cr.query(uri,null,null,null,null);

        }
        catch (Exception ex){
            String message = ex.getMessage();
            return null;
        }
    }

}
