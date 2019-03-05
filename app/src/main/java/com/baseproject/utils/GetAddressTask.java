package com.baseproject.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mushahid on 3/15/2017
 */

public class GetAddressTask extends AsyncTask<Void, String, String> {

    private double latitude;
    private double longitude;
    private Geocoder geocoder;
    //comment
    private String country ="";

    private AddressListner addressListner;

    public GetAddressTask(Context context) {
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    public GetAddressTask(Context context,double lat, double lng) {
        geocoder = new Geocoder(context, Locale.getDefault());
        latitude = lat;
        longitude = lng;
    }


    public void setLatLng(double lat, double lng){
        latitude = lat;
        longitude = lng;
    }






    @Override
    protected String doInBackground(Void[] params) {

        try {

            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append(",");
                }
                sb.append(address.getCountryName());
                country = address.getCountryName();
                return sb.toString();
            }
        } catch (IOException e) {
            Log.e("AmHappy", "Unable connect to Geocoder");
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(addressListner!=null){
            if(s==null){
                addressListner.onError();
            }else{
                addressListner.onAddressFound(s,country);
            }
        }
        super.onPostExecute(s);
    }

    public void setAddressListner(AddressListner addressListner) {
        this.addressListner = addressListner;
    }


    public void getAddress(){
        execute();
    }

    public interface AddressListner{
        void onAddressFound(String Address, String Country);
        void onError();
    }

}
