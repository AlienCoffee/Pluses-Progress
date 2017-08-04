package tk.pluses.plusesprogress.io;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RequestResult implements Serializable {

    /**
     * Response code of request.
     *
     * Read about HTTP response codes (it's similar to them)
     * */
    public final int CODE;

    /**
     * String address of host where request was sent.
     * */
    public final String HOST;

    /**
     * All fields that was received from server answer.
     *
     * Guaranteed that there are fields <i>code<i/>,
     * <i>message</i>, <i>comment</i> and <i>cause</i>
     * */
    private Map <String, String> FIELDS;

    public RequestResult (int code, String host, Map <String, String> fields) {
        this.CODE = code;
        this.HOST = host;
        this.FIELDS = new HashMap <> (fields);
    }

    /**
     * Get field value by key.
     *
     * If key doesn't exist or null
     * then will be returned null
     * */
    public String getField (String key) {
        if (key == null) { return null; }
        return FIELDS.get (key);
    }

}
