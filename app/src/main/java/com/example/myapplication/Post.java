package com.example.myapplication;

public class Post {
    private String edittext;
    private Boolean isDelete;
    private String imagePath;
    private String article;

    public Post() {
        edittext = "";
        isDelete=false;
        imagePath = "";
        article = "";
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
    public void setArticle(String article){
        this.article = article;
    }
    public String getArticle(){
        return this.article;
    }
}
