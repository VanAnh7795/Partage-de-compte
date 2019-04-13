package upec.projetandroid20182019.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import upec.projetandroid20182019.model.Plan;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DBPlan extends SQLiteOpenHelper{

    public static final String TAG = DBPlan.class.getSimpleName();
    public static final String DATABASE_NAME = "plan_list";
    public static final String TABLE_PLAN = "table_plan";
    public static final String ID ="id";
    public static final String TITLE ="title";
    public static final String DESCRIPTION ="description";
    public static final String CURRENCY ="currency";

    private Context context;

    public DBPlan(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
        Log.d(TAG,"DBManager");
    }

    //create table plan
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE_PLAN  = "CREATE TABLE " +TABLE_PLAN +" (" +
                ID + " INTEGER primary key, " +
                TITLE + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                CURRENCY + " TEXT)";
        db.execSQL(SQL_CREATE_TABLE_PLAN);
        Toast.makeText(context, "Create successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PLAN);
        onCreate(db);
        Toast.makeText(context, "Drop successfully", Toast.LENGTH_SHORT).show();
    }

    /*get values from database*/
    public List<Plan> getAllPlan(){
        List<Plan> listPlan = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = " SELECT * FROM " + TABLE_PLAN;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                Plan plan = new Plan();
                plan.setId(cursor.getInt(0));
                plan.setTitle(cursor.getString(1));
                plan.setDescription(cursor.getString(2));
                plan.setCurrency(cursor.getString(3));
                listPlan.add(plan);
            }while(cursor.moveToNext());
        }
        db.close();
        return listPlan;
    }

    //get plan by idPlan
    public Plan getPlanByID(int planId){
        SQLiteDatabase db = this.getWritableDatabase();
        Plan plan = new Plan();
        String selectQuery = " SELECT * FROM " + TABLE_PLAN + " WHERE " + ID + "="  + planId;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        plan.setId(cursor.getInt(0));
        plan.setTitle(cursor.getString(1));
        plan.setDescription(cursor.getString(2));
        plan.setCurrency(cursor.getString(3));
        cursor.close();
        return plan;
    }

    //delete plan by idPlan
    public int deletePlanByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_PLAN, ID + "=?", new String[]{String.valueOf(id)});
        return (int) result;
    }

    //insert data into plan_table
    public int insertData(String title, String description, String currency) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, title);
        contentValues.put(DESCRIPTION, description);
        contentValues.put(CURRENCY, currency);
        long result = db.insert(TABLE_PLAN, null, contentValues);
        Log.i(TAG, "result : " + result);
        db.close();
        return (int) result;
    }

}
