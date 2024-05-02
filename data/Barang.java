package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Barang {
    int Id,Stok,Minimal;
    String Nama,Status;

    public Barang(int Id, String Nama, int Stok, int Minimal,String Status) {
        this.Id = Id;
        this.Nama = Nama;
        this.Stok = Stok;
        this.Minimal = Minimal;
        this.Status = Status;
    }

    public static ObservableList<Barang> list() {
        return FXCollections.observableArrayList(
                new Barang(1, "10kg/Cap Buah lai", 10,5, "Tersedia"),
                new Barang(2, "20kg/Cap Buah lai", 0,5, "Tidak Tersedia"),
                new Barang(3, "5kg/Cap Buah lai", 3,5, "Tersedia")
        );
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getStok() {
        return Stok;
    }

    public void setStok(int stok) {
        Stok = stok;
    }

    public int getMinimal() {
        return Minimal;
    }

    public void setMinimal(int minimal) {
        Minimal = minimal;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

   
    
}
