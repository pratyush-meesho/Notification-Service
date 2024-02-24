package com.meesho.notificationserver.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meesho.notificationserver.payload.response.NotifyApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotifySuccessApiResponse implements NotifyApiResponse {
    @JsonProperty("data")
    NotifySuccessApiResponseData notifySuccessApiResponseData;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class NotifySuccessApiResponseData {
        @JsonProperty("requestId")
        private String requestId;
        @JsonProperty("comments")
        private String comments;

    }
}
