package hanu.a2_1901040026.mycart.DB;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040026.mycart.models.Product;

public class ProductCursorWrapper extends CursorWrapper {
    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Product getProduct() {
        int id = getInt(getColumnIndex(DbSchema.ProductsTable.Cols.ID));
         String name = getString(getColumnIndex(DbSchema.ProductsTable.Cols.NAME));
        String thumbnail = getString(getColumnIndex(DbSchema.ProductsTable.Cols.THUMBNAIL));
        int unitPrice = getInt(getColumnIndex(DbSchema.ProductsTable.Cols.PRICE));
        int quantity = getInt(getColumnIndex(DbSchema.ProductsTable.Cols.QUANTITY));


        return new Product(id,name,thumbnail,unitPrice,quantity);
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();

        moveToFirst();
        while (!isAfterLast()) {
            Product product = getProduct();
            products.add(product);

            moveToNext();
        }

        return products;
    }
}
