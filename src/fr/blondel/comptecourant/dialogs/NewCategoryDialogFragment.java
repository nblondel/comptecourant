package fr.blondel.comptecourant.dialogs;

import java.util.List;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import fr.blondel.comptecourant.R;

public class NewCategoryDialogFragment extends DialogFragment {  
  private Button buttonOk = null;
  private Button buttonCancel = null;
  private EditText categoryText = null;
  
  private static String DialogBoxTitle;
  private NewCategoryDialogListener parent;
  private List<String> categoryList = null;

  public interface NewCategoryDialogListener {
    void addNewCategory(String category);
  }
  
  public NewCategoryDialogFragment(NewCategoryDialogListener parent, List<String> categoryList) {
    this.parent = parent;
    this.categoryList = categoryList;
  }

  public void setDialogTitle(String title) {
    DialogBoxTitle = title;
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.new_category, container);
    
    // Get the view items
    buttonOk = (Button) view.findViewById(R.id.new_category_button_ok);
    buttonCancel = (Button) view.findViewById(R.id.new_category_button_cancel);
    categoryText = (EditText) view.findViewById(R.id.new_category_category_text);

    // Button listener
    buttonOk.setOnClickListener(ButtonOkListener);
    buttonCancel.setOnClickListener(ButtonCancelListener);

    // set the title for the dialog
    getDialog().setTitle(DialogBoxTitle);

    return view;
  }

  // ---create an anonymous class to act as a button click listener
  private OnClickListener ButtonOkListener = new OnClickListener() {
    public void onClick(View v) {
      try {
        String category = categoryText.getText().toString();
        if(categoryList == null || categoryList.contains(category)) {
          Toast.makeText(v.getContext(), "Cette catégorie existe déjà", Toast.LENGTH_SHORT).show();
        } else {
          parent.addNewCategory(category);
          dismiss();
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
}
