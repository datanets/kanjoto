# otashu

## TODO:
- add spinners for velocity in CreateNotesetActivity
- add spinners for length in CreateNotesetActivity
- add "edit noteset" feature part 2: save updated data
- fix refresh notesets list in "view all notesets" activity after deletion
- add full range of notes to note spinners
- organize data sources
- organize models


## DONE:
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
#### emotions
- id
- name

#### notes
- id
- noteset_id
- notevalue
- velocity
- length

#### notesets
- id
- name


## NOTES:
- approaches to try first for generating music:
  - Approach 1:
    - gather related notes from database based on given emotion
    - convert query results into individual "noteset bundles"
    - randomly choose from list of "noteset bundles"
  - Approach 2:
    - randomly query database for notesets and notes based on given emotion