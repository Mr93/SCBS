package com.example.dendimon.scbs;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Telephony;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.channels.FileChannel;

/**
 * Created by Dendimon on 10/19/2015.
 */
public class SMSBackup extends Activity {
    static Context mcontext;

    @Override
    public  void  onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backupsms);
        mcontext = SMSBackup.this;
        createFolder();
        getXML();
    }

    private void createFolder(){
        File SCBS_SMS = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "SCBS_SMS");
        if(!SCBS_SMS.exists()){
            SCBS_SMS.mkdir();
        }
    }

    private void getXML (){

        final String sTime = ""+System.currentTimeMillis() ;
        final String sFile = "SMS"+"_"+sTime+".xml";
        final String sPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SCBS_SMS/"+sTime;
        Log.d("PathXML", sPath);
         FileOutputStream fileos = null;
        XmlSerializer xmlSerializer;

        File sSCBS = new File (sPath);
        sSCBS.mkdir();
        try{
             fileos = new FileOutputStream(new File(sPath+"/"+sFile),true);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Telephony.Sms.Inbox.CONTENT_URI


        Cursor sCursor = mcontext.getContentResolver().query(Uri.parse("content://sms"),null,null,null,null);
        sCursor.moveToFirst();

            try{

                xmlSerializer = Xml.newSerializer();
                StringWriter writer = new StringWriter();
                xmlSerializer.setOutput(writer);
                xmlSerializer.startDocument("UTF-8", true);
                xmlSerializer.startTag(null, "smsData");
                for (int i = 0;i < sCursor.getCount();i++){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                        xmlSerializer.startTag(null, "item");
                        xmlSerializer.startTag(null, "_id");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms._ID)));
                        xmlSerializer.endTag(null, "_id");
                        xmlSerializer.startTag(null, "thread_id");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.THREAD_ID)));
                        xmlSerializer.endTag(null, "thread_id");
                        xmlSerializer.startTag(null, "address");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.ADDRESS)));
                        xmlSerializer.endTag(null, "address");
                        xmlSerializer.startTag(null, "date");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.DATE)));
                        xmlSerializer.endTag(null, "date");
                        xmlSerializer.startTag(null, "date_sent");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.DATE_SENT)));
                        xmlSerializer.endTag(null, "date_sent");
                        xmlSerializer.startTag(null, "read");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.READ)));
                        xmlSerializer.endTag(null, "read");
                        xmlSerializer.startTag(null, "status");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.STATUS)));
                        xmlSerializer.endTag(null, "status");
                        xmlSerializer.startTag(null, "type");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.TYPE)));
                        xmlSerializer.endTag(null, "type");
                        xmlSerializer.startTag(null, "body");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.BODY)));
                        xmlSerializer.endTag(null, "body");
                        xmlSerializer.startTag(null, "locked");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.LOCKED)));
                        xmlSerializer.endTag(null, "locked");
                        xmlSerializer.endTag(null, "item");
                    }else {
                        xmlSerializer.startTag(null, "item");
                        xmlSerializer.startTag(null, "thread_id");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex("thread_id")));
                        xmlSerializer.endTag(null, "thread_id");
                        xmlSerializer.startTag(null, "address");
                        xmlSerializer.text(""+sCursor.getString(sCursor.getColumnIndex("address")));
                        xmlSerializer.endTag(null, "address");
                        xmlSerializer.startTag(null, "date");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex("date")));
                        xmlSerializer.endTag(null, "date");
                        xmlSerializer.startTag(null, "read");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex("read")));
                        xmlSerializer.endTag(null, "read");
                        xmlSerializer.startTag(null, "status");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex("status")));
                        xmlSerializer.endTag(null, "status");
                        xmlSerializer.startTag(null, "type");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex("type")));
                        xmlSerializer.endTag(null, "type");
                        xmlSerializer.startTag(null, "body");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex("body")));
                        xmlSerializer.endTag(null, "body");
                        xmlSerializer.startTag(null, "locked");
                        xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex("locked")));
                        xmlSerializer.endTag(null, "locked");
                        xmlSerializer.endTag(null, "item");

                        Log.d("Check_Error",sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.THREAD_ID)) + " ADDRESS: " + sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.ADDRESS)) + " DATE: " +sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.DATE))
                        +" READ: " + sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.READ)) +" STATUS: " + sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.STATUS)) +" TYPE: " + sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.TYPE)) +
                                " BODY: " +   sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.BODY)) +" LOCKED: " + sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.LOCKED)));
                    }
                    sCursor.moveToNext();
                }
                xmlSerializer.endTag(null, "smsData");
                xmlSerializer.endDocument();
                xmlSerializer.flush();
                String dataWrite = writer.toString();
                fileos.write(dataWrite.getBytes());
                fileos.close();
            }
             //

            catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
