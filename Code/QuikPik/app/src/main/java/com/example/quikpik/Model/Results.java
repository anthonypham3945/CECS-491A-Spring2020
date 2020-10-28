package com.example.quikpik.Model;

public class Results {
    private String[] types;

    private String business_status;

    private String icon;

    private String rating;

    private Photos[] photos;

    private String reference;

    private String user_ratings_total;

    private String price_level;

    private String scope;

    private String name;

    private Opening_hours opening_hours;

    private Geometry geometry;

    private String vicinity;

    private Plus_code plus_code;

    private String place_id;

    public String[] getTypes ()
    {
        return types;
    }

    public void setTypes (String[] types)
    {
        this.types = types;
    }

    public String getBusiness_status ()
    {
        return business_status;
    }

    public void setBusiness_status (String business_status)
    {
        this.business_status = business_status;
    }

    public String getIcon ()
    {
        return icon;
    }

    public void setIcon (String icon)
    {
        this.icon = icon;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    public Photos[] getPhotos ()
    {
        return photos;
    }

    public void setPhotos (Photos[] photos)
    {
        this.photos = photos;
    }

    public String getReference ()
    {
        return reference;
    }

    public void setReference (String reference)
    {
        this.reference = reference;
    }

    public String getUser_ratings_total ()
    {
        return user_ratings_total;
    }

    public void setUser_ratings_total (String user_ratings_total)
    {
        this.user_ratings_total = user_ratings_total;
    }

    public String getPrice_level ()
    {
        return price_level;
    }

    public void setPrice_level (String price_level)
    {
        this.price_level = price_level;
    }

    public String getScope ()
    {
        return scope;
    }

    public void setScope (String scope)
    {
        this.scope = scope;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Opening_hours getOpening_hours ()
    {
        return opening_hours;
    }

    public void setOpening_hours (Opening_hours opening_hours)
    {
        this.opening_hours = opening_hours;
    }

    public Geometry getGeometry ()
    {
        return geometry;
    }

    public void setGeometry (Geometry geometry)
    {
        this.geometry = geometry;
    }

    public String getVicinity ()
    {
        return vicinity;
    }

    public void setVicinity (String vicinity)
    {
        this.vicinity = vicinity;
    }

    public Plus_code getPlus_code ()
    {
        return plus_code;
    }

    public void setPlus_code (Plus_code plus_code)
    {
        this.plus_code = plus_code;
    }

    public String getPlace_id ()
    {
        return place_id;
    }

    public void setPlace_id (String place_id)
    {
        this.place_id = place_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [types = "+types+", business_status = "+business_status+", icon = "+icon+", rating = "+rating+", photos = "+photos+", reference = "+reference+", user_ratings_total = "+user_ratings_total+", price_level = "+price_level+", scope = "+scope+", name = "+name+", opening_hours = "+opening_hours+", geometry = "+geometry+", vicinity = "+vicinity+", plus_code = "+plus_code+", place_id = "+place_id+"]";
    }
}

