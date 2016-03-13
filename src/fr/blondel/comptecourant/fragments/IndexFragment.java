package fr.blondel.comptecourant.fragments;

import fr.blondel.comptecourant.Consts;
import fr.blondel.comptecourant.R;
import fr.blondel.comptecourant.database.DatabaseHandler;
import fr.blondel.comptecourant.database.IncomeTable;
import fr.blondel.comptecourant.database.RecordTable;
import fr.blondel.comptecourant.models.Record;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class IndexFragment extends Fragment {
  public IndexFragment() {
    // Empty constructor required for fragment subclasses
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_index, container, false);
    String section_name = getArguments().getString(Consts.ARG_SECTION_NUMBER);
    getActivity().setTitle(section_name);

    Button button1 = (Button)rootView.findViewById(R.id.index_button_1);
    button1.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        /* Get database handler */
        DatabaseHandler handlerDB = new DatabaseHandler(getActivity());
        
        /* Create an income, charge and consumption entries */
        IncomeTable incomeTable = new IncomeTable(handlerDB);
        
        RecordTable recordTable = new RecordTable(handlerDB);
        try {
          recordTable.open();
          long id = recordTable.add(new Record("Test", 0, 0, 0));
          Log.d(getTag(), "New query ID: " + id);
          Cursor cursor = recordTable.get(id);
          Toast.makeText(rootView.getContext(), "ID" + id + ": "+ recordTable.toString(cursor), Toast.LENGTH_SHORT).show();

          recordTable.close();
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
    });
    
    Button button2 = (Button)rootView.findViewById(R.id.index_button_2);
    button2.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        /* Get database handler */
        DatabaseHandler handlerDB = new DatabaseHandler(getActivity());
        RecordTable recordTable = new RecordTable(handlerDB);
        try {
          recordTable.open();

          // ---update contact---
          if (recordTable.update(1, "new2"))
            Toast.makeText(rootView.getContext(), "Update successful.", Toast.LENGTH_SHORT).show();
          else
            Toast.makeText(rootView.getContext(), "Update failed.", Toast.LENGTH_SHORT).show();

          // ---delete a contact---
          if (recordTable.delete(1))
            Toast.makeText(rootView.getContext(), "Delete successful.", Toast.LENGTH_SHORT).show();
          else
            Toast.makeText(rootView.getContext(), "Delete failed.", Toast.LENGTH_SHORT).show();

          recordTable.close();
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
    });

    return rootView;
  }
}
