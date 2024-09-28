package com.BMS.service.serviceImpl;


import com.BMS.exceptions.APIException;
import com.BMS.exceptions.ResourceNotFoundException;
import com.BMS.model.Cart;
import com.BMS.model.CartItem;
import com.BMS.model.Product;
import com.BMS.model.ShoppingSession;
import com.BMS.payloads.CartDTO;
import com.BMS.payloads.CartResponse;
import com.BMS.repository.CartItemRepository;
import com.BMS.repository.CartRepository;
import com.BMS.repository.ProductRepository;
import com.BMS.repository.ShoppingSessionRepository;
import com.BMS.service.CartService;
import com.BMS.service.ShoppingSessionService;
import com.BMS.util.CustomMapper;
import com.BMS.util.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ShoppingSessionService shoppingSessionService;
    private final ShoppingSessionRepository shoppingSessionRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final SessionManager sessionManager;
    private final CustomMapper mapper = new CustomMapper(modelMapper);


    // Get the session ID or Create New one if not found.
    @Override
    public CartDTO addItemToCart(HttpServletRequest request, HttpServletResponse response, Long productId, Integer quantity) {

        ShoppingSession session = sessionManager.manageSession(request, response);


        Cart cart = session.getCart();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Ensure product is in stock
        if (product.getQuantity() <= 0) {
            throw new APIException("Product is out of stock");
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId)
                .orElse(null);

        if (cartItem == null) {
            cartItem = new CartItem();
            cart.getCartItems().add(cartItem);
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(0);
        }

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setProductPrice(product.getPrice());
//        cartItem = cartItemRepository.save(cartItem);

        cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));
        cartRepository.save(cart);

        return mapper.cartToCartDTO(cart);
    }

    @Override
    public CartDTO getCustomerCart(HttpServletRequest request, HttpServletResponse response) {
        ShoppingSession session = sessionManager.manageSession(request, response);
        Cart cart = session.getCart();

        // Initialize a CartDTO and check if the cart is null
        CartDTO cartDTO = new CartDTO();

        if (cart != null) {
            // Map the cart to CartDTO if the cart exists
            cartDTO = mapper.cartToCartDTO(cart);
        }
        return cartDTO;
    }

    @Override
    public CartResponse getAllCarts(Integer pageNumber, Integer pageLimit, Long cartId){
        Pageable pageable = PageRequest.of(pageNumber, pageLimit, Sort.unsorted());
        Page<Cart> cartPage;

        if(cartId != null) {
            cartPage = cartRepository.findCartByCartId(cartId, pageable);
        }else{
            cartPage = cartRepository.findAll(pageable);
        }


        List<Cart> carts = cartPage.getContent();
        List<CartDTO> cartDTOS = mapper.cartsToCartDTOS(carts);


        CartResponse cartResponse = new CartResponse();
        cartResponse.setData(cartDTOS);
        cartResponse.setPageNumber(cartPage.getNumber());
        cartResponse.setPageLimit(cartPage.getSize());
        cartResponse.setTotalPages(cartPage.getTotalPages());
        cartResponse.setTotalElements(cartPage.getTotalElements());

        return cartResponse;

    }




    @Override
    public CartDTO updateProductQuantityInCart(HttpServletRequest request, HttpServletResponse response, Long productId, Integer quantity){
        ShoppingSession shoppingSession = sessionManager.manageSession(request, response);

        Cart cart = shoppingSession.getCart();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("No product found with ID: " + productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + productId + " not found"));

        if (product.getQuantity() == 0) {
            throw new APIException(product.getName() + " is out of stock");
        }


        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setProductPrice(product.getPrice());


        cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));


        cartItemRepository.save(cartItem);


        cartRepository.save(cart);


        return mapper.cartToCartDTO(cart);
    }


   @Override
    public void deleteProductFromCart(HttpServletRequest request, HttpServletResponse response, Long productId){
        ShoppingSession session = sessionManager.manageSession(request, response);

        Cart cart = session.getCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("No Product were found by ID: " + productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + productId + " not found in cart"));

        cart.getCartItems().remove(cartItem);
        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));


        cartItem.setCart(null);
        cartItem.setProduct(null);
        cartItemRepository.deleteById(cartItem.getCartItemId());
        cartRepository.save(cart);
    }

    public void clearCart(Long cartId){
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Product with ID " + cartId + " not found"));
        List<CartItem> cartItems = new ArrayList<>();
        cart.setCartItems(cartItems);

        cartRepository.save(cart);


    }

}
