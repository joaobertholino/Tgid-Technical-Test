package dev.joaobertholino.tgidtechnicaltest.service.exception.exceptiontemplate;

import java.time.LocalDateTime;

public record ExceptionTemplate(LocalDateTime timestamp, Integer status, String error, String message, String path) {
}
