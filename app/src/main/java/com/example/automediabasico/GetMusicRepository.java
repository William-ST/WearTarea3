package com.example.automediabasico;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by William_ST on 05/03/19.
 */

public class GetMusicRepository {

    private final String TAG = GetMusicRepository.class.getCanonicalName();
    private final String URL = "http://storage.googleapis.com/automotive-media/music.json";
    private RequestQueue requestQueue;
    private Gson gson;
    private Musica musica;

    public GetMusicRepository(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
    }

    public void getRepositorioMusical() {
        StringRequest request = new StringRequest(Request.Method.GET, URL, onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            musica = gson.fromJson(response, Musica.class);
            Log.d(TAG, "Número de pistas de audio: " + musica.getMusica().size());
            int slashPos = URL.lastIndexOf('/');
            String path = URL.substring(0, slashPos + 1);

            for (int i = 0; i < musica.getMusica().size(); i++) {
                PistaAudio pista = musica.getMusica().get(i);
                if (!pista.getSource().startsWith("http"))
                    pista.setSource(path + pista.getSource());
                if (!pista.getImage().startsWith("http"))
                    pista.setImage(path + pista.getImage());
                musica.getMusica().set(i, pista);
            }

            if (parseTree != null) {
                parseTree.parse(new TreeMapperMusic(musica));
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, error.toString());
        }
    };

    private ParseTree parseTree;

    public interface ParseTree {
        void parse(TreeMapperMusic treeMapperMusic);
    }

    public void setParseTree(ParseTree parseTree) {
        this.parseTree = parseTree;
    }
}
