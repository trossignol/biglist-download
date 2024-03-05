package fr.rossi.biglistdownload.util;

import io.smallrye.mutiny.Multi;

// see https://github.com/quarkusio/quarkus/issues/26253
public class HackIssue26253 {
    private static final String ERROR_KEY = "ERROR: ";
    private final Multi<String> multi;

    public HackIssue26253(Multi<String> multi) {
        this.multi = multi;
    }

    public Multi<String> producesSSE() {
        return this.multi
                .onCompletion().continueWith("<< END OF STREAM >>")
                .onFailure().recoverWithItem(error -> ERROR_KEY + error);
    }

    public Multi<String> consumesSSE() {
        return this.multi.invoke(value -> {
            if (value != null && value.startsWith(ERROR_KEY)) {
                throw new RuntimeException("Error consuming SSE: " + value.substring(ERROR_KEY.length()));
            }
        });
    }
}
