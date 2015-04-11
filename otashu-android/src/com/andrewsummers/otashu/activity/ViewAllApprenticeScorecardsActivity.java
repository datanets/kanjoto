
package com.andrewsummers.otashu.activity;

import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.adapter.ApprenticeScorecardAdapter;
import com.andrewsummers.otashu.data.ApprenticeScorecardsDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.ApprenticeScorecard;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

/**
 * View all ApprenticeScorecards as a list.
 * <p>
 * This activity allows a user to view a list of all saved Apprentice test scores.
 * </p>
 */
public class ViewAllApprenticeScorecardsActivity extends ListActivity {
    private ListView listView = null;
    private int selectedPositionInList = 0;
    private ApprenticeScorecardAdapter adapter = null;
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    /**
     * onCreate override used to gather and display a list of all apprenticeScorecards saved in
     * database.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize ListView
        listView = getListView();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        // set title for list activity
        ViewGroup listHeader = (ViewGroup) getLayoutInflater().inflate(R.layout.list_header,
                listView, false);
        TextView headerText = (TextView) listHeader.findViewById(R.id.list_header_title);
        headerText.setText(R.string.view_all_apprentice_scorecards_list_header);
        listView.addHeaderView(listHeader, "", false);

        fillList();
    }

    public void fillList() {
        List<ApprenticeScorecard> allApprenticeScorecards = new LinkedList<ApprenticeScorecard>();
        ApprenticeScorecardsDataSource lds = new ApprenticeScorecardsDataSource(this);
        allApprenticeScorecards = lds
                .getAllApprenticeScorecards(apprenticeId, OtashuDatabaseHelper.COLUMN_TAKEN_AT);
        lds.close();

        // pass list data to adapter
        adapter = new ApprenticeScorecardAdapter(this, allApprenticeScorecards);

        listView.setAdapter(adapter);

        // get individual apprenticeScorecard details
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                // launch details activity
                Intent intent = new Intent(view.getContext(),
                        ViewApprenticeScorecardDetailActivity.class);

                intent.putExtra("list_id", id);
                startActivity(intent);
            }
        });

        // register context menu
        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_apprentice_scorecards, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle menu item selection
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        selectedPositionInList = info.position;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_apprentice_scorecard, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.context_menu_view:
                intent = new Intent(this, ViewApprenticeScorecardDetailActivity.class);
                intent.putExtra("list_id", info.id);
                startActivity(intent);
                return true;
            case R.id.context_menu_delete:
                confirmDelete();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_confirm_delete_message).setTitle(
                R.string.dialog_confirm_delete_title);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked ok
                // go ahead and delete apprenticeScorecard

                // get correct apprenticeScorecard id to delete
                ApprenticeScorecard apprenticeScorecardToDelete = getApprenticeScorecardFromListPosition(selectedPositionInList);

                deleteApprenticeScorecard(apprenticeScorecardToDelete);

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context,
                        context.getResources().getString(R.string.apprentice_scorecard_deleted),
                        duration);
                toast.show();

                // refresh list
                adapter.removeItem(selectedPositionInList);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // user clicked cancel
                // just go back to list for now
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public ApprenticeScorecard getApprenticeScorecardFromListPosition(long rowId) {
        long apprenticeScorecardId = rowId;

        List<Long> allApprenticeScorecardsData = new LinkedList<Long>();
        ApprenticeScorecardsDataSource lds = new ApprenticeScorecardsDataSource(this);

        // get string version of returned apprenticeScorecard list
        allApprenticeScorecardsData = lds.getAllApprenticeScorecardListDBTableIds(apprenticeId);
        lds.close();

        // prevent crashes due to lack of database data
        if (allApprenticeScorecardsData.isEmpty()) {
            allApprenticeScorecardsData.add((long) 0);
        }

        Long[] allApprenticeScorecards = allApprenticeScorecardsData
                .toArray(new Long[allApprenticeScorecardsData.size()]);

        ApprenticeScorecard apprenticeScorecard = lds
                .getApprenticeScorecard(apprenticeId,
                        allApprenticeScorecards[(int) apprenticeScorecardId]);

        lds.close();

        return apprenticeScorecard;
    }

    public void deleteApprenticeScorecard(ApprenticeScorecard apprenticeScorecard) {
        ApprenticeScorecardsDataSource lds = new ApprenticeScorecardsDataSource(this);
        lds.deleteApprenticeScorecard(apprenticeScorecard);
        lds.close();
    }

    @Override
    public void onResume() {
        super.onResume();

        // refresh list
        adapter.clear();
        fillList();
    }
}
