
package summea.kanjoto.activity;

import java.util.List;

import summea.kanjoto.data.EdgesDataSource;
import summea.kanjoto.data.KanjotoDatabaseHelper;
import summea.kanjoto.model.Edge;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import summea.kanjoto.R;

public class DatabaseDumperEdgesActivity extends Activity {
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        EdgesDataSource edds = new EdgesDataSource(this);
        List<Edge> allEdges = edds.getAllEdgesByApprentice(apprenticeId);
        edds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText
                .setText(debugText.getText().toString() + "Table: Edges\n"
                        + KanjotoDatabaseHelper.COLUMN_ID + "|"
                        + KanjotoDatabaseHelper.COLUMN_GRAPH_ID + "|"
                        + KanjotoDatabaseHelper.COLUMN_EMOTION_ID + "|"
                        + KanjotoDatabaseHelper.COLUMN_FROM_NODE_ID + "|"
                        + KanjotoDatabaseHelper.COLUMN_TO_NODE_ID + "|"
                        + KanjotoDatabaseHelper.COLUMN_WEIGHT + "|"
                        + KanjotoDatabaseHelper.COLUMN_POSITION + "|"
                        + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "\n");

        for (Edge edge : allEdges) {

            String newText = debugText.getText().toString();
            newText += edge.getId() + "|"
                    + edge.getGraphId() + "|"
                    + edge.getEmotionId() + "|"
                    + edge.getFromNodeId() + "|"
                    + edge.getToNodeId() + "|"
                    + edge.getWeight() + "|"
                    + edge.getPosition() + "|"
                    + edge.getApprenticeId() + "\n";

            debugText.setText(newText);
        }
    }
}
