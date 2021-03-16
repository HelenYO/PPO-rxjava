package ru.akirakozov.sd.rxjava.amazon;

import org.bson.Document;

public class Product {
    public String name;
    public double price;
    public String id;

    //public static int idMaxCount = 0;//можно как с юзерами, но хочу для дебага нормальные значения

    public Product(String name, double price, String id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public Product(String name, double price) {
        this(name, price, null);
    }

    public Product(Document doc) {
        this(
                doc.getString("name"),
                doc.getDouble("price"),
                doc.get("_id").toString()
        );
    }

    public Document toDoc() {
        return new Document()
                .append("name", name)
                .append("price", price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                "}\n";
    }

    public String toStringCurrency(Currency currency) {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + (price / currency.exchangeRate()) + " " + currency + '\'' +
                "}\n";
    }
}
