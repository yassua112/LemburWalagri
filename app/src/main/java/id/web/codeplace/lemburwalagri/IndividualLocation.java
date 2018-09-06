package id.web.codeplace.lemburwalagri;

import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * POJO class for an individual location
 */
public class IndividualLocation {

    private String name;
    private String alamat;
    private LatLng location;

    public IndividualLocation(String name, String adress, LatLng location) {
        this.name = name;
        this.alamat = adress;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.alamat = name;
    }

    public String getAddress() {
        return alamat;
    }

    public void setAdress(String adress) {
        this.alamat = adress;
    }

    public LatLng getLocation() {
        return location;
    }

}