package com.BMS.Controller;

import com.BMS.util.AppConstants;
import com.BMS.payloads.CartDTO;
import com.BMS.payloads.CartResponse;
import com.BMS.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.BMS.util.AppConstants.getSessionIdFromCookie;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;

   @PostMapping("/public/cart/add")
public ResponseEntity<CartDTO> addItemToCart(
        @RequestParam(value = "productId", required = true) Long productId,
        @RequestParam(value = "quantity", required = false, defaultValue = "1") Integer quantity,
        HttpServletRequest request,
        HttpServletResponse response
)
{
    // Log the request details
    System.out.println("Received request to add item to cart");
    System.out.println("Product ID: " + productId);
    System.out.println("Quantity: " + quantity);

    // Retrieve and log the session ID
    String sessionId = getSessionIdFromCookie(request);
    System.out.println("Session ID: " + sessionId);

    try {

    return new ResponseEntity<>(cartService.addItemToCart(request, response, productId, quantity), HttpStatus.CREATED);
} catch (Exception e) {
    e.printStackTrace(); // Log the stack trace
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return an appropriate response
}
}


    @GetMapping("/admin/cart/search")
    public ResponseEntity<CartResponse> getAllCarts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageLimit", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageLimit,
            @RequestParam(value = "cartId", required = false) Long cartId
    ){
        return new ResponseEntity<>(cartService.getAllCarts(pageNumber, pageLimit, cartId), HttpStatus.OK);
    }

    @GetMapping("/public/cart")
    public ResponseEntity<CartDTO> getCustomerCart(HttpServletRequest request, HttpServletResponse response){
        return new ResponseEntity<>(cartService.getCustomerCart(request, response), HttpStatus.OK);
    }



    @PostMapping("/public/cart/update")
    public ResponseEntity<CartDTO> updateProductQuantityInCart(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "productId", required = true) Long productId,
            @RequestParam(value = "quantity", defaultValue = "1", required = false) Integer quantity
    )
    {
        return new ResponseEntity<>(cartService.updateProductQuantityInCart(request,response, productId, quantity), HttpStatus.OK);
    }

    @DeleteMapping("/public/cart/delete")
    public ResponseEntity<String>deleteProductFromCart(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "productId", required = true) Long productId
    )
    {
        cartService.deleteProductFromCart(request, response, productId);
        return new ResponseEntity<>("Product has been successfully deleted", HttpStatus.OK);
    }

}
