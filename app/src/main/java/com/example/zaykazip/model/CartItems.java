package com.example.zaykazip.model;

public class CartItems {
    private String itemId;          // Firebase push key
    private String foodName;
    private String foodPrice;
    private String foodDescription;
    private String foodImage;
    private String foodQuantity;
    private String foodIngredient;

    // Required empty constructor for Firebase
    public CartItems() {}

    public CartItems(String foodName, String foodPrice, String foodDescription,
                     String foodImage, String foodQuantity, String foodIngredient) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDescription = foodDescription;
        this.foodImage = foodImage;
        this.foodQuantity = foodQuantity;
        this.foodIngredient = foodIngredient;
    }

    // 🔹 itemId getter & setter
    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    // Other Getters & Setters
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public String getFoodPrice() { return foodPrice; }
    public void setFoodPrice(String foodPrice) { this.foodPrice = foodPrice; }

    public String getFoodDescription() { return foodDescription; }
    public void setFoodDescription(String foodDescription) { this.foodDescription = foodDescription; }

    public String getFoodImage() { return foodImage; }
    public void setFoodImage(String foodImage) { this.foodImage = foodImage; }

    public String getFoodQuantity() { return foodQuantity; }
    public void setFoodQuantity(String foodQuantity) { this.foodQuantity = foodQuantity; }

    public String getFoodIngredient() { return foodIngredient; }
    public void setFoodIngredient(String foodIngredient) { this.foodIngredient = foodIngredient; }
}
