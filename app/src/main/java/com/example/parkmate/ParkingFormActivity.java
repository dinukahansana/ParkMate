package com.example.parkmate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class ParkingFormActivity extends AppCompatActivity {

    public static final String EXTRA_RECORD_ID = "record_id";

    private EditText etVehicleNumber;
    private RadioGroup rgVehicleType;
    private SeekBar seekHours;
    private CheckBox cbPaid;
    private TextView tvFormTitle;
    private TextView tvHours;
    private TextView tvRateDetails;
    private TextView tvTotalFee;
    private Button btnSaveRecord;

    private int editingRecordId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parking_form);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (view, windowInsets) -> {
                    Insets padding = windowInsets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                                    | WindowInsetsCompat.Type.ime()
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

        etVehicleNumber = findViewById(R.id.etVehicleNumber);
        rgVehicleType = findViewById(R.id.rgVehicleType);
        seekHours = findViewById(R.id.seekHours);
        cbPaid = findViewById(R.id.cbPaid);
        tvFormTitle = findViewById(R.id.tvFormTitle);
        tvHours = findViewById(R.id.tvHours);
        tvRateDetails = findViewById(R.id.tvRateDetails);
        tvTotalFee = findViewById(R.id.tvTotalFee);
        btnSaveRecord = findViewById(R.id.btnSaveRecord);

        Button btnCancel = findViewById(R.id.btnCancel);

        editingRecordId = getIntent().getIntExtra(EXTRA_RECORD_ID, -1);

        if (editingRecordId != -1) {
            ParkingRecord record = ParkingStore.findById(editingRecordId);

            if (record == null) {
                Toast.makeText(
                        this,
                        R.string.form_record_missing,
                        Toast.LENGTH_LONG
                ).show();

                finish();
                return;
            }

            tvFormTitle.setText(R.string.form_edit_title);
            btnSaveRecord.setText(R.string.form_save_changes);

            if (savedInstanceState == null) {
                loadRecord(record);
            }
        }

        rgVehicleType.setOnCheckedChangeListener(
                (group, checkedId) -> updateFeePreview()
        );

        seekHours.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar, int progress, boolean fromUser) {
                        updateFeePreview();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        btnSaveRecord.setOnClickListener(view -> saveRecord());
        btnCancel.setOnClickListener(view -> finish());

        updateFeePreview();
    }

    private void loadRecord(ParkingRecord record) {
        etVehicleNumber.setText(record.vehicleNumber);
        seekHours.setProgress(record.hours - 1);
        cbPaid.setChecked(record.paid);

        if ("Motorcycle".equals(record.vehicleType)) {
            rgVehicleType.check(R.id.rbMotorcycle);
        } else if ("Car".equals(record.vehicleType)) {
            rgVehicleType.check(R.id.rbCar);
        } else if ("Van".equals(record.vehicleType)) {
            rgVehicleType.check(R.id.rbVan);
        }
    }

    private String getSelectedVehicleType() {
        int selectedId = rgVehicleType.getCheckedRadioButtonId();

        if (selectedId == R.id.rbMotorcycle) {
            return "Motorcycle";
        } else if (selectedId == R.id.rbCar) {
            return "Car";
        } else if (selectedId == R.id.rbVan) {
            return "Van";
        }

        return null;
    }

    private int getParkingHours() {
        return seekHours.getProgress() + 1;
    }

    private void updateFeePreview() {
        int hours = getParkingHours();

        String durationText = getResources().getQuantityString(
                R.plurals.parking_hours, hours, hours
        );

        tvHours.setText(durationText);

        String vehicleType = getSelectedVehicleType();

        if (vehicleType == null) {
            tvRateDetails.setText(R.string.form_select_type);
            tvTotalFee.setText(R.string.form_fee_placeholder);
            return;
        }

        int rate = ParkingRecord.getRateForType(vehicleType);
        int totalFee = rate * hours;

        tvRateDetails.setText(
                getString(R.string.form_rate_details, rate, durationText)
        );

        tvTotalFee.setText(
                getString(R.string.money_amount, totalFee)
        );
    }

    private void saveRecord() {
        String vehicleNumber = etVehicleNumber.getText()
                .toString()
                .trim()
                .toUpperCase(Locale.ROOT);

        if (vehicleNumber.isEmpty()) {
            etVehicleNumber.setError(
                    getString(R.string.form_number_required)
            );

            etVehicleNumber.requestFocus();
            return;
        }

        String vehicleType = getSelectedVehicleType();

        if (vehicleType == null) {
            Toast.makeText(
                    this,
                    R.string.form_select_type,
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int hours = getParkingHours();
        boolean paid = cbPaid.isChecked();

        btnSaveRecord.setEnabled(false);

        int messageId;

        if (editingRecordId == -1) {
            ParkingStore.add(vehicleNumber, vehicleType, hours, paid);
            messageId = R.string.form_saved_count;
        } else {
            boolean updated = ParkingStore.update(
                    editingRecordId,
                    vehicleNumber,
                    vehicleType,
                    hours,
                    paid
            );

            if (!updated) {
                Toast.makeText(
                        this,
                        R.string.form_record_missing,
                        Toast.LENGTH_LONG
                ).show();

                finish();
                return;
            }

            messageId = R.string.form_updated_count;
        }

        Toast.makeText(
                this,
                getString(messageId, ParkingStore.getCount()),
                Toast.LENGTH_LONG
        ).show();

        finish();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        updateFeePreview();
    }
}