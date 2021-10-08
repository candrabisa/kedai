package com.candra.kedai.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.candra.kedai.R;
import com.candra.kedai.activity.ProductDetails;
import com.candra.kedai.model.FavoriteModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyHolder> {

    Context context;
    List<FavoriteModel> listFavorit;
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

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

        holder.btn_deleteFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Perhatian!");
                builder.setMessage("Apakah kamu yakin ingin menghapus "+nama_produk.toUpperCase() + " dari daftar favorit?" );
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference dRef= FirebaseDatabase.getInstance().getReference("Wishlist")
                                .child(fUser.getUid()).child(id_produk);
                        dRef.removeValue();
                        Snackbar.make(v, nama_produk.toUpperCase() + " berhasil dihapus dari favorit", BaseTransientBottomBar.LENGTH_SHORT)
                                .setAction("Oke", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).show();
                    }
                });
                builder.setNegativeButton("batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFavorit.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView iv_fotoProduk;
        TextView tv_namaProduk, tv_hargaProduk;
        ImageButton btn_deleteFav;
        SwipeRefreshLayout sw_favorit;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            iv_fotoProduk = itemView.findViewById(R.id.iv_fotoProdukFav);
            tv_namaProduk = itemView.findViewById(R.id.tv_namaProdukFav);
            tv_hargaProduk = itemView.findViewById(R.id.tv_hargaProdukFav);
            btn_deleteFav = itemView.findViewById(R.id.btn_deleteFav);
            sw_favorit = itemView.findViewById(R.id.sw_favorit);

        }
    }
}
