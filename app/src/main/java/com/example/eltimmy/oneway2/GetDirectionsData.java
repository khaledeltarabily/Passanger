package com.example.eltimmy.oneway2;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by eltimmy on 3/16/2018.
 */



public class GetDirectionsData extends AsyncTask<Object,String,String> {

    GoogleMap mMap;
    String url;
    String getDirectionsData;
    int waypoints;
    JSONArray jsonArray;

    String distance, duration;

    ArrayList<Marker> markers;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        markers = (ArrayList<Marker>) objects[2];
        waypoints=(int)objects[3];


        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            getDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {
        DataParser dataParser = new DataParser();
        waypoints=dataParser.getlength(s);


        for(int i=0;i<waypoints;i++)//create polylines between all points
        {
            String[] directionsList = null;
            directionsList = dataParser.parseDirections(s,i);
            displayDirectionList(directionsList);
        }
        int dur=0;
        int marker_index=0;
        jsonArray=dataParser.getWaypointOrder(s);


        for(int i=0;i<waypoints-1;i++)//create duration
        {
            try {
                marker_index=jsonArray.getInt(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            duration=dataParser.getDuration(s,i);
            dur=dur+Integer.parseInt(duration);

            markers.get(marker_index).setTitle("Duration = "+dur/60+" mins");
        }
        int dis=0;
        for(int i=0;i<waypoints-1;i++)//create distance
        {
            try {
                marker_index=jsonArray.getInt(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            distance=dataParser.getDistance(s,i);
            dis=dis+Integer.parseInt(distance);

            markers.get(marker_index).setSnippet("Distance = "+dis+" M");
        }
    }

    //create polylines between two points
    private void displayDirectionList(String[] directionsList) {

        int count = directionsList.length;
        for (int i = 0; i < count; i++) {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.BLUE);
            options.width(10);
            options.addAll(PolyUtil.decode(directionsList[i]));

            mMap.addPolyline(options);
        }
    }


}