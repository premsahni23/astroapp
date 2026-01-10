
package com.dekhokaun.mindarobackend.exception;

import lombok.Getter;

@Getter
public class ServiceUnavailableException extends RuntimeException {

    private final String serviceName;
    private final String alternativeSuggestion;

    public ServiceUnavailableException(String serviceName, String message) {
        super(message);
        this.serviceName = serviceName;
        this.alternativeSuggestion = null;
    }

    public ServiceUnavailableException(String serviceName, String message, String alternativeSuggestion) {
        super(message);
        this.serviceName = serviceName;
        this.alternativeSuggestion = alternativeSuggestion;
    }

}
