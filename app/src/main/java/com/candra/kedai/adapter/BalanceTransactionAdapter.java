package com.candra.kedai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.candra.kedai.R;
import com.candra.kedai.model.BalanceTransactionModel;

import java.util.List;

public class BalanceTransactionAdapter extends RecyclerView.Adapter<BalanceTransactionAdapter.MyHolder> {

    Context context;
    List<BalanceTransactionModel> listTransaksi;

    public BalanceTransactionAdapter(Context context, List<BalanceTransactionModel> listTransaksi) {
        this.context = context;
        this.listTransaksi = listTransaksi;
    }

    @NonNull
    @Override
    public BalanceTransactionAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_transaction_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BalanceTransactionAdapter.MyHolder holder, int position) {
        final String invoice = listTransaksi.get(position).getNomor_transaksi();
        final String tgl_transaksi = listTransaksi.get(position).getWaktu_transaksi();
        final String status_transaksi = listTransaksi.get(position).getStatus_transaksi();
        final int nominal_transaksi = listTransaksi.get(position).getNominal_transaksi();

        holder.tv_invoiceTOPUP.setText(invoice);
        holder.tv_tglTopup.setText(tgl_transaksi);
        holder.status_TransaksiTopup.setText(status_transaksi);
        holder.tv_nominalTransaksi.setText("Rp. "+nominal_transaksi);
    }

    @Override
    public int getItemCount() {
        return listTransaksi.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_invoiceTOPUP, tv_tglTopup, status_TransaksiTopup, tv_nominalTransaksi;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            tv_invoiceTOPUP = itemView.findViewById(R.id.tv_invoiceTOPUP);
            tv_tglTopup = itemView.findViewById(R.id.tv_tglTopup);
            status_TransaksiTopup = itemView.findViewById(R.id.status_TransaksiTopup);
            tv_nominalTransaksi = itemView.findViewById(R.id.tv_nominalTransaksi);

        }
    }
}
