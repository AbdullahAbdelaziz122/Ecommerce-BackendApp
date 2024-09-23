package com.BMS.service.serviceImpl;

import com.BMS.util.AppConstants;
import com.BMS.model.Cart;
import com.BMS.model.ShoppingSession;
import com.BMS.repository.ShoppingSessionRepository;
import com.BMS.service.ShoppingSessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

import static com.BMS.util.AppConstants.getSessionIdFromCookie;


@Service
@RequiredArgsConstructor
public class ShoppingSessionServiceImpl implements ShoppingSessionService {



    private final ShoppingSessionRepository shoppingSessionRepository;

    public String startSession(HttpServletResponse response, HttpServletRequest request) {
        // Check if the user already has a session id
        String sessionId = getSessionIdFromCookie(request);
        ShoppingSession shoppingSession = null;

        if (sessionId != null) {
            shoppingSession = shoppingSessionRepository.findBySessionId(sessionId).orElse(null);
        }

        if (sessionId == null || shoppingSession == null) {
            // Create a new session id
            sessionId = UUID.randomUUID().toString();
            shoppingSession = new ShoppingSession();
            shoppingSession.setSessionId(sessionId);
            shoppingSession.setLastActivity(new Date());
            shoppingSession.setCart(new Cart());

            // save Shopping-Session
            shoppingSessionRepository.save(shoppingSession);

            // set session ID in a cookie
            Cookie cookie = new Cookie(AppConstants.COOKIE_NAME, sessionId);
            cookie.setMaxAge(AppConstants.COOKIE_MAX_AGE);
            cookie.setPath(AppConstants.COOKIE_PATH);
            response.addCookie(cookie);
        } else {
            // Update last activity for existing session
            shoppingSession.setLastActivity(new Date());
            shoppingSessionRepository.save(shoppingSession);
        }

        return  sessionId;
    }


}




