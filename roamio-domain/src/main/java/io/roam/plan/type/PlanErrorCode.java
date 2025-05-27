package io.roam.plan.type;

import org.springframework.http.HttpStatus;

import io.roam.common.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlanErrorCode implements ErrorCode {
    PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "PLAN_4041", "계획을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "PLAN_4042", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_INVITED(HttpStatus.CONFLICT, "PLAN_4091", "이미 초대된 사용자입니다."),
    ONLY_PLAN_OWNER_CAN_INVITE(HttpStatus.FORBIDDEN, "PLAN_4031", "계획 소유자만 초대할 수 있습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
