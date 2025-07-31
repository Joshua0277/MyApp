package com.example.myapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.*;

public class RecordFragment extends Fragment {

    private Map<String, LinearLayout> containers = new HashMap<>();
    private Map<String, ArrayList<FoodInput>> foodInputs = new HashMap<>();
    private Button btnSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        containers.put("Protein", view.findViewById(R.id.Pcontainer));
        containers.put("Carb", view.findViewById(R.id.Ccontainer));
        containers.put("Other", view.findViewById(R.id.Ocontainer));

        foodInputs.put("Protein", new ArrayList<>());
        foodInputs.put("Carb", new ArrayList<>());
        foodInputs.put("Other", new ArrayList<>());

        addNewFoodRow("Protein", R.layout.protein_input);
        addNewFoodRow("Carb", R.layout.carb_input);
        addNewFoodRow("Other", R.layout.other_input);

        view.findViewById(R.id.btn_add_protein).setOnClickListener(v -> addNewFoodRow("Protein", R.layout.protein_input));
        view.findViewById(R.id.btn_add_carb).setOnClickListener(v -> addNewFoodRow("Carb", R.layout.carb_input));
        view.findViewById(R.id.btn_add_other).setOnClickListener(v -> addNewFoodRow("Other", R.layout.other_input));

        btnSubmit = view.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> {
            String summary = generateSummaryText();
            showBottomSheetDialog(summary);
        });

        return view;
    }

    private void addNewFoodRow(String type, int layoutId) {
        LinearLayout container = containers.get(type);
        ArrayList<FoodInput> list = foodInputs.get(type);

        View foodRow = LayoutInflater.from(requireContext()).inflate(layoutId, container, false);
        EditText nameInput = foodRow.findViewById(R.id.editTextP);
        EditText amountInput = foodRow.findViewById(R.id.editTextPP);
        Spinner unitSpinner = foodRow.findViewById(R.id.spinner_P);

        list.add(new FoodInput(nameInput, amountInput, unitSpinner));
        container.addView(foodRow);
    }

    private String generateSummaryText() {
        StringBuilder data = new StringBuilder();

        for (String type : foodInputs.keySet()) {
            data.append(type).append(":\n");
            for (FoodInput input : foodInputs.get(type)) {
                data.append("- Name: ").append(input.getName())
                        .append(", Amount: ").append(input.getAmount())
                        .append(", Unit: ").append(input.getUnit()).append("\n");
            }
        }

        return data.toString();
    }

    private void showBottomSheetDialog(String userInputContent) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_layout, null);

        TextView tvUserInput = bottomSheetView.findViewById(R.id.tv_user_input);
        Button btnModify = bottomSheetView.findViewById(R.id.btn_modify);
        Button btnConfirm = bottomSheetView.findViewById(R.id.btn_confirm);

        tvUserInput.setText(userInputContent);

        btnModify.setOnClickListener(v -> bottomSheetDialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            saveDataToDatabase();
            Toast.makeText(requireContext(), "數據已儲存！", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void saveDataToDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        
        // 建立摘要字串
        Map<String, StringBuilder> summaryMap = new HashMap<>();
        summaryMap.put("Protein", new StringBuilder());
        summaryMap.put("Carb", new StringBuilder());
        summaryMap.put("Other", new StringBuilder());

        for (String type : foodInputs.keySet()) {
            for (FoodInput input : foodInputs.get(type)) {
                if (!input.getName().isEmpty() && !input.getAmount().isEmpty()) {
                    dbHelper.insertDetailedRecord(date, type, input.getName(), input.getAmount(), input.getUnit());
                    // 建立摘要
                    summaryMap.get(type).append(input.getName()).append(" ")
                            .append(input.getAmount()).append(input.getUnit()).append(", ");
                }
            }
        }
        
        // 儲存摘要
        dbHelper.insertSummaryRecord(
                date,
                trimSummary(summaryMap.get("Protein")),
                trimSummary(summaryMap.get("Carb")),
                trimSummary(summaryMap.get("Other"))
        );

        Toast.makeText(requireContext(), "資料已儲存至資料庫", Toast.LENGTH_SHORT).show();
    }
    
    private String trimSummary(StringBuilder sb) {
        if (sb.length() > 2) {
            return sb.substring(0, sb.length() - 2);
        }
        return sb.toString();
    }

    // FoodInput 類別（可以放在這或獨立成檔案）
    public static class FoodInput {
        private final EditText nameInput;
        private final EditText amountInput;
        private final Spinner unitSpinner;

        public FoodInput(EditText nameInput, EditText amountInput, Spinner unitSpinner) {
            this.nameInput = nameInput;
            this.amountInput = amountInput;
            this.unitSpinner = unitSpinner;
        }

        public String getName() {
            return nameInput.getText().toString().trim();
        }

        public String getAmount() {
            return amountInput.getText().toString().trim();
        }

        public String getUnit() {
            return unitSpinner.getSelectedItem().toString();
        }
    }
}
