package com.candra.kedai.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.candra.kedai.R;
import com.candra.kedai.activity.ProductDetails;
import com.candra.kedai.model.CategoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class LastViewSearchAdapter extends RecyclerView.Adapter<LastViewSearchAdapter.MyHolder> {

    Context context;
    List<CategoryModel> modelList;
    private final int limit = 5;

    public LastViewSearchAdapter(Context context, List<CategoryModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_recent_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String id_produk = modelList.get(position).getId_produk();
        final String foto_produk = modelList.get(position).getUrl_images_produk();
        final String kategori = modelList.get(position).getKategori();
        final String detail_kategori = modelList.get(position).getDetail_kategori();

        Glide.with(context).load(foto_produk).centerCrop().into(holder.iv_recentView);

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
        if(modelList.size() > limit){
            return limit;
        }
        else
        {
            return modelList.size();
        }

    }

    static class MyHolder extends RecyclerView.ViewHolder {
        ImageView iv_recentView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            iv_recentView = itemView.findViewById(R.id.iv_recentView);

        }
    }
}
