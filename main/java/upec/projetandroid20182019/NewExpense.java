package upec.projetandroid20182019;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import upec.projetandroid20182019.adapter.CustomAdapterBalance;
import upec.projetandroid20182019.data.DBExpense;
import upec.projetandroid20182019.data.DBParticipant;
import upec.projetandroid20182019.data.DBPlan;
import upec.projetandroid20182019.model.Participant;
import upec.projetandroid20182019.model.Plan;

public class NewExpense extends AppCompatActivity {
    public static final String ID_PLAN_EXPENSE = "ID_PLAN_EXPENSE";

    private int idPlan;
    private DBPlan dbPlan;
    private DBParticipant dbParticipant;
    private DBExpense dbExpense;
    private TextView currency;
    private EditText editTitle, editDate, editAmount;
    private TextInputLayout layoutTitle, layoutAmount;
    private AutoCompleteTextView actvPaidBy;
    private ImageView imageDown;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Plan currentPlan;
    private List<Participant> participantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        initWidget();

        if (getIntent() != null && getIntent().hasExtra(ID_PLAN_EXPENSE)) {
            idPlan = getIntent().getIntExtra(ID_PLAN_EXPENSE, 0);
        }
        currentPlan = dbPlan.getPlanByID(idPlan);
        currency.setText(currentPlan.getCurrency());

        createPaidBy(); //create list participant
    }

    public void initWidget(){
        dbPlan = new DBPlan(this);
        dbParticipant = new DBParticipant(this);
        dbExpense = new DBExpense(this);
        currency = findViewById(R.id.text_currency);

        editTitle = findViewById(R.id.edit_title_expense);//title
        editAmount = findViewById(R.id.edit_amount);//amount
        editDate = findViewById(R.id.edit_date);//date
        actvPaidBy = findViewById(R.id.actv_paid_by);//name

        imageDown = findViewById(R.id.chooseName);
        layoutTitle = findViewById(R.id.layout_title);
        layoutAmount = findViewById(R.id.layout_amount);

        String currentDateTimeString = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        editDate.setText(currentDateTimeString); //default current date
    }

    //choose name participant
    public void createPaidBy(){
        participantList = (ArrayList<Participant>) dbParticipant.getAllParticipant(idPlan);
        ArrayList<String> list_name_participant = new ArrayList<>();

        for (Participant participant: participantList){
            list_name_participant.add(participant.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,list_name_participant);
        actvPaidBy.setAdapter(adapter);

        imageDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actvPaidBy.showDropDown();
            }
        });
    }

    //check title expense
    private boolean validateTitle() {
        if (editTitle.getText().toString().trim().isEmpty()) {
            layoutTitle.setError("The title be at least 1 character long");
            return false;
        } else {
            layoutTitle.setErrorEnabled(false);
        }
        return true;
    }

    //check amount expense
    private boolean validateAmount() {
        if (editAmount.getText().toString().trim().isEmpty()) {
            layoutAmount.setError("The amount be is not empty");
            return false;
        } else {
            layoutAmount.setErrorEnabled(false);
        }
        return true;
    }

    //choose date
    public void chooseDate(View v){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener, year, month, day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                editDate.setText(date);
            }
        };

    }

    //save new expense
    public void saveExpense(View v){
        if (!validateTitle() || !validateAmount() || actvPaidBy.getText().toString().isEmpty()){
            return;
        }

        String amount = editAmount.getText().toString();
        double double_amount = Double.parseDouble(amount);
        int result = dbExpense.insertData(idPlan,
                editTitle.getText().toString(),
                double_amount,
                editDate.getText().toString(),
                actvPaidBy.getText().toString());
        if (result != -1){
            Toast.makeText(this, "Inserted data", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Not inserted data", Toast.LENGTH_LONG).show();
        }

        setResult(RESULT_OK);
        finish();
    }

    public void backExpense(View v){
        finish();
    }
}
