package tk.pluses.plusesprogress.io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class RequestForm implements Serializable {

    /**
     *
     * */
    public final String HOST;

    /**
     *
     * */
    private final List <NameValuePair> PARAMS;

    public RequestForm (String host) {
        if (host == null || host.length () == 0) {
            throw new IllegalArgumentException ("Host string can't be empty");
        }

        this.HOST = host;
        this.PARAMS = new ArrayList <> ();
    }

    public void addParam (String key, String value) {
        if (key == null) { return; }
        PARAMS.add (new BasicNameValuePair (key, value));
    }

    public void removeParam (String key) {
        if (key == null) { return; }

        int index = -1, pointer = 0;
        for (NameValuePair pair : PARAMS) {
            if (pair.getName ().equals (key)) {
                index = pointer;
                break;
            }

            pointer ++;
        }

        if (index != -1) { PARAMS.remove (index); }
    }

    public List <NameValuePair> getParams () {
        return new ArrayList <> (PARAMS);
    }

}
