package io.github.sher1234.service.model.base;

import java.io.Serializable;

@SuppressWarnings("all")
public class VisitImage implements Serializable {

    private String ImageNumber;
    private String VisitNumber;
    private String ImagePath;
    private String Extension;

    public VisitImage() {

    }

    VisitImage(String imageNumber, String visitNumber, String imagePath, String extension) {
        ImageNumber = imageNumber;
        VisitNumber = visitNumber;
        ImagePath = imagePath;
        Extension = extension;
    }

    public String getImageNumber() {
        return ImageNumber;
    }

    public void setImageNumber(String imageNumber) {
        ImageNumber = imageNumber;
    }

    public String getVisitNumber() {
        return VisitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        VisitNumber = visitNumber;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
