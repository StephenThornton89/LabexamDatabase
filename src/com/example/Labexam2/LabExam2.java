package com.example.Labexam2;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;


public class LabExam2 extends Activity {

    String FNAME= "";
    String MINIT = "";
    String LNAME = "";
    String SSN = "";
    String BDATE = "";
    String ADDRESS = "";
    String SEX = "";
    String SALARY = "";
    String SUPERSSN = "";
    String DNO = "";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void button3 (View v) {
        TextView txtEr = (TextView) findViewById(R.id.error);
        TextView txt1 = (TextView) findViewById(R.id.textView3);
        SQLiteDatabase db = this.openOrCreateDatabase("employeedb", MODE_PRIVATE, null);
        String query = CreateQuery2();
        db.beginTransaction();
        try {
            db.execSQL(query);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            txtEr.append("Implicit rollback");
        } finally {
            db.endTransaction();
        }
    }
    public void button2 (View v) throws IOException, XmlPullParserException {
        TextView txtEr = (TextView)findViewById(R.id.error);
        SQLiteDatabase db = this.openOrCreateDatabase("employeedb",MODE_PRIVATE,null);
        TextView txt1 = (TextView)findViewById(R.id.textView2);
        Integer xmlResFile = R.xml.test;

        String inner = "";
        String outer = "";
        int flag = 0;

        int tag = 0;
        String nodeName  = "";
        txt1.append("\nProceeding to parse xml\n");

        XmlPullParser parser = getResources().getXml(xmlResFile);

        int eventType = -1;
        while(eventType != XmlPullParser.END_DOCUMENT){
            eventType = parser.next();

            if(eventType == XmlPullParser.START_DOCUMENT){
                //  txt1.append("\nStart_Document");
            }
            else if (eventType == XmlPullParser.END_DOCUMENT){
                //txt1.append("\nEnd_Document");
            }
            else if (eventType == XmlPullParser.START_TAG){
                flag++;
                nodeName = parser.getName();
                if(flag == 2)
                    outer = nodeName;
                if(flag == 3)
                    inner = nodeName;
                getAttributes(parser);
            }
            else if(eventType == XmlPullParser.END_TAG){
                if(flag == 2){
                    String query = CreateQuery();
                    db.beginTransaction();
                    try{
                        //txt1.append(query+"\n");
                        db.execSQL(query);
                        //txt1.append("Success");
                        Toast.makeText(getApplicationContext(), "Employee data saved to database ", Toast.LENGTH_SHORT).show();
                        db.setTransactionSuccessful();
                    }catch (SQLException e){
                        txtEr.append("Implicit rollback");
                    }finally{
                        db.endTransaction();
                    }

                    ClearStrings();
                    tag ++;
                }
                flag--;
            }
            else if(eventType == XmlPullParser.TEXT){
                if(flag == 2){
                    outer += " Text: "+parser.getText();
                }
                if(flag == 3){
                    if (inner.equalsIgnoreCase("FNAME"))
                        FNAME = parser.getText();
                    else if (inner.equalsIgnoreCase("MINIT"))
                        MINIT = parser.getText();
                    else if (inner.equalsIgnoreCase("LNAME"))
                        LNAME = parser.getText();
                    else if (inner.equalsIgnoreCase("SSN")) {
                        SSN = parser.getText();
                    }
                    else if (inner.equalsIgnoreCase("BDATE")) {
                        BDATE = parser.getText();
                    }
                    else if (inner.equalsIgnoreCase("ADDRESS")) {
                        ADDRESS = parser.getText();
                    }
                    else if (inner.equalsIgnoreCase("SEX")) {
                        SEX = parser.getText();
                    }
                    else if (inner.equalsIgnoreCase("SALARY")) {
                        SALARY = parser.getText();
                    }
                    else if (inner.equalsIgnoreCase("SUPERSSN")) {
                        SUPERSSN = parser.getText();
                    }
                    else if (inner.equalsIgnoreCase("DNO")) {
                        DNO = parser.getText();
                    }
                }
            }

        }//While
    }//On click listener

    public void button1 (View v){

        SQLiteDatabase db = this.openOrCreateDatabase("employeedb",MODE_PRIVATE,null);
        TextView txt2 = (TextView)findViewById(R.id.textView);
        txt2.setText("Database Created\n");

        db.beginTransaction();
        try {
            db.execSQL("create table names (recid integer PRIMARY KEY autoincrement,FNAME text,MINIT text,LNAME text, SSN text, BDATE text, ADDRESS text, SEX text, SALARY text, SUPERSSN text,DNO text);");
            txt2.append("New table created\n");
            db.execSQL("insert into names(FNAME, MINIT, LNAME, SSN, BDATE,ADDRESS, SEX, SALARY, SUPERSSN,DNO ) values ('Test','Test1','Test2','Test3','Test4','Test5','Test6','Test7','Test8','Test9')");
            txt2.append("Data inserted\n");
            db.setTransactionSuccessful();
        } catch(SQLiteException e) {
            txt2.append("Database roolback Here!!!!!!!!!!!\n");

        }finally{
            db.endTransaction();
        }
    }
    public void change1 (View v){
        useInsertMethod();
    }

    private String CreateQuery(){

        String Insert_data = "insert into names(FNAME, MINIT, LNAME, SSN, BDATE,ADDRESS, SEX, SALARY, SUPERSSN,DNO ) values ('"+FNAME+"','"+MINIT+"','"+LNAME+"','"+SSN+"','"+BDATE+"','"+ADDRESS+"','"+SEX+"','"+SALARY+"','"+SUPERSSN+"','"+DNO+"');";
        return Insert_data;
    }
    private String CreateQuery2(){

        String max_salary = "select max(SALARY) FROM names values('"+SALARY+"');";
        return max_salary;
    }
    private void ClearStrings(){
        FNAME = "";
        MINIT = "";
        LNAME = "";
        SSN = "";
        BDATE = "";
        ADDRESS = "";
        SEX = "";
        SALARY = "";
        SUPERSSN = "";
        DNO = "";
    }

    public void useInsertMethod() {
        // an alternative to SQL "insert into table values(...)"
        // ContentValues is an Android dynamic row-like container
        TextView txt1 = (TextView) findViewById(R.id.textView3);
        try {
            SQLiteDatabase db = this.openOrCreateDatabase("employeedb",MODE_PRIVATE,null);
            ContentValues initialValues = new ContentValues();
            EditText txtDescription = (EditText)findViewById(R.id.editText);

            String string = txtDescription.getText().toString();
            initialValues.put("FNAME", string);
            db.update("names",initialValues , "2",null);
           // int rowPosition = (int) db.insert("names", null, initialValues);
            //int rowPosition = (int) db.insert("names", null, initialValues);
            //txt1.append("\n-useInsertMethod rec added at: " + rowPosition);
        } catch (Exception e) {
            txt1.append("\n-useInsertMethod - Error: " + e.getMessage());
        }

    }

    private void getAttributes(XmlPullParser parser){
        TextView txt1 = (TextView)findViewById(R.id.textView);
        String name = parser.getName();

        if(name != null){
            int size = parser.getAttributeCount();
            for(int i = 0; i < size; i++){
                String attributesname = parser.getAttributeName(i);
                String attributesvalue = parser.getAttributeValue(i);
                txt1.append("\n Attrib <Key, Value>"+ attributesname+", "+ attributesvalue);
            }


        }

    }

}
