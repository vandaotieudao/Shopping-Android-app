package hanu.a2_1901040026.mycart;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import hanu.a2_1901040026.mycart.adapter.CartAdapter;
import hanu.a2_1901040026.mycart.models.Constants;
import hanu.a2_1901040026.mycart.models.Product;

public class MainActivity extends AppCompatActivity {
    private String url = "https://mpr-cart-api.herokuapp.com/products";
    //private String url = "https://api.json-generator.com/templates/t9IxfDViZwPT/data";
    List<Product> productList = new ArrayList<>();
    RecyclerView recyclerView;
    CartAdapter adapter;
    EditText searchBox;
    ImageView search;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());

        Constants.executor.execute(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        GetData getData = new GetData();
                        getData.execute();

                    }
                });
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar = findViewById(R.id.progressBar);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

        });
            searchBox = findViewById(R.id.searchBox);
            searchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    filter(editable.toString());
                }
            });

    }
//
//    public void onClick(View v) {
//        Intent intent = new Intent(MainActivity.this, AddToCartActivity.class);
//        startActivity(intent);}

private void filter(String text) {
        ArrayList<Product> products = new ArrayList<>();
        for ( Product product : productList){
            if(product.getName().toLowerCase(Locale.ROOT).contains((text.toLowerCase(Locale.ROOT)))){
                products.add(product);
            }
            adapter.filterList(products);
        }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cart_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        super.onOptionsItemSelected(item);
                Intent intent = new Intent(MainActivity.this, AddToCartActivity.class);
                startActivityForResult(intent, 1);
        return super.onOptionsItemSelected(item);
    }
    public class GetData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            return loadJSON(url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            if (loadJSON(url) == null) {
//                return;
            //}
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Product product = new Product();
                    product.setId(object.getInt("id"));
                    product.setName(object.getString("name"));
                    product.setThumbnail(object.getString("thumbnail"));
                    product.setUnitPrice(object.getInt("unitPrice"));


                    productList.add(product);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
                recyclerData(productList);

        }


    }
public void recyclerData(List<Product> productList){

    recyclerView = findViewById(R.id.recyclerView);
    adapter = new CartAdapter(productList, this);

    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

    recyclerView.setAdapter(adapter);

}
    public String loadJSON(String link) {
        URL url;
        HttpURLConnection urlConnection;
        progressBar = findViewById(R.id.progressBar);
        try {
            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            progressBar.setProgress(0);
            InputStream is = urlConnection.getInputStream();
            Scanner sc = new Scanner(is);
            StringBuilder result = new StringBuilder();
            String line;
            progressBar.setProgress(50);
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                result.append(line);
            }
            progressBar.setProgress(100);
            return result.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}