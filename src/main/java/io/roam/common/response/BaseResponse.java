package io.roam.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseResponse {
    @JsonIgnore
    protected final HttpStatus status;
    protected final boolean success;
}