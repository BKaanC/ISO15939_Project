package model;

import java.util.ArrayList;
import java.util.List;

// Scenario = bir senaryo (ör: "Scenario C — Team Alpha")
// kullanıcı Step 2'de mode + senaryo seçiyor
// her senaryonun birden fazla dimension'ı var
public class Scenario {

    private String name;                 // ör: "Scenario C — Team Alpha"
    private String description;          // kısa açıklama (kullanıcıya göstermek için)
    private Mode mode;                   // hangi moda ait (Health/Education)
    private List<Dimension> dimensions;  // senaryonun boyutları

    public Scenario(String name, String description, Mode mode) {
        this.name = name;
        this.description = description;
        this.mode = mode;
        this.dimensions = new ArrayList<>();
    }

    // dimension eklemek için
    public void addDimension(Dimension dimension) {
        dimensions.add(dimension);
    }

    // ---- getter'lar ----

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Mode getMode() {
        return mode;
    }

    public List<Dimension> getDimensions() {
        return dimensions;
    }

    // Step 5'te gap analysis için en düşük skorlu dimension'ı bulmamız lazım
    // bu yüzden burada bir helper metod yazdım
    public Dimension findLowestScoringDimension() {
        if (dimensions.isEmpty()) {
            return null;
        }
        Dimension lowest = dimensions.get(0);
        for (Dimension d : dimensions) {
            if (d.calculateDimensionScore() < lowest.calculateDimensionScore()) {
                lowest = d;
            }
        }
        return lowest;
    }

    // JRadioButton gibi liste elemanlarında kısa metin göstermek için
    @Override
    public String toString() {
        return name;
    }
}
