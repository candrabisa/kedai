package com.candra.kedai.model.category;

public class CategoryModel {

    private String nama_produk, desc, url_images_produk;
    private int harga;

    public CategoryModel(){

    }

    public CategoryModel(String nama_produk, String desc, String url_images_produk, int harga) {
        this.nama_produk = nama_produk;
        this.desc = desc;
        this.url_images_produk = url_images_produk;
        this.harga = harga;
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
}
