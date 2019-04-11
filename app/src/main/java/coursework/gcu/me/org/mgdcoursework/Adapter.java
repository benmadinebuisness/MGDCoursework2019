package coursework.gcu.me.org.mgdcoursework;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

// Student ID: S1507379
// BMADIN200
// Computer Games (Software Development)

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>
{
    ArrayList<EarthQuakeMaster> earthQuakeMasterList;
    Context context;

    public Adapter(ArrayList<EarthQuakeMaster> earthQuakeMasterList, Context context)
    {
        // Adapter constructor
        this.earthQuakeMasterList = earthQuakeMasterList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        // Sets view component
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview, viewGroup, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i)
    {
        // Sets earthquake master to current
        EarthQuakeMaster earthQuakeMaster = earthQuakeMasterList.get(i);

        myViewHolder.rawDataDisplay.setText(earthQuakeMaster.GetLocationName());
    }

    @Override
    public int getItemCount()
    {
        // Returns size of array
        return earthQuakeMasterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // Variables
        public Button moreInfoButton;
        public TextView rawDataDisplay;

        public MyViewHolder(@NonNull View itemView)
        {
            // Sets variablez
            super(itemView);
            rawDataDisplay = (TextView) itemView.findViewById(R.id.rawDataDisplay);
            moreInfoButton = (Button) itemView.findViewById(R.id.cardViewInfoButton);
            moreInfoButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId()) {
                case R.id.cardViewInfoButton:
                    // If button is clicked then create new intent going to Informatuion Class
                    // Assigns values
                    Intent openInfo = new Intent(v.getContext(), InformationScreen.class );
                    EarthQuakeMaster earthQuakeMaster = earthQuakeMasterList.get(this.getAdapterPosition());
                    openInfo.putExtra("location", earthQuakeMaster.GetLocationName());
                    openInfo.putExtra("coordinates", earthQuakeMaster.getLongatude() + ", " + earthQuakeMaster.getLatitude());
                    openInfo.putExtra("date", earthQuakeMaster.getDate());
                    openInfo.putExtra("time", earthQuakeMaster.getTime());
                    openInfo.putExtra("magnitude", earthQuakeMaster.getMagnitude());
                    openInfo.putExtra("depth", earthQuakeMaster.getDepth());
                    v.getContext().startActivity(openInfo);
                    break;
            }
        }
    }
}