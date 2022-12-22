package com.example.expensetracker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ExpenseViewModel extends AndroidViewModel {

    private LiveData<List<ModelDatabase>> mPengeluarans;
    private DatabaseDao databaseDao;
    private LiveData<Integer> mTotalPrice;
    private LiveData<Integer> mTotalSaving;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);

        databaseDao = DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
        mPengeluarans = databaseDao.getAllPengeluaran();
        mTotalPrice = databaseDao.getTotalPengeluaran();
        mTotalSaving = databaseDao.getTotalSaving();
    }

    public LiveData<List<ModelDatabase>> getPengeluaran() {
        return mPengeluarans;
    }

    public LiveData<Integer> getTotalPengeluaran() {
        return mTotalPrice;
    }

    public LiveData<Integer> getTotalSaving() {
        return mTotalSaving;
    }

    public void deleteAllData() {
        Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        databaseDao.deleteAllPengeluaran();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public String deleteSingleData(final int uid) {
        String sKeterangan;
        try {
            Completable.fromAction(() -> databaseDao.deleteSinglePengeluaran(uid))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
            sKeterangan = "OK";
        } catch (Exception e) {
            sKeterangan = "NO";
        }
        return sKeterangan;
    }

}
