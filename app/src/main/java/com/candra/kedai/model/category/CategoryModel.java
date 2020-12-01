package com.candra.kedai.model.category;

import com.google.gson.annotations.SerializedName;

import java.util.IdentityHashMap;

public class CategoryModel {

    @SerializedName("id_produk")
    private String id_produk;
    @SerializedName("nama_produk")
    private String nama_produk;
    @SerializedName("desc")
    private String desc;
    @SerializedName("url_images_produk")
    private String url_images_produk;
    @SerializedName("harga")
    private int harga;
    @SerializedName("kategori")
    private String kategori;
    @SerializedName("detail_kategori")
    private String detail_kategori;

    // bsa ngga lu tambahin variabel nama_kategori sama nama makanan

    //apa gw bikin model baru?
    //seharusnya stau model sama ini
    // siapp nir nanti gw cobain deh
    // intinya kalo data sudah dapet di set satu model aja kesini nanti gampang buat apa apa
    // nah iya nir harusnya, cuma yg gw binging kalo nanti yang diklik beda kategori loh kan harusny bisa
    // di putextra tpi kan di adapter kayak//

    public CategoryModel(){

    }

    public CategoryModel(String id_produk, String nama_produk, String desc, String url_images_produk, int harga, String kategori, String detail_kategori) {
        this.id_produk = id_produk;
        this.nama_produk = nama_produk;
        this.desc = desc;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
