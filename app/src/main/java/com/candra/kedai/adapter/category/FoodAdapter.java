package com.candra.kedai.adapter.category;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.candra.kedai.R;
import com.candra.kedai.activity.ProductDetails;
import com.candra.kedai.model.category.FoodModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyHolder> {

    Context context;
    List<FoodModel>listFood;

    public FoodAdapter(Context context, List<FoodModel> listFood) {
        this.context = context;
        this.listFood = listFood;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_buatkamu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //ambil data
        final String foto_produk = listFood.get(position).getUrl_images_produk();
        final String nama_produk = listFood.get(position).getNama_produk();
        final int harga_produk = listFood.get(position).getHarga();

        final String id_produk = listFood.get(position).getNama_produk();

        //set data
        holder.tv_namaProduk.setText(nama_produk);
        holder.tv_hargaProduk.setText(harga_produk);
        try {
            Picasso.get().load(foto_produk).placeholder(R.drawable.burger).into(holder.iv_Produk);
        } catch (Exception e){
            Toast.makeText(context, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("nama_produk", nama_produk);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFood.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        ImageView iv_Produk;
        TextView tv_namaProduk, tv_hargaProduk;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            iv_Produk = itemView.findViewById(R.id.iv_produk);
            tv_namaProduk = itemView.findViewById(R.id.tv_namaproduk);
            tv_hargaProduk = itemView.findViewById(R.id.tv_hargaproduk);
        }

    }
}
