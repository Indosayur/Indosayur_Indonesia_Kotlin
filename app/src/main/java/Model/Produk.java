package Model;

import java.io.Serializable;

public class Produk implements Serializable {

    public int Id;
    public String name;
    public String harga;
    public String deskripsi;
    public int kategori_id;
    public String gambar;
    public String created_at;
    public String updated_at;

}
