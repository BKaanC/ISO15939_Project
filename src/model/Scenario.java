package model;

import java.util.ArrayList;
import java.util.List;

// Scenario =bir senaryo ("Scenario C — Team Alpha")
// kullanıcı Step 2'de mode + senaryo seçiyor
// her senaryonun birden fazla dimension'ı vardır
public class Scenario {

    private String name;
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

    // getter'lar

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

    // step 5 gap analizi için en düşük skorlu dimensionı bulan metod
    public Dimension findLowestScoringDimension() {
        if (dimensions.isEmpty()) {
            return null;
        }
        Dimension lowest = dimensions.get(0);
        for (int i = 1; i < dimensions.size(); i++) {
            Dimension d = dimensions.get(i);
            if (d.calculateDimensionScore() < lowest.calculateDimensionScore()) {
                lowest = d;
            }
        }
        return lowest;
    }

    // JRadioButton gibi liste elemanlarında kısa metinde göstermek için
    @Override
    public String toString() {
        return name;
    }
}
