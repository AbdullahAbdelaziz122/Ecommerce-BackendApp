package com.BMS.util;

import com.BMS.model.CartItem;
import com.BMS.model.ShoppingSession;
import com.BMS.repository.CartItemRepository;
import com.BMS.repository.ShoppingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionCleanupService {

    private final ShoppingSessionRepository shoppingSessionRepository;
    private final CartItemRepository cartItemRepository;


    @Scheduled(fixedRate = AppConstants.SCHEDULED_CLEAN_TIME_FOR_COOKIES)
    public void cleanUpExpiredSessions() {
        long expirationTime = System.currentTimeMillis() - AppConstants.COOKIE_MAX_AGE; // 8 hours in milliseconds
        List<ShoppingSession> sessions = shoppingSessionRepository.findAll();
        int deletedCount = 0;
        for (ShoppingSession session : sessions) {
            if (session.getLastActivity().getTime() < expirationTime) {
                shoppingSessionRepository.delete(session);
                deletedCount++;
            }
        }
        System.out.println("Deleted " + deletedCount + " expired shopping sessions.");
    }

    @Scheduled(fixedRate = AppConstants.SCHEDULED_CLEAN_TIME_FOR_COOKIES)
    public void cleanUpCartItemsWithoutCartId() {
    List<CartItem> cartsItems = cartItemRepository.findAll();
    int deletedCount = 0;
    for (CartItem cartItem : cartsItems) {
        if (cartItem.getCart() == null) {
            cartItemRepository.delete(cartItem);
            deletedCount++;
        }
    }
    System.out.println("Deleted " + deletedCount + " cart items without a shopping session.");
}


}
