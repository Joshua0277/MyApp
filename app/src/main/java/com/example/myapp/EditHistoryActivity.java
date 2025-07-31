package com.example.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditHistoryActivity extends AppCompatActivity {

    private LinearLayout container;
    private Button btnSave;
    private String recordDate;
    private List<DetailRecord> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_history);

        recordDate = getIntent().getStringExtra("record_date");
        container = findViewById(R.id.container_inputs);
        btnSave = findViewById(R.id.btn_save);

        loadDetailRecords(recordDate);

        btnSave.setOnClickListener(v -> saveUpdates());
    }

    private void loadDetailRecords(String date) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        recordList = dbHelper.getDetailsByDate(date);

        for (DetailRecord detail : recordList) {
            View row = LayoutInflater.from(this).inflate(R.layout.edit_row, container, false);

            EditText name = row.findViewById(R.id.edit_name);
            EditText amount = row.findViewById(R.id.edit_amount);
            Spinner unit = row.findViewById(R.id.spinner_unit);

            name.setText(detail.name);
            amount.setText(detail.amount);
            // spinner 預設選擇
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.unit_options, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            unit.setAdapter(adapter);

            int pos = adapter.getPosition(detail.unit);
            if (pos >= 0) unit.setSelection(pos);

            container.addView(row);
        }
    }

    private void saveUpdates() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.deleteRecordByDate(recordDate); // 先刪除原資料

        int count = container.getChildCount();
        Map<String, StringBuilder> summary = new HashMap<>();
        summary.put("Protein", new StringBuilder());
        summary.put("Carb", new StringBuilder());
        summary.put("Other", new StringBuilder());

        for (int i = 0; i < count; i++) {
            View row = container.getChildAt(i);
            EditText name = row.findViewById(R.id.edit_name);
            EditText amount = row.findViewById(R.id.edit_amount);
            Spinner unit = row.findViewById(R.id.spinner_unit);

            String inputName = name.getText().toString().trim();
            String inputAmount = amount.getText().toString().trim();
            String inputUnit = unit.getSelectedItem().toString();
            String category = getCategoryByIndex(i); // 你可以用原本的 category

            if (!inputName.isEmpty() && !inputAmount.isEmpty()) {
                dbHelper.insertDetailedRecord(recordDate, category, inputName, inputAmount, inputUnit);
                summary.get(category).append(inputName).append(" ")
                        .append(inputAmount).append(inputUnit).append(", ");
            }
        }

        dbHelper.insertSummaryRecord(
                recordDate,
                trim(summary.get("Protein")),
                trim(summary.get("Carb")),
                trim(summary.get("Other"))
        );

        Toast.makeText(this, "紀錄已更新", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getCategoryByIndex(int index) {
        // 可根據順序分類或記錄類別 (建議改進)
        if (index < 3) return "Protein";
        else if (index < 6) return "Carb";
        else return "Other";
    }

    private String trim(StringBuilder sb) {
        return sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "";
    }
}
