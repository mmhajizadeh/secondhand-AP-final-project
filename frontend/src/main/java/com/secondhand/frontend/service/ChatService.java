package com.secondhand.frontend.service;

import com.secondhand.frontend.service.dto.ConversationResponse;
import com.secondhand.frontend.service.dto.MessageResponse;
import com.secondhand.frontend.service.dto.SendMessageRequest;
import com.secondhand.frontend.service.dto.StartConversationRequest;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Wraps the / api / conversations endpoints
 */
public class ChatService {

    public MessageResponse startConversation(StartConversationRequest requestBody) throws ApiException, Exception {
        String json = ApiClient.mapper().writeValueAsString(requestBody);

        HttpRequest request = ApiClient.requestBuilder("/conversations")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = ApiClient.send(request);
        ApiClient.throwIfError(response);

        return ApiClient.mapper().readValue(response.body(), MessageResponse.class);
    }

    public List<ConversationResponse> getMyConversations() throws ApiException, Exception {
        HttpRequest request = ApiClient.requestBuilder("/conversations")
                .GET()
                .build();

        HttpResponse<String> response = ApiClient.send(request);
        ApiClient.throwIfError(response);

        return ApiClient.mapper().readValue(response.body(),
                ApiClient.mapper().getTypeFactory().constructCollectionType(List.class, ConversationResponse.class));
    }

    public List<MessageResponse> getMessages(Long conversationId) throws ApiException, Exception {
        HttpRequest request = ApiClient.requestBuilder("/conversations/" + conversationId + "/messages")
                .GET()
                .build();

        HttpResponse<String> response = ApiClient.send(request);
        ApiClient.throwIfError(response);

        return ApiClient.mapper().readValue(response.body(),
                ApiClient.mapper().getTypeFactory().constructCollectionType(List.class, MessageResponse.class));
    }

    public MessageResponse sendMessage(Long conversationId, SendMessageRequest requestBody) throws ApiException, Exception {
        String json = ApiClient.mapper().writeValueAsString(requestBody);

        HttpRequest request = ApiClient.requestBuilder("/conversations/" + conversationId + "/messages")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = ApiClient.send(request);
        ApiClient.throwIfError(response);

        return ApiClient.mapper().readValue(response.body(), MessageResponse.class);
    }
}
