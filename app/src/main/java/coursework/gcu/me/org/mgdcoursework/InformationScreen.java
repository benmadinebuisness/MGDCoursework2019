package coursework.gcu.me.org.mgdcoursework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.util.concurrent.Executors;

// Student ID: S1507379
// BMADIN200
// Computer Games (Software Development)

public class InformationScreen extends AppCompatActivity implements Runnable
{
    private TextView locationInfo;
    private TextView coordinateInfo;
    private TextView dateInfo;
    private TextView timeInfo;
    private TextView magnitudeInfo;
    private TextView depthInfo;

    private String locationInfoString;
    private String coordinateInfoString;
    private String dateInfoString;
    private String timeInfoString;
    private String magnitudeInfoString;
    private String depthInfoString;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_screen);
        StartThread();
    }

    private void StartThread()
    {
        // Creates a new thread and calls the methods
        Executors.newFixedThreadPool(10).execute(new Runnable() {
            @Override
            public void run() {
                Log.e("Ben", "Getting Called at Information: " + Thread.currentThread().getName());
                InstianiteComponents();
                InstianiteExtra();
                SetInformation();
            }
        });
    }

    @Override
    public void run()
    {

    }

    private void InstianiteComponents()
    {
        // Sets the variables
        locationInfo = (TextView) findViewById(R.id.locationInfo);
        coordinateInfo = (TextView) findViewById(R.id.coordinateInfo);
        dateInfo = (TextView) findViewById(R.id.dateInfo);
        timeInfo = (TextView) findViewById(R.id.timeInfo);
        magnitudeInfo = (TextView) findViewById(R.id.magnitudeInfo);
        depthInfo = (TextView) findViewById(R.id.depthInfo);
    }

    private void InstianiteExtra()
    {
        // Gets intents, assigns the variables with values
        Bundle extra = getIntent().getExtras();
        locationInfoString = (String)extra.get("location");
        coordinateInfoString = (String)extra.get("coordinates");
        dateInfoString = (String)extra.get("date");
        timeInfoString = (String)extra.get("time");
        magnitudeInfoString = (String)extra.get("magnitude");
        depthInfoString = (String)extra.get("depth");
    }

    private void SetInformation()
    {
        // Sets each text field
        locationInfo.setText("  Location: " + locationInfoString + "    ");
        coordinateInfo.setText("    Coordinates: " + coordinateInfoString + "   ");
        dateInfo.setText("  Date: " + dateInfoString + "    ");
        timeInfo.setText("  Time: " + timeInfoString+ "   ");
        magnitudeInfo.setText(" Magnitude: " + magnitudeInfoString + "   ");
        depthInfo.setText(" Depth: " + depthInfoString + "   ");
    }
}
