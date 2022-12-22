package com.example.expensetracker;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class IncomeViewModel extends AndroidViewModel {

    private LiveData<List<ModelDatabase>> mPemasukan;
    private DatabaseDao databaseDao;
    private LiveData<Integer> mTotalPrice;
    private LiveData<Integer> mTotalSaving;

    public IncomeViewModel(@NonNull Application application) {
        super(application);

        databaseDao = DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
        mPemasukan = databaseDao.getAllPemasukan();
        mTotalPrice = databaseDao.getTotalPemasukan();
        mTotalSaving = databaseDao.getTotalSaving();
    }

    public LiveData<List<ModelDatabase>> getPemasukan() {
        return mPemasukan;
    }

    public LiveData<Integer> getTotalPemasukan() {
        return mTotalPrice;
    }

    public LiveData<Integer> getTotalSaving() {
        return mTotalSaving;
    }

    public void deleteAllData() {
        Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        databaseDao.deleteAllPemasukan();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public String deleteSingleData(final int uid) {
        String sKeterangan;
        try {
            Completable.fromAction(() -> databaseDao.deleteSinglePemasukan(uid))
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