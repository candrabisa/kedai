package com.candra.kedai.adapter.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.candra.kedai.R;
import com.candra.kedai.activity.ProductDetails;
import com.candra.kedai.model.category.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {

    Context context;
    List<CategoryModel>listCategory;

    public CategoryAdapter(Context context, List<CategoryModel> listCategory) {
        this.context = context;
        this.listCategory = listCategory;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_buatkamu, parent, false));
    }

    private static final String TAG = "CategoryAdapter";
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        //ambil data
        final String foto_produk = listCategory.get(position).getUrl_images_produk();
        final String nama_produk = listCategory.get(position).getNama_produk();
        final String desc = listCategory.get(position).getDesc();
        final int harga_produk = listCategory.get(position).getHarga();

        final String id_produk = listCategory.get(position).getId_produk();
        final String kategori = listCategory.get(position).getKategori();
        final String detail_kategri = listCategory.get(position).getDetail_kategori();
        final String rekomendasi = listCategory.get(position).getRekomendasi();

        //set data
        holder.tv_namaProduk.setText(nama_produk);
        holder.tv_hargaProduk.setText(String.valueOf(harga_produk));
        try {
            Glide.with(context).load(foto_produk).centerCrop().fitCenter().into(holder.iv_Produk);
        } catch (Exception e){
            Toast.makeText(context, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, ProductDetails.class);
                    intent.putExtra("id_produk", id_produk);
                    intent.putExtra("kategori", kategori);
                    intent.putExtra("detail_kategori", detail_kategri);
                    context.startActivity(intent);
                } catch (Exception e){
                    Toast.makeText(context, "Lihat di kategori", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public Filter getFilterProduk() {
        return filterProduk;
    }

    private Filter filterProduk = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CategoryModel> filterList = new ArrayList<>();
            if (constraint == null || constraint.length() ==0 ){
                filterList.addAll(listCategory);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (CategoryModel model : listCategory){
                    if (model.getNama_produk().toLowerCase().contains(filterPattern)){
                        filterList.add(model);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listCategory.clear();
            listCategory.addAll((List<CategoryModel>)results.values);
            notifyDataSetChanged();
        }
    };

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
