package tk.pluses.plusesprogress.io;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RequestResult implements Serializable {

    /**
     *
     * */
    public final int CODE;

    /**
     *
     * */
    public final String HOST;

    /**
     *
     * */
    private Map <String, String> FIELDS;

    public RequestResult (int code, String host, Map <String, String> fields) {
        this.CODE = code;
        this.HOST = host;
        this.FIELDS = new HashMap <> (fields);
    }

    public String getField (String key) {
        if (key == null) { return null; }
        return FIELDS.get (key);
    }

}
