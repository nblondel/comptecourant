package fr.blondel.comptecourant.dialogs;

import java.util.ArrayList;
import java.util.List;

import fr.blondel.comptecourant.R;
import fr.blondel.comptecourant.database.CategoryTable;
import fr.blondel.comptecourant.database.DatabaseHandler;
import fr.blondel.comptecourant.dialogs.NewCategoryDialogFragment.NewCategoryDialogListener;
import fr.blondel.comptecourant.models.Category;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewAmountDialogFragment extends DialogFragment implements NewCategoryDialogListener {
  private NewAmountDialogFragment instance = null;
  private DatabaseHandler handlerDB = null;
  private ProgressDialog progressDialog = null;
  
  private Button buttonOk = null;
  private Button buttonCancel = null;
  private Button buttonAdd = null;
  private EditText amountText = null;
  
  private Spinner categorySpinner = null;
  private List<String> categoryList = new ArrayList<String>();
  private ArrayAdapter<String> categoryAdapter = null;
  
  private static String DialogBoxTitle;
  private NewAmountDialogListener parent;

  public interface NewAmountDialogListener {
    void addNewAmount(String category, double amount);
  }
  
  public NewAmountDialogFragment(NewAmountDialogListener parent) {
    this.parent = parent;
    this.instance = this;
  }

  public void setDialogTitle(String title) {
    DialogBoxTitle = title;
  }
  
  private Handler messageHandler = new Handler(Looper.getMainLooper()) {
    public void handleMessage(Message msg) {
      super.handleMessage(msg);

      /* Load visible list */
      categoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryList);
      categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      categorySpinner.setAdapter(categoryAdapter);
      progressDialog.dismiss();
    }
  };
  
  private class CategoriesLoadingThread extends Thread {
    public void run() {
      
      /* Get all incomes */
      CategoryTable categoryTable = new CategoryTable(handlerDB);

      try {
        categoryTable.open();
        Cursor categoryCursor = categoryTable.getAll();
        
        /* Fill arraylist with the categories */
        categoryList.clear();
        for (int i = 0; i < categoryCursor.getCount(); i++) {
          categoryCursor.moveToPosition(i);
          categoryList.add(categoryCursor.getString(0));
        }
        categoryCursor.close();
        categoryTable.close();

      } catch (Exception e) {
        e.printStackTrace();
      }
      
      messageHandler.sendEmptyMessage(0);
    }
  };


  @Override
  public void addNewCategory(String category) {
    if(!categoryList.contains(category)) { // Should not append because this callback function is called only if the category does not exist

      /* Loading */
      progressDialog = ProgressDialog.show(getActivity(), "", "Chargement...");

      try {
        /* Create an income, charge and consumption entries */
        CategoryTable categoryTable = new CategoryTable(handlerDB);
        categoryTable.open();


        /* Add new income in database */
        categoryTable.add(new Category(category));
        categoryTable.close();

        categoryList.add(category);
        int spinnerPosition = categoryAdapter.getPosition(category);
        categorySpinner.setSelection(spinnerPosition); // Force the selection of the new category
      } catch(Exception e) {
        e.printStackTrace();
      }

      progressDialog.dismiss();
    }
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.new_amount, container);
    
    /* Get database handler */
    handlerDB = new DatabaseHandler(getActivity());
    // Get the view items
    buttonOk = (Button) view.findViewById(R.id.new_amount_button_ok);
    buttonCancel = (Button) view.findViewById(R.id.new_amount_button_cancel);
    buttonAdd = (Button) view.findViewById(R.id.new_amount_button_add_category);
    amountText = (EditText) view.findViewById(R.id.new_amount_amount_text);
    categorySpinner = (Spinner) view.findViewById(R.id.new_amount_category_spinner);

    // Button listener
    buttonOk.setOnClickListener(ButtonOkListener);
    buttonCancel.setOnClickListener(ButtonCancelListener);
    buttonAdd.setOnClickListener(ButtonAddListener);

    // set the title for the dialog
    getDialog().setTitle(DialogBoxTitle);
    
    /* Load categories */
    progressDialog = ProgressDialog.show(getActivity(), "", "Chargement...");
    /* Reload incomes list */
    CategoriesLoadingThread categoriesLoadingThread = new CategoriesLoadingThread();
    categoriesLoadingThread.start();

    return view;
  }

  // ---create an anonymous class to act as a button click listener
  private OnClickListener ButtonOkListener = new OnClickListener() {
    public void onClick(View v) {
      try {
        Double amount = Double.parseDouble(amountText.getText().toString());
        Log.i(getTag(), "amount: "+amount);
        if(amount >= 0.0 && amount <= 10000.0) {
          parent.addNewAmount(categorySpinner.getSelectedItem().toString(), amount);
          dismiss();
        } else {
          Toast.makeText(v.getContext(), "Le montant doit être supérieur à 0 et inférieur à 10.000", Toast.LENGTH_SHORT).show();
        }
      } catch(Exception e) {
        e.printStackTrace();
        Toast.makeText(v.getContext(), "Le montant n'a pas pu être ajouté", Toast.LENGTH_SHORT).show();
      }
    }
  };

  private OnClickListener ButtonCancelListener = new OnClickListener() {
    public void onClick(View v) {
      dismiss();
    }
  };

  private OnClickListener ButtonAddListener = new OnClickListener() {
    public void onClick(View v) {
      try {
        /* Open the new category/amount dialog */
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        NewCategoryDialogFragment newCategoryDialog = new NewCategoryDialogFragment(instance, categoryList);
        newCategoryDialog.setCancelable(true);
        newCategoryDialog.setDialogTitle("Ajouter catégorie");
        newCategoryDialog.show(fragmentManager, "NewCategoryDialogFragment");
      } catch(Exception e) {
        e.printStackTrace();
        Toast.makeText(v.getContext(), "La catégorie n'a pas pu être ajoutée", Toast.LENGTH_SHORT).show();
      }
    }
  };
}
