package com.candra.kedai.model.listorder;

import com.google.gson.annotations.SerializedName;

public class ListOrderModel {

    @SerializedName("invoice")
    private String invoice;
    @SerializedName("tgl_transaksi")
    private String tgl_transaksi;
    @SerializedName("waktu_transaksi")
    private String waktu_transaksi;
    @SerializedName("nama_kategori")
    private String nama_kategori;
    @SerializedName("detail_kategori")
    private String detail_kategori;
    @SerializedName("id_produk")
    private String id_produk;
    @SerializedName("nama_produk")
    private String nama_produk;
    @SerializedName("harga_produk")
    private Integer harga_produk;
    @SerializedName("url_images_produk")
    private String url_images_produk;
    @SerializedName("qty")
    private Integer qty;
    @SerializedName("nama_pemesan")
    private String nama_pemesan;
    @SerializedName("jenis_alamat")
    private String jenis_alamat;
    @SerializedName("alamat_lengkap")
    private String alamat_lengkap;
    @SerializedName("kelurahan")
    private String kelurahan;
    @SerializedName("kecamatan")
    private String kecamatan;
    @SerializedName("kab_kota")
    private String kab_kota;
    @SerializedName("provinsi")
    private String provinsi;
    @SerializedName("total_harga_pesanan")
    private Integer total_harga_pesanan;
    @SerializedName("harga_ongkir")
    private Integer harga_ongkir;
    @SerializedName("pakai_voucher")
    private Integer pakai_voucher;
    @SerializedName("total_pembayaran")
    private Integer total_pembayaran;
    @SerializedName("metode_pembayaran")
    private String metode_pembayaran;
    @SerializedName("status_pembayaran")
    private String status_pembayaran;
    @SerializedName("status_pesanan")
    private String status_pesanan;

    public ListOrderModel(){

    }

    public ListOrderModel(String invoice, String tgl_transaksi, String waktu_transaksi, String nama_kategori, String detail_kategori, String id_produk, String nama_produk, Integer harga_produk, String url_images_produk, Integer qty, String nama_pemesan, String jenis_alamat, String alamat_lengkap, String kelurahan, String kecamatan, String kab_kota, String provinsi, Integer total_harga_pesanan, Integer harga_ongkir, Integer pakai_voucher, Integer total_pembayaran, String metode_pembayaran, String status_pembayaran, String status_pesanan) {
        this.invoice = invoice;
        this.tgl_transaksi = tgl_transaksi;
        this.waktu_transaksi = waktu_transaksi;
        this.nama_kategori = nama_kategori;
        this.detail_kategori = detail_kategori;
        this.id_produk = id_produk;
        this.nama_produk = nama_produk;
        this.harga_produk = harga_produk;
        this.url_images_produk = url_images_produk;
        this.qty = qty;
        this.nama_pemesan = nama_pemesan;
        this.jenis_alamat = jenis_alamat;
        this.alamat_lengkap = alamat_lengkap;
        this.kelurahan = kelurahan;
        this.kecamatan = kecamatan;
        this.kab_kota = kab_kota;
        this.provinsi = provinsi;
        this.total_harga_pesanan = total_harga_pesanan;
        this.harga_ongkir = harga_ongkir;
        this.pakai_voucher = pakai_voucher;
        this.total_pembayaran = total_pembayaran;
        this.metode_pembayaran = metode_pembayaran;
        this.status_pembayaran = status_pembayaran;
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

    public String getWaktu_transaksi() {
        return waktu_transaksi;
    }

    public void setWaktu_transaksi(String waktu_transaksi) {
        this.waktu_transaksi = waktu_transaksi;
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

    public String getNama_pemesan() {
        return nama_pemesan;
    }

    public void setNama_pemesan(String nama_pemesan) {
        this.nama_pemesan = nama_pemesan;
    }

    public String getJenis_alamat() {
        return jenis_alamat;
    }

    public void setJenis_alamat(String jenis_alamat) {
        this.jenis_alamat = jenis_alamat;
    }

    public String getAlamat_lengkap() {
        return alamat_lengkap;
    }

    public void setAlamat_lengkap(String alamat_lengkap) {
        this.alamat_lengkap = alamat_lengkap;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKab_kota() {
        return kab_kota;
    }

    public void setKab_kota(String kab_kota) {
        this.kab_kota = kab_kota;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public Integer getTotal_harga_pesanan() {
        return total_harga_pesanan;
    }

    public void setTotal_harga_pesanan(Integer total_harga_pesanan) {
        this.total_harga_pesanan = total_harga_pesanan;
    }

    public Integer getHarga_ongkir() {
        return harga_ongkir;
    }

    public void setHarga_ongkir(Integer harga_ongkir) {
        this.harga_ongkir = harga_ongkir;
    }

    public Integer getPakai_voucher() {
        return pakai_voucher;
    }

    public void setPakai_voucher(Integer pakai_voucher) {
        this.pakai_voucher = pakai_voucher;
    }

    public Integer getTotal_pembayaran() {
        return total_pembayaran;
    }

    public void setTotal_pembayaran(Integer total_pembayaran) {
        this.total_pembayaran = total_pembayaran;
    }

    public String getMetode_pembayaran() {
        return metode_pembayaran;
    }

    public void setMetode_pembayaran(String metode_pembayaran) {
        this.metode_pembayaran = metode_pembayaran;
    }

    public String getStatus_pembayaran() {
        return status_pembayaran;
    }

    public void setStatus_pembayaran(String status_pembayaran) {
        this.status_pembayaran = status_pembayaran;
    }

    public String getStatus_pesanan() {
        return status_pesanan;
    }

    public void setStatus_pesanan(String status_pesanan) {
        this.status_pesanan = status_pesanan;
    }
}
