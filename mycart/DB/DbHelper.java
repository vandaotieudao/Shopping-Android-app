package hanu.a2_1901040026.mycart.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "cart.db";
    private static final int DB_VERSION = 1;
    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+ DbSchema.ProductsTable.NAME + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbSchema.ProductsTable.Cols.ID +"INTEGER,"+
                DbSchema.ProductsTable.Cols.THUMBNAIL + " TEXT, " +
                DbSchema.ProductsTable.Cols.NAME + " TEXT, " +
                DbSchema.ProductsTable.Cols.PRICE + " INTEGER, " +
                DbSchema.ProductsTable.Cols.QUANTITY + " INTEGER" + ")");

// other tables here
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("My Cart", "My Cart: upgrading DB; dropping/recreating tables.");
// drop old tables
        db.execSQL("DROP TABLE IF EXISTS "+ DbSchema.ProductsTable.NAME);
// other tables here
// create new tables
        onCreate(db);
    }
}
