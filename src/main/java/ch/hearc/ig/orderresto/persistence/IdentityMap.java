package ch.hearc.ig.orderresto.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class IdentityMap<T, K> {

    private final Map<K, T> cache = new HashMap<>();

    public T get(K id) {
        return cache.get(id);
    }

    public void put(K id, T object) {
        cache.put(id, object);
    }

    public boolean contains(K id) {
        return cache.containsKey(id);
    }

    public Collection<T> getAll() {
        return cache.values();
    }
}
