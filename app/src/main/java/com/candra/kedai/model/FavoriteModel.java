package com.candra.kedai.model;

import com.google.gson.annotations.SerializedName;

public class FavoriteModel {
    @SerializedName("id_produk")
    private String id_produk;
    @SerializedName("nama_produk")
    private String nama_produk;
    @SerializedName("url_images_produk")
    private String url_images_produk;
    @SerializedName("harga")
    private int harga;
    @SerializedName("kategori")
    private String kategori;
    @SerializedName("detail_kategori")
    private String detail_kategori;

    public FavoriteModel(){

    }

    public FavoriteModel(String id_produk, String nama_produk, String url_images_produk, int harga, String kategori, String detail_kategori) {
        this.id_produk = id_produk;
        this.nama_produk = nama_produk;
        this.url_images_produk = url_images_produk;
        this.harga = harga;
        this.kategori = kategori;
        this.detail_kategori = detail_kategori;
    }

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getUrl_images_produk() {
        return url_images_produk;
    }

    public void setUrl_images_produk(String url_images_produk) {
        this.url_images_produk = url_images_produk;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getDetail_kategori() {
        return detail_kategori;
    }

    public void setDetail_kategori(String detail_kategori) {
        this.detail_kategori = detail_kategori;
    }
}
