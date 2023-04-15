package com.example.readbook.book;

import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable {

    private String img;
    private String name;
    private Date date; // Ngày đọc của account
    private String author;
    private String description;
    private String type;
    private int numberOfRead;
    private String language; // Ngôn ngữ viết của sách
    private String pdf;

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public Book(String name) {
        this.name = name;
    }

    public Book(String img, String name) {
        this.img = img;
        this.name = name;
    }

    public Book(String img, String name, String author) {
        this.img = img;
        this.name = name;
        this.author = author;
    }

    public Book(String img, String name, Date date, String author) {
        this.img = img;
        this.name = name;
        this.date = date;
        this.author = author;
    }

    public Book(String img, String name, Date date, String author, String description, String type, int numberOfRead, String language) {
        this.img = img;
        this.name = name;
        this.date = date;
        this.author = author;
        this.description = description;
        this.type = type;
        this.numberOfRead = numberOfRead;
        this.language = language;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumberOfRead() {
        return numberOfRead;
    }

    public void setNumberOfRead(int numberOfRead) {
        this.numberOfRead = numberOfRead;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
