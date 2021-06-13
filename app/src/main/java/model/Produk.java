package model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "keranjang")
public class Produk implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    public int IdTb;


    public int Id;
    public String name;
    public String harga;
    public String deskripsi;
    public int kategori_id;
    public String gambar;
    public String created_at;
    public String updated_at;
    public int jumlah = 1;
    public boolean selected;

}
