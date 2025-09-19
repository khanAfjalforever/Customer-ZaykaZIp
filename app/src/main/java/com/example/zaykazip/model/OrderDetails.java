package com.example.zaykazip.model;

import java.util.ArrayList;

public class OrderDetails {

    private String userUid;
    private String userName;
    private ArrayList<String> foodNames;
    private ArrayList<String> foodPrices;
    private ArrayList<String> foodImages;
    private ArrayList<String> foodQuantities;
    private String address;
    private String totalPrice;
    private String phoneNumber;
    private String itemPushKey;
    private long currentTime;
    private boolean orderAccepted;
    private boolean paymentReceived;

    // Empty constructor required for Firebase
    public OrderDetails() {}

    public OrderDetails(String userUid, String userName,
                        ArrayList<String> foodNames, ArrayList<String> foodPrices, ArrayList<String> foodImages,
                        ArrayList<String> foodQuantities, String address, String totalPrice,
                        String phoneNumber, String itemPushKey, long currentTime,
                        boolean orderAccepted, boolean paymentReceived) {
        this.userUid = userUid;
        this.userName = userName;
        this.foodNames = foodNames;
        this.foodPrices = foodPrices;
        this.foodImages = foodImages;
        this.foodQuantities = foodQuantities;
        this.address = address;
        this.totalPrice = totalPrice;
        this.phoneNumber = phoneNumber;
        this.itemPushKey = itemPushKey;
        this.currentTime = currentTime;
        this.orderAccepted = orderAccepted;
        this.paymentReceived = paymentReceived;
    }

    // Getters
    public String getUserUid() { return userUid; }
    public String getUserName() { return userName; }
    public ArrayList<String> getFoodNames() { return foodNames; }
    public ArrayList<String> getFoodPrices() { return foodPrices; }
    public ArrayList<String> getFoodImages() { return foodImages; }
    public ArrayList<String> getFoodQuantities() { return foodQuantities; }
    public String getAddress() { return address; }
    public String getTotalPrice() { return totalPrice; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getItemPushKey() { return itemPushKey; }
    public long getCurrentTime() { return currentTime; }
    public boolean isOrderAccepted() { return orderAccepted; }
    public boolean isPaymentReceived() { return paymentReceived; }

    // Setters
    public void setUserUid(String userUid) { this.userUid = userUid; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setFoodNames(ArrayList<String> foodNames) { this.foodNames = foodNames; }
    public void setFoodPrices(ArrayList<String> foodPrices) { this.foodPrices = foodPrices; }
    public void setFoodImages(ArrayList<String> foodImages) { this.foodImages = foodImages; }
    public void setFoodQuantities(ArrayList<String> foodQuantities) { this.foodQuantities = foodQuantities; }
    public void setAddress(String address) { this.address = address; }
    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setItemPushKey(String itemPushKey) { this.itemPushKey = itemPushKey; }
    public void setCurrentTime(long currentTime) { this.currentTime = currentTime; }
    public void setOrderAccepted(boolean orderAccepted) { this.orderAccepted = orderAccepted; }
    public void setPaymentReceived(boolean paymentReceived) { this.paymentReceived = paymentReceived; }
}
