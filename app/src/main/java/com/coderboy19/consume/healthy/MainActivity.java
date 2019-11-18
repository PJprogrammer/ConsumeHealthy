package com.coderboy19.consume.healthy;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity{
    private ArrayList<Meal> meals;
    private MealListAdapter adapter;
    private HistoryListAdapter adapterOne;
    private HistoryListAdapter adapterTwo;

    private List<Future<String>> futureList;
    private Future<Bitmap> futureImage;

    //Dialog private fields
    private String mealType;
    private ArrayList<String> foods;
    private ArrayList<String> calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle("Consume Healthy");

        meals = new ArrayList<>();
        adapterOne = new HistoryListAdapter(meals);

        //Set Up Listeners
        final RecyclerView mealList = findViewById(R.id.food_List);
        mealList.setHasFixedSize(true);
        mealList.setLayoutManager(new LinearLayoutManager(mealList.getContext()));
        mealList.setItemAnimator(new DefaultItemAnimator());
        adapter = new MealListAdapter(meals);
        mealList.setAdapter(adapter);

        final FloatingActionButton fab = findViewById(R.id.addFood_Btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddMealDialog();
            }
        });


       BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.tabs_NavigationView);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fab.setVisibility(View.VISIBLE);
                        mealList.setAdapter(adapter);
                        return true;
                    case R.id.navigation_dashboard:
                        fab.setVisibility(View.INVISIBLE);
                        mealList.setAdapter(adapterOne);
                        return true;
                    case R.id.navigation_notifications:
                        return true;
                }
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_classlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.i("MenuItem","Settings");
            return true;
        }
        if (id == R.id.action_addclass) {
            Log.i("MenuItem","Add Class");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void startAddMealDialog() {
        final Dialog mealSetup = new Dialog(this);
        mealSetup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mealSetup.setContentView(R.layout.dialog_addfood);
        mealSetup.show();

        final ExecutorService executorService = Executors.newFixedThreadPool(3);
        futureList = new ArrayList<>();

        foods = new ArrayList<>();
        calories = new ArrayList<>();
        Spinner mealTypes = mealSetup.findViewById(R.id.mealType_Spinner);
        mealTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mealType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mealType = "Breakfast";
            }
        });
        final EditText foodText = mealSetup.findViewById(R.id.selectFood_EditText);
        final EditText caloriesText = mealSetup.findViewById(R.id.calories_editText);
        caloriesText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && foods.isEmpty())
                    Toast.makeText(MainActivity.this,R.string.calories_message,Toast.LENGTH_LONG).show();
            }
        });
        final TextView listFood = mealSetup.findViewById(R.id.listFood_Text);
        final TextView listCalories = mealSetup.findViewById(R.id.listCalories_Text);
        FloatingActionButton addEntry = mealSetup.findViewById(R.id.add_entry_FAB);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(foods.isEmpty()) {
                    ImageCallable imageCallable = new ImageCallable(foodText.getText().toString());
                    futureImage = executorService.submit(imageCallable);

                }
                foods.add(foodText.getText().toString());
                if(caloriesText.getText().toString().equals("")) {
                    calories.add("...");
                    CaloriesCallable caloriesCallable = new CaloriesCallable(foodText.getText().toString());
                    Future<String> future = executorService.submit(caloriesCallable);
                    futureList.add(future);
                } else {
                    calories.add(caloriesText.getText().toString() + " calories");
                }

                String foodList = foods.get(0);
                String caloriesList = calories.get(0);
                for (int i = 1; i < foods.size(); i++) {
                    foodList += "\n" + foods.get(i);
                    caloriesList += "\n" + calories.get(i);
                }

                listFood.setText(foodList);
                listCalories.setText(caloriesList);

                foodText.setText("");
                caloriesText.setText("");
            }
        });

        Button finishBtn = mealSetup.findViewById(R.id.finish_Button);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!foods.isEmpty()) {
                        Bitmap image = futureImage.get();
                            for (int i = 0; i < calories.size(); i++) {
                                if(calories.get(i).equals("...")) {
                                    calories.set(i,futureList.get(i).get());
                                }
                            }
                        meals.add(new Meal(mealType,foods,calories,image));
                        adapter.notifyDataSetChanged();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                mealSetup.dismiss();
            }
        });

    }


}
