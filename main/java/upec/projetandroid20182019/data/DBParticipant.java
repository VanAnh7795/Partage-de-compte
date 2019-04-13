package upec.projetandroid20182019.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import upec.projetandroid20182019.model.Participant;


public class DBParticipant extends SQLiteOpenHelper{

    public static final String TAG = DBParticipant.class.getSimpleName();
    public static final String DATABASE_NAME = "participant_list";
    public static final String TABLE_PARTICIPANT = "participants";
    public static final String ID_PARTICIPANT = "id_participant";
    public static final String NAME = "name";
    public static final String ID_PLAN = "Plan_id";

    private Context context;

    public DBParticipant(Context context) {
        super(context, DATABASE_NAME,null, 1);
        Log.d(TAG, "DBManager: ");
        this.context = context;
    }

    //create table participant
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " +TABLE_PARTICIPANT +" (" +
               ID_PARTICIPANT + " integer primary key , " +
                NAME + " TEXT, " +
                ID_PLAN + " INTEGER NOT NULL)";
        db.execSQL(sqlQuery);
        Toast.makeText(context, "Create successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PARTICIPANT);
        onCreate(db);
        Toast.makeText(context, "Drop successfully", Toast.LENGTH_SHORT).show();
    }

    //Get all list participant
    public List<Participant> getAllParticipant(int idPlan) {
        List<Participant> listParticipant = new ArrayList<Participant>();

        String selectQuery = "SELECT  * FROM " + TABLE_PARTICIPANT + " WHERE " + ID_PLAN + " = " + idPlan;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Participant participant = new Participant();
                participant.setId(cursor.getInt(0));
                participant.setName(cursor.getString(1));
                participant.setPlanid(cursor.getInt(2));
                listParticipant.add(participant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listParticipant;
    }

    //insert data
    public void insertParticipant(ArrayList<String> listParticipant, int idPlan){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try{
        for (String name:listParticipant){
            cv.put(NAME, name);
            cv.put(ID_PLAN, idPlan);
            db.insertOrThrow(TABLE_PARTICIPANT, null, cv);
            }
        } catch (Exception e){
            Log.e("Problem",e+"");
        }
        db.close();
    }

    //delete participant by idPlan
    public void deleteParticipantByIdPlan(Participant participant){
        int idPlan = participant.getPlanid();
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PARTICIPANT, ID_PLAN + " = " + idPlan, null);
    }

    //number of participant
    public int getParticipantCount(int idPlan) {
        String countQuery = " SELECT "+ NAME +" FROM " + TABLE_PARTICIPANT + " WHERE " + ID_PLAN + " = " + idPlan ;;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
