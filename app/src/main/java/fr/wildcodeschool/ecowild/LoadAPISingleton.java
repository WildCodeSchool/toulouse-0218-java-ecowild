package fr.wildcodeschool.ecowild;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by bastienwcs on 26/04/18.
 */

public class LoadAPISingleton {

    private static LoadAPISingleton sInstance = null;
    private ArrayList<ClusterModel> clusterList = new ArrayList<>();
    private boolean mGlassAPILoaded = false;
    private boolean mPaperAPILoaded = false;

    public LoadAPISingleton() {
    }

    public static LoadAPISingleton getInstance() {
        if (sInstance == null) {
            sInstance = new LoadAPISingleton();
        }
        return sInstance;

    }

    public ArrayList<ClusterModel> getClusterList() {
        return clusterList;
    }

    public void loadAPIs(Context context, final LoadAPIListener listener) {
        // Crée une file d'attente pour les requêtes vers l'API
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String urlGlass = "https://data.toulouse-metropole.fr/api/records/1.0/search/?dataset=recup-verre&refine.commune=TOULOUSE&rows=1600";

        // Création de la requête vers l'API, ajout des écouteurs pour les réponses et erreurs possibles
        JsonObjectRequest jsonObjectRequestGlass = new JsonObjectRequest(
                Request.Method.GET, urlGlass, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray records = response.getJSONArray("records");

                            for (int c = 0; c < records.length(); c++) {
                                JSONObject recordslist = records.getJSONObject(c);
                                JSONObject geometry = recordslist.getJSONObject("geometry");
                                JSONObject location = recordslist.getJSONObject("fields");
                                String address = location.getString("adresse");

                                JSONArray coordinate = geometry.getJSONArray("coordinates");
                                String abs = coordinate.getString(0);
                                String ordo = coordinate.getString(1);
                                double valueAbs = Double.parseDouble(abs);
                                double valueOrdo = Double.parseDouble(ordo);
                                String type = "Verre";
                                String id = "v" + c;

                                //Cluster et Liste de celui ci pour aller aussi en ArrayList
                                ClusterModel cluster = new ClusterModel(valueOrdo, valueAbs, address, type);
                                clusterList.add(cluster);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mGlassAPILoaded = true;
                        if (mPaperAPILoaded) {
                            listener.onBothAPILoaded();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Afficher l'erreur
                        Log.d("VOLLEY_ERROR", "onErrorResponse: " + error.getMessage());
                    }
                }
        );

        // On ajoute la requête à la file d'attente
        requestQueue.add(jsonObjectRequestGlass);

        /** Partie Json Papier/plastique**/
        // Crée une file d'attente pour les requêtes vers l'API

        String urlPaper = "https://data.toulouse-metropole.fr/api/records/1.0/search/?dataset=recup-emballage&refine.commune=TOULOUSE&rows=300";

        // Création de la requête vers l'API, ajout des écouteurs pour les réponses et erreurs possibles
        JsonObjectRequest jsonObjectRequestPaper = new JsonObjectRequest(
                Request.Method.GET, urlPaper, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray records = response.getJSONArray("records");

                            for (int c = 0; c < records.length(); c++) {
                                JSONObject recordslist = records.getJSONObject(c);
                                JSONObject geometry = recordslist.getJSONObject("geometry");
                                JSONObject location = recordslist.getJSONObject("fields");
                                String address = location.getString("adresse");
                                //convert coordinates
                                JSONArray coordinate = geometry.getJSONArray("coordinates");
                                String abs = coordinate.getString(0);
                                String ordo = coordinate.getString(1);
                                double valueAbs = Double.parseDouble(abs);
                                double valueOrdo = Double.parseDouble(ordo);
                                String type = "Papier/Plastique";
                                //Cluster et Liste de celui ci pour aller aussi en ArrayList
                                ClusterModel cluster = new ClusterModel(valueOrdo, valueAbs, address, type);
                                clusterList.add(cluster);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mPaperAPILoaded = true;
                        if (mGlassAPILoaded) {
                            listener.onBothAPILoaded();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Afficher l'erreur
                        Log.d("VOLLEY_ERROR", "onErrorResponse: " + error.getMessage());
                    }
                }
        );

        // On ajoute la requête à la file d'attente
        requestQueue.add(jsonObjectRequestPaper);
    }

    public interface LoadAPIListener {

        void onBothAPILoaded();
    }
}
