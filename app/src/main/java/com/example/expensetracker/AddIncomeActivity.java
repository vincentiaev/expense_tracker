package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class AddIncomeActivity extends AppCompatActivity {

    private static String KEY_IS_EDIT = "key_is_edit";
    private static String KEY_DATA = "key_data";

    public static void startActivity(Context context, boolean isEdit, ModelDatabase pemasukan) {
        Intent intent = new Intent(new Intent(context, AddIncomeActivity.class));
        intent.putExtra(KEY_IS_EDIT, isEdit);
        intent.putExtra(KEY_DATA, pemasukan);
        context.startActivity(intent);
    }

    private AddIncomeViewModel addIncomeViewModel;

    private boolean mIsEdit = false;
    private int strId = 0;

    Toolbar toolbar;
    TextInputEditText etKeterangan, etTanggal, etJmlUang;
    Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        toolbar = findViewById(R.id.toolbar);
        etKeterangan = findViewById(R.id.et_keterangan);
        etTanggal = findViewById(R.id.et_tanggal);
        etJmlUang = findViewById(R.id.et_jmlhUang);
        btnSimpan = findViewById(R.id.btnSimpan);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        addIncomeViewModel = ViewModelProviders.of(this).get(AddIncomeViewModel.class);

        loadData();
        initAction();
    }

    private void loadData() {
        mIsEdit = getIntent().getBooleanExtra(KEY_IS_EDIT, false);
        if (mIsEdit) {
            ModelDatabase pemasukan = getIntent().getParcelableExtra(KEY_DATA);
            if (pemasukan != null) {
                strId = pemasukan.uid;
                String keterangan = pemasukan.keterangan;
                String tanggal = pemasukan.tanggal;
                int uang = pemasukan.jmlUang;

                etKeterangan.setText(keterangan);
                etTanggal.setText(tanggal);
                etJmlUang.setText(String.valueOf(uang));
            }
        }
    }

    private void initAction() {
        etTanggal.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String strFormatDefault = "d MMMM yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormatDefault, Locale.getDefault());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    etTanggal.setText(simpleDateFormat.format(calendar.getTime()));
                }
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddIncomeActivity.this, date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            calendar.set(Calendar.DAY_OF_MONTH, 1);
            long minDate = calendar.getTimeInMillis();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            long maxDate = calendar.getTimeInMillis();
            datePickerDialog.getDatePicker().setMinDate(minDate);
            datePickerDialog.getDatePicker().setMaxDate(maxDate);
            datePickerDialog.show();
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTipe = "pemasukan";
                String strKeterangan = etKeterangan.getText().toString();
                String strTanggal = etTanggal.getText().toString();
                String strJmlUang = etJmlUang.getText().toString();

                if (strKeterangan.isEmpty()) {
                    etKeterangan.setError("Keterangan harus diisi");
                }  else if (strTanggal.isEmpty()){
                    etTanggal.setError("Tanggal harus diisi");
                } else if (strJmlUang.isEmpty()){
                    etJmlUang.setError("Jumlah uang harus diisi");
                } else {
                    if (mIsEdit) {
                        addIncomeViewModel.updatePemasukan(strId, strKeterangan, strTanggal,
                                Integer.parseInt(strJmlUang));
                    } else {
                        addIncomeViewModel.addPemasukan(strTipe, strKeterangan, strTanggal,
                                Integer.parseInt(strJmlUang));
                    }
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(AddIncomeActivity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}