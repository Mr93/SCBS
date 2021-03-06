package com.example.dendimon.scbs;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends Activity {
  //  final String myPackageName = getPackageName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/




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


              /*  Intent sendIntent = new Intent(Intent.ACTION_INSERT);
                sendIntent.putExtra("sms_body", "Content of the SMS goes here...");
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);*/


                /*Intent intent =
                        new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                        myPackageName);

                startService(intent);*/

                //                startActivity(intent);

                /*Uri newUri;
                ContentValues values = new ContentValues();
                values.put(Telephony.Sms.ADDRESS, "123456789");
                values.put(Telephony.Sms.BODY, "foo bar");
                getContentResolver().insert(Telephony.Sms.Inbox.CONTENT_URI, values);*/


                /*Uri uri = Uri.parse("content://sms/");
                ContentValues cv2 = new ContentValues();
                cv2.put("address", "+91956322222");
                cv2.put("date", "1309632433677");
                cv2.put("read", 1);
                cv2.put("type", 2);
                cv2.put("body", "Hey");
                getContentResolver().insert(uri, cv2);
                *//** This is very important line to solve the problem *//*
                getContentResolver().delete(Uri.parse("content://sms/conversations/-1"), null, null);
                cv2.clear();*/


            }
        });

        Button btnGetSMS = (Button) findViewById(R.id.btnGetSMS);
        btnGetSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getSMS();
                cursor.moveToFirst();


                if (cursor.getCount() > 0) {

                    for (int i = 0; i < cursor.getCount(); i++) {
                        String mid = cursor.getString(cursor.getColumnIndex("_id"));
                        String mbody = cursor.getString(cursor.getColumnIndex("body"));
                        String maddress = cursor.getString(cursor.getColumnIndex("address"));
                        String mread = cursor.getString(cursor.getColumnIndex("read"));
                        String mseen = cursor.getString(cursor.getColumnIndex("seen"));

                        Log.d("mid", mid);
                        Log.d("mbody", mbody);
                        Log.d("maddress", maddress);
                        Log.d("mread", mread);
                        Log.d("mseen", mseen);


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

        Button btnBackupSMS = (Button) findViewById(R.id.btnBackupSMS);
        btnBackupSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backupIntent = new Intent(MainActivity.this,SMSBackup.class);
                startActivity(backupIntent);
            }
        });

        Button btnParseXML = (Button) findViewById(R.id.btnParseXML);
        btnParseXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                domParser();
            }
        });

    }

    public void displayContacts(View v){
        Cursor cursor = getContacts();
//        String[] name = new String[10];
//        String[] id = new String[10];
        cursor.moveToFirst();
       // Log.d("ID", (cursor.getCount()));
        Log.d("get count", String.valueOf(cursor.getCount()));

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

    public void domParser()
    {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                Intent intent =
                        new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                        getPackageName());
                startActivity(intent);
            }
            boolean duplicate = false;
            Cursor sCursor = getContentResolver().query(Uri.parse("content://sms"),null,null,null,null);
            sCursor.moveToFirst();
            DocumentBuilderFactory fac= DocumentBuilderFactory.newInstance();
            DocumentBuilder builder= fac.newDocumentBuilder();
            FileInputStream fIn=new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SCBS_SMS/1445410687768/SMS_1445410687768.xml");
            Document doc=builder.parse(fIn);
            Element root= doc.getDocumentElement();
            NodeList list= root.getChildNodes();
            String datashow="";
            String SENT_SMS_CONTENT_PROVIDER_URI_OLDER_API_19 = "content://sms";
            for(int i=0;i<list.getLength();i++)
            {
                Node node=list.item(i);
                if(node instanceof Element)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                        Element item =(Element) node;
                        NodeList listChild=item.getElementsByTagName("_id");
                        int _id = Integer.parseInt(listChild.item(0).getTextContent()) ;
                        listChild=item.getElementsByTagName("thread_id");
                        int thread_id = Integer.parseInt(listChild.item(0).getTextContent());
                        listChild=item.getElementsByTagName("address");
                        String address =listChild.item(0).getTextContent();
                        listChild=item.getElementsByTagName("date");
                        String date = listChild.item(0).getTextContent();
                        listChild=item.getElementsByTagName("date_sent");
                        int date_sent =Integer.parseInt(listChild.item(0).getTextContent());
                        listChild=item.getElementsByTagName("read");
                        int read =Integer.parseInt(listChild.item(0).getTextContent());
                        listChild=item.getElementsByTagName("status");
                        int status =Integer.parseInt(listChild.item(0).getTextContent());
                        listChild=item.getElementsByTagName("type");
                        int type =Integer.parseInt(listChild.item(0).getTextContent());
                        listChild=item.getElementsByTagName("body");
                        String body =listChild.item(0).getTextContent();
                        listChild=item.getElementsByTagName("locked");
                        int locked =Integer.parseInt(listChild.item(0).getTextContent());

                        datashow+=_id+"-"+thread_id+"-"+address+"-"+date+"-"+date_sent+"_"+read+"-"+status+"-"+type+"-"+body+"-"+locked
                                +"\n---------\n";

                        ContentValues values = new ContentValues();
                        values.put("address",address);
                        values.put("body",body);
                        values.put("type", type);
                        values.put("date", date);
                        values.put("read", read);
                        for (int j = 0; j < sCursor.getCount(); j++){
                            if(address.equals(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.ADDRESS)))&&date.equals(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.DATE)))){
                                duplicate=true;
                                break;
                            }
                        }
                        /*values.put("address", 1);
                        values.put("_id", _id);
                        values.put("thread_id", thread_id);

                        values.put("date", date);
                        values.put("date_sent", date_sent);
                        values.put("read", read);
                        values.put("status", status);

                        values.put("body", body);
                        values.put("locked", locked);
                        values.put("protocol", 0);
                        values.put("service_center", "");*/
                        if(duplicate == false){
                            getContentResolver().insert(Uri.parse("content://sms/"), values);
                           // getContentResolver().delete(Uri.parse("content://sms/conversations/-1"), null, null);
                            values.clear();
                        }

                    } else{
                        Element item =(Element) node;
                        NodeList listChild=item.getElementsByTagName("thread_id");
                        String thread_id =listChild.item(0).getTextContent();
                        listChild=item.getElementsByTagName("address");
                        String address =listChild.item(0).getTextContent();
                        listChild=item.getElementsByTagName("date");
                        String date =listChild.item(0).getTextContent();
                        listChild=item.getElementsByTagName("read");
                        String read =listChild.item(0).getTextContent();
                        listChild=item.getElementsByTagName("status");
                        String status =listChild.item(0).getTextContent();
                        listChild=item.getElementsByTagName("type");
                        String type =listChild.item(0).getTextContent();
                        listChild=item.getElementsByTagName("body");
                        String body =listChild.item(0).getTextContent();
                        listChild=item.getElementsByTagName("locked");
                        String locked =listChild.item(0).getTextContent();

                        datashow+=thread_id+"-"+address+"-"+date+"_"+read+"-"+status+"-"+type+"-"+body+"-"+locked
                                +"\n---------\n";

                        ContentValues values = new ContentValues();
                       // values.put("thread_id", thread_id);
                        values.put("address", address);
                        values.put("date", date);
                        values.put("read", read);
                       // values.put("status", status);
                        values.put("type", type);
                        values.put("body", body);

                        for (int j = 0; j < sCursor.getCount(); j++){
                            if(address.equals(sCursor.getString(sCursor.getColumnIndex("address")))&&date.equals(sCursor.getString(sCursor.getColumnIndex("date")))){
                                duplicate=true;
                                break;
                            }
                        }
                       // values.put("locked", locked);
                        if(duplicate == false) {
                            getContentResolver().insert(Uri.parse(SENT_SMS_CONTENT_PROVIDER_URI_OLDER_API_19), values);
                            // getContentResolver().delete(Uri.parse("content://sms/conversations/-1"), null, null);
                            values.clear();
                        }

                    }
                }
            }
            Log.d("ParseXML",datashow);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
