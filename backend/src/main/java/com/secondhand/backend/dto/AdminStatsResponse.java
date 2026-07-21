package com.secondhand.backend.dto;

public class AdminStatsResponse {
    private long totalUsers;
    private long activeUsers;
    private long blockedUsers;
    private long totalConversations;
    private long totalRatings;

    public AdminStatsResponse() {
    }

    public AdminStatsResponse(long totalUsers, long activeUsers, long blockedUsers, long totalConversations, long totalRatings) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.blockedUsers = blockedUsers;
        this.totalConversations = totalConversations;
        this.totalRatings = totalRatings;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(long activeUsers) {
        this.activeUsers = activeUsers;
    }

    public long getBlockedUsers() {
        return blockedUsers;
    }

    public void setBlockedUsers(long blockedUsers) {
        this.blockedUsers = blockedUsers;
    }

    public long getTotalConversations() {
        return totalConversations;
    }

    public void setTotalConversations(long totalConversations) {
        this.totalConversations = totalConversations;
    }

    public long getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(long totalRatings) {
        this.totalRatings = totalRatings;
    }
}
