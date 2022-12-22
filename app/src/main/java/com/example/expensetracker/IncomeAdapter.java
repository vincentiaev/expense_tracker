package com.example.expensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {

    private Context context;
    private List<ModelDatabase> list;
    private PemasukanAdapterCallback mAdapterCallback;

    public IncomeAdapter(Context context, List<ModelDatabase> list,
                         PemasukanAdapterCallback adapterCallback) {
        this.context = context;
        this.list = list;
        this.mAdapterCallback = adapterCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_data,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelDatabase modelDatabase = list.get(position);
        holder.bindData(modelDatabase);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clear() {
        int size = this.list.size();
        this.list.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addData(List<ModelDatabase> pengeluarans) {
        this.list = pengeluarans;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_harga, tv_note, tv_date;
        public ImageView iv_delete;

        ViewHolder(View itemView) {
            super(itemView);

            tv_harga = itemView.findViewById(R.id.tv_harga);
            tv_note = itemView.findViewById(R.id.tv_note);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_delete = itemView.findViewById(R.id.iv_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelDatabase pemasukan = list.get(getAdapterPosition());
                    mAdapterCallback.onEdit(pemasukan);
                }
            });

            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelDatabase pemasukan = list.get(getAdapterPosition());
                    mAdapterCallback.onDelete(pemasukan);
                }
            });
        }

        void bindData(ModelDatabase item) {
            int price = item.jmlUang;
            String initPrice = FunctionHelper.rupiahFormat(price);
            tv_harga.setText(initPrice);

            String note = item.keterangan;
            tv_note.setText(note);

            String date = item.tanggal;
            tv_date.setText(date);
        }
    }

    public interface PemasukanAdapterCallback {
        void onEdit(ModelDatabase modelDatabase);

        void onDelete(ModelDatabase modelDatabase);
    }

}
