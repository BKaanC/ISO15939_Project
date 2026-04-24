package model;

// kullanıcı Step 2'de kalite tipini seçiyor
// ya Product (ürün kalitesi) ya da Process (süreç kalitesi) oluyor
// enum kullandım çünkü sabit sayıda seçenek var ve yenisi eklenmez
public enum QualityType {

    PRODUCT("Product Quality", "Software product characteristics: performance, security, usability, reliability"),
    PROCESS("Process Quality", "Development process characteristics: sprint efficiency, code quality, team collaboration");

    // ekranda gösterilecek isim
    private final String label;
    // kısa açıklama (radio butonun yanında gösterebiliriz)
    private final String description;

    QualityType(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
