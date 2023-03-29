package hanu.a2_1901040026.mycart;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040026.mycart.DB.CartManager;
import hanu.a2_1901040026.mycart.adapter.AddedAdapter;
import hanu.a2_1901040026.mycart.models.Product;

public class AddToCartActivity extends AppCompatActivity {
     RecyclerView recyclerView;
    AddedAdapter adapter;
    TextView totalOfPrice;
    CartManager cartManager;
    MaterialButton checkout;
    int total= 0;
    public static List<Product> products = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

cartManager = CartManager.getInstance(this);
products = cartManager.all();
adapter = new AddedAdapter(products,this);
        recyclerView = findViewById(R.id.added);

        recyclerView.setLayoutManager(new LinearLayoutManager(AddToCartActivity.this));

        recyclerView.setAdapter(adapter);

checkout= findViewById(R.id.checkout_btn);
        checkout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Toast.makeText(AddToCartActivity.this, "Checkout ok. Thank for Using",Toast.LENGTH_SHORT).show();

    }
});

    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int i=0;i<products.size();i++) {
            total += products.get(i).getUnitPrice() * products.get(i).getQuantity();
        }
        totalOfPrice = findViewById(R.id.priceTotal);
        totalOfPrice.setText( "đ "+ total);
    }

}
