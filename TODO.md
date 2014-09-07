# otashu collection

## TODO:
- add spinner for length
- add spinner for velocity


## DONE:
- 20140906: added database import feature
- 20140905: added database export feature
- 20140903: decide on table structure for emotion id (decision for now: if SQLite really does support `WHERE IN ()` then tagged emotions can be stored as ids in noteset table rows... and tags can be searched for using numbers rather than strings)
- 20140902: create new noteset + create related notes in database
- 20140901: separate out notesets and notes
- 20140901: separate database opener and data source methods


## DATABASE:
#### emotion
- id
- name

#### note
- id
- noteset_id
- notevalue
- velocity
- length

#### notesets
- id
- name
