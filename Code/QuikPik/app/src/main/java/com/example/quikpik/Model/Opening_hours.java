package com.example.quikpik.Model;

public class Opening_hours {
    private String open_now;

    public String getOpen_now ()
    {
        if(false){
            return "No";
        }
        return "Yes";
    }

    public void setOpen_now (String open_now)
    {
        this.open_now = open_now;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [open_now = "+open_now+"]";
    }
}
