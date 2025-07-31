package com.example.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;

public class HomeFragment extends Fragment {
    private Button btnRecord;
    private Button btnHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false); // ✅ 先取得 `View`
        // 初始化 UI 元件
        Button btnRecord = view.findViewById(R.id.btn_record);  // ✅ 使用 `view.findViewById()`
        Button btnHistory = view.findViewById(R.id.btn_view_history);

        // 設定按鈕點擊事件
        btnRecord.setOnClickListener(v -> {
            Fragment recordFragment = new RecordFragment();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, recordFragment)  // ✅ 切換到 RecordFragment
                    .addToBackStack(null)  // ✅ 讓使用者可以按「返回」回到 HomeFragment
                    .commit();
        });


        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), HistoryFragment.class);
            startActivity(intent);
        });

        return view;
    }
}
