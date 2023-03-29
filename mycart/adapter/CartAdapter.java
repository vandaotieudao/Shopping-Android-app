package hanu.a2_1901040026.mycart.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040026.mycart.DB.CartManager;
import hanu.a2_1901040026.mycart.DB.DbHelper;
import hanu.a2_1901040026.mycart.R;
import hanu.a2_1901040026.mycart.models.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private List<Product> productList;
    private  Context context;

    List<Product> products = new ArrayList<>();
    CartManager cartManager;
    public CartAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.product_item,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,  int position) {
        Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.price.setText("đ " + String.valueOf(product.getUnitPrice()));
        ImageLoad load = new ImageLoad(productList.get(position).getThumbnail(), holder.thumbnail);
        load.execute();
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addprotuctToCart(product,context);
                Toast.makeText(context, "Successfully added. Please check on Cart",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void filterList(ArrayList<Product> products){
        productList = products;
        notifyDataSetChanged();
    }
    public  static  class MyViewHolder extends  RecyclerView.ViewHolder{
            TextView name;
            TextView price;
            ImageView thumbnail;
            ImageButton add;
        Context context;
       List<Product> products;
       CartManager cartManager;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.added_thumbnail);
            name = itemView.findViewById(R.id.added_name);
            price = itemView.findViewById(R.id.added_price);
            add = itemView.findViewById(R.id.add);
//            cartManager = cartManager.getInstance(context);
//            products = cartManager.all();
        }
    }


    public class ImageLoad extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoad(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }
    private boolean addprotuctToCart(Product product, Context mContext) {
        boolean isAdded = false;
        int pos = -1;
        //save in db
        //connect db
        DbHelper orderHelper = new DbHelper(mContext);
        SQLiteDatabase db = orderHelper.getWritableDatabase();
        // manipulate db

        String Query = "Select * from cart where id = " + product.getId();
        Cursor cursor = db.rawQuery(Query,null);
        if(cursor.getCount() <= 0) {
            cursor.close();
        } else {
            isAdded=true;
            cursor.close();
        }

//        for( int i =0; i< orders.size();i++){
//            if(orders.get(i).getId()== product.getId()) {
//                pos=i;
//                Log.i("Trung`","Trung`");
//                isAdded = true;
//                //thuc hien update
//            }
//        }
        if (!isAdded) {
            Product order = new Product(product.getId(),product.getName(),product.getThumbnail(),product.getUnitPrice(),1);
            products.add(order);

            String sql = "INSERT INTO cart(id,name,thumbnail, Unitprice, quantity) VALUES (?,?, ?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sql);
            // bind params

            statement.bindLong(1,product.getId());
            statement.bindString(2, product.getName());

            statement.bindString(3, product.getThumbnail());
            //!TODO : CHECK LAI CAI PRICE NAY
            statement.bindDouble(4,product.getUnitPrice());
//     !TODO   if() -> chinh lai cai quantity nay ( ca 3 cai tren)
            statement.bindLong(5,1);

            // run query
            int id =(int) statement.executeInsert(); // auto generated id
            // close connection
            return id > 0;
        } else {
            String strUpdate = "UPDATE cart SET quantity = quantity +1 WHERE id = "+ product.getId();
            db.execSQL(strUpdate);
        }

        db.close();

        // create statement

        return true;
    }

}
