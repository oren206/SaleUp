package com.saleup;

public class Offer {
    int Price;
    int LocationId;
    String EndDate;

    public Offer(int price, int locationId, String endDate){
        this.Price = price;
        this.LocationId = locationId;
        this.EndDate = endDate;
    }
}
