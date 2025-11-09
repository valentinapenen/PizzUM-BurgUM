package com.example.PizzUMBurgUM.entities.enums;

public enum TamanoPizza {
    CHICA(300.0),
    MEDIANA(400.0),
    GRANDE(550.0);

    private final double precioBase;

    TamanoPizza(double precioBase) {
        this.precioBase = precioBase;
    }

    public double getPrecioBase() {
        return precioBase;
    }
}
