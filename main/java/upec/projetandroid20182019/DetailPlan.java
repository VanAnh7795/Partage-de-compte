package upec.projetandroid20182019;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import upec.projetandroid20182019.adapter.CustomAdapterBalance;
import upec.projetandroid20182019.adapter.CustomAdapterExpense;
import upec.projetandroid20182019.adapter.CustomAdapterPaid;
import upec.projetandroid20182019.data.DBExpense;
import upec.projetandroid20182019.data.DBParticipant;
import upec.projetandroid20182019.data.DBPlan;
import upec.projetandroid20182019.model.Balance;
import upec.projetandroid20182019.model.Expense;
import upec.projetandroid20182019.model.Paid;
import upec.projetandroid20182019.model.Participant;
import upec.projetandroid20182019.model.Plan;


public class DetailPlan extends AppCompatActivity {

    public static final int REQUEST_NEW_EXPENSE = 1;
    public static final int REQUEST_EDIT_PLAN = 2;

    public static final String ID_PLAN_DETAIL = "ID_PLAN_DETAIL";

    private int idPlan;

    private DBPlan dbPlan;
    private DBExpense dbExpense;
    private DBParticipant dbParticipant;
    private Plan currentPlan;

    private ListView lvListExpense, lvListBalance, lvListPaid;
    private LinearLayout llBalance, llFooter;
    private CardView cardViewExpense, cardViewBalance;
    private TextView barTitle, barName, tvTotalExpense, tvMyTotal;
    private ImageButton addNewExpense;

    private CustomAdapterExpense customAdapterExpense;
    private CustomAdapterBalance customAdapterBalance;
    private CustomAdapterPaid customAdapterPaid;

