package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.recycler_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<HistoryRecord> rawList = dbHelper.getAllSummaryRecords();
        List<History> historyList = convertToHistoryList(rawList);

        historyAdapter = new HistoryAdapter(historyList, date -> {
            Intent intent = new Intent(requireContext(), EditHistoryActivity.class);
            intent.putExtra("record_date", date);
            startActivity(intent);
        });

        recyclerView.setAdapter(historyAdapter);
        return view;
    }
    private List<History> convertToHistoryList(List<HistoryRecord> recordList) {
        List<History> list = new ArrayList<>();
        for (HistoryRecord record : recordList) {
            List<String> detailStrings = new ArrayList<>();
            detailStrings.add("蛋白質: " + record.proteinSummary);
            detailStrings.add("碳水: " + record.carbSummary);
            detailStrings.add("其他: " + record.otherSummary);
            list.add(new History(record.date, detailStrings, false));
        }
        return list;
    }
}
