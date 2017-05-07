package com.progdawn.collectaholic;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Dawn Myers on 5/7/2017.
 */

public class Collectable {

    private UUID mId;
    private String mName;
    private String mSeries;
    private Date mDate;

    public Collectable(){
        this(UUID.randomUUID());
    }

    public Collectable(UUID id){
        mId = id;
        mDate = new Date();
    }

    public UUID getId(){
        return mId;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public String getSeries(){
        return mSeries;
    }

    public void setSeries(String series){
        mSeries = series;
    }

    public Date getDate(){
        return mDate;
    }

    public void setDate(Date date){
        mDate = date;
    }

    public String getPhotoFilename(){
        return "IMG_" +getId().toString() + ".jpg";
    }
}
