package com.candra.kedai.model;

import com.google.gson.annotations.SerializedName;

public class BalanceTransactionModel {
    @SerializedName("batas_pembayaran")
    private String batas_pembayaran;
    @SerializedName("metode_pembayaran")
    private String metode_pembayaran;
    @SerializedName("nominal_transaksi")
    private int nominal_transaksi;
    @SerializedName("nomor_transaksi")
    private String nomor_transaksi;
    @SerializedName("status_transaksi")
    private String status_transaksi;
    @SerializedName("waktu_transaksi")
    private String waktu_transaksi;

    public BalanceTransactionModel(String batas_pembayaran, String metode_pembayaran, int nominal_transaksi, String nomor_transaksi, String status_transaksi, String waktu_transaksi) {
        this.batas_pembayaran = batas_pembayaran;
        this.metode_pembayaran = metode_pembayaran;
        this.nominal_transaksi = nominal_transaksi;
        this.nomor_transaksi = nomor_transaksi;
        this.status_transaksi = status_transaksi;
        this.waktu_transaksi = waktu_transaksi;
    }

    public BalanceTransactionModel(){

    }

    public String getBatas_pembayaran() {
        return batas_pembayaran;
    }

    public void setBatas_pembayaran(String batas_pembayaran) {
        this.batas_pembayaran = batas_pembayaran;
    }

    public String getMetode_pembayaran() {
        return metode_pembayaran;
    }

    public void setMetode_pembayaran(String metode_pembayaran) {
        this.metode_pembayaran = metode_pembayaran;
    }

    public int getNominal_transaksi() {
        return nominal_transaksi;
    }

    public void setNominal_transaksi(int nominal_transaksi) {
        this.nominal_transaksi = nominal_transaksi;
    }

    public String getNomor_transaksi() {
        return nomor_transaksi;
    }

    public void setNomor_transaksi(String nomor_transaksi) {
        this.nomor_transaksi = nomor_transaksi;
    }

    public String getStatus_transaksi() {
        return status_transaksi;
    }

    public void setStatus_transaksi(String status_transaksi) {
        this.status_transaksi = status_transaksi;
    }

    public String getWaktu_transaksi() {
        return waktu_transaksi;
    }

    public void setWaktu_transaksi(String waktu_transaksi) {
        this.waktu_transaksi = waktu_transaksi;
    }
}
