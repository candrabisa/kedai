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
import com.candra.kedai.activity.ProductDetails;
import com.candra.kedai.model.FavoriteModel;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyHolder> {

    Context context;
    List<FavoriteModel> listFavorit;

    public FavoriteAdapter(Context context, List<FavoriteModel> listFavorit) {
        this.context = context;
        this.listFavorit = listFavorit;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context)
        .inflate(R.layout.item_favorite, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String id_produk = listFavorit.get(position).getId_produk();
        final String nama_produk = listFavorit.get(position).getNama_produk();
        final int harga_produk = listFavorit.get(position).getHarga();
        final String foto_produk = listFavorit.get(position).getUrl_images_produk();
        final String kategori = listFavorit.get(position).getKategori();
        final String detail_kategori = listFavorit.get(position).getDetail_kategori();

        holder.tv_namaProduk.setText(nama_produk);
        holder.tv_hargaProduk.setText(String.valueOf(harga_produk));
        Glide.with(context).load(foto_produk).centerCrop().fitCenter().into(holder.iv_fotoProduk);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("id_produk", id_produk);
                intent.putExtra("kategori", kategori);
                intent.putExtra("detail_kategori", detail_kategori);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listFavorit.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView iv_fotoProduk;
        TextView tv_namaProduk;
        TextView tv_hargaProduk;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            iv_fotoProduk = itemView.findViewById(R.id.iv_fotoProdukFav);
            tv_namaProduk = itemView.findViewById(R.id.tv_namaProdukFav);
            tv_hargaProduk = itemView.findViewById(R.id.tv_hargaProdukFav);

        }
    }
}
