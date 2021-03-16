package ru.akirakozov.sd.rxjava.amazon;

import com.mongodb.rx.client.*;
import org.bson.Document;
import ru.akirakozov.sd.rxjava.amazon.User;
import rx.Observable;

public class WebReact {

    private static MongoClient client = createMongoClient();

    private MongoDatabase getDatabase() {
        return client.getDatabase("rxtest2");
    }

    private static MongoClient createMongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }

    public MongoCollection<Document> getUserCollection() {
        return getDatabase().getCollection("user");
    }

    public MongoCollection<Document> getProductCollection() {
        return getDatabase().getCollection("product");
    }

    public Observable<Product> getProductList() {
        return getProductCollection().find().toObservable().map(Product::new);
    }

    public Observable<Success> saveUser(User user) {
        return getUserCollection().insertOne(user.toDoc());
    }

    public Observable<Success> addProduct(Product product) {
        return getProductCollection().insertOne(product.toDoc());
    }

    public Observable<User> getUserByLogin(String login) {
        return getUserCollection().find().toObservable()
                .filter(doc -> doc.get("login").equals(login))
                .map(User::new);
    }

    public Observable<User> getUserById(String id) {
        return getUserCollection().find().toObservable()
                .filter(doc -> doc.get("_id").toString().equals(id))
                .map(User::new);
    }
}