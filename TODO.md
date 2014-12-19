# otashu

## TODO:
- implement second stage of Apprentice logic ("no" response -> update Apprentice knowledge)
- finish fixing HashMap to SparseArray conversion
- save generated song to file
- create emotion fingerprint
- noteset folders (lists by label or emotion)
- show what note is playing for playback visualization
- add playback speed in settings
- standardize strings view naming
- add "noteset enabled" column to notesets database table
- convert bookmark serialized values to json strings


## DONE:
- 20141216: added "keep alarm set until turned off by user" feature
- 20141214: added alarm clock settings
- 20141214: added alarm clock feature
- 20141207: cleaned up "create emotion" activity UI formatting
- 20141207: cleaned up "create label" activity UI formatting
- 20141207: cleaned up "edit label" activity UI formatting
- 20141207: cleaned up "create notevalue" activity UI formatting
- 20141207: cleaned up "create noteset" activity UI formatting
- 20141206: added notevalue details activity
- 20141206: added delete notevalue feature
- 20141205: added note color on "noteset details" activity
- 20141203: added default playback instrument in settings
- 20141128: fixed new emotions not being created
- 20141128: fixed new notesets not being created
- 20141128: worked on fixing database leaks
- 20141128: cleaned up label details activity formatting
- 20141128: connected label color to emotion details activity
- 20141128: created custom adapter for "list all labels" activity
- 20141128: added custom adapter for "list all bookmarks" activity listview
- 20141128: fixed double playback for apprentice activity
- 20141128: looked into using graphics for note playback visualization
- 20141128: added simple noteset visualizer to "generate" activity
- 20141126: added "infinite scroll" for "view all notesets" activity
- 20141126: fixed get correct list item for emotion delete on "view all emotions" activity
- 20141126: fixed get correct list item for emotion edit on "view all emotions" activity
- 20141125: fixed get correct list item for noteset edit on "view all notesets" activity
- 20141125: fixed get correct list item for noteset delete on "view all notesets" activity
- 20141125: fixed unintentional "emotion paint" on "view all notesets" activity when scrolling
- 20141123: show note values as note names in "view all notesets" activity
- 20141123: show noteset emotion in "view all notesets" activity list
- 20141122: fixed noteset-view issue when choosing row outside array bounds in "view all notesets" activity
- 20141121: added custom adapter for list all notesets activity listview
- 20141121: trying out a better way to get both notesets and related notes for "list all notesets" activity
- 20141119: prevent noteset from playing twice when "play" button pressed
- 20141117: added play bookmark from "view all bookmarks" activity
- 20141116: added play bookmark from "bookmark details" activity
- 20141115: cleaned up emotion details activity formatting
- 20141115: cleaned up bookmark details activity formatting
- 20141114: added "bookmark generated songs" feature
- 20141114: added bookmark button to "choose emotion" activity
- 20141110: added label to "create emotion" activity
- 20141110: get spinner list of label options for emotion activities
- 20141109: added label to "edit emotion" activity
- 20141109: added label to "view emotion details" activity
- 20141108: change instrument in "choose emotion" activity
- 20141107: added label to emotions database table
- 20141101: connected "edit noteset" menu item in "noteset details" activity to actual noteset id
- 20141101: connected "delete noteset" menu item in "noteset details" activity to actual noteset id
- 20141031: added "edit noteset" menu item when viewing noteset details
- 20141031: added "delete noteset" menu item when viewing noteset details
- 20141030: added read note velocity in "create noteset" activity
- 20141030: added read note velocity in "edit noteset" activity
- 20141030: added read note velocity in "view noteset details" activity
- 20141030: added read note velocity in "generate music" activity
- 20141030: added read note velocity in "apprentice" activity
- 20141030: added read note length in "apprentice" activity
- 20141027: added read note length in "create noteset" activity
- 20141027: added read note length in "edit noteset" activity
- 20141027: added read note length in "view noteset details" activity
- 20141027: added read note length in "generate music" activity
- 20141026: added "edit label" activity
- 20141026: added "delete label" activity
- 20141026: added "view all labels" activity context menu
- 20141025: created labels database table
- 20141025: added "view all labels" activity
- 20141025: added "create label" activity
- 20141025: added label table to "database dumper" activity
- 20141025: added "view label details" activity
- 20141023: removed noteset "name" field for now from views
- 20141023: updated "create noteset" view
- 20141023: updated "edit noteset" view
- 20141022: updated "view noteset detail" view
- 20141017: fixed main activity layout test (added ViewAllNotesetsActivity test) 
- 20141015: added plans for doing something with "no" response from user in ApprenticeActivity
- 20141011: fixed prevent screen orientation change for generate music playback
- 20141011: added more detail to Logic A
- 20141010: added end-note-matching Logic A to "generate music" activity
- 20141008: fixed main activity landscape issue (when switching from portrait to landscape)
- 20141007: created icons for main menu grid buttons
- 20141006: fixed connect main menu grid buttons to activities
- 20141005: fixed change main menu buttons to a GridView format
- 20141004: added noteset playback feature on CreateNotesetActivity
- 20141004: added noteset playback feature on EditNotesetActivity
- 20141003: added noteset playback feature on ViewNotesetDetailActivity
- 20141003: added "play again" button for ApprenticeActivity
- 20141003: fixed get another noteset after "yes" response from user in ApprenticeActivity
- 20141002: added generate random noteset in ApprenticeActivity
- 20141002: added ask user if randomly-generated noteset sounds like a particular emotion
- 20141002: added save randomly-generated noteset to database if "yes"
- 20140930: work on Apprentice begins
- 20140929: added emotion and noteset tables to Database Dumper
- 20140928: added support for velocity spinner in EditNotesetActivity
- 20140928: added support for length spinner in EditNotesetActivity
- 20140927: added spinners for velocity in CreateNotesetActivity
- 20140927: added spinners for length in CreateNotesetActivity
- 20140927: cleaned up unnecessary information in manifest file
- 20140926: added "position" column to notes database table
- 20140926: fixed refresh "view all notesets" list after adding new noteset
- 20140926: fixed refresh "view all emotions" list after adding new emotion
- 20140926: added "database dumper" activity for debugging
- 20140925: added "view emotion details" feature
- 20140925: added "delete emotion" feature
- 20140925: added "view all emotions context menu" feature
- 20140925: added "view emotion details after clicking emotion name in ViewAllEmotionsActivity list" feature
- 20140924: added "view note values as note names (C3, D3, etc.) in view all notesets activity" feature
- 20140924: added "view note values as note names (C3, D3, etc.) in edit noteset activity" feature
- 20140924: added "view note values as note names (C3, D3, etc.) in view noteset details activity" feature
- 20140923: added "view note values as note names (C3, D3, etc.) in create noteset activity" feature
- 20140922: added "edit emotion" feature
- 20140921: added more formatting to Settings activity (now uses PreferenceActivity)
- 20140921: added "refresh view all emotions list after adding new emotion" feature
- 20140921: added a simple layout test
- 20140920: organized activities
- 20140920: organized models
- 20140920: organized data sources
- 20140920: added "return to original activity after playing music back" feature
- 20140920: added "view" option to context menu in "list all notesets" activity
- 20140919: added full range of notes to note spinners
- 20140918: added "edit noteset" feature part 2: save updated data
- 20140918: fixed refresh notesets list in "view all notesets" activity after deletion
- 20140917: added "edit noteset" feature part 1: read original data
- 20140916: added "delete noteset" feature
- 20140915: connected up "view noteset details" activity
- 20140914: added context menu for "edit, delete" noteset options
- 20140913: added "emotion spinner lists get data from emotion database table rows" functionality
- 20140913: added "create emotion" feature
- 20140913: added "view all emotions" feature
- 20140913: added emotion database table
- 20140913: added menu create button (and menu settings button) to "view all notesets" activity
- 20140913: fixed crash that happens when switching to landscape view in MainActivity
- 20140912: added "end music playback" functionality (when exiting GenerateMusicActivity)
- 20140911: added playback of generated midi file
- 20140910: added "write chosen notesets to midi file" feature
- 20140910: added random noteset picker (using gathered data structure)
- 20140909: added note gathering feature for selecting notesets that match a user-selected emotion
- 20140909: added emotion_id to notesets table in database
- 20140908: added ChooseEmotionActivity
- 20140907: added toasts to database import/export process
- 20140906: added database import feature
- 20140905: added database export feature
- 20140903: decide on table structure for emotion id (decision for now: if SQLite really does support `WHERE IN ()` then tagged emotions can be stored as ids in noteset table rows... and tags can be searched for using numbers rather than strings)
- 20140902: create new noteset + create related notes in database
- 20140901: separate out notesets and notes
- 20140901: separate database opener and data source methods


## DATABASE:
#### bookmarks
- id
- name
- serialized_value

#### emotions
- id
- name
- label_id

#### labels
- id
- name
- color

#### notes
- id
- noteset_id
- notevalue
- velocity
- length
- position

#### notesets
- id
- name

#### notevalues
- id
- notevalue
- notelabel


## NOTES:
- approaches to try first for generating music:
  - Approach 1:
    - gather related notes from database based on given emotion
    - convert query results into individual "noteset bundles"
    - randomly choose from list of "noteset bundles"
  - Approach 2:
    - randomly query database for notesets and notes based on given emotion
- serializedValue is a serialized value of notesets (with the following properties):
  notevalue:velocity:length:position|