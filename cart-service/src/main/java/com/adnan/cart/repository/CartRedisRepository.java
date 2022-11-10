package com.adnan.cart.repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

@Repository
public class CartRedisRepository {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Jedis jedis = new Jedis();
    
    public void addItemToCart(String key, Object item) {
        try {
            String jsonObject = objectMapper.writeValueAsString(item);
            jedis.sadd(key, jsonObject);

        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    public Collection<Object> getCart(String key, Class type) {
        Collection<Object> cart = new ArrayList<>();
        for (String smember : jedis.smembers(key)) {
            try {
                cart.add(objectMapper.readValue(smember, type));
            }
            catch (JsonParseException e) {
                e.printStackTrace();
            }
            catch (JsonMappingException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cart;
    }
    
    public void removeItemFromCart(String key, Object item) {
        try {
            String jsonObject = objectMapper.writeValueAsString(item);
            jedis.srem(key, jsonObject);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteCart(String key) {
        jedis.del(key);
    }
    
}
