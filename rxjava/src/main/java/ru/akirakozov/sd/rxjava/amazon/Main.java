package ru.akirakozov.sd.rxjava.amazon;

import rx.Observable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("login");
        map.put("login", list);
        Observable<String> profile = RxNettyHttpServer.getProductList(map);
        String first = profile.toBlocking().first();
        System.out.println(first);
    }
}
