package com.coderboy19.consume.healthy;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder>{
    private ArrayList<Meal> listItems;

    public HistoryListAdapter(ArrayList<Meal> listItems) {
        this.listItems = listItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title_Text;
        TextView date_Text;
        TextView mealType_Text;
        TextView calories_mealType_Text;

        public ViewHolder(View itemView) {
            super(itemView);

            title_Text = itemView.findViewById(R.id.title_Text);
            date_Text = itemView.findViewById(R.id.date_Text);
            mealType_Text = itemView.findViewById(R.id.mealType_Text);
            calories_mealType_Text = itemView.findViewById(R.id.calories_mealType_Text);

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
    public HistoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HistoryListAdapter.ViewHolder holder, final int position) {
        holder.date_Text.setText(getTodayDate());
        String mealTypes = "Breakfast\nLunch\nDinner\nSnack\n\nTotal Calories:";
        holder.mealType_Text.setText(mealTypes);
        int[] calories = getCaloriesByMealType();
        String caloriesText = calories[0]+"\n"+calories[1]+"\n"+calories[2]+"\n"+
                calories[3]+"\n\n"+calories[4];
        holder.calories_mealType_Text.setText(caloriesText);
    }

    public String getTodayDate() {
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }

    public int[] getCaloriesByMealType() {
        int[] calories = new int[5];

        for (int i = 0; i < listItems.size(); i++) {
            if(listItems.get(i).getMealType().equals("Breakfast")) {
                calories[0] += getCalories(listItems.get(i).getCalories());
            } else if (listItems.get(i).getMealType().equals("Lunch")) {
                calories[1] += getCalories(listItems.get(i).getCalories());
            } else if (listItems.get(i).getMealType().equals("Dinner")) {
                calories[2] += getCalories(listItems.get(i).getCalories());
            } else {
                calories[3] += getCalories(listItems.get(i).getCalories());
            }
        }

        for (int i = 0; i < 4; i++) {
            calories[4] += calories[i];
        }

        return calories;
    }

    public int getCalories(ArrayList<String> calories) {
        int totalCalories = 0;
        for (String cal: calories) {
            if(!cal.equals("NA")) {
                String test = cal.substring(0,cal.indexOf(" "));
                totalCalories += Integer.parseInt(test);
            }

        }
        return totalCalories;
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
