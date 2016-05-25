package com.saleup;


public class Item {
    int ItemId;
    String Image;
    String Description;
    String EndDate;

    public Item(int ItemId, String img, String description, String endDate){
        this.ItemId = ItemId;
        this.Image = img;
        this.Description = description;
        this.EndDate = endDate;
    }
}
