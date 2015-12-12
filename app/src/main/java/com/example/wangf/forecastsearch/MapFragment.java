package com.example.wangf.forecastsearch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.model.LatLng;
import com.hamweather.aeris.maps.AerisMapView;
import com.hamweather.aeris.maps.AerisMapView.AerisMapType;
import com.hamweather.aeris.maps.MapViewFragment;
import com.hamweather.aeris.tiles.AerisTile;
import com.hamweather.aeris.communication.AerisEngine;


public class MapFragment extends MapViewFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // setting up secret key and client id for oauth to aeris
        AerisEngine.initWithKeys(this.getString(R.string.aeris_client_id), this.getString(R.string.aeris_client_secret), "com.example.wangf.forecastsearch");

        try {
            Double lat = ResultActivity.jsonResult.optDouble("latitude");
            Double lng = ResultActivity.jsonResult.optDouble("longitude");
            int zoom = 9;

            View view = inflater.inflate(R.layout.fragment_interactive_maps, container, false);
            mapView = (AerisMapView) view.findViewById(R.id.aerisfragment_map);
            mapView.init(savedInstanceState, AerisMapType.GOOGLE);
            mapView.moveToLocation(new LatLng(lat, lng), zoom);

            // show the radar tile overlay
            mapView.addLayer(AerisTile.RADSAT);

            return view;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }

    }
}
