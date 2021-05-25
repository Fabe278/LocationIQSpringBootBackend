package at.fhainzinger.service;

import at.fhainzinger.Settings;
import at.fhainzinger.data.AddressResource;
import at.fhainzinger.data.LocationResource;
import at.fhainzinger.data.LongitudeLatitude;
import at.fhainzinger.exceptions.LocationException;
import at.fhainzinger.exceptions.LocationNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class LocationIQDataService {

    public String getAddress(String latitude, String longitude) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("apiKey", Settings.ACCESTOKEN);
        vars.put("latitude", latitude);
        vars.put("longitude", longitude);
        try{
            ResponseEntity<AddressResource> response =
                    restTemplate.getForEntity(Settings.LOCATION_IQ_SEARCH_BY_LONGITUDE_LATITUDE, AddressResource.class, vars);
            AddressResource addressResource = response.getBody();
            assert addressResource != null;
            return addressResource.getAddress().getRoad() + " " + addressResource.getAddress().getHouse_number();
        } catch (RestClientResponseException e) {
            if(e.getRawStatusCode() == 400) {
                throw new LocationNotFoundException("The location was not found");
            } else {
                throw new LocationException(e.getResponseBodyAsString());
            }
        }
    }

    public LongitudeLatitude getLongitudeLatitudeByAddress(String address) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("apiKey", Settings.ACCESTOKEN);
        vars.put("searchString", address);
        try{
            ResponseEntity<LocationResource[]> response =
                    restTemplate.getForEntity(Settings.LOCATION_IQ_SEARCH_BY_ADDRESS, LocationResource[].class, vars);
            LongitudeLatitude result = new LongitudeLatitude();
            LocationResource[] locations = response.getBody();
            assert locations != null;
            result.setLongitude(locations[0].getLon());
            result.setLatitude(locations[0].getLat());
            return result;
        } catch (RestClientResponseException e) {
            if(e.getRawStatusCode() == 400) {
                throw new LocationNotFoundException(String.format("The location with the address %d was not found", address));
            } else {
                throw new LocationException(e.getResponseBodyAsString());
            }
        }
    }

    private static String callGetRequest(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            con.disconnect();
            return content.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ignored) {
        }

        return "";
    }
}
