/**
 * @author Aby Mokua
 * @ID S1732294
 */

package org.me.earthquake.Model;

import java.io.Serializable;

public class Item implements Serializable {

    private String title;
    private String magnitude;
    private String date;
    private String depth;
    private String description;
    private String lat;
    private String lon;
    private String location;

    public Item(String location, String title, String magnitude, String date, String depth, String description, String lat, String lon) {
        this.magnitude = magnitude;
        this.date = date;
        this.depth = depth;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.location = location;
    }

    public Item(Item item) {
        this.magnitude = item.getMagnitude();
        this.date = item.getDate();
        this.depth = item.getDepth();
        this.description = item.getDescription();
        this.lat = item.getLat();
        this.lon = item.getLon();
        this.location = item.getLocation();
    }

    public Item(){
        this("", "", "", "","","","", "");
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", magnitude='" + magnitude + '\'' +
                ", date='" + date + '\'' +
                ", depth='" + depth + '\'' +
                ", description='" + description + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
