package com.BMS.util;

import com.BMS.model.Cart;
import com.BMS.model.ShoppingSession;
import com.BMS.repository.CartRepository;
import com.BMS.repository.ShoppingSessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SessionManager {

    private final ShoppingSessionRepository shoppingSessionRepository;
    private final CartRepository cartRepository;

    public ShoppingSession manageSession(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {

        String sessionId = getSessionIdFromCookie(request);


        if (sessionId != null) {
            Optional<ShoppingSession> shoppingSession = shoppingSessionRepository.findBySessionId(sessionId);
            if (shoppingSession.isPresent()) {

                // Check if the session is expired
                if (isSessionExpired(shoppingSession.get())) {
                    shoppingSessionRepository.delete(shoppingSession.get());
                    removeSessionCookie(response);
                    return createNewSession(response);
                }

                // If session is valid, update last activity and continue
                shoppingSession.get().setLastActivity(new Date());
                shoppingSessionRepository.save(shoppingSession.get());
                return shoppingSession.get();
            }
        }

        // No valid session found, create a new one
        return createNewSession(response);
    }

    private ShoppingSession createNewSession(HttpServletResponse response) {
        // Generate a new session ID
        String newSessionId = UUID.randomUUID().toString();
        ShoppingSession newShoppingSession = new ShoppingSession();

        // Create a new cart for the session
        Cart cart = new Cart();
        cart.setShoppingSession(newShoppingSession); // Set the relationship

        // Set properties of the shopping session
        newShoppingSession.setCart(cart);
        newShoppingSession.setSessionId(newSessionId);
        newShoppingSession.setLastActivity(new Date());

        // Save the new cart first
        cart = cartRepository.save(cart);

        // Now save the shopping session
        newShoppingSession.setCart(cart);
        newShoppingSession = shoppingSessionRepository.save(newShoppingSession);

        // Set the new session ID in a cookie
        Cookie cookie = new Cookie(AppConstants.COOKIE_NAME, newSessionId);
        cookie.setMaxAge(AppConstants.COOKIE_MAX_AGE);
        cookie.setPath("/");
        response.addCookie(cookie);

        return newShoppingSession;
    }


    private boolean isSessionExpired(ShoppingSession session) {
        long lastActivityTime = session.getLastActivity().getTime();
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastActivityTime) > AppConstants.COOKIE_MAX_AGE * 1000L;
    }

    private void removeSessionCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(AppConstants.COOKIE_NAME, null);
        cookie.setMaxAge(0); // Expire immediately
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String getSessionIdFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(AppConstants.COOKIE_NAME)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
