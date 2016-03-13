package fr.blondel.comptecourant.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import fr.blondel.comptecourant.Consts;
import fr.blondel.comptecourant.R;
import fr.blondel.comptecourant.adapters.IncomeListAdapter;
import fr.blondel.comptecourant.database.CategoryTable;
import fr.blondel.comptecourant.database.DatabaseHandler;
import fr.blondel.comptecourant.database.IncomeTable;
import fr.blondel.comptecourant.dialogs.NewAmountDialogFragment;
import fr.blondel.comptecourant.dialogs.NewAmountDialogFragment.NewAmountDialogListener;
import fr.blondel.comptecourant.models.Income;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class IncomeFragment extends Fragment implements NewAmountDialogListener {
  private IncomeFragment instance = null;
  private View rootView = null;
  private ListView incomeListView = null;

  private ArrayList<HashMap<String, String>> adapterList = new ArrayList<HashMap<String, String>>();
  private IncomeListAdapter adapter = null;
  private ProgressDialog progressDialog = null;
  private DatabaseHandler handlerDB = null;

  public IncomeFragment() {
    this.instance = this;
  }
  
  private Handler messageHandler = new Handler(Looper.getMainLooper()) {
    public void handleMessage(Message msg) {
      super.handleMessage(msg);

      /* Load visible list */
      adapter = new IncomeListAdapter(getActivity(), adapterList);
      incomeListView.setAdapter(adapter);
      progressDialog.dismiss();
    }
  };

  private class IncomeLoadingThread extends Thread {
    public void run() {
      
      /* Get all incomes */
      IncomeTable incomeTable = new IncomeTable(handlerDB);
      CategoryTable categoryTable = new CategoryTable(handlerDB);

      try {
        incomeTable.open();
        categoryTable.open();

        Cursor incomesCursor = incomeTable.getAll();
        /* Fill arraylist with the incomes */
        adapterList.clear();

        for (int i = 0; i < incomesCursor.getCount(); i++) {
          incomesCursor.moveToPosition(i);
          int categoryId = incomesCursor.getInt(0);
          incomesCursor.moveToPosition(i);
          double amount = incomesCursor.getDouble(1);
          
          /* Get category name from id */
          Cursor cursor = categoryTable.get(categoryId);
          String category = "" + categoryId;
          if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            category = cursor.getString(0);
            cursor.close();
          }

          HashMap<String, String> temp = new HashMap<String, String>();
          temp.put(IncomeListAdapter.firstColumn, category);
          temp.put(IncomeListAdapter.secondColumn, String.valueOf(amount));
          adapterList.add(temp);
        }
        
        incomesCursor.close();
        incomeTable.close();
        categoryTable.close();

      } catch (Exception e) {
        e.printStackTrace();
      }
      
      messageHandler.sendEmptyMessage(0);
    }
  };
  
  @Override
  public void addNewAmount(String category, double amount) {
    /* Loading */
    progressDialog = ProgressDialog.show(getActivity(), "", "Chargement...");
    
    try {
      /* Create an income, charge and consumption entries */
      IncomeTable incomeTable = new IncomeTable(handlerDB);
      CategoryTable categoryTable = new CategoryTable(handlerDB);

      incomeTable.open();
      categoryTable.open();

      /* Get category name from id */
      Cursor cursor = categoryTable.get(category);
      cursor.moveToFirst();
      long categoryId = cursor.getLong(0);
      /* Add new income in database */
      Income newIncome = new Income(categoryId, amount);
      incomeTable.add(newIncome);

      cursor.close();
      incomeTable.close();
      categoryTable.close();

      /* Update list with new income */
      HashMap<String, String> temp = new HashMap<String, String>();
      temp.put(IncomeListAdapter.firstColumn, category);
      temp.put(IncomeListAdapter.secondColumn, String.valueOf(newIncome.getAmount()));
      adapterList.add(temp);
      adapter.notifyDataSetChanged();
    } catch (Exception e) {
      e.printStackTrace();
    }

    progressDialog.dismiss();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_income, container, false);
    getActivity().setTitle(getArguments().getString(Consts.ARG_SECTION_NUMBER));

    /* Get database handler */
    handlerDB = new DatabaseHandler(getActivity());
    /* Create list view */
    incomeListView = (ListView)rootView.findViewById(R.id.income_list);
    incomeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        int pos = position + 1;
        Toast.makeText(getActivity(), Integer.toString(pos) + " Clicked", Toast.LENGTH_SHORT).show();
      }
    });

    Button addButton = (Button) rootView.findViewById(R.id.income_button_add);
    addButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          /* Open the new category/amount dialog */
          FragmentManager fragmentManager = getActivity().getFragmentManager();
          NewAmountDialogFragment newAmountDialog = new NewAmountDialogFragment(instance);
          newAmountDialog.setCancelable(true);
          newAmountDialog.setDialogTitle("Ajouter revenu");
          newAmountDialog.show(fragmentManager, "NewAmountDialogFragment");

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    
    Button modifyButton = (Button) rootView.findViewById(R.id.income_button_modify);
    modifyButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          /* Allow the modification in the list */
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    /* Load incomes */
    progressDialog = ProgressDialog.show(getActivity(), "", "Chargement...");
    /* Reload incomes list */
    IncomeLoadingThread incomeLoadingThread = new IncomeLoadingThread();
    incomeLoadingThread.start();

    return rootView;
  }
}
