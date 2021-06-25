package util

object Config {
    private const val IpNow = "192.168.1.7" //ganti juga di xml/config_network.xml
    const val baseUr12 = "http://$IpNow/Indosayur/public/"
    const val ProdukUrl = baseUr12 + "storage/produk/"
}