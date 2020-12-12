package com.candra.kedai.model.listorder;

import com.google.gson.annotations.SerializedName;

public class ListOrderModel {

    @SerializedName("invoice")
    private String invoice;
    @SerializedName("tgl_transaksi")
    private String tgl_transaksi;
    @SerializedName("nama_kategori")
    private String nama_kategori;
    @SerializedName("detail_kategori")
    private String detail_kategori;
    @SerializedName("nama_produk")
    private String nama_produk;
    @SerializedName("harga_produk")
    private Integer harga_produk;
    @SerializedName("url_images_produk")
    private String url_images_produk;
    @SerializedName("qty")
    private Integer qty;
    @SerializedName("total_pembayaran")
    private Integer total_pembayaran;
    @SerializedName("status_pesanan")
    private String status_pesanan;

    public ListOrderModel() {

    }

    public ListOrderModel(String invoice, String tgl_transaksi, String nama_kategori, String detail_kategori, String nama_produk, Integer harga_produk, String url_images_produk, Integer qty, Integer total_pembayaran, String status_pesanan) {
        this.invoice = invoice;
        this.tgl_transaksi = tgl_transaksi;
        this.nama_kategori = nama_kategori;
        this.detail_kategori = detail_kategori;
        this.nama_produk = nama_produk;
        this.harga_produk = harga_produk;
        this.url_images_produk = url_images_produk;
        this.qty = qty;
        this.total_pembayaran = total_pembayaran;
        this.status_pesanan = status_pesanan;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getTgl_transaksi() {
        return tgl_transaksi;
    }

    public void setTgl_transaksi(String tgl_transaksi) {
        this.tgl_transaksi = tgl_transaksi;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public String getDetail_kategori() {
        return detail_kategori;
    }

    public void setDetail_kategori(String detail_kategori) {
        this.detail_kategori = detail_kategori;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public Integer getHarga_produk() {
        return harga_produk;
    }

    public void setHarga_produk(Integer harga_produk) {
        this.harga_produk = harga_produk;
    }

    public String getUrl_images_produk() {
        return url_images_produk;
    }

    public void setUrl_images_produk(String url_images_produk) {
        this.url_images_produk = url_images_produk;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getTotal_pembayaran() {
        return total_pembayaran;
    }

    public void setTotal_pembayaran(Integer total_pembayaran) {
        this.total_pembayaran = total_pembayaran;
    }

    public String getStatus_pesanan() {
        return status_pesanan;
    }

    public void setStatus_pesanan(String status_pesanan) {
        this.status_pesanan = status_pesanan;
    }
}

