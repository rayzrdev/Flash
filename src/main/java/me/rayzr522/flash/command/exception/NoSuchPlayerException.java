package me.rayzr522.flash.command.exception;

public class NoSuchPlayerException extends RuntimeException {
    private final String username;

    public NoSuchPlayerException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
