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
public class NotifyFailureApiResponse implements NotifyApiResponse {
    @JsonProperty("error")
    private NotifyFailureApiResponseData notifyFailureApiResponseData;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NotifyFailureApiResponseData {
        @JsonProperty("code")
        private String code;
        @JsonProperty("message")
        private String message;
    }
}
