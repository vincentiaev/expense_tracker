package com.example.expensetracker;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddExpenseViewModel extends AndroidViewModel {

    private DatabaseDao databaseDao;

    public AddExpenseViewModel(@NonNull Application application) {
        super(application);

        databaseDao = DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
    }

    public void addPengeluaran(final String type, final String note, final String date, final int price) {
        Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        ModelDatabase pengeluaran = new ModelDatabase();
                        pengeluaran.tipe = type;
                        pengeluaran.keterangan = note;
                        pengeluaran.tanggal = date;
                        pengeluaran.jmlUang = price;
                        databaseDao.insertPengeluaran(pengeluaran);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void updatePengeluaran(final int uid, final String note, final String date, final int price) {
        Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        databaseDao.updateDataPengeluaran(note, date, price, uid);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

}
