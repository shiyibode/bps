package org.nmgns.bps.system.utils.exceptions;

import java.util.List;

public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;
    private List<String> details; // For validation errors

    public ErrorResponse(int status, String message, long timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ErrorResponse(int status, String message, long timestamp, List<String> details) {
        this(status, message, timestamp);
        this.details = details;
    }

    // Getters and setters
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<String> getDetails() {
        return details;
    }
}
