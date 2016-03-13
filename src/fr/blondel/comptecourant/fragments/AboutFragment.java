package fr.blondel.comptecourant.fragments;

import fr.blondel.comptecourant.Consts;
import fr.blondel.comptecourant.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {
  public AboutFragment() {
    // Empty constructor required for fragment subclasses
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_about, container, false);
    String section_name = getArguments().getString(Consts.ARG_SECTION_NUMBER);
    getActivity().setTitle(section_name);

    /* Set version */
    TextView versionTextView = ((TextView)rootView.findViewById(R.id.version_label_value));
    if(versionTextView != null) versionTextView.setText(R.string.version);
    
    /* Set website */
    TextView websiteTextView = ((TextView)rootView.findViewById(R.id.website_label_value));
    if(websiteTextView != null) websiteTextView.setText(R.string.website);

    return rootView;
  }
}
