package Main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(Context context) {
        super(context, "userdetails", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE `userdetails` (\n" +
                "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`name`\tVARCHAR(45),\n" +
                "\t`pickuppalce`\tVARCHAR(45),\n" +
                "\t`endpalce`\tVARCHAR(45),\n" +
                "\t`company`\tVARCHAR(45),\n" +
                "\t`occupation`\tVARCHAR(45),\n" +
                "\t`address`\tVARCHAR(200),\n" +
                "\t`nic`\tVARCHAR(45),\n" +
                "\t`mobile`\tVARCHAR(45),\n" +
                "\t`details`\tVARCHAR(45)\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE userdetails");
        onCreate(db);


    }
}
