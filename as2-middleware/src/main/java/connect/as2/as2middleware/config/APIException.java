package connect.as2.as2middleware.config;

import connect.as2.as2middleware.utils.Response;
import lombok.Getter;

@Getter
public class APIException extends RuntimeException {
    private final Response response;

    public APIException(Response objMessage) {
        super(objMessage.toString());
        this.response = objMessage;
    }

}