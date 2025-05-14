package io.roam.external.oauth2.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record GoogleUserInfoResponse(
    @JsonProperty("id") String id,
    @JsonProperty("email") String email,
    @JsonProperty("verified_email") Boolean verifiedEmail,
    @JsonProperty("name") String name,
    @JsonProperty("given_name") String givenName,
    @JsonProperty("family_name") String familyName,
    @JsonProperty("picture") String picture
) {
} 