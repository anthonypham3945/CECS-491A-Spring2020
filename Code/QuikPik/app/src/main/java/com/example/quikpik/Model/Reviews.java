package com.example.quikpik.Model;

public class Reviews {
    private String author_name;

    private String profile_photo_url;

    private String author_url;

    private String rating;

    private String language;

    private String text;

    private String time;

    private String relative_time_description;

    public String getAuthor_name ()
    {
        return author_name;
    }

    public void setAuthor_name (String author_name)
    {
        this.author_name = author_name;
    }

    public String getProfile_photo_url ()
    {
        return profile_photo_url;
    }

    public void setProfile_photo_url (String profile_photo_url)
    {
        this.profile_photo_url = profile_photo_url;
    }

    public String getAuthor_url ()
    {
        return author_url;
    }

    public void setAuthor_url (String author_url)
    {
        this.author_url = author_url;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    public String getLanguage ()
    {
        return language;
    }

    public void setLanguage (String language)
    {
        this.language = language;
    }

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getTime ()
    {
        return time;
    }

    public void setTime (String time)
    {
        this.time = time;
    }

    public String getRelative_time_description ()
    {
        return relative_time_description;
    }

    public void setRelative_time_description (String relative_time_description)
    {
        this.relative_time_description = relative_time_description;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [author_name = "+author_name+", profile_photo_url = "+profile_photo_url+", author_url = "+author_url+", rating = "+rating+", language = "+language+", text = "+text+", time = "+time+", relative_time_description = "+relative_time_description+"]";
    }
}
