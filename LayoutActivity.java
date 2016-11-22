package getaroom.floor;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class LayoutActivity extends AppCompatActivity {

    private Spinner spinner;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
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
        addListenerOnSpinnerItemSelection();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.floor_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        CustomOnItemSelectedListener listener = new CustomOnItemSelectedListener();
        Log.d("LISTENER", listener.toString());
        spinner.setOnItemSelectedListener(listener);
        new ParseInsightDataTask().execute(listener);
        new ParseTimeDataTask().execute(listener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Layout Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public final int[] EMPTY = {}; //Choose a Floor
        public final int[] FLOOR1 = {111 , 110 , 109};  //First Floor Room Numbers
        public final int[] FLOOR2 = {224 , 225 , 228 , 206 , 205 , 202 , 220};
        public final int[] FLOOR3 = {315 , 316 , 319 , 317 , 308 , 309 , 310 , 311 , 301};
        public final int[] FLOOR4 = {404 , 406 , 415 , 414 , 413 , 411 , 412};
        private HashMap<String, ArrayList<ArrayList<String>>> library = new HashMap<>();
        private ArrayList<String> timestamps = new ArrayList();

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();
            Log.d("SELECT", "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString());

            generateTable(pos, library.get("f"+pos));
//            RoomProvider populate = new RoomProvider();
//            populate.populateRoom(pos);

        }

        public void setLibData(HashMap<String, ArrayList<ArrayList<String>>> library) { //Setter
            this.library = library;
        }

        public void setTimeData(ArrayList<String> timestamps) { //To Be Sent to DecisionTree
            this.timestamps = timestamps;
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

        private void generateTable(int pos, ArrayList<ArrayList<String>> floor_data) {
            TableLayout table = (TableLayout) findViewById(R.id.main_table);
            table.removeAllViews();
            table.addView(generateTableRow(), new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)); //Add the row to the table layout

            populateTable(table ,floor_data , pos);

        }
            /* Creates Format for TableLayout */
        private TableRow generateTableRow() {

            TableRow tr_head = new TableRow(LayoutActivity.this);
            tr_head.setId(10+0);
            tr_head.setBackgroundColor(Color.GRAY);
            tr_head.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView label_roomNum = new TextView(LayoutActivity.this);
            label_roomNum.setId(20+0);
            label_roomNum.setText("Room");
            label_roomNum.setTextColor(Color.WHITE);
            label_roomNum.setPadding(5, 5, 5, 5);
            tr_head.addView(label_roomNum);// add the column to the table row here

            TextView label_students = new TextView(LayoutActivity.this);
            label_students.setId(21+0);// define id that must be unique
            label_students.setText("Last Updated"); // set the text for the header
            label_students.setTextColor(Color.WHITE); // set the color
            label_students.setPadding(5, 5, 5, 5); // set the padding (if required)
            tr_head.addView(label_students); // add the column to the table row here

            TextView label_prediction = new TextView(LayoutActivity.this);
            label_prediction.setId(22+0);
            label_prediction.setText("Predicted");
            label_prediction.setTextColor(Color.WHITE);
            label_prediction.setPadding(5, 5, 5, 5);
            tr_head.addView(label_prediction);

            return tr_head;
        }

        private void populateTable(TableLayout table, ArrayList<ArrayList<String>> room_data, int pos) {

            int[] rooms;
            switch (pos) {
                case 0 : rooms = EMPTY; break;
                case 1 : rooms = FLOOR1;  break;
                case 2 : rooms = FLOOR2;  break;
                case 3 : rooms = FLOOR3;  break;
                case 4 : rooms = FLOOR4;  break;
                default : rooms = null;   break;
            }


            for (int count=0; count<rooms.length; count++) {
                int roomNum = rooms[count];// get the Room Number
                String students = room_data.get(count).get(0);// get the Number Students
                DecisionTree predict = new DecisionTree(timestamps.get(0), students, roomNum);
                String predict_message = "";

                if(predict.predict())
                    predict_message = "FULL";
                else predict_message = "EMPTY";

                // Create the table row
                TableRow tr = new TableRow(LayoutActivity.this);
//                if(count%2!=0) tr.setBackgroundColor(Color.GRAY);
                tr.setId(100+count);
                tr.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                //Create two columns to add as table data
                // Create a TextView to add date
                TextView labelROOMNUM = new TextView(LayoutActivity.this);
                labelROOMNUM.setId(200+count);
                labelROOMNUM.setText(roomNum+"");
                labelROOMNUM.setPadding(2, 0, 5, 0);
                labelROOMNUM.setTextColor(Color.BLACK);
                tr.addView(labelROOMNUM);


                TextView labelSTUDENTS = new TextView(LayoutActivity.this);
                labelSTUDENTS.setId(200+count);
                labelSTUDENTS.setText(students);
                labelSTUDENTS.setTextColor(Color.BLACK);
                tr.addView(labelSTUDENTS);

                TextView labelPREDICTED = new TextView(LayoutActivity.this);
                labelPREDICTED.setId(200+count);
                labelPREDICTED.setText(predict_message);
                labelPREDICTED.setTextColor(Color.BLACK);
                tr.addView(labelPREDICTED);


                // finally add this to the table row
                table.addView(tr, new TableLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }


    }



    /**
     * Created by Chris on 11/20/2016.
     */

    public class ParseInsightDataTask extends AsyncTask<CustomOnItemSelectedListener, Integer, HashMap<String, ArrayList<ArrayList<String>>>> {

        protected CustomOnItemSelectedListener listener;

//        protected void onPreExecute(CustomOnItemSelectedListener... listeners) {
//
//            listener = listeners[0];
//        }

        protected HashMap<String, ArrayList<ArrayList<String>>> doInBackground(CustomOnItemSelectedListener... listeners) {
            listener = listeners[0]; //instantiate spinner CustomOnItemSelectedListener
            AssetManager assetManager = getResources().getAssets();

            ArrayList<String>  room111_dat = new ArrayList<>(100); //Instantiate Arrays for each Room
            ArrayList<String>  room110_dat = new ArrayList<>(100);
            ArrayList<String>  room109_dat = new ArrayList<>(100);
            ArrayList<String>  room224_dat = new ArrayList<>(100);
            ArrayList<String>  room225_dat = new ArrayList<>(100);
            ArrayList<String>  room228_dat = new ArrayList<>(100);
            ArrayList<String>  room226_dat = new ArrayList<>(100);
            ArrayList<String>  room205_dat = new ArrayList<>(100);
            ArrayList<String>  room202_dat = new ArrayList<>(100);
            ArrayList<String>  room220_dat = new ArrayList<>(100);
            ArrayList<String>  room315_dat = new ArrayList<>(100);
            ArrayList<String>  room316_dat = new ArrayList<>(100);
            ArrayList<String>  room319_dat = new ArrayList<>(100);
            ArrayList<String>  room317_dat = new ArrayList<>(100);
            ArrayList<String>  room308_dat = new ArrayList<>(100);
            ArrayList<String>  room309_dat = new ArrayList<>(100);
            ArrayList<String>  room310_dat = new ArrayList<>(100);
            ArrayList<String>  room311_dat = new ArrayList<>(100);
            ArrayList<String>  room301_dat = new ArrayList<>(100);
            ArrayList<String>  room404_dat = new ArrayList<>(100);
            ArrayList<String>  room406_dat = new ArrayList<>(100);
            ArrayList<String>  room415_dat = new ArrayList<>(100);
            ArrayList<String>  room414_dat = new ArrayList<>(100);
            ArrayList<String>  room413_dat = new ArrayList<>(100);
            ArrayList<String>  room411_dat = new ArrayList<>(100);
            ArrayList<String>  room412_dat = new ArrayList<>(100);

            ArrayList<ArrayList<String>> floor1 = new ArrayList<>(); //Wrapper ArrayList for rooms on floor i (Lists of String-Lists)
            ArrayList<ArrayList<String>> floor2 = new ArrayList<>();
            ArrayList<ArrayList<String>> floor3 = new ArrayList<>();
            ArrayList<ArrayList<String>> floor4 = new ArrayList<>();
            floor1.add(room111_dat); floor1.add(room110_dat); floor1.add(room109_dat); floor2.add(room224_dat); floor2.add(room225_dat); floor2.add(room228_dat);
            floor2.add(room226_dat); floor2.add(room205_dat); floor2.add(room202_dat); floor2.add(room220_dat); floor3.add(room315_dat); floor3.add(room316_dat);
            floor3.add(room319_dat); floor3.add(room317_dat); floor3.add(room308_dat); floor3.add(room309_dat); floor3.add(room310_dat); floor3.add(room311_dat);
            floor3.add(room301_dat); floor4.add(room404_dat); floor4.add(room406_dat); floor4.add(room415_dat); floor4.add(room414_dat); floor4.add(room413_dat);
            floor4.add(room411_dat); floor4.add(room412_dat);
            HashMap<String, ArrayList<ArrayList<String>>> library = new HashMap<>(); //Wrapper HashMap for floors in library
            library.put("f0", new ArrayList<ArrayList<String>>()); library.put("f1", floor1); library.put("f2", floor2); library.put("f3", floor3); library.put("f4", floor4);

            try {
                InputStream room_pops = assetManager.open("room_pops.csv");
                Scanner reader = new Scanner(room_pops);

                while(reader.hasNextLine()) {
                    for(int i=1; i<library.size(); i++) { //Iterate through Floors (x4)
                        for(int j=0; j<library.get("f"+i).size(); j++) {//Iterate through Rooms (x3,x7,x9,x7)
                            library.get("f" + i).get(j).add(reader.next()); //Insert value to room_dat
                        }
                    }
                }
            } catch (FileNotFoundException e) { e.printStackTrace(); }
            catch ( IOException io) { io.printStackTrace(); }


            return library;
        }

        protected void onProgressUpdate(Integer... integers) {



        }




        protected void onPostExecute(HashMap result) {
//            display.setText("Dat Size: "+result.size()+"\nRoomDat Size: "+result.get(0).size());
//            for(int i=0; i<result.size(); i++)
//                for(int j=0; j<result.get(i).size(); )
//                display.append(result.get(i).get(j) + "\n");

            listener.setLibData(result); // set insight data for Listener
        }

    }

    public class ParseTimeDataTask extends AsyncTask<CustomOnItemSelectedListener, Integer, ArrayList<String>> {

        protected CustomOnItemSelectedListener listener;

//        protected void onPreExecute(CustomOnItemSelectedListener... listeners) {
//
//            listener = listeners[0];
//        }

        protected ArrayList<String> doInBackground(CustomOnItemSelectedListener... listeners) {
            listener = listeners[0]; //instantiate spinner CustomOnItemSelectedListener
            AssetManager assetManager = getResources().getAssets();

            ArrayList<String> timestamps = new ArrayList<>(100); //List of timestamps
            try {
                InputStream dates_dat = assetManager.open("dates_dat.csv");
                Scanner reader = new Scanner(dates_dat);

                while(reader.hasNextLine()) {
                    timestamps.add(reader.next());
                }
                Log.d("TIMESTAMPS", timestamps.get(0));
            } catch (FileNotFoundException e) {
                Log.d("FILENOTFOUND", "FIX IT");
                e.printStackTrace(); }
            catch ( IOException io) { io.printStackTrace(); }


            return timestamps;
        }

        protected void onProgressUpdate(Integer... integers) {



        }


        protected void onPostExecute(ArrayList result) {
//            display.setText("Dat Size: "+result.size()+"\nRoomDat Size: "+result.get(0).size());
//            for(int i=0; i<result.size(); i++)
//                for(int j=0; j<result.get(i).size(); )
//                display.append(result.get(i).get(j) + "\n");

            listener.setTimeData(result); // set insight data for Listener
        }

    }
}
