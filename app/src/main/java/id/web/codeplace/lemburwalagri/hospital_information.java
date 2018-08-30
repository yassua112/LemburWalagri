package id.web.codeplace.lemburwalagri;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class hospital_information extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get hospital JSON
    private static String url = "https://api.myjson.com/bins/cr8ic";
    ArrayList<HashMap<String,String>> hospitalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_information);

        hospitalList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetHospitals().execute();
    }

    // Bagian ListView data Rumah Sakit dari parsing JSON

    private class GetHospitals extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(hospital_information.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray hospitals= jsonObj.getJSONArray("hospitals");

                    // looping through All Hospitals
                    for (int i = 0; i < hospitals.length(); i++) {
                        JSONObject c = hospitals.getJSONObject(i);

                        String hosp_id = c.getString("hosp_id");
                        String hosp_name = c.getString("hosp_name");
                        String hosp_address = c.getString("hosp_address");

                        // Phone node is JSON Object
                        JSONObject hosp_phone = c.getJSONObject("hosp_phone");
                        String office = hosp_phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> hospital = new HashMap<>();

                        // adding each child node to HashMap key => value
                        hospital.put("hosp_id", hosp_id);
                        hospital.put("hosp_name", hosp_name);
                        hospital.put("hosp_address", hosp_address);
                        hospital.put("office", office);

                        // adding hospital to hospital list
                        hospitalList.add(hospital);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    hospital_information.this, hospitalList,
                    R.layout.list_item, new String[]{"hosp_name", "hosp_address",
                    "office"}, new int[]{R.id.hosp_name,
                    R.id.hosp_address, R.id.office});

            lv.setAdapter(adapter);
        }

    }

    //Akhir bagian ListView data Rumah Sakit parsing JSON
}
