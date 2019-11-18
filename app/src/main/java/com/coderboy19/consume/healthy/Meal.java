package com.coderboy19.consume.healthy;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Meal {
    private String mealType;
    private ArrayList<String> foods;
    private ArrayList<String> calories;
    private Bitmap foodImage;

    public Meal(String mealType, ArrayList<String> foods, ArrayList<String> calories, Bitmap foodImage) {
        this.mealType = mealType;
        this.foods = foods;
        this.calories = calories;
        this.foodImage = foodImage;
    }

    public String getMealType() {
        return mealType;
    }

    public String getFood(int pos) {
        return foods.get(pos);
    }

    public ArrayList<String> getFoods() {
        return foods;
    }

    public ArrayList<String> getCalories() {
        return calories;
    }

    public Bitmap getFoodImage() {
        return foodImage;
    }

}
