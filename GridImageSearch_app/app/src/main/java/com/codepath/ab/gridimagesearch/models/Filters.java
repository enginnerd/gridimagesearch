package com.codepath.ab.gridimagesearch.models;

import java.io.Serializable;

/**
 * Created by andrewblaich on 2/27/15.
 */
public class Filters implements Serializable{
    public String as_sitesearch; //as_sitesearch=photobucket.com
    public String imgcolor;      /*
                                    imgcolor=black
                                    imgcolor=blue
                                    imgcolor=brown
                                    imgcolor=gray
                                    imgcolor=green
                                    imgcolor=orange
                                    imgcolor=pink
                                    imgcolor=purple
                                    imgcolor=red
                                    imgcolor=teal
                                    imgcolor=white
                                    imgcolor=yellow
                                */
    public String imgsz;        /*
                                    imgsz=icon restricts results to small images
                                    imgsz=small|medium|large|xlarge restricts results to medium-sized images
                                    imgsz=xxlarge restricts results to large images
                                    imgsz=huge restricts resykts to extra-large images
                                */
    public String imgtype;      /*
                                    imgtype=face restricts results to images of faces.
                                    imgtype=photo restricts results to photographic images.
                                    imgtype=clipart restricts results to clipart images.
                                    imgtype=lineart restricts results to line drawing images.
                                */
    public String safe;         /*
                                    safe=active enables the highest level of safe search filtering.
                                    safe=moderate enables moderate safe search filtering (default).
                                    safe=off disables safe search filtering.
                                */

    public Filters(){

    }

    public String getSiteSearch(){
        return as_sitesearch;
    }

    public String getImageColor(){
        return imgcolor;
    }

    public String getImageSize(){
        return imgsz;
    }

    public String getImageType(){
        return imgtype;
    }

    public String getSafeSearch(){
        return safe;
    }

    public void setSiteSearch(String as_sitesearch){
        this.as_sitesearch = as_sitesearch;
    }

    public void setImageColor(String imgcolor){
        this.imgcolor = imgcolor;
    }

    public void setImageSize(String imgsz){
        this.imgsz = imgsz;
    }

    public void setImageType(String imgtype){
        this.imgtype = imgtype;
    }

    public void setSafeSearch(String safe){
        this.safe = safe;
    }

}
