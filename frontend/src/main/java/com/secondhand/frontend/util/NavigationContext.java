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
}
