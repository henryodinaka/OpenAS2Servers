package connect.as2.as2middleware.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * Created by Odinaka Onah on 01 June, 2021.
 */
@Data
@Slf4j
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
public class Response implements Serializable {

    private String code;
    private String status;
    private Object data;
    @JsonIgnore
    private int httpCode;

    public Response(int httpCode,String code, String status, Object data) {
        this.code = ("200".equals(code) ? "00" : code);
        this.status = status;
        this.data = data;
        this.httpCode = httpCode;
    }
    public Response(int httpCode,String code, String status) {
        this.code = ("200".equals(code) ? "00" : code);
        this.status = status;
        this.httpCode = httpCode;
    }
    public Response(String code, String status, Object data) {
        this.code = ("200".equals(code) ? "00" : code);
        this.status = status;
        this.data = data;
    }
}