package de.arthurpicht.utils.pdfbox2;

public class UtilsPdfboxException extends Exception {

    public UtilsPdfboxException() {
    }

    public UtilsPdfboxException(String message) {
        super(message);
    }

    public UtilsPdfboxException(String message, Throwable cause) {
        super(message, cause);
    }

    public UtilsPdfboxException(Throwable cause) {
        super(cause);
    }

    public UtilsPdfboxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
