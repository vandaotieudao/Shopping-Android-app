package hanu.a2_1901040026.mycart.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import hanu.a2_1901040026.mycart.AddToCartActivity;
import hanu.a2_1901040026.mycart.DB.DbHelper;
import hanu.a2_1901040026.mycart.R;
import hanu.a2_1901040026.mycart.models.Product;

public class AddedAdapter extends RecyclerView.Adapter<AddedAdapter.AddedHolder> {

    public List<Product> addedList;
    private Context context;
int total =0 ;
    public AddedAdapter(List<Product> addedList, Context context) {
        this.addedList = addedList;
        this.context = context;
    }
    public AddedAdapter(List<Product> addedList) {
        this.addedList = addedList;
    }

    @NonNull
    @Override
    public AddedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.added_item,parent, false);
        return new AddedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedHolder holder, int position) {
       holder.bind(addedList.get(position));

    }

    @Override
    public int getItemCount() {
        return addedList.size();
    }

    public  class AddedHolder extends RecyclerView.ViewHolder{
        TextView added_name, added_price, quantity, unitTotal;
        TextView totalPrice = ((AddToCartActivity) context).findViewById(R.id.priceTotal);
        ImageButton plus, minus;
        ImageView added_thumbnail;

        public AddedHolder(@NonNull View itemView) {
            super(itemView);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            quantity = itemView.findViewById(R.id.quantity);
            unitTotal= itemView.findViewById(R.id.unitTotal);
            added_thumbnail = itemView.findViewById(R.id.added_thumbnail);
            added_name = itemView.findViewById(R.id.added_name);
            added_price = itemView.findViewById(R.id.added_price);
        }

        public void bind(Product product) {
            added_name.setText(product.getName());
            added_price.setText("đ " +product.getUnitPrice());

            ImageLoad load = new ImageLoad(product.getThumbnail(),added_thumbnail);
            load.execute();
            quantity.setText(String.valueOf(product.getQuantity()));
            int quantityInt  = Integer.parseInt(quantity.getText().toString());
            int price = product.getUnitPrice();
            int unitTotal1 = price * quantityInt;
            unitTotal.setText(String.valueOf(unitTotal1));

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DbHelper dbHelper = new DbHelper(context);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    String update = "UPDATE cart SET quantity = quantity +1 WHERE id = "+ product.getId();
                    product.setQuantity(product.getQuantity()+1);
                    db.execSQL(update);
                    notifyDataSetChanged();
                    db.close();
                        total =0;
                    for(int i = 0 ; i < addedList.size(); i++) {

                        total += addedList.get(i).getUnitPrice() * addedList.get(i).getQuantity();
                    }
                    totalPrice.setText("đ̳ "+total);
                }
            });
minus.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if(product.getQuantity()==1) {
            String strDelete ="DELETE from cart WHERE id= "+product.getId();
            db.execSQL(strDelete);
            addedList.remove(product);
            notifyDataSetChanged();
            db.close();
        } else {
            product.setQuantity(product.getQuantity()-1);
            String update = "UPDATE cart SET quantity = quantity -1 WHERE id = "+ product.getId();
            db.execSQL(update);
            notifyDataSetChanged();
            db.close();
        }
        total = 0;
        for(int i = 0 ; i < addedList.size(); i++) {

            total += addedList.get(i).getUnitPrice() * addedList.get(i).getQuantity();
        }
        totalPrice.setText("đ̳ "+total);
    }
});
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


}
