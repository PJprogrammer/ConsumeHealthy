package com.coderboy19.consume.healthy;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.ViewHolder> {

    private ArrayList<Meal> listItems;

    public MealListAdapter(ArrayList<Meal> listItems) {
        this.listItems = listItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView mealIcon;
        public TextView mealType;
        public TextView foodOne;
        public ImageView foodImage;
        public TextView foodList;
        public TextView caloriesList;

        public ViewHolder(View itemView) {
            super(itemView);
            mealIcon = itemView.findViewById(R.id.meal_icon_IMG);
            mealType = itemView.findViewById(R.id.mealType_Text);
            foodOne = itemView.findViewById(R.id.FoodOne_Text);
            foodImage = itemView.findViewById(R.id.food_Image);
            foodList = itemView.findViewById(R.id.food_Text);
            caloriesList = itemView.findViewById(R.id.calories_Text);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }

    @Override
    public MealListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_meal,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MealListAdapter.ViewHolder holder, final int position) {
        Meal meal = listItems.get(position);
        String mealType = meal.getMealType();

        if(mealType.equals("Breakfast")) {
            holder.mealIcon.setImageResource(R.drawable.ic_breakfast);
        } else if(mealType.equals("Lunch")) {
            holder.mealIcon.setImageResource(R.drawable.ic_lunch_box);
        } else if(mealType.equals("Dinner")) {
            holder.mealIcon.setImageResource(R.drawable.ic_plate);
        } else if(mealType.equals("Snack")) {
            holder.mealIcon.setImageResource(R.drawable.ic_popcorn);
        }

        holder.mealType.setText(mealType);
        holder.foodOne.setText(meal.getFood(0));
        if(meal.getFoodImage() != null)
            holder.foodImage.setImageBitmap(meal.getFoodImage());
        holder.foodList.setText(getAllFood(meal));
        holder.caloriesList.setText(getAllCalories(meal));
    }

    public String getAllFood(Meal meal) {
        String foodString = "";
        ArrayList<String> foods = meal.getFoods();
        for (int i = 0; i < foods.size(); i++) {
            foodString += foods.get(i);
            if(i != (foods.size()-1))
                foodString += "\n";
        }
        return foodString;
    }

    public String getAllCalories(Meal meal) {
        String caloriesString = "";
        ArrayList<String> calories = meal.getCalories();
        for (int i = 0; i < calories.size(); i++) {
            caloriesString += calories.get(i);
            if(i != (calories.size()-1))
                caloriesString += "\n";
        }
        return caloriesString;
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
