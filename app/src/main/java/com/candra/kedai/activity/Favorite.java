package com.candra.kedai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.candra.kedai.R;
import com.candra.kedai.adapter.FavoriteAdapter;
import com.candra.kedai.model.FavoriteModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Favorite extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    ImageView btn_back;
    TextView tv_belumAdaFavorit;
    ProgressBar progressBar;

    DatabaseReference dRef;
    FirebaseUser fUser;

    SwipeRefreshLayout sw_favorit;

    List<FavoriteModel> listFav = new ArrayList<>();
    FavoriteAdapter adapterFav;

    Integer wishlist = 0;
    RecyclerView rv_favorit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        progressBar = findViewById(R.id.pb_wishlist);
        tv_belumAdaFavorit = findViewById(R.id.tv_belumAdaFavorit);
        btn_back = findViewById(R.id.btn_backFavorit);
        rv_favorit = findViewById(R.id.rv_favorit);
        sw_favorit = findViewById(R.id.sw_favorit);

        sw_favorit.setOnRefreshListener(this);
        listFavorit();
        rv_favorit.setHasFixedSize(true);
        rv_favorit.setLayoutManager(new GridLayoutManager(Favorite.this, 2));

        tv_belumAdaFavorit.setVisibility(View.INVISIBLE);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void listFavorit(){
        listFav.clear();
        Query query = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(fUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    FavoriteModel favoriteModel = ds.getValue(FavoriteModel.class);
                    listFav.add(favoriteModel);

                    adapterFav = new FavoriteAdapter(Favorite.this, listFav);
                    adapterFav.notifyDataSetChanged();
                    rv_favorit.setAdapter(adapterFav);
                }
                if (adapterFav == null){
                    rv_favorit.setVisibility(View.GONE);
                    tv_belumAdaFavorit.setVisibility(View.VISIBLE);
                } else {

                }
                progressBar.setVisibility(View.GONE);
                sw_favorit.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRefresh() {
        listFavorit();
    }
}