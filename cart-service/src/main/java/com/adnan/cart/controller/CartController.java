package com.adnan.cart.controller;

import com.adnan.cart.model.Cart;
import com.adnan.cart.model.Item;
import com.adnan.cart.service.CartService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);
    
    @Autowired
    private CartService cartService;
    
    @GetMapping ("")
    public ResponseEntity<Cart> getCart(HttpServletRequest request) {
        String[] fields = request.getHeader("cookie").split("=");
        String cartId = fields[fields.length - 1];
        LOGGER.info("Finding cart by id: {}", cartId);
        Cart cart = cartService.getCart(cartId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
    
    @PostMapping(value = "/add", params = {"productId", "quantity"})
    public ResponseEntity<Cart> addProductToCart (HttpServletRequest request,
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") Integer quantity) {
        String[] fields = request.getHeader("cookie").split("=");
        String cartId = fields[fields.length - 1];
        try {
            if (cartService.checkIfItemExist(cartId, productId)) {
                LOGGER.info("Changing item quantity, where cartId = {}", cartId);
                cartService.changeItemQuantity(cartId, productId, quantity);
            }
            else {
                LOGGER.info("Adding item to cart: cartId={}, productId={}", cartId, productId);
                cartService.addItemToCart(cartId, productId, quantity);
            }
            Cart cart = cartService.getCart(cartId);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
        catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<Cart> deleteItem (HttpServletRequest request, @PathVariable("itemId") Long itemId) {
        String[] fields = request.getHeader("cookie").split("=");
        String cartId = fields[fields.length - 1];
        try {
            if (cartService.checkIfItemExist(cartId, itemId)) {
                LOGGER.info("Deleting item (productId = {})", itemId);
                cartService.deleteItem(cartId, itemId);
                Cart cart = cartService.getCart(cartId);
                return new ResponseEntity<>(cart, HttpStatus.OK);
            }
            else {
                LOGGER.error("Item (productId = {}) is not found", itemId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception ex) {
            LOGGER.error("", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/clear")
    public ResponseEntity<Void> clearCart(HttpServletRequest request) {
        String[] fields = request.getHeader("cookie").split("=");
        String cartId = fields[fields.length - 1];
        LOGGER.info("Clear cart (cartId = {})", cartId);
        cartService.clearCart(cartId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping("/fetch")
    public List<Item> fetchCartItems(@RequestHeader("cookie") String cartId) {
        LOGGER.info("Finding cart by id: {}", cartId);
        Cart cart = cartService.getCart(cartId);
        LOGGER.info("Clear cart (cartId = {})", cartId);
        cartService.clearCart(cartId);
        return cart.getItems();
    }
    
}
