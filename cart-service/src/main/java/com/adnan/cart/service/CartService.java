package com.adnan.cart.service;

import com.adnan.cart.model.Cart;
import com.adnan.cart.model.Item;
import com.adnan.cart.model.Product;
import com.adnan.cart.repository.CartRedisRepository;
import com.adnan.cart.util.CartUtility;
import com.adnan.cart.util.ItemUtility;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);
    
    @Autowired
    private ProductClientService productClientService;

    @Autowired
    private CartRedisRepository cartRedisRepository;
    
    public Cart getCart(String cartId) {
        List<Item> items = (List)cartRedisRepository.getCart(cartId, Item.class);
        Cart cart = new Cart(cartId, items, CartUtility.getTotalCostForCart(items));
        return cart;
    }
    
    public boolean checkIfItemExist(String cartId, Long productId) {
        List<Item> items = (List)cartRedisRepository.getCart(cartId, Item.class);
        for (Item item : items) {
            if((item.getProduct().getId()).equals(productId)) {
                return true;
            }
        }
        return false;
    }
    
    public void addItemToCart(String cartId, Long productId, Integer quantity) {
        LOGGER.info("Fetching product (id = {}) using FeignClient", productId);
        Product product = productClientService.getCartProductById(productId);
        if (product == null) {
            LOGGER.error("Product (id = {}) is not found", productId);
            return;
        }
        Item item = new Item(product, quantity, ItemUtility.getSubTotalCostForItem(product, quantity));
        cartRedisRepository.addItemToCart(cartId, item);
    }
    
    public void changeItemQuantity(String cartId, Long productId, Integer quantity) {
        LOGGER.info("Fetching product (id = {}) using FeignClient", productId);
        Product product = productClientService.getCartProductById(productId);
        List<Item> items = (List)cartRedisRepository.getCart(cartId, Item.class);
        for (Item item : items) {
            if ((item.getProduct().getId()).equals(productId)) {
                cartRedisRepository.removeItemFromCart(cartId, item);
                item.setQuantity(quantity);
                item.setSubTotalCost(ItemUtility.getSubTotalCostForItem(product, quantity));
                cartRedisRepository.addItemToCart(cartId, item);
                break;
            }
        }
    }
    
    public void deleteItem(String cartId, Long productId) {
        List<Item> items = (List)cartRedisRepository.getCart(cartId, Item.class);
        for (Item item : items) {
            if ((item.getProduct().getId()).equals(productId)) {
                cartRedisRepository.removeItemFromCart(cartId, item);
                break;
            }
        }
    }
    
    public void clearCart(String cartId) {
        cartRedisRepository.deleteCart(cartId);
    }
    
}
