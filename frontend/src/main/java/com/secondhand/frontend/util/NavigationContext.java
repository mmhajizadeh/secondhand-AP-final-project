package com.secondhand.frontend.util;

/**
 * Tiny holder for passing navigation contexts (conversation IDs, ad IDs, seller details)
 * between scenes and popups.
 */
public class NavigationContext {

    // Conversation Context
    private static Long currentConversationId;
    private static String currentConversationTitle;

    // Advertisement & Seller Context
    private static Long targetAdvertisementId;
    private static Long targetSellerId;
    private static String targetSellerUsername;

    private NavigationContext() {
    }

    // Conversation Methods
    public static void setCurrentConversation(Long conversationId, String title) {
        currentConversationId = conversationId;
        currentConversationTitle = title;
    }

    public static Long getCurrentConversationId() {
        return currentConversationId;
    }

    public static String getCurrentConversationTitle() {
        return currentConversationTitle;
    }

    // Advertisement & Seller Methods
    public static Long getTargetAdvertisementId() {
        return targetAdvertisementId;
    }

    public static void setTargetAdvertisementId(Long id) {
        targetAdvertisementId = id;
    }

    public static Long getTargetSellerId() {
        return targetSellerId;
    }

    public static void setTargetSellerId(Long id) {
        targetSellerId = id;
    }

    public static String getTargetSellerUsername() {
        return targetSellerUsername;
    }

    public static void setTargetSellerUsername(String username) {
        targetSellerUsername = username;
    }

    // Clear Context
    public static void clear() {
        currentConversationId = null;
        currentConversationTitle = null;
        targetAdvertisementId = null;
        targetSellerId = null;
        targetSellerUsername = null;
    }
}