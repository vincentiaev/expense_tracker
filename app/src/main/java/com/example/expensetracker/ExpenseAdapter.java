package com.example.expensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private Context context;
    private List<ModelDatabase> list;
    private PengeluaranAdapterCallback mAdapterCallback;

    public ExpenseAdapter(Context context, List<ModelDatabase> list, PengeluaranAdapterCallback adapterCallback) {
        this.context = context;
        this.list = list;
        this.mAdapterCallback = adapterCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_data, parent, false);
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

        public TextView tvPrice, tvNote, tvDate;
        public ImageView ivDelete;

        ViewHolder(View itemView) {
            super(itemView);

            tvPrice = itemView.findViewById(R.id.tv_harga);
            tvNote = itemView.findViewById(R.id.tv_note);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivDelete = itemView.findViewById(R.id.iv_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelDatabase pengeluaran = list.get(getAdapterPosition());
                    mAdapterCallback.onEdit(pengeluaran);
                }
            });

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelDatabase pengeluaran = list.get(getAdapterPosition());
                    mAdapterCallback.onDelete(pengeluaran);
                }
            });
        }

        void bindData(ModelDatabase item) {
            int price = item.jmlUang;
            String initPrice = FunctionHelper.rupiahFormat(price);
            tvPrice.setText(initPrice);

            String note = item.keterangan;
            tvNote.setText(note);

            String date = item.tanggal;
            tvDate.setText(date);
        }
    }

    public interface PengeluaranAdapterCallback {
        void onEdit(ModelDatabase modelDatabase);

        void onDelete(ModelDatabase modelDatabase);
    }

}