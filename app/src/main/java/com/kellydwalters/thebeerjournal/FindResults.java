package com.kellydwalters.thebeerjournal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FindResults extends AppCompatActivity {

    private String apiURL = "http://api.brewerydb.com/v2/search?q=";
    private String apiKey = "key=ba3c926f89626f68edeb102cb57f0a93";
    private String apiSearchType = "&type=beer&";

    private ListView mainListView ;
    private ArrayList<ListItem> item = new ArrayList<>();
    CustomListView customListView;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_results);

        // get the beer name from the intent passed in and set it to the query string
        Intent intent = getIntent();
        String query = intent.getStringExtra("beerName");

        // build the api call using the passed in beername query value
        String url = apiURL + query + apiSearchType +  apiKey;
        FindResults.OkHttpHandler okHttpHandler= new FindResults.OkHttpHandler();

        okHttpHandler.execute(url);
    }

    public class OkHttpHandler extends AsyncTask {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(Object[] params) {
            mainListView = findViewById( R.id.mainListView );

            //builds the http request
            Request.Builder builder = new Request.Builder();
            //builds the url
            builder.url(params[0].toString());
            // sets the request to the built url
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d("KELLLY", o.toString());
            parseResponse(o.toString());
            setClickHandler();

        }
    }

    private void setClickHandler() {
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("KELLLY", "click!!");
                Log.d("TAG", "onItemClick: "+ position);

                Intent resultIntent = new Intent();

                resultIntent.putExtra("name", item.get(position).getName());
                resultIntent.putExtra("abv", item.get(position).getAbv());
                resultIntent.putExtra("description", item.get(position).getDescription());


                if (resultIntent != null) {
                    setResult(RESULT_OK, resultIntent);

                }
                else{
                    Toast.makeText(FindResults.this, "Data was NOT saved successfully", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    private void parseResponse(String response) {
        try{

            // Convert String to json object
            JSONObject reader = new JSONObject(response);

            // Nesting levels of nodes
            // for beer name: ['data'][i]['name']
            // for abv: ['data'][i]['abv']
            // description ['data'][0]['style']['description']
            // image ['data'][i]['labels']['icon']


            String name = "n/a";
            String abv = "n/a";
            String description = "n/a";
            String image = null;

            // get Data json object
            JSONArray jsonData = reader.getJSONArray("data");

            for (int i = 0; i < jsonData.length(); i++) {
                // get the main level of items
                JSONObject c = jsonData.getJSONObject(i);

                if (c.has("name")) {
                    name =  c.getString("name");
                }

                if (c.has("abv")) {
                    abv = c.getString("abv");
                }

                //description is nested
                JSONObject style = c.getJSONObject("style");

                if (style.has("description")) {
                    description = style.getString("description");
                }

                // Label is nested
                if (c.has("labels")) {
                    JSONObject label = c.getJSONObject("labels");
                    image = label.getString("medium");
                }
                else {
                    // grab a no image available url if there isn't one in the json
                    image = "http://tutaki.org.nz/wp-content/uploads/2016/04/no-image-available.png";
                }



                ListItem listItem = new ListItem();

                listItem.setName(name);
                listItem.setAbv(abv);
                listItem.setDescription(description);
                listItem.setImage(image);

                item.add(listItem);

                ArrayList<String> titleList = new ArrayList<String>();
                ArrayList<String> imageList = new ArrayList<String>();
                ArrayList<String> abvList = new ArrayList<String>();

                for(ListItem li: item){
                    titleList.add(li.getName());
                    abvList.add(li.getAbv());
                    imageList.add(li.getImage());
                }

                // Resets the adapter after each request, so that everything is added
                customListView = new CustomListView(FindResults.this, titleList, abvList, imageList);
                mainListView.setAdapter( customListView );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        setResult(this.RESULT_CANCELED);
        Toast.makeText(this, "No data selected", Toast.LENGTH_SHORT).show();


        super.onBackPressed(); //calls this.finish
    }

}
