package com.example.expensetracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class IncomeFragment extends Fragment implements IncomeAdapter.PemasukanAdapterCallback {

    private IncomeAdapter incomeAdapter;
    private IncomeViewModel incomeViewModel;
    private List<ModelDatabase> modelDatabaseList = new ArrayList<>();
    TextView tvTotal, tvNotFound;
    Button btnHapus;
    FloatingActionButton fabAdd;
    RecyclerView rvListData;

    public IncomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pemasukan, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTotal = view.findViewById(R.id.tv_total);
        tvNotFound = view.findViewById(R.id.tv_kosong);
        btnHapus = view.findViewById(R.id.btn_delete);
        fabAdd = view.findViewById(R.id.float_add_btn);
        rvListData = view.findViewById(R.id.rv_data);

        tvNotFound.setVisibility(View.GONE);

        initAdapter();
        observeData();
        initAction();
    }

    private void initAdapter() {
        incomeAdapter = new IncomeAdapter(requireContext(), modelDatabaseList, this);
        rvListData.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvListData.setItemAnimator(new DefaultItemAnimator());
        rvListData.setAdapter(incomeAdapter);
    }

    private void observeData() {
        incomeViewModel = ViewModelProviders.of(this).get(IncomeViewModel.class);
        incomeViewModel.getPemasukan().observe(requireActivity(),
                new Observer<List<ModelDatabase>>() {
                    @Override
                    public void onChanged(List<ModelDatabase> pemasukan) {
                        if (pemasukan.isEmpty()) {
                            btnHapus.setVisibility(View.GONE);
                            tvNotFound.setVisibility(View.VISIBLE);
                            rvListData.setVisibility(View.GONE);
                        } else {
                            btnHapus.setVisibility(View.VISIBLE);
                            tvNotFound.setVisibility(View.GONE);
                            rvListData.setVisibility(View.VISIBLE);
                        }
                        incomeAdapter.addData(pemasukan);
                    }
                });

        incomeViewModel.getTotalPemasukan().observe(requireActivity(),
                new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        if (integer == null) {
                            int totalPrice = 0;
                            String initPrice = FunctionHelper.rupiahFormat(totalPrice);
                            tvTotal.setText(initPrice);
                        } else {
                            int totalPrice = integer;
                            String initPrice = FunctionHelper.rupiahFormat(totalPrice);
                            tvTotal.setText(initPrice);
                        }
                    }
                });
    }

    private void initAction() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddIncomeActivity.startActivity(requireActivity(), false, null);
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                builder.setTitle("Hapus semua pemasukan?");
                builder.setMessage("Apakah kamu yakin ingin menghapus semua pemasukan?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        incomeViewModel.deleteAllData();
                        tvTotal.setText("0");
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();

            }
        });
    }

    @Override
    public void onEdit(ModelDatabase modelDatabase) {
        AddIncomeActivity.startActivity(requireActivity(), true, modelDatabase);
    }

    @Override
    public void onDelete(ModelDatabase modelDatabase) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setMessage("Hapus data ini?");
        alertDialogBuilder.setPositiveButton("Ya", (dialogInterface, i) -> {
            int uid = modelDatabase.uid;
            String sKeterangan = incomeViewModel.deleteSingleData(uid);
            if (sKeterangan.equals("OK")) {
                Toast.makeText(requireContext(), "Data yang dipilih sudah dihapus",
                        Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogBuilder.setNegativeButton("Tidak", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}