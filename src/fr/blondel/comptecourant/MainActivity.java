package fr.blondel.comptecourant;

import fr.blondel.comptecourant.Consts.FragmentSection;
import fr.blondel.comptecourant.fragments.AboutFragment;
import fr.blondel.comptecourant.fragments.IndexFragment;
import fr.blondel.comptecourant.fragments.IncomeFragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends Activity {
  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;

  private CharSequence mDrawerTitle;
  private CharSequence mTitle;
  private String[] mNaviguationTitles;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTitle = mDrawerTitle = getTitle();
    mNaviguationTitles = getResources().getStringArray(R.array.title_array);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);

    // set a custom shadow that overlays the main content when the drawer opens
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    // set up the drawer's list view with items and click listener
    mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mNaviguationTitles));
    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    // enable ActionBar app icon to behave as action to toggle nav drawer
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setHomeButtonEnabled(true);

    // ActionBarDrawerToggle ties together the the proper interactions
    // between the sliding drawer and the action bar app icon
    mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
    mDrawerLayout, /* DrawerLayout object */
    R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
    R.string.drawer_open, /* "open drawer" description for accessibility */
    R.string.drawer_close /* "close drawer" description for accessibility */
    ) {
      public void onDrawerClosed(View view) {
        getActionBar().setTitle(mTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }

      public void onDrawerOpened(View drawerView) {
        getActionBar().setTitle(mDrawerTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }
    };
    mDrawerLayout.setDrawerListener(mDrawerToggle);

    if (savedInstanceState == null) {
      selectItem(FragmentSection.ACCUEIL);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  /* Called whenever we call invalidateOptionsMenu() */
  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    // If the nav drawer is open, hide action items related to the content view
    boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
    menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // The action bar home/up action should open or close the drawer.
    // ActionBarDrawerToggle will take care of this.
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    // Handle action buttons
    switch (item.getItemId()) {
    case R.id.action_websearch:
      // create intent to perform web search for this planet
      Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
      intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
      // catch event that there's no activity to handle intent
      if (intent.resolveActivity(getPackageManager()) != null) {
        startActivity(intent);
      } else {
        Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
      }
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

  /* The click listner for ListView in the navigation drawer */
  private class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      try {
        selectItem(FragmentSection.fromInt(position));
      } catch(Exception e) {
        System.err.println(e.getMessage());
      }
    }
  }

  private void selectItem(FragmentSection section) {
    boolean selectionItemResult = false;
    Fragment fragment = null;
    
    FragmentManager fragmentManager = getFragmentManager();
    Bundle args = new Bundle();
    String section_name = getResources().getStringArray(R.array.title_array)[section.getValue()];
    args.putString(Consts.ARG_SECTION_NUMBER, section_name);

    switch(section) {
    // Update the main content by replacing fragments
    case ACCUEIL:
      fragment = new IndexFragment();
      fragment.setArguments(args);
      fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
      selectionItemResult = true;
      break;

    case REVENUS:
      fragment = new IncomeFragment();
      fragment.setArguments(args);
      fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
      selectionItemResult = true;
      break;

    case DEPENSES:
      Toast.makeText(getApplicationContext(), "Bient�t...", Toast.LENGTH_SHORT).show();
      break;

    case CONSOMMATION:
      Toast.makeText(getApplicationContext(), "Bient�t...", Toast.LENGTH_SHORT).show();
      break;

    case PREVISIONS:
      Toast.makeText(getApplicationContext(), "Bient�t...", Toast.LENGTH_SHORT).show();
      break;

    case ECONOMIE:
      Toast.makeText(getApplicationContext(), "Bient�t...", Toast.LENGTH_SHORT).show();
      break;

    case A_PROPOS:
      fragment = new AboutFragment();
      fragment.setArguments(args);
      fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
      selectionItemResult = true;
      break;

    default:
      /* Default is main page */
      fragment = new IndexFragment();
      fragment.setArguments(args);
      fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
      break;
    }

    if(selectionItemResult) {
      // update selected item and title, then close the drawer
      mDrawerList.setItemChecked(section.getValue(), true);
      setTitle(mNaviguationTitles[section.getValue()]);
      mDrawerLayout.closeDrawer(mDrawerList);
    }
  }

  @Override
  public void setTitle(CharSequence title) {
    mTitle = title;
    getActionBar().setTitle(mTitle);
  }

  /**
   * When using the ActionBarDrawerToggle, you must call it during
   * onPostCreate() and onConfigurationChanged()...
   */

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // Sync the toggle state after onRestoreInstanceState has occurred.
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    // Pass any configuration change to the drawer toggls
    mDrawerToggle.onConfigurationChanged(newConfig);
  }
}
