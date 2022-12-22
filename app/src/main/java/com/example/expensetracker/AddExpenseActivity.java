package com.example.expensetracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private static String KEY_IS_EDIT = "key_is_edit";
    private static String KEY_DATA = "key_data";

    public static void startActivity(Context context, boolean isEdit, ModelDatabase pengeluaran) {
        Intent intent = new Intent(new Intent(context, AddExpenseActivity.class));
        intent.putExtra(KEY_IS_EDIT, isEdit);
        intent.putExtra(KEY_DATA, pengeluaran);
        context.startActivity(intent);
    }

    private AddExpenseViewModel addExpenseViewModel;

    private boolean mIsEdit = false;
    private int strUid = 0;

    Toolbar toolbar;
    TextInputEditText et_keterangan, et_tanggal, et_jmlhUang;
    Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        toolbar = findViewById(R.id.toolbar);
        et_keterangan = findViewById(R.id.et_keterangan);
        et_tanggal = findViewById(R.id.et_tanggal);
        et_jmlhUang = findViewById(R.id.et_jmlhUang);
        btnSimpan = findViewById(R.id.btnSimpan);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        addExpenseViewModel = ViewModelProviders.of(this).get(AddExpenseViewModel.class);

        loadData();
        initAction();
    }

    private void initAction() {
        et_tanggal.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String strFormatDefault = "d MMMM yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormatDefault, Locale.getDefault());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    et_tanggal.setText(simpleDateFormat.format(calendar.getTime()));
                }
            };


            DatePickerDialog datePickerDialog = new DatePickerDialog(AddExpenseActivity.this, date,
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
                String strTipe = "pengeluaran";
                String strKeterangan = et_keterangan.getText().toString();
                String strTanggal = et_tanggal.getText().toString();
                String strJmlUang = et_jmlhUang.getText().toString();

                if (strKeterangan.isEmpty()) {
                    et_keterangan.setError("Keterangan harus diisi");
                }  else if (strTanggal.isEmpty()){
                    et_tanggal.setError("Tanggal harus diisi");
                } else if (strJmlUang.isEmpty()){
                    et_jmlhUang.setError("Jumlah uang harus diisi");
                }
                else {
                    if (mIsEdit) {
                        addExpenseViewModel.updatePengeluaran(strUid, strKeterangan,
                                strTanggal, Integer.parseInt(strJmlUang));
                    } else {
                        addExpenseViewModel.addPengeluaran(strTipe, strKeterangan,
                                strTanggal, Integer.parseInt(strJmlUang));
                    }
                    finish();
                }
            }
        });
    }

    private void loadData() {
        mIsEdit = getIntent().getBooleanExtra(KEY_IS_EDIT, false);
        if (mIsEdit) {
            ModelDatabase pengeluaran = getIntent().getParcelableExtra(KEY_DATA);
            if (pengeluaran != null) {
                strUid = pengeluaran.uid;
                String keterangan = pengeluaran.keterangan;
                String tanggal = pengeluaran.tanggal;
                int uang = pengeluaran.jmlUang;

                et_keterangan.setText(keterangan);
                et_tanggal.setText(tanggal);
                et_jmlhUang.setText(String.valueOf(uang));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(AddExpenseActivity.this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
