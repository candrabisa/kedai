package com.candra.kedai.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.candra.kedai.R;
import com.candra.kedai.activity.OrderDetails;
import com.candra.kedai.model.ListOrderModel;

import java.util.List;

public class ListOrderAdapter extends RecyclerView.Adapter<ListOrderAdapter.MyHolder> {

    Context context;
    List<ListOrderModel> listOrder;

    public ListOrderAdapter(Context context, List<ListOrderModel> listOrder) {
        this.context = context;
        this.listOrder = listOrder;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context)
        .inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String invoice = listOrder.get(position).getInvoice();
        final String tgl_transaksi = listOrder.get(position).getTgl_transaksi();
        final String nama_produk = listOrder.get(position).getNama_produk();
        final String foto_produk = listOrder.get(position).getUrl_images_produk();
        final Integer qty_pesanan = listOrder.get(position).getQty();
        final Integer harga_produk = listOrder.get(position).getHarga_produk();
        final Integer total_pembayaran = listOrder.get(position).getTotal_pembayaran();
        final String status_pesanan = listOrder.get(position).getStatus_pesanan();

        holder.tv_invoice.setText(invoice);
        holder.tv_tglTransaksi.setText(tgl_transaksi);
        holder.tv_namaProduk.setText(nama_produk);
        holder.tv_qtyPesanan.setText(qty_pesanan + "x");
        holder.tv_hargaProduk.setText("Rp. " + harga_produk);
        holder.tv_totalPembayaran.setText("Rp. " + total_pembayaran);
        holder.tv_statusPesanan.setText(status_pesanan.toUpperCase());

        Glide.with(context).load(foto_produk).centerCrop().fitCenter()
                .into(holder.iv_fotoProduk);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetails.class);
                intent.putExtra("invoice", invoice);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        ImageView iv_fotoProduk;
        TextView tv_tglTransaksi, tv_invoice, tv_namaProduk, tv_hargaProduk, tv_qtyPesanan
                , tv_totalPembayaran, tv_statusPesanan;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            iv_fotoProduk = itemView.findViewById(R.id.iv_produkListOrder);
            tv_tglTransaksi = itemView.findViewById(R.id.tv_tglPembelian);
            tv_invoice = itemView.findViewById(R.id.tv_invoicePembelian);
            tv_namaProduk = itemView.findViewById(R.id.tv_namaProdukListOrder);
            tv_hargaProduk = itemView.findViewById(R.id.tv_hargaProdukListOrder);
            tv_qtyPesanan = itemView.findViewById(R.id.tv_qtyPembelian);
            tv_totalPembayaran = itemView.findViewById(R.id.tv_totalPembayaranListOrder);
            tv_statusPesanan = itemView.findViewById(R.id.tv_statusPesananListOrder);
        }
    }
}
