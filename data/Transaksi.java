package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Transaksi {
    int Id, Jumlah;
    String IdFaktur,Nama, Pembeli, Penjual, Alamat, Tipe, Tanggal;

    public Transaksi(int Id, String IdFaktur, String Nama, int Jumlah, String Pembeli, String Tanggal, String Penjual, String Alamat, String Tipe) {
        this.Id = Id;
        this.IdFaktur = IdFaktur;
        this.Nama = Nama;
        this.Jumlah = Jumlah;
        this.Pembeli = Pembeli;
        this.Tanggal = Tanggal;
        this.Penjual = Penjual;
        this.Alamat = Alamat;
        this.Tipe = Tipe;
    }

    // private Date parseDate(String dateStr) {
    //     try {
    //         SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    //         return format.parse(dateStr);
    //     } catch (ParseException e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    // }

    public static ObservableList<Transaksi> Tlist() {
        return FXCollections.observableArrayList(
            new Transaksi(1, "FA001", "10kg/ cap buah lai", 10, "Jono", "01-01-2024", "Joni", "Gudang Khatulistiwa", "Masuk"),
            new Transaksi(2, "FA001", "10kg/ cap buah lai", 10, "Jono", "01-01-2024", "Joni", "Gudang Khatulistiwa", "Keluar")
            );
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getJumlah() {
        return Jumlah;
    }

    public void setJumlah(int jumlah) {
        Jumlah = jumlah;
    }

    public String getIdFaktur() {
        return IdFaktur;
    }

    public void setIdFaktur(String idFaktur) {
        IdFaktur = idFaktur;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getPembeli() {
        return Pembeli;
    }

    public void setPembeli(String pembeli) {
        Pembeli = pembeli;
    }

    public String getPenjual() {
        return Penjual;
    }

    public void setPenjual(String penjual) {
        Penjual = penjual;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public String getTipe() {
        return Tipe;
    }

    public void setTipe(String tipe) {
        Tipe = tipe;
    }

    public String getTanggal() {
        return Tanggal;
    }

    public void setTanggal(String tanggal) {
        Tanggal = tanggal;
    }


    
}
