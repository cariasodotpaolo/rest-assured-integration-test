package mp.cariaso.springboot.api.exception;

public class UnauthorizedException extends Exception {

    public UnauthorizedException(String message) {
        super(message);
    }
}
