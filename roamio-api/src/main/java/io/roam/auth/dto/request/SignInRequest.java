package io.roam.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SignInRequest(
    @NotNull(message = "아이디는 필수 항목입니다")
    @NotBlank(message = "아이디는 필수 항목입니다")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,16}$", message = "아이디는 4~16자의 영문 대소문자와 숫자만 가능합니다")
    String userId,

    @NotNull(message = "비밀번호는 필수 항목입니다")
    @NotBlank(message = "비밀번호는 필수 항목입니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자 이상이며, 하나 이상의 문자, 숫자, 특수 문자를 포함해야 합니다")
    String password
) {
}
