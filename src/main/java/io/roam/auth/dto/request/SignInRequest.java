package io.roam.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignInRequest(
    @NotBlank(message = "아이디는 필수 항목입니다")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,16}$", message = "아이디는 4~16자의 영문 대소문자와 숫자만 가능합니다")
    String userId,
    
    @NotBlank(message = "비밀번호는 필수 항목입니다")
    String password
) {
}
