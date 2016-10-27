package com.gluonhq.codevault.git;

public class GitRepoException extends RuntimeException {

    public GitRepoException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitRepoException(String message) {
        super(message);
    }

    public GitRepoException(Throwable cause) {
        super(cause);
    }
}
