package com.secondhand.frontend.util;

/**
 * Tiny holder for passing the currently-open conversation id between the
 * conversations list screen and the conversation detail screen, since
 * SceneManager.switchTo() only takes an FXML path (no constructor
 * parameters). Set before navigating, read in the destination controllers
 * initialize().
 */
public class NavigationContext {

    private static Long currentConversationId;
    private static String currentConversationTitle;
    private static Long targetAdvertisementId;
    private static String targetSellerUsername;

    private NavigationContext() {
    }

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

    public static Long getTargetAdvertisementId() {
        return targetAdvertisementId;
    }

    public static void setTargetAdvertisementId(Long id) {
        targetAdvertisementId = id;
    }

    public static String getTargetSellerUsername() {
        return targetSellerUsername;
    }

    public static void setTargetSellerUsername(String username) {
        targetSellerUsername = username;
    }
}
