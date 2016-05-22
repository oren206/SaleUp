package com.saleup;

public class ChatMessage {
    int ItemId;
    String UserName;
    String Message;

    public ChatMessage(int itemId, String userName, String message){
        this.ItemId = itemId;
        this.UserName = userName;
        this.Message = message;
    }
}
