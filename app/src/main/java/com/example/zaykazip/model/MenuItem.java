package com.example.zaykazip.model;

public class MenuItem {

    private String foodName;
    private String foodPrice;
    private String foodDescription;
    private String foodImage;   // ✅ matches Firebase
    private String foodIngredient; // ✅ matches Firebase

    // Empty constructor (Firebase needs this)
    public MenuItem() {}

    // Full constructor
    public MenuItem(String foodName, String foodPrice, String foodDescription,
                    String foodImage, String foodIngredient) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDescription = foodDescription;
        this.foodImage = foodImage;
        this.foodIngredient = foodIngredient;
    }

    // Getters
    public String getFoodName() {
        return foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public String getFoodImage() {   // ✅ change here
        return foodImage;
    }

    public String getFoodIngredient() {  // ✅ change here
        return foodIngredient;
    }

    // Setters
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public void setFoodIngredient(String foodIngredient) {
        this.foodIngredient = foodIngredient;
    }
}
