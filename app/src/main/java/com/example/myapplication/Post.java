package com.example.myapplication;

public class Post {
    private String edittext;
    private Boolean isDelete;
    private String imagePath;

    public Post() {
        edittext = "";
        isDelete=false;
        imagePath = "";
    }

    public String getEdittext() {
        return edittext;
    }

    public void setEdittext(String textinput) {
        this.edittext = textinput;
    }

    public void setImagePath(String imagepath) {this.imagePath = imagepath; };

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public String getImagePath() {return imagePath; };
}
