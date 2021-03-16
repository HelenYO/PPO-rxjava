package ru.akirakozov.sd.rxjava.amazon;

import ru.akirakozov.sd.rxjava.amazon.Currency;
import org.bson.Document;

public class User {
    public final String id;
    public final String name;
    public final String login;
    public final Currency currency;


    public User(Document doc) {
        this(doc.get("_id").toString(), doc.getString("name"), doc.getString("login"), doc.getString("currency"));
    }

    public User(String id, String name, String login, String currency) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.currency = new Currency(currency);
    }

    public User(String name, String login, String currency) {
        this(null, name, login, currency);
    }

    public Document toDoc() {
        return new Document()
                .append("name", name)
                .append("login", login)
                .append("currency", currency.toString());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}