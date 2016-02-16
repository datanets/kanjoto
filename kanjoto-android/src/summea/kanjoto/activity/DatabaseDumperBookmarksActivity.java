
package summea.kanjoto.activity;

import java.util.List;

import summea.kanjoto.data.BookmarksDataSource;
import summea.kanjoto.data.KanjotoDatabaseHelper;
import summea.kanjoto.model.Bookmark;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import summea.kanjoto.R;

public class DatabaseDumperBookmarksActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        BookmarksDataSource bds = new BookmarksDataSource(this);
        List<Bookmark> allBookmarks = bds.getAllBookmarks();
        bds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Bookmarks\n"
                + KanjotoDatabaseHelper.COLUMN_ID + "|" + KanjotoDatabaseHelper.COLUMN_NAME + "|"
                + KanjotoDatabaseHelper.COLUMN_SERIALIZED_VALUE + "\n");

        for (Bookmark bookmark : allBookmarks) {

            String newText = debugText.getText().toString();
            newText += bookmark.getId() + "|" + bookmark.getName() + "|"
                    + bookmark.getSerializedValue() + "\n";

            debugText.setText(newText);
        }
    }
}
