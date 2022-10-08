package com.blamejared.funkyframes.util;

import java.util.HashMap;

public class SelfKeyedHashMap<K, V extends SelfKeyable<K>> extends HashMap<K, V> {
    
    public V add(V value) {
        
        return put(value.getKey(), value);
    }
    
}
