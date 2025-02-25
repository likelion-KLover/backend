package team.klover.server.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReturnCode {
    SUCCESS("0000", "SUCCESS_MESSAGE"),

    WRONG_PARAMETER("4000", "WRONG_PARAMETER_MESSAGE"),
    NOT_FOUND_ENTITY("4001", "NOT_FOUND_ENTITY_MESSAGE"),
    ALREADY_EXIST("4002", "ALREADY_EXIST_MESSAGE"),
    WRONG_PASSWORD("4003", "WRONG_PASSWORD_MESSAGE"),
    NOT_AUTHORIZED("4004", "NOT_AUTHORIZED_MESSAGE"),
    EXPIRED_TOKEN("4005", "EXPIRED_TOKEN_MESSAGE"),
    INVALID_REQUEST("4006", "INVALID_REQUEST_MESSAGE"),

    INTERNAL_ERROR("5000", "INTERNAL_ERROR_MESSAGE");
    private String returnCode;
    private String returnMessage;
}