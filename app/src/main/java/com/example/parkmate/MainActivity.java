package com.example.parkmate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvRecordCount;
    private TextView tvCollectedTotal;
    private TextView tvEmptyState;
    private LinearLayout recordsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (view, windowInsets) -> {
                    Insets padding = windowInsets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    view.setPadding(
                            padding.left,
                            padding.top,
                            padding.right,
                            padding.bottom
                    );

                    return windowInsets;
                }
        );

        WindowCompat.getInsetsController(
                getWindow(), getWindow().getDecorView()
        ).setAppearanceLightStatusBars(true);

        WindowCompat.getInsetsController(
                getWindow(), getWindow().getDecorView()
        ).setAppearanceLightNavigationBars(true);

        tvRecordCount = findViewById(R.id.tvRecordCount);
        tvCollectedTotal = findViewById(R.id.tvCollectedTotal);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        recordsContainer = findViewById(R.id.recordsContainer);

        Button btnAddRecord = findViewById(R.id.btnAddRecord);

        btnAddRecord.setOnClickListener(view -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    ParkingFormActivity.class
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshRecords();
    }

    private void refreshRecords() {
        tvRecordCount.setText(
                String.valueOf(ParkingStore.getCount())
        );

        tvCollectedTotal.setText(
                getString(
                        R.string.money_amount,
                        ParkingStore.getCollectedTotal()
                )
        );

        recordsContainer.removeAllViews();

        if (ParkingStore.getCount() == 0) {
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
        }

        for (ParkingRecord record : ParkingStore.getAll()) {
            View row = getLayoutInflater().inflate(
                    R.layout.item_parking_record,
                    recordsContainer,
                    false
            );

            TextView tvVehicleNumber =
                    row.findViewById(R.id.tvRowVehicleNumber);

            TextView tvStatus =
                    row.findViewById(R.id.tvRowStatus);

            TextView tvSummary =
                    row.findViewById(R.id.tvRowSummary);

            TextView tvFee =
                    row.findViewById(R.id.tvRowFee);

            Button btnViewDetails =
                    row.findViewById(R.id.btnViewDetails);

            tvVehicleNumber.setText(record.vehicleNumber);

            tvStatus.setText(
                    record.paid
                            ? R.string.status_paid
                            : R.string.status_unpaid
            );

            tvStatus.setTextColor(Color.parseColor(
                    record.paid ? "#167438" : "#8A5A00"
            ));

            tvStatus.setBackgroundColor(Color.parseColor(
                    record.paid ? "#E2F6E9" : "#FFF0C2"
            ));

            String duration = getResources().getQuantityString(
                    R.plurals.parking_hours,
                    record.hours,
                    record.hours
            );

            tvSummary.setText(
                    getString(
                            R.string.record_summary,
                            record.vehicleType,
                            duration
                    )
            );

            tvFee.setText(
                    getString(
                            R.string.money_amount,
                            record.getTotalFee()
                    )
            );

            btnViewDetails.setOnClickListener(view -> {
                Intent intent = new Intent(
                        MainActivity.this,
                        ParkingDetailsActivity.class
                );

                intent.putExtra(
                        ParkingDetailsActivity.EXTRA_RECORD_ID,
                        record.id
                );

                startActivity(intent);
            });

            recordsContainer.addView(row);
        }
    }
}