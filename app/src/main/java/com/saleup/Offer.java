package com.saleup;

public class Offer {
    int OfferId;
    int Price;
    int LocationId;
    String EndDate;

    public Offer(int offerId, int price, int locationId, String endDate){
        this.OfferId = offerId;
        this.Price = price;
        this.LocationId = locationId;
        this.EndDate = endDate;
    }
}
