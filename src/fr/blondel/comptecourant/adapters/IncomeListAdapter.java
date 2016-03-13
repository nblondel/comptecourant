package fr.blondel.comptecourant.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import fr.blondel.comptecourant.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IncomeListAdapter extends BaseAdapter {
  private ArrayList<HashMap<String, String>> list = null;
  private Activity activity = null;
  
  private TextView categoryText;
  private TextView amountText;
  
  public static String firstColumn = "1";
  public static String secondColumn = "2";

  public IncomeListAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
    super();
    this.activity = activity;
    this.list = list;
  }

  @Override
  public int getCount() {
    return list.size();
  }

  @Override
  public Object getItem(int position) {
    return list.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = activity.getLayoutInflater();
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.income_list, null);

      categoryText = (TextView) convertView.findViewById(R.id.category);
      amountText = (TextView) convertView.findViewById(R.id.amount);
    }

    HashMap<String, String> map = list.get(position);
    categoryText.setText(map.get(firstColumn));
    amountText.setText(map.get(secondColumn));

    return convertView;
  }
}
