package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private HistoryAdapter adapter;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        btnBack = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);

        List<HistoryRecord> recordList = dbHelper.getAllSummaryRecords();
        List<History> historyList = convertToHistoryList(recordList);

        adapter = new HistoryAdapter(historyList, date -> {
            Intent intent = new Intent(HistoryActivity.this, EditHistoryActivity.class);
            intent.putExtra("record_date", date);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());
    }

    // 將 HistoryRecord 轉為 History（用於可展開 + 顯示）
    private List<History> convertToHistoryList(List<HistoryRecord> recordList) {
        List<History> list = new ArrayList<>();
        for (HistoryRecord record : recordList) {
            List<String> detailStrings = new ArrayList<>();
            detailStrings.add("蛋白質: " + record.proteinSummary);
            detailStrings.add("碳水: " + record.carbSummary);
            detailStrings.add("其他: " + record.otherSummary);
            list.add(new History(record.date, detailStrings,false));
        }
        return list;
    }
}
