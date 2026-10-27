package io.github.yonecircle.watchtransform;

public record StatusSnapshot(WXStatus status, String message, Throwable cause) {
    
}
