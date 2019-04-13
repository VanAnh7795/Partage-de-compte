package upec.projetandroid20182019.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import upec.projetandroid20182019.model.Balance;
import upec.projetandroid20182019.model.Expense;


public class DBExpense extends SQLiteOpenHelper {

    public static final String TAG = DBExpense.class.getSimpleName();
    public static final String DATABASE_NAME = "expense_list";
    private Context context;

    public static final String TABLE_EXPENSE = "table_expense";
    public static final String ID = "id_expense";
    public static final String ID_PLAN = "id_plan";
    public static final String TITLE = "title";
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";
    public static final String NAME_PARTICIPANT = "name_participant";

    public DBExpense(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
        Log.d(TAG, "DBManager");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE_EXPENSE = "CREATE TABLE " + TABLE_EXPENSE + " (" +
                ID + " INTEGER primary key, " +
                ID_PLAN + " INTERGER, " +
                TITLE + " TEXT, " +
                AMOUNT + " DOUBLE DEFAULT 0," +
                DATE + " TEXT, " +
                NAME_PARTICIPANT + " TEXT )";
        db.execSQL(SQL_CREATE_TABLE_EXPENSE);
        Toast.makeText(context, "Create expense successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        onCreate(db);
        Toast.makeText(context, "Drop successfully", Toast.LENGTH_SHORT).show();
    }

    /*get list Expense by idPlan */
    public List<Expense> getAllExpense(int idPlan) {
        List<Expense> listExpense = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = " SELECT * FROM " + TABLE_EXPENSE + " WHERE " + ID_PLAN + " = " + idPlan + " AND " + AMOUNT + "> 0" ;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(cursor.getInt(0));
                expense.setIdPlan(cursor.getInt(1));
                expense.setTitle(cursor.getString(2));
                expense.setAmount(cursor.getDouble(3));
                expense.setDate(cursor.getString(4));
                expense.setNameParticipant(cursor.getString(5));
                listExpense.add(expense);
            } while (cursor.moveToNext());
        }
        db.close();
        return listExpense;
    }

    public int insertData(int idPlan, String title, Double amount, String date, String nameParticipant) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID_PLAN, idPlan);
        contentValues.put(TITLE, title);
        contentValues.put(AMOUNT, amount);
        contentValues.put(DATE, date);
        contentValues.put(NAME_PARTICIPANT, nameParticipant);
        long result = db.insert(TABLE_EXPENSE, null, contentValues);
        Log.i(TAG, "result : " + result);
        db.close();
        return (int) result;
    }

    //get total amount
    public double getTotal(int idPlan) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT SUM(" + AMOUNT + ") FROM " + TABLE_EXPENSE + " WHERE " + ID_PLAN  +" = " + idPlan, null);
        double amount;
        if(cur.moveToFirst())
            amount = cur.getDouble(0);
        else
            amount = -1;
        cur.close();
        return amount;
    }

    public int insertNameAmount(int idPlan, double amount, String nameParticipant){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_PLAN, idPlan);
        contentValues.put(AMOUNT, amount);
        contentValues.put(NAME_PARTICIPANT, nameParticipant);
        long result = db.insert(TABLE_EXPENSE, null, contentValues);
        Log.i(TAG, "result : " + result);
        db.close();
        return (int) result;
    }

    //get total amount participant
    public List<Balance> getTotalParticipant(int idPlan) {
        DBParticipant dbParticipant = new DBParticipant(context);
        int numberParticipant = dbParticipant.getParticipantCount(idPlan);
        double avg = this.getTotal(idPlan)/(double)numberParticipant;
        List<Balance> balanceList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT " + NAME_PARTICIPANT + ", SUM(" + AMOUNT + ") FROM " + TABLE_EXPENSE + " WHERE " + ID_PLAN  +" = " +idPlan + " GROUP BY " + NAME_PARTICIPANT;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                Balance balance = new Balance();
                balance.setName(cursor.getString(0));
                NumberFormat formatter = new DecimalFormat("#0.00");
                balance.setAmount(Double.parseDouble(formatter.format(cursor.getDouble(1) - avg)));
                balanceList.add(balance);
            } while(cursor.moveToNext());
        }
        db.close();
        return balanceList;
    }

    //deleteExpense by ID expense
    public int deleteExpenseByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_EXPENSE, ID + "=?", new String[]{String.valueOf(id)});
    }

    //delete expense
    public void deleteExpenseByIdPlan(Expense expense){
        int idPlan = expense.getIdPlan();
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSE, ID_PLAN + "=?", new String[]{String.valueOf(idPlan)});
    }

}
