package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static String CATEGORY_ARRAY = "com.example.app.CATEGORY_ARRAY";
    public static String COST_ARRAY = "com.example.app.COST_ARRAY";

    private static final String TAG = "MainActivity";
    private ArrayList<ExampleItem> exampleList;
    private TextView theDate;
    private Button btnGoCalendar;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button buttonSave;
    private EditText editTextDelete;
    private int mImageResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        theDate = (TextView) findViewById(R.id.dateLabel);
        btnGoCalendar = (Button) findViewById(R.id.datePickerBtn);

        loadData();
        buildRecyclerView();
        setInsertButton();

        buttonSave = findViewById(R.id.saveBtn);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });


        Button getChart = (Button) findViewById(R.id.chartBtn);
        getChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPieChart();
            }
        });

        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        theDate.setText(date);

        btnGoCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(exampleList);
        editor.putString("task list", json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<ExampleItem>>() {}.getType();
        exampleList = gson.fromJson(json, type);

        if(exampleList == null){
            exampleList = new ArrayList<>();
        }
    }

    public void openPieChart(){
        String[] category = {"Trans", "Entertain", "Food", "Shop", "Other"};
        int[] cost = {0,0,0,0,0};

        for (int i = 0; i < exampleList.size(); i++){
            if(exampleList.get(i).getCategory().equals("Transportation"))
                cost[0] = cost[0] + Integer.parseInt(exampleList.get(i).getCost());
            else if(exampleList.get(i).getCategory().equals("Entertainment"))
                cost[1] = cost[1] + Integer.parseInt(exampleList.get(i).getCost());
            else if(exampleList.get(i).getCategory().equals("Food"))
                cost[2] = cost[2] + Integer.parseInt(exampleList.get(i).getCost());
            else if(exampleList.get(i).getCategory().equals("Shopping"))
                cost[3] = cost[3] + Integer.parseInt(exampleList.get(i).getCost());
            else
                cost[4] = cost[4] + Integer.parseInt(exampleList.get(i).getCost());
        }


        Intent intent = new Intent(this, PieChart.class);
        intent.putExtra(CATEGORY_ARRAY, category);
        intent.putExtra(COST_ARRAY, cost);
        startActivity(intent);
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                deleteItem(position);
            }
        });
    }

    public void setInsertButton() {
        Button buttonInsert = findViewById(R.id.insertBtn);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText category = findViewById(R.id.insertCategory);
                EditText amount = findViewById(R.id.insertAmount);
                insertItem(category.getText().toString(), amount.getText().toString());
                //System.out.printf("%s%n", exampleList.get(0));
            }
        });
    }
    public void insertItem(String line1, String line2){
        if(line1.toLowerCase().equals("transportation")) {
            mImageResource = R.drawable.ic_directions_bus_black_24dp;
            line1="Transportation";
        }
        else if(line1.toLowerCase().equals("entertainment")) {
            mImageResource = R.drawable.ic_movie_black_24dp;
            line1 = "Entertainment";
        }
        else if(line1.toLowerCase().equals("food")) {
            mImageResource = R.drawable.ic_restaurant_black_24dp;
            line1 = "Food";
        }
        else if(line1.toLowerCase().equals("shopping")) {
            mImageResource = R.drawable.ic_shopping_cart_black_24dp;
            line1 = "Shopping";
        }
        else {
            mImageResource = R.drawable.ic_android_black_24dp;
            line1 = "Other";
        }

        exampleList.add(new ExampleItem(mImageResource, line1, line2));
        for (int i = 0; i < exampleList.size(); i++){
            Log.d(TAG, "onCreate: name: " + exampleList.get(i).getCategory());
        }
        mAdapter.notifyItemInserted(exampleList.size());
    }

    public void deleteItem(int position){
        exampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }
}
