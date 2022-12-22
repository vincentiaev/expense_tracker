package com.example.expensetracker;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ExpenseFragment extends Fragment implements ExpenseAdapter.PengeluaranAdapterCallback {

    private ExpenseAdapter expenseAdapter;
    private ExpenseViewModel expenseViewModel;
    private List<ModelDatabase> modelDatabase = new ArrayList<>();
    TextView tv_total, tv_kosong;
    Button btn_hapus;
    FloatingActionButton float_add_btn;
    RecyclerView rv_data;

    public ExpenseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pengeluaran, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_total = view.findViewById(R.id.tv_total);
        tv_kosong = view.findViewById(R.id.tv_kosong);
        btn_hapus = view.findViewById(R.id.btn_delete);
        float_add_btn = view.findViewById(R.id.float_add_btn);
        rv_data = view.findViewById(R.id.rv_data);

        tv_kosong.setVisibility(View.GONE);

        initAdapter();
        observeData();
        initAction();
    }

    private void initAction() {
        float_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExpenseActivity.startActivity(requireActivity(), false, null);
            }
        });

        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Hapus semua pengeluaran?");
                builder.setMessage("Apakah kamu yakin ingin menghapus semua pengeluaran?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        expenseViewModel.deleteAllData();
                        tv_total.setText("0");
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

    private void initAdapter() {
        expenseAdapter = new ExpenseAdapter(requireContext(), modelDatabase, this);
        rv_data.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv_data.setItemAnimator(new DefaultItemAnimator());
        rv_data.setAdapter(expenseAdapter);
    }

    private void observeData() {
        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        expenseViewModel.getPengeluaran().observe(requireActivity(),
                new Observer<List<ModelDatabase>>() {
                    @Override
                    public void onChanged(List<ModelDatabase> pengeluaran) {
                        if (pengeluaran.isEmpty()) {
                            btn_hapus.setVisibility(View.GONE);
                            tv_kosong.setVisibility(View.VISIBLE);
                            rv_data.setVisibility(View.GONE);
                        } else {
                            btn_hapus.setVisibility(View.VISIBLE);
                            tv_kosong.setVisibility(View.GONE);
                            rv_data.setVisibility(View.VISIBLE);
                        }
                        expenseAdapter.addData(pengeluaran);
                    }
                });

        expenseViewModel.getTotalPengeluaran().observe(requireActivity(),
                new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        if (integer == null) {
                            int totalPrice = 0;
                            String initPrice = FunctionHelper.rupiahFormat(totalPrice);
                            tv_total.setText(initPrice);
                        } else {
                            int totalPrice = integer;
                            String initPrice = FunctionHelper.rupiahFormat(totalPrice);
                            tv_total.setText(initPrice);
                        }
                    }
                });
    }

    @Override
    public void onEdit(ModelDatabase modelDatabase) {
        AddExpenseActivity.startActivity(requireActivity(), true, modelDatabase);
    }

    @Override
    public void onDelete(ModelDatabase modelDatabase) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setMessage("Hapus data ini?");
        alertDialogBuilder.setPositiveButton("Ya", (dialogInterface, i) -> {
            int uid = modelDatabase.uid;
            String sKeterangan = expenseViewModel.deleteSingleData(uid);
            if (sKeterangan.equals("OK")) {
                Toast.makeText(requireContext(), "Data yang dipilih sudah dihapus", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogBuilder.setNegativeButton("tidak", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}