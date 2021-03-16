package ru.akirakozov.sd.rxjava.amazon;

import io.reactivex.netty.protocol.http.server.HttpServer;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.netty.buffer.ByteBuf;
import rx.Observable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ru.akirakozov.sd.rxjava.amazon.WebReact;

public class RxNettyHttpServer {

    private static final WebReact storage = new WebReact();

    public static void main(final String[] args) {
        HttpServer
                .newServer(8080)
                .start((req, resp) -> {
                    Observable<String> response = handleRequest(req);
                    return resp.writeString(response);
//                    String name = req.getDecodedPath().substring(1);
//                    Observable<String> response = Observable
//                            .just(name)
//                            .map(usd -> "Hello, " + name + "!");
//
//                    return resp.writeString(response);
                })
                .awaitShutdown();
    }


    public static Observable<String> handleRequest(HttpServerRequest<ByteBuf> req) {
        String query = req.getDecodedPath().substring(1);
        Map<String, List<String>> parameters = req.getQueryParameters();
        switch (query) {
            case "registerUser":
                return registerUser(parameters);
            case "getProfile":
                return getProfile(parameters);
            case "addProduct":
                return addProduct(parameters);
            case "getProductList":
                return getProductList(parameters);
            default:
                return error("Unknown method call");
        }
    }

    private static String getParameter(Map<String, List<String>> queryParams, String paramName) {
        List<String> param2 = queryParams.get(paramName);
        if (!param2.isEmpty()) {
            return param2.get(0);
        }
        return null;
    }

    private static Observable<User> getUser(Map<String, List<String>> params) {
        String login = getParameter(params, "login");
        if (login != null) {
            return storage.getUserByLogin(login);
        }
        String id = getParameter(params, "id");
        if (id != null) {
            return storage.getUserById(id);
        }
        return null;
    }


    public static Observable<String> error(String text) {
        return Observable.just(text);
    }

    public static Observable<String> getProfile(Map<String, List<String>> params) {
        Observable<User> user = getUser(params);
        if (user != null) {
            return user.map(User::toString);
        }
        return error("no info");//никогда не дойдет, хз как говорить пользователю что такого чела нет, мб как-то внутрь юзера кинуть флаг на валидность и как-то по-другому фильтровать по логину, чтобы невлидный юзер всегда подходил и тогда надо просто при создании таблицы сразу в нее его закинуть
    }

    public static Observable<String> registerUser(Map<String, List<String>> params) {
        String login = getParameter(params, "login");
        if (login == null) {
            return error("need login");
        }
        String name = getParameter(params, "name");
        if (name == null) {
            return error("need name");
        }
        String curr = getParameter(params, "currency");
        if (curr == null) {
            return error("need currency");
        }

        Currency currency = new Currency(curr);
        if (!currency.isCorrect) {
            return error("type euro/dollar/ruble");
        }

        User user = new User(name, login, curr);
        return storage.saveUser(user).map(Objects::toString);
    }

    public static Observable<String> addProduct(Map<String, List<String>> params) {
        String name = getParameter(params, "name");
        if (name == null) {
            return error("type name of product");
        }
        String price = getParameter(params, "price");
        if (price == null) {
            return error("type price of product");
        }
        double price2 = Double.parseDouble(price);
        return storage.addProduct(new Product(name, price2)).map(Objects::toString);
    }

    public static Observable<String> getProductList(Map<String, List<String>> params) {
        Observable<User> user = getUser(params);
        if (user == null) {
            return error("type login/id");
        }
        return user.flatMap(
                u -> storage.getProductList()
                        .map(p -> p.toStringCurrency(u.currency))
        );
    }
}