package com.example.parkmate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class ParkingDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_RECORD_ID = "record_id";

    private int recordId;

    private TextView tvVehicleNumber;
    private TextView tvStatus;
    private TextView tvRecordId;
    private TextView tvVehicleType;
    private TextView tvHours;
    private TextView tvRate;
    private TextView tvFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parking_details);

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

        tvVehicleNumber = findViewById(R.id.tvDetailVehicleNumber);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tvRecordId = findViewById(R.id.tvDetailRecordId);
        tvVehicleType = findViewById(R.id.tvDetailVehicleType);
        tvHours = findViewById(R.id.tvDetailHours);
        tvRate = findViewById(R.id.tvDetailRate);
        tvFee = findViewById(R.id.tvDetailFee);

        Button btnEditRecord = findViewById(R.id.btnEditRecord);
        Button btnDeleteRecord = findViewById(R.id.btnDeleteRecord);
        Button btnBackRecords = findViewById(R.id.btnBackRecords);

        recordId = getIntent().getIntExtra(EXTRA_RECORD_ID, -1);

        btnEditRecord.setOnClickListener(view -> {
            if (ParkingStore.findById(recordId) == null) {
                showMissingRecord();
                return;
            }

            Intent intent = new Intent(
                    ParkingDetailsActivity.this,
                    ParkingFormActivity.class
            );

            intent.putExtra(
                    ParkingFormActivity.EXTRA_RECORD_ID,
                    recordId
            );

            startActivity(intent);
        });

        btnDeleteRecord.setOnClickListener(
                view -> confirmDelete()
        );

        btnBackRecords.setOnClickListener(
                view -> finish()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayRecord();
    }

    private void displayRecord() {
        ParkingRecord record = ParkingStore.findById(recordId);

        if (record == null) {
            showMissingRecord();
            return;
        }

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

        tvRecordId.setText(
                getString(R.string.details_record_id, record.id)
        );

        tvVehicleType.setText(
                getString(
                        R.string.details_vehicle_type,
                        record.vehicleType
                )
        );

        String duration = getResources().getQuantityString(
                R.plurals.parking_hours,
                record.hours,
                record.hours
        );

        tvHours.setText(
                getString(R.string.details_duration, duration)
        );

        String hourlyRate = getString(
                R.string.money_amount,
                record.getHourlyRate()
        );

        tvRate.setText(
                getString(R.string.details_hourly_rate, hourlyRate)
        );

        tvFee.setText(
                getString(
                        R.string.money_amount,
                        record.getTotalFee()
                )
        );
    }

    private void confirmDelete() {
        ParkingRecord record = ParkingStore.findById(recordId);

        if (record == null) {
            showMissingRecord();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage(
                        getString(
                                R.string.delete_message,
                                record.vehicleNumber
                        )
                )
                .setNegativeButton(R.string.form_cancel, null)
                .setPositiveButton(
                        R.string.delete_confirm,
                        (dialog, which) -> {
                            boolean deleted =
                                    ParkingStore.delete(recordId);

                            Toast.makeText(
                                    this,
                                    deleted
                                            ? R.string.delete_success
                                            : R.string.form_record_missing,
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();
                        }
                )
                .show();
    }

    private void showMissingRecord() {
        Toast.makeText(
                this,
                R.string.form_record_missing,
                Toast.LENGTH_LONG
        ).show();

        finish();
    }
}