package com.quawlebs.drupp.helpers;

import android.util.Log;

import com.android.volley.toolbox.HttpResponse;
import com.google.android.gms.maps.model.LatLng;
import com.quawlebs.drupp.models.Matrix;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import retrofit2.http.Url;

public class MapDirection {
    public final static String DRIVING = "driving";
    public final static String WALKING = "walking";
    private final static String LOG_TAG = "MapDirection";
    private final static String KEY = "AIzaSyAGzbD-bWySCHhvpKti_40Jsvti3TljxB8";

    public Document getXMLFromLatLong(LatLng firstLatLong, LatLng lastLatLong, String Mode) {
        String MapURL = "https://maps.googleapis.com/maps/api/directions/xml?"
                + "origin=" + firstLatLong.latitude + "," + firstLatLong.longitude
                + "&destination=" + lastLatLong.latitude + "," + lastLatLong.longitude
                + "&key=" + KEY
                + "&sensor=false&units=metric&mode=driving";
        Log.i(LOG_TAG, "Map URL " + MapURL);
        try {
            Log.i(LOG_TAG, "Connection Started");
            URL url = new URL(MapURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            HttpClient mHttpClient = new DefaultHttpClient();
            httpURLConnection.connect();

            //HttpResponse response = mHttpClient.execute(new HttpPost(MapURL), new BasicHttpContext());
            // InputStream mInputStream = response.getEntity().getContent();
            InputStream mInputStream = httpURLConnection.getInputStream();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Log.i(LOG_TAG, "got Document");
            return builder.parse(mInputStream);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception getXMLFromLatLong " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private String getDurDist(Document mDocument, String tagName, String type) {
        try {
            NodeList mNodeList = mDocument.getElementsByTagName(tagName);
            Node mNode = mNodeList.item(0);
            NodeList mNodeList2 = mNode.getChildNodes();
            Node mNode2 = mNodeList2.item(getNodeIndex(mNodeList2, type));
            Log.i(LOG_TAG, "getDurDist " + mNode2.getTextContent());
            return mNode2.getTextContent();
        } catch (Exception e) {
            Log.i(LOG_TAG, "getDurDist " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getDurationText(Document mDocument) {
        return getDurDist(mDocument, "duration", "text");
    }

    public String getDurationValue(Document mDocument) {
        return getDurDist(mDocument, "duration", "value");
    }

    public String getDistanceText(Document mDocument) {
        return getDurDist(mDocument, "distance", "text");
    }

    public String getDistanceValue(Document mDocument) {
        return getDurDist(mDocument, "distance", "value");
    }

    private String getAddress(Document mDocument, String tagName) {
        try {
            NodeList nl1 = mDocument.getElementsByTagName(tagName);
            Node node1 = nl1.item(0);
            Log.i(LOG_TAG, "getAddress " + node1.getTextContent());
            return node1.getTextContent();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception getAddress " + e.getMessage());
        }
        return null;
    }

    public String getFirstLatLongAddress(Document mDocument) {
        return getAddress(mDocument, "start_address");
    }

    public String getLastLatLongAddress(Document mDocument) {
        return getAddress(mDocument, "end_address");
    }


    public ArrayList<LatLng> getLatLongDirectionsList(Document mDocument) {
        ArrayList<LatLng> lstLantLong = new ArrayList<LatLng>();
        Log.d(LOG_TAG, "mDocument " + mDocument);
        NodeList mStepNodeList = mDocument.getElementsByTagName("step");
        if (mStepNodeList.getLength() > 0) {
            for (int i = 0; i < mStepNodeList.getLength(); i++) {
                Node mNode1 = mStepNodeList.item(i);
                NodeList mNodeList = mNode1.getChildNodes();
                Node locationNode = mNodeList.item(getNodeIndex(mNodeList, "start_location"));
                NodeList mNodeList2 = locationNode.getChildNodes();
                Node latNode = mNodeList2.item(getNodeIndex(mNodeList2, "lat"));
                double lat = Double.parseDouble(latNode.getTextContent());
                Node lngNode = mNodeList2.item(getNodeIndex(mNodeList2, "lng"));
                double lng = Double.parseDouble(lngNode.getTextContent());
                lstLantLong.add(new LatLng(lat, lng));

                locationNode = mNodeList.item(getNodeIndex(mNodeList, "polyline"));
                mNodeList2 = locationNode.getChildNodes();
                latNode = mNodeList2.item(getNodeIndex(mNodeList2, "points"));

                String txtNode = latNode.getTextContent();
                ArrayList<LatLng> arr = new ArrayList<LatLng>();
                int mIndex = 0;
                int mlatNodeLength = txtNode.length();
                int latitude = 0;
                int longitude = 0;
                while (mIndex < mlatNodeLength) {
                    int b, shift = 0, result = 0;
                    do {
                        b = txtNode.charAt(mIndex++) - 63;
                        result |= (b & 0x1f) << shift;
                        shift += 5;
                    } while (b >= 0x20);
                    int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                    latitude += dlat;
                    shift = 0;
                    result = 0;
                    do {
                        b = txtNode.charAt(mIndex++) - 63;
                        result |= (b & 0x1f) << shift;
                        shift += 5;
                    } while (b >= 0x20);
                    int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                    longitude += dlng;

                    LatLng position = new LatLng((double) latitude / 1E5, (double) longitude / 1E5);
                    arr.add(position);
                }

                for (int j = 0; j < arr.size(); j++) {
                    lstLantLong.add(new LatLng(arr.get(j).latitude, arr.get(j).longitude));
                }

                locationNode = mNodeList.item(getNodeIndex(mNodeList, "end_location"));
                mNodeList2 = locationNode.getChildNodes();
                latNode = mNodeList2.item(getNodeIndex(mNodeList2, "lat"));
                lat = Double.parseDouble(latNode.getTextContent());
                lngNode = mNodeList2.item(getNodeIndex(mNodeList2, "lng"));
                lng = Double.parseDouble(lngNode.getTextContent());
                lstLantLong.add(new LatLng(lat, lng));
            }
        }

        return lstLantLong;
    }

    private int getNodeIndex(NodeList nl, String nodename) {
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equals(nodename))
                return i;
        }
        return -1;
    }


//    public Matrix getDistanceMatrix(double cLat,double cLng,double dLat,double dLng){
//        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins="+cLat+","+cLng+
//                "&destinations="+dLat+","+dLng+"&key=" + KEY;
//        getJSONFromAPI(url);
//    }
//
//    private DeliveryData parseJson(String json){
//        try {
//            DeliveryData deliveryData = new DeliveryData();
//
//            JSONObject distanceJson = new JSONObject(json)
//                    .getJSONArray("rows")
//                    .getJSONObject(0)
//                    .getJSONArray("elements")
//                    .getJSONObject(0)
//                    .getJSONObject("distance");
//            Double distanceDouble = null ;
//            String distance = distanceJson.get("text").toString();
//            if (distance.contains("km")){
//                distanceDouble = Double.parseDouble(distance.replace("km", ""));
//
//            }else {
//                distanceDouble = Double.parseDouble("0." + distance.replace("m", ""));
//            }
//
//            deliveryData.setDistance(distanceDouble);
//
//            return deliveryData;
//        }catch (JSONException e){
//            e.printStackTrace();
//            return null;
//        }
//    public static String getJSONFromAPI (String url){
//        String output = "";
//        try {
//            URL apiEnd = new URL(url);
//            int responseCode;
//            HttpURLConnection connection;
//            InputStream is;
//
//            connection = (HttpURLConnection) apiEnd.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setReadTimeout(15000);
//            connection.setConnectTimeout(15000);
//            connection.connect();
//
//            responseCode = connection.getResponseCode();
//            if(responseCode < HttpURLConnection.HTTP_BAD_REQUEST){
//                is = connection.getInputStream();
//            }else {
//                is = connection.getErrorStream();
//            }
//
//            output = convertISToString(is);
//            is.close();
//            connection.disconnect();
//
//        }  catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return output;
//    }
//
//    private static String convertISToString(InputStream is){
//        StringBuffer buffer = new StringBuffer();
//
//        try {
//
//            BufferedReader br;
//            String row;
//
//            br = new BufferedReader(new InputStreamReader(is));
//            while ((row = br.readLine())!= null){
//                buffer.append(row);
//            }
//            br.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return buffer.toString();
//    }
}
