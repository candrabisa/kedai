package com.candra.kedai.adapter;

import static android.icu.text.DateFormat.LONG;
import static android.icu.text.DateFormat.getDateTimeInstance;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.ULocale;
import android.os.Build;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.candra.kedai.R;
import com.candra.kedai.activity.ProductDetails;
import com.candra.kedai.model.CategoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyHolder> {

    Context context;
    List<CategoryModel> categoryModelList;
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

    public SearchAdapter(Context context, List<CategoryModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_searching_products, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String id_produk = categoryModelList.get(position).getId_produk();
        final String nama_produk = categoryModelList.get(position).getNama_produk();
        final int harga_produk = categoryModelList.get(position).getHarga();
        final String foto_produk = categoryModelList.get(position).getUrl_images_produk();
        final String kategori = categoryModelList.get(position).getKategori();
        final String detail_kategori = categoryModelList.get(position).getDetail_kategori();

        holder.tv_namaProduk.setText(nama_produk);
        holder.tv_hargaProduk.setText(String.valueOf(harga_produk));
        Glide.with(context).load(foto_produk).centerCrop().into(holder.iv_fotoProduk);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Long tsLong = System.currentTimeMillis();
                String ts = tsLong.toString();
                
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:", Locale.getDefault());
                String lastView = sdf.format(calendar.getTime());

                DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("terakhirDiCari")
                        .child(fUser.getUid()).child(ts);
                dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().child("id_produk").setValue(id_produk);
                        snapshot.getRef().child("timestamp").setValue(ts);
                        snapshot.getRef().child("waktu_lihat").setValue(sdf.format(calendar.getTime()));
                        snapshot.getRef().child("detail_kategori").setValue(detail_kategori);
                        snapshot.getRef().child("kategori").setValue(kategori);
                        snapshot.getRef().child("url_images_produk").setValue(foto_produk);
                        snapshot.getRef().child("harga").setValue(harga_produk);
                        snapshot.getRef().child("nama_produk").setValue(nama_produk);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
        return categoryModelList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_namaProduk, tv_hargaProduk;
        ImageView iv_fotoProduk;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            tv_namaProduk = itemView.findViewById(R.id.tv_namaProdukSearch);
            tv_hargaProduk = itemView.findViewById(R.id.tv_hargaProdukSearch);
            iv_fotoProduk = itemView.findViewById(R.id.iv_fotoProdukItemSearch);
        }
    }
}