    private List<Expense> expenseList;
    private List<Balance> balanceList, paidList;
    private List<Paid> paidListView;
    private List<Participant> list_participant = new ArrayList<>();

    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_plan);

        initWidget();

        if (getIntent().hasExtra(ID_PLAN_DETAIL)) {
            idPlan = getIntent().getIntExtra(ID_PLAN_DETAIL, 0);
        }

        dbExpense = new DBExpense(this);
        balanceList = dbExpense.getTotalParticipant(idPlan);
        expenseList = dbExpense.getAllExpense(idPlan);

        paidList = dbExpense.getTotalParticipant(idPlan);
        paidListView = minCashFlowRec(paidList);

        getHeader();

        setAdapterExpense();
        setAdapterBalance();
        setAdapterPaid();

        holdToDelete(); //delete expense
        changView();
        updateTotalExpense();
        updateMyTotal();

    }

    public int getMin(List<Balance> listPaid){
        int min = 0;
        for(int i = 1; i < listPaid.size(); i++){
            if(listPaid.get(i).getAmount() < listPaid.get(min).getAmount()){
                min = i;
            }
        }
        return min;
    }

    public int getMax(List<Balance> listPaid){
        int max = 0;
        for(int i = 1; i < listPaid.size(); i++){
            if(listPaid.get(i).getAmount() > listPaid.get(max).getAmount()){
                max = i;
            }
        }
        return max;
    }

    public double minOf2(double x, double y){
        return (x < y) ? x:y;
    }

    public List<Paid> minCashFlowRec(List<Balance> balanceList){
        List<Paid> paidList = new ArrayList<>();
        int mxCredit = getMax(balanceList);
        int mxDebit = getMin(balanceList);
        double amount_mxDebit = balanceList.get(mxDebit).getAmount();
        double amount_mxCredit = balanceList.get(mxCredit).getAmount();
        while (amount_mxCredit != 0 && amount_mxDebit !=  0){
            double min = minOf2(-amount_mxDebit, amount_mxCredit);
            amount_mxCredit -= min;
            amount_mxDebit += min;
            Log.d("Tra tien","Person"+ balanceList.get(mxDebit).getName() + min +" to "+ balanceList.get(mxCredit).getAmount());
            NumberFormat formatter = new DecimalFormat("#0.00");
            Paid paid = new Paid (balanceList.get(mxDebit).getName(), balanceList.get(mxCredit).getName(), Double.parseDouble(formatter.format(min)));
            paidList.add(paid);
            balanceList.get(mxDebit).setAmount(amount_mxDebit);
            balanceList.get(mxCredit).setAmount(amount_mxCredit);
            mxCredit = getMax(balanceList);
            mxDebit = getMin(balanceList);
            amount_mxDebit = balanceList.get(mxDebit).getAmount();
            amount_mxCredit = balanceList.get(mxCredit).getAmount();
        }
        return paidList;

    }

    public void initWidget(){
        barName = findViewById(R.id.bar_name);
        barTitle = findViewById(R.id.bar_title);
        tvTotalExpense = findViewById(R.id.totalExpenses);
        tvMyTotal = findViewById(R.id.myTotal);
        addNewExpense = findViewById(R.id.newExpense);

        lvListExpense = findViewById(R.id.list_view_expense);
        lvListBalance = findViewById(R.id.list_balance);
        lvListPaid = findViewById(R.id.list_paid);

        llBalance = findViewById(R.id.ll_balance);
        llBalance.setVisibility(View.INVISIBLE);
        llFooter = findViewById(R.id.ll_footer);

        cardViewExpense = findViewById(R.id.cv_expense);
        cardViewBalance = findViewById(R.id.cv_balance);

    }

    //change view from list expense to list balance
    public void changView(){
        lvListExpense.setVisibility(View.VISIBLE);
        cardViewExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llBalance.setVisibility(View.INVISIBLE);
                lvListExpense.setVisibility(View.VISIBLE);
            }
        });

        cardViewBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvListExpense.setVisibility(View.INVISIBLE);
                //llFooter.setVisibility(View.INVISIBLE);
                //addNewExpense.setVisibility(View.INVISIBLE);
                llBalance.setVisibility(View.VISIBLE);
            }
        });
    }


    //get title + list name participant
    public void getHeader(){
        dbPlan = new DBPlan(this);
        currentPlan = dbPlan.getPlanByID(idPlan);
        barTitle.setText(currentPlan.getTitle());
        dbParticipant = new DBParticipant(this);
        list_participant = (ArrayList<Participant>) dbParticipant.getAllParticipant(idPlan);
        for (int i = 0; i < list_participant.size(); i++){
            barName.append(list_participant.get(i).getName());
            if (i != (list_participant.size() - 1)){
                barName.append(", ");
            }
        }
    }

    //update my total after add my new expense
    public void updateMyTotal(){
        balanceList = dbExpense.getTotalParticipant(idPlan);
        if (balanceList.size() != 0) {
            double myTotal = balanceList.get(0).getAmount();
            tvMyTotal.setText(Double.toString(myTotal)+ " " + currentPlan.getCurrency());
        }
    }

    //update total expense after add a new expense
    private void updateTotalExpense(){
        total = dbExpense.getTotal(idPlan);
        tvTotalExpense.setText(Double.toString(total)+ " " + currentPlan.getCurrency());
    }

    //update all list
    private  void updateAllList(){
        //update list expense
        expenseList = dbExpense.getAllExpense(idPlan);
        customAdapterExpense.setData(expenseList);

        //update list balance
        balanceList = dbExpense.getTotalParticipant(idPlan);
        customAdapterBalance.setData(balanceList);

        //update list paid
        paidList = dbExpense.getTotalParticipant(idPlan);
        paidListView = minCashFlowRec(paidList);
        customAdapterPaid.setData(paidListView);
    }

    //check result from new expense
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NEW_EXPENSE) {
            if (resultCode == RESULT_OK) {
                updateTotalExpense();//update total
                updateMyTotal();//update my total
                updateAllList();
            }
        }

        if (requestCode == REQUEST_EDIT_PLAN){
            //
        }
    }

    private void setAdapterBalance(){
        if(customAdapterBalance == null){
            customAdapterBalance = new CustomAdapterBalance(this, R.layout.item_list_balance, balanceList); /*custom listView*/
            lvListBalance.setAdapter(customAdapterBalance);
        } else {
            customAdapterBalance.setData(balanceList);
            lvListBalance.setSelection(customAdapterBalance.getCount()-1);
        }
    }

    private void setAdapterExpense(){
        if(customAdapterExpense == null){
            customAdapterExpense = new CustomAdapterExpense(this, R.layout.item_list_expense, expenseList); /*custom listView*/
            lvListExpense.setAdapter(customAdapterExpense);
        } else {
            customAdapterExpense.setData(expenseList);
            lvListExpense.setSelection(customAdapterExpense.getCount()-1);
        }
    }

    private void setAdapterPaid(){
        if(customAdapterPaid == null){
            customAdapterPaid = new CustomAdapterPaid(this, R.layout.item_list_paid, paidListView); /*custom listView*/
            lvListPaid.setAdapter(customAdapterPaid);
        } else {
            customAdapterPaid.setData(paidListView);
            lvListPaid.setSelection(customAdapterPaid.getCount()-1);
        }
    }


    //delete plan and delete
    private void holdToDelete() {
        lvListExpense.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete")
                        .setMessage("Do you want to Delete")
                        .setIcon(R.drawable.ic_delete_black_24dp)

                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                final Expense expense = expenseList.get(position);
                                int idExpense = expense.getId();
                                int result = dbExpense.deleteExpenseByID(idExpense);
                                if (result > 0) {
                                    Toast.makeText(getApplicationContext(), "Delete successfully", Toast.LENGTH_LONG).show();
                                    updateAllList();
                                    updateMyTotal();
                                    updateTotalExpense();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Delete failed", Toast.LENGTH_LONG).show();
                                }
                                dialog.dismiss();
                            }

                        })

                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        })
                        .create();
                myQuittingDialogBox.show();
                return true;
            }
        });

    }

    //back MainActivity
    public void backButton(View v){
        finish();
    }

    //add new expense
    public void addExpense(View v){
        Intent it = new Intent(this, NewExpense.class);
        it.putExtra(NewExpense.ID_PLAN_EXPENSE, idPlan);
        startActivityForResult(it, 1);
    }

    //edit plan
    public void editPlan(View v){
        Intent it = new Intent(this, EditPlan.class);
        it.putExtra(EditPlan.ID_PLAN_EDIT, idPlan);
        startActivityForResult(it, 2);
    }
}
