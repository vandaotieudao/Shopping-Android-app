package hanu.a2_1901040026.mycart.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import hanu.a2_1901040026.mycart.models.Product;

public class CartManager {

    private static CartManager instance;

    private static final String INSERT_STMT =
            "INSERT INTO " + DbSchema.ProductsTable.NAME + "(id, name,thumbnail, price, quantity) VALUES (?,?, ?, ?, ?)";

    private static final String UPDATE_STMT =
            "UPDATE " + DbSchema.ProductsTable.NAME + " SET " + DbSchema.ProductsTable.Cols.QUANTITY + "= ? WHERE id = ?";
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    public static CartManager getInstance(Context context) {
        if (instance == null) {
            instance = new CartManager(context);
        }

        return instance;
    }

    private CartManager(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    public List<Product> all() {
        String sql = "SELECT * FROM " + DbSchema.ProductsTable.NAME + " GROUP BY " + DbSchema.ProductsTable.Cols.NAME;
        Cursor cursor = db.rawQuery(sql, null);
        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);

        return cursorWrapper.getProducts();
    }

    public boolean add(Product product) {
        SQLiteStatement statement = db.compileStatement(INSERT_STMT);

        statement.bindString(1, product.getThumbnail());
        statement.bindString(2, product.getName());
        statement.bindLong(3, (long) product.getUnitPrice());
        statement.bindLong(4, (long) (product.getQuantity()));

        int id = (int) statement.executeInsert();
        // a
        if (id > 0) {
            product.setId(id);
            return true;
        }

        return false;
    }

    public boolean update (Product product) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbSchema.ProductsTable.Cols.QUANTITY, product.getQuantity());
        long result = db.update(DbSchema.ProductsTable.NAME, contentValues, "id=?", new String[]{product.getId() + ""});
        return result > 0;

    }

    public boolean delete(long id) {
        int result = db.delete(DbSchema.ProductsTable.NAME, "id = ?", new String[]{ id+"" });

        return result > 0;
    }

}
