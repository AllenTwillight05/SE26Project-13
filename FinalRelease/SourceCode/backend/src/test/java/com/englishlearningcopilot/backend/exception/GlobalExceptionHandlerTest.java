package com.englishlearningcopilot.backend.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.englishlearningcopilot.backend.dto.ErrorResponse;
import com.englishlearningcopilot.backend.service.speech.xfyun.XfyunAsrException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        when(request.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    void mapsApplicationExceptionsToExpectedStatuses() {
        assertError(handler.handleBadRequest(new BadRequestException("bad"), request), HttpStatus.BAD_REQUEST, "bad");
        assertError(handler.handleConflict(new ConflictException("conflict"), request), HttpStatus.CONFLICT, "conflict");
        assertError(handler.handleNotFound(new ResourceNotFoundException("missing"), request), HttpStatus.NOT_FOUND, "missing");
    }

    @Test
    void mapsSecurityExceptionsToUnauthorizedAndForbidden() {
        assertError(handler.handleUnauthorized(new BadCredentialsException("login"), request), HttpStatus.UNAUTHORIZED, "login");
        assertError(handler.handleForbidden(new AccessDeniedException("denied"), request), HttpStatus.FORBIDDEN, "denied");
    }

    @Test
    void mapsRequestFormatExceptions() {
        assertError(
                handler.handleBadRequest(new IllegalArgumentException("illegal"), request),
                HttpStatus.BAD_REQUEST,
                "illegal"
        );
        assertError(
                handler.handleUnsupportedMediaType(new HttpMediaTypeNotSupportedException("text/plain"), request),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Unsupported request content type."
        );
        assertError(
                handler.handleMalformedRequest(new MultipartException("bad multipart"), request),
                HttpStatus.BAD_REQUEST,
                "Malformed request body."
        );
    }

    @Test
    void mapsSpeechAndGenericExceptions() {
        assertError(
                handler.handleSpeechRecognition(new XfyunAsrException("asr failed"), request),
                HttpStatus.BAD_GATEWAY,
                "asr failed"
        );

        when(request.getMethod()).thenReturn("GET");
        assertError(
                handler.handleGeneric(new RuntimeException("boom"), request),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected server error."
        );
    }

    private static void assertError(
            org.springframework.http.ResponseEntity<ErrorResponse> response,
            HttpStatus status,
            String message
    ) {
        assertThat(response.getStatusCode()).isEqualTo(status);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(status.value());
        assertThat(response.getBody().message()).isEqualTo(message);
        assertThat(response.getBody().path()).isEqualTo("/api/test");
    }
}
