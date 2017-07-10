package tk.pluses.plusesprogress.io;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClients;

public class RequestIO extends AsyncTaskLoader<RequestResult> {

    private final RequestForm FORM;

    public RequestIO (Context context, RequestForm form) {
        super (context);
        this.FORM = form;
    }

    @Override
    protected void onStartLoading () {
        forceLoad ();
    }

    @Override
    public RequestResult loadInBackground () {
        try {
            HttpClient client = HttpClients.createDefault ();
            HttpPost post = new HttpPost (FORM.HOST);
            post.setEntity (new UrlEncodedFormEntity (FORM.getParams (), "UTF-8"));

            HttpResponse response = client.execute (post);
            InputStream is = response.getEntity ().getContent ();
            InputStreamReader isr = new InputStreamReader (is, "UTF-8");
            BufferedReader br = new BufferedReader (isr);
            Map <String, String> answer = parse (br.readLine ());

            String code = answer.get ("code");
            int responseCode = 0;
            if (code != null && code.length () > 0) {
                try {
                    responseCode = Integer.parseInt (code);
                } catch (Exception e) {}
            }

            return new RequestResult (responseCode, FORM.HOST, answer);
        } catch (UnsupportedEncodingException uee) {
            // Just handle exception
        } catch (IOException ioe) {
            // Just handle exception
        }

        return null;
    }

    private Map <String, String> parse (String json) {
        Map <String, String> answer = new HashMap <> ();

        try {
            JSONObject root = new JSONObject (json);
            Iterator <String> keys = root.keys ();
            while (keys.hasNext ()) {
                String key = keys.next ();
                answer.put (key, root.getString (key));
            }
        } catch (JSONException e) {
            answer.put ("code", "0");
        }

        return answer;
    }

}
