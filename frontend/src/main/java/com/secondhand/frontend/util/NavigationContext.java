package com.secondhand.frontend.util;

/**
 * Tiny holder for passing navigation contexts (conversation IDs, ad IDs, seller details)
 * between scenes and popups.
 */
public class NavigationContext {

    // --- Conversation Context ---
    private static Long currentConversationId;
    private static String currentConversationTitle;

    // --- Chat Navigation Target (Used ONLY for starting a chat) ---
    private static Long targetAdvertisementId;
    private static Long targetSellerId;
    private static String targetAdvertisementTitle;
    private static String targetSellerUsername;

    // --- Rating Context (ISOLATED for the Rate Seller Screen) ---
    private static Long ratingSellerId;
    private static Long ratingAdvertisementId;
    private static String ratingSellerUsername;
    private static String ratingAdvertisementTitle;

    private NavigationContext() {
    }

    // --- Conversation Methods ---
    public static void setCurrentConversation(Long conversationId, String title) {
        currentConversationId = conversationId;
        currentConversationTitle = title;
    }

    public static Long getCurrentConversationId() { return currentConversationId; }
    public static String getCurrentConversationTitle() { return currentConversationTitle; }

    // --- Chat Navigation Methods ---
    public static Long getTargetAdvertisementId() { return targetAdvertisementId; }
    public static void setTargetAdvertisementId(Long id) { targetAdvertisementId = id; }

    public static String getTargetAdvertisementTitle() { return targetAdvertisementTitle; }
    public static void setTargetAdvertisementTitle(String title) { targetAdvertisementTitle = title; }

    public static Long getTargetSellerId() { return targetSellerId; }
    public static void setTargetSellerId(Long id) { targetSellerId = id; }

    public static String getTargetSellerUsername() { return targetSellerUsername; }
    public static void setTargetSellerUsername(String username) { targetSellerUsername = username; }

    // --- Rating Context Methods ---
    public static Long getRatingSellerId() { return ratingSellerId; }
    public static void setRatingSellerId(Long id) { ratingSellerId = id; }

    public static Long getRatingAdvertisementId() { return ratingAdvertisementId; }
    public static void setRatingAdvertisementId(Long id) { ratingAdvertisementId = id; }

    public static String getRatingSellerUsername() { return ratingSellerUsername; }
    public static void setRatingSellerUsername(String username) { ratingSellerUsername = username; }

    public static String getRatingAdvertisementTitle() { return ratingAdvertisementTitle; }
    public static void setRatingAdvertisementTitle(String title) { ratingAdvertisementTitle = title; }

    // --- Clear Context ---
    public static void clear() {
        currentConversationId = null;
        currentConversationTitle = null;
        targetAdvertisementId = null;
        targetSellerId = null;
        targetSellerUsername = null;
        targetAdvertisementTitle = null;
        ratingSellerId = null;
        ratingAdvertisementId = null;
        ratingSellerUsername = null;
        ratingAdvertisementTitle = null;
    }
}