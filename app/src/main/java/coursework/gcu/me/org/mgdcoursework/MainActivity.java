package coursework.gcu.me.org.mgdcoursework;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Collections;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Comparator;
import java.lang.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.support.v7.widget.RecyclerView;

// Student ID: S1507379
// BMADIN200
// Computer Games (Software Development)

//
// ─── MAIN ACTIVITY CLASS ────────────────────────────────────────────────────────
//

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    // Variables
    private Spinner spinner;
    private ArrayList tempList = new ArrayList();
    private EditText editText;
    private int textLength;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<EarthQuakeMaster> earthQuakeMasterList = new ArrayList<>();
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private boolean portrait;
    // Variables End

    @Override
    protected void onCreate(Bundle savedInstanceState)
    { // Start of onCreate
        // Displays the name of the current thread.
        Log.e("Ben", "Getting Called at Start: " + Thread.currentThread().getName());
        super.onCreate(savedInstanceState);
        // Calls the method 'Repeating'
        Repeating();
        // Checks the orientation.
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            // Sets portrait value to true as orientation is portrait.
            portrait = true;
            // Sets xml files
            setContentView(R.layout.activity_portrait);
            // Calls 'layoutVertical' method.
            LayoutVertical();
        }
        // Else if orientation is equal to landscape do the following.
        else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            portrait = false;
            setContentView(R.layout.activity_landscape);
            LayoutHorizontal();
        }
        // Calls the 'UIThread' method
        UIThread();
    } // End of onCreate

    private void UIThread()
    {
        // Creates a new thread that resus a fixed number of threads
        Executors.newFixedThreadPool(10).execute(new Runnable() {
            @Override
            public void run() {
                // Sets the threads name
                Thread.currentThread().setName("UI Thread");
                // Displays name
                Log.e("Ben", "Getting Called at UI Thread: " + Thread.currentThread().getName());
                // Calls methods
                InstiantiateViewComponetns();
                InstiantiateSpinner();
                InstiantiateAdapter();
            }
        });
    }

    private void Repeating()
    {
        // Creates a new thread that is called every 300 seconds, calling the 'Task' class. It rreuses 10 threads.
        Executors.newScheduledThreadPool(10).scheduleAtFixedRate(new Task(urlSource), 0, 300, TimeUnit.SECONDS);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        // Method is called if screen rotates
        super.onConfigurationChanged(newConfig);
        // If config is equal to landscape
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            // Set boolean
            portrait = false;
            // Set XML
            setContentView(R.layout.activity_landscape);
            // Call Methods
            UIThread();
            LayoutHorizontal();

        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            // Set boolean
            portrait = true;
            // Set XML
            setContentView(R.layout.activity_portrait);
            // Call Methods
            UIThread();
            LayoutVertical();
        }
    }

    public void InstiantiateSpinner()
    {
        // Create array adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sorts, R.layout.spinner_item);
        // Set its layout to spinner item
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        // Sets the adapter
        spinner.setAdapter(adapter);
        // Adds listener
        spinner.setOnItemSelectedListener(this);
    }

    public void InstiantiateViewComponetns()
    {
        // Sets variables
        spinner = (Spinner)findViewById(R.id.spinner);
        editText = (EditText)findViewById(R.id.searchBar);
        // Adds texts listener
        editText.addTextChangedListener(new TextWatcher()
        {
            // Essential Methods
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // Sets the text length to text entered
                textLength = editText.getText().length();
                // Clears temporary list
                tempList.clear();
                // Runs for the length of text length
                for (int i = 0; i < earthQuakeMasterList.size(); i++)
                {
                    // If textlength is smaller than location names
                    if (textLength <= earthQuakeMasterList.get(i).GetLocationName().length())
                    {
                        // If the text length contains the same letters
                        if (earthQuakeMasterList.get(i).GetLocationName().toLowerCase().trim().contains(editText.getText().toString().toLowerCase().trim()))
                        {
                            // Adds results to the list
                            tempList.add(earthQuakeMasterList.get(i));
                        }
                    }
                    if (textLength <= earthQuakeMasterList.get(i).getMagnitude().length())
                    {
                        // If the text length contains the same letters
                        if (earthQuakeMasterList.get(i).getMagnitude().toLowerCase().trim().contains(editText.getText().toString().toLowerCase().trim()))
                        {
                            // Adds results to the list
                            tempList.add(earthQuakeMasterList.get(i));
                        }
                    }
                }
                // Creates new adapter for new temp list
                adapter = new Adapter(tempList, new MainActivity());
                // Sets the adapter
                recyclerView.setAdapter(adapter);
                // Sets recycler view values depending on rotation
                if (portrait)
                    InitaliseRecyclerView(1,1);
                else
                    InitaliseRecyclerView(4,0);
            }
        });
    }

    public void InitaliseRecyclerView(int rows, int rotation)
    {
        // Sets the layout of the application.
        recyclerView.setLayoutManager(new GridLayoutManager(this, rows, rotation, false));
    }

    public void LayoutVertical()
    {
        // Sets the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // Sets it so it has a fixed size
        recyclerView.setHasFixedSize(true);
        // Assigns rows and rotation values
        InitaliseRecyclerView(1, 1);
    }

    public void LayoutHorizontal()
    {
        // Sets the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // Sets it so it has a fixed size
        recyclerView.setHasFixedSize(true);
        // Assigns rows and rotation values
        InitaliseRecyclerView(4, 0);
    }

    public void InstiantiateAdapter()
    {
        // Assings the adapter to the earthquakemasterlist
        adapter = new Adapter(earthQuakeMasterList, this);
        // Sets the adapter
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        // Sets the position of the items in the spinner
        String text = parent.getItemAtPosition(position).toString();
        // If statements to check what drop down seleciton was picked
        if (text.equals("Sort most Northerly"))
            Collections.sort(earthQuakeMasterList, new CompareNorth());
        if (text.equals("Sort most Easterly"))
            Collections.sort(earthQuakeMasterList, new CompareEast());
        if (text.equals("Sort most Southerly"))
            Collections.sort(earthQuakeMasterList, new CompareSouth());
        if (text.equals("Sort most Westerly"))
            Collections.sort(earthQuakeMasterList, new CompareWest());
        if (text.equals("Sort Smallest Magnitude"))
            Collections.sort(earthQuakeMasterList, new CompareMagnitudeDec());
        if (text.equals("Sort Largest Magnitude"))
            Collections.sort(earthQuakeMasterList, new CompareMagnitudeAsc());
        if (text.equals("Sort Deepest Earthquake"))
            Collections.sort(earthQuakeMasterList, new CompareDeepesteAsc());
        if (text.equals("Sort Shallowest Earthquake"))
            Collections.sort(earthQuakeMasterList, new CompareShallowestDec());
        // Sets the adapter and recycler view to display the results
        adapter = new Adapter(earthQuakeMasterList, this);
        recyclerView.setAdapter(adapter);
        // Sets recycler view values depending on rotation
        if (portrait)
            InitaliseRecyclerView(1, 1);
        else
            InitaliseRecyclerView(4, 0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    //
    // ─── TASK CLASS ─────────────────────────────────────────────────────────────────
    //

    private class Task implements Runnable //Params, Progress, Result
    {
        // Variables
        private String url;
        EarthQuakeMaster earthQuakeMaster = new EarthQuakeMaster();
        // End Variables

        public Task(String aurl)
        {
            // Constructor, sets the url
            url = aurl;
        }

        @Override
        public void run()
        {
            // Sets the thread name
            Thread.currentThread().setName("Parser Thread");
            // Displays thread name
            Log.e("Ben", "Getting Called At Parse: " + Thread.currentThread().getName());
            // Variables
            URL aurl;
            URLConnection yc;
            XmlPullParserFactory pullParserFactory;
            // End Variables
            try
            {
                // Sets url
                aurl = new URL(url);
                // Opens the connection
                yc = aurl.openConnection();
                // Sets pull parser factory
                pullParserFactory = XmlPullParserFactory.newInstance();
                // Sets pull parser
                XmlPullParser parser = pullParserFactory.newPullParser();
                // Sets the feature
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                // Sets the input value
                parser.setInput(yc.getInputStream(), null);
                // Sets the array list to the results form pull parser.
                earthQuakeMasterList = parseXML(parser);
                // Run the adapter on the UI thread
                MainActivity.this.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                       InstiantiateAdapter();
                    }
                });
            }
            catch (XmlPullParserException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        private ArrayList<EarthQuakeMaster> parseXML(XmlPullParser parser) throws XmlPullParserException, IOException
        {
            // Sets earthquake to null
            earthQuakeMaster = null;
            // Creates the earthquake master array list
            ArrayList<EarthQuakeMaster> earthQuakeMasterArrayList = null;
            // Event type equal to parser event type
            int eventType = parser.getEventType();
            // While it doesnt equaol the end of the document
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                String name;
                switch(eventType)
                {
                    // If its the start of the document then create new array list
                    case XmlPullParser.START_DOCUMENT:
                    earthQuakeMasterArrayList = new ArrayList();
                    break;
                    // If its the starting tag, check if its equal to 'item'. If its equal to description then add data.
                    case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if(name.equals("item"))
                    {
                        earthQuakeMaster = new EarthQuakeMaster();
                    }
                    else if (earthQuakeMaster != null)
                    {
                       if(name.equals("description"))
                        {
                            earthQuakeMaster.description = SeperateData(parser.nextText());
                        }
                    }
                    break;
                    case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("item") && earthQuakeMaster != null)
                    {
                        earthQuakeMasterArrayList.add(earthQuakeMaster);
                    }
                    break;
                }
                eventType = parser.next();
            }
            return earthQuakeMasterArrayList;
        }

        private String SeperateData(String dataToSeperate)
        {
            // Seperates parts of descriptio using a semi colon. Adds data to earthquake master class
            String[] parts = dataToSeperate.split(";");
            String data1 = parts[0]; // This is date
            String dayDate = data1.substring(18, data1.length() - 10);
            earthQuakeMaster.date = dayDate;

            String data2 = parts[1]; // This is location
            String location = data2.substring(11, data2.length());
            earthQuakeMaster.locationName = location;

            String data3 = parts[2]; // This is Lat/Long
            String lat = data3.substring(10, 17);
            String longa = data3.substring(18, 24);
            earthQuakeMaster.latitude = lat;
            earthQuakeMaster.longatude = longa;

            String data4 = parts[3]; // This is Depth
            String depth = data4.subSequence(8,9).toString();
            earthQuakeMaster.depth = depth;

            String data5 = parts[4]; // This is magnitude
            String mag = data5.subSequence(13, 16).toString();
            earthQuakeMaster.magnitude = mag;

            String data6 = parts[0]; // This is time
            String time = data6.substring(35, 44);
            earthQuakeMaster.time = time;

            return dayDate + time + location + lat + longa + depth + mag;
        }
    }

    //
    // ─── SORTING CLASSES ─────────────────────────────────────────────────────────────────
    //

    class CompareNorth implements Comparator<EarthQuakeMaster>
    {
        @Override
        public int compare(EarthQuakeMaster o1, EarthQuakeMaster o2)
        {
            return o2.getLatitude().compareTo(o1.getLatitude());
        }
    }

    class CompareSouth implements Comparator<EarthQuakeMaster>
    {
        @Override
        public int compare(EarthQuakeMaster o1, EarthQuakeMaster o2)
        {
            return o1.getLatitude().compareTo(o2.getLatitude());
        }
    }

    class CompareEast implements Comparator<EarthQuakeMaster>
    {
        @Override
        public int compare(EarthQuakeMaster o1, EarthQuakeMaster o2)
        {
            return o2.getLongatude().compareTo(o1.getLongatude());
        }
    }

    class CompareWest implements Comparator<EarthQuakeMaster>
    {
        @Override
        public int compare(EarthQuakeMaster o1, EarthQuakeMaster o2)
        {
            return o1.getLongatude().compareTo(o2.getLongatude());
        }
    }

    class CompareDeepesteAsc implements Comparator<EarthQuakeMaster>
    {
        @Override
        public int compare(EarthQuakeMaster o1, EarthQuakeMaster o2)
        {
            return o2.getDepth().compareTo(o1.getDepth());
        }
    }

    class CompareShallowestDec implements Comparator<EarthQuakeMaster>
    {
        @Override
        public int compare(EarthQuakeMaster o1, EarthQuakeMaster o2)
        {
            return o1.getDepth().compareTo(o2.getDepth());
        }
    }

    class CompareMagnitudeAsc implements Comparator<EarthQuakeMaster>
    {
        @Override
        public int compare(EarthQuakeMaster o1, EarthQuakeMaster o2)
        {
            return o2.getMagnitude().compareTo(o1.getMagnitude());
        }
    }

    class CompareMagnitudeDec implements Comparator<EarthQuakeMaster>
    {
        @Override
        public int compare(EarthQuakeMaster o1, EarthQuakeMaster o2)
        {
            return o1.getMagnitude().compareTo(o2.getMagnitude());
        }
    }
}