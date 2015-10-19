package com.example.dendimon.scbs;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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
        File SCBS_SMS = new File("/sdcard/SCBS_SMS");
        if(!SCBS_SMS.exists()){
            SCBS_SMS.mkdir();
        }
    }

    private void getXML (){
        final String sFile = "SMS"+"_"+System.currentTimeMillis()+".xml";
        final String sPath = "/sdcard/SCBS_SMS/"+System.currentTimeMillis();
        Log.d("PathXML", sPath);
         FileOutputStream fileos = null;
        XmlSerializer xmlSerializer;

        File sSCBS = new File ("/sdcard/SCBS_SMS/"+System.currentTimeMillis());
        sSCBS.mkdir();
        try{
             fileos = new FileOutputStream(new File(sPath+"/"+sFile),true);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Cursor sCursor = mcontext.getContentResolver().query(Telephony.Sms.CONTENT_URI,null,null,null,null);
        sCursor.moveToFirst();

            try{

                xmlSerializer = Xml.newSerializer();
                StringWriter writer = new StringWriter();
                xmlSerializer.setOutput(writer);
                xmlSerializer.startDocument("UTF-8", true);
                xmlSerializer.startTag(null, "smsData");
                for (int i = 0;i < sCursor.getCount();i++){
                    xmlSerializer.startTag(null, "item");
                    xmlSerializer.startTag(null, "id");
                    xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms._ID)));
                    xmlSerializer.endTag(null, "id");
                    xmlSerializer.startTag(null, "address");
                    xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.ADDRESS)));
                    xmlSerializer.endTag(null, "address");
                    xmlSerializer.startTag(null, "body");
                    xmlSerializer.text(sCursor.getString(sCursor.getColumnIndex(Telephony.Sms.BODY)));
                    xmlSerializer.endTag(null, "body");
                    xmlSerializer.endTag(null, "item");
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
