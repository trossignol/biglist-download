package fr.rossi.biglistdownload.util;

import io.smallrye.mutiny.Multi;

// see https://github.com/quarkusio/quarkus/issues/26253
public class HackIssue26253 {
    private static final String ERROR_KEY = "ERROR: ";
    private static final String END_KEY = "<< END OF STREAM >>";
    private final Multi<String> multi;

    public HackIssue26253(Multi<String> multi) {
        this.multi = multi;
    }

    public Multi<String> producesSSE() {
        return this.multi
                // Hack for JS EventSource which not close itself
                .onCompletion().continueWith(END_KEY)
                // Hack for issue 26253 of Quarkus
                .onFailure().recoverWithItem(error -> ERROR_KEY + error);
    }

    public Multi<String> consumesSSE() {
        return this.multi
                .filter(value -> !END_KEY.equals(value))
                .map(SseConsumerException::checkMessage);
    }

    public static class SseConsumerException extends RuntimeException {
        private SseConsumerException(String message) {
            super("Error consuming SSE: " + message);
        }

        private static String checkMessage(String value) {
            if (value.startsWith(ERROR_KEY)) {
                throw new SseConsumerException(value.substring(ERROR_KEY.length()));
            }
            return value;
        }
    }
}
