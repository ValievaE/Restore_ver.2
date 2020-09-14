package com.example.restore_ver2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    private List<Map<String,String>> content;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SimpleAdapter adapter;
    private String[] arrayContent;

    private static final String KEY = "key";
    private ArrayList<Integer> removedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY)){
            removedItems.addAll(Objects.requireNonNull(savedInstanceState.getIntegerArrayList(KEY)));
        }

        
        deleteRemoveItems(fillContent());

        String[] from = new String[]{"title", "subtitle"};
        int[] to = new int[]{R.id.textViewTitle, R.id.textViewSubtitle};
        adapter = new SimpleAdapter(this, content, R.layout.list_item, from, to);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                removedItems.add(position);
                content.remove(position);
                adapter.notifyDataSetChanged();
            }
        });


        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                content.clear();
                content.addAll(fillContent());
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putIntegerArrayList(KEY, removedItems);
        super.onSaveInstanceState(outState);
    }

    private List<Map<String,String>> fillContent() {
        arrayContent = getString(R.string.large_text).split("\\n\\n");
        content = new ArrayList<>();
        Map<String, String> map;
        for (int i = 0; i < arrayContent.length; i++) {
            map = new HashMap<>();
            map.put("title", arrayContent[i]);
            map.put("subtitle", String.valueOf(arrayContent[i].length()));
            content.add(map);
        }
        return content;
    }

    private List<Map<String,String>> deleteRemoveItems(List<Map<String,String>> content){
        content = fillContent();
        for (int i: removedItems){
            content.remove(i);
        }
        return content;
    }

}