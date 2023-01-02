package com.statuses.flavoured.model;


public class ImageModel {
    private String path;
    private boolean isSelected = false;

    public ImageModel(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
}
