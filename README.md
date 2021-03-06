
# TDT4145 Database project

Compile: `mvn clean compile`

Run client: `mvn exec:java`

Database configurations can be found in dbconnection.properties files. Select which properties file to use in the program.properties file.

`<ObjectType>` is one of the following (Case sensitive):
 - Workout
 - Exercise
 - ExerciseWithEquipment
 - Equipment
 - ExerciseGroup

`<RelatedObjectType>` is one of the following (Case sensitive):
  - Workout
  - ExerciseGroup

`<TimeStamp>` should be on the form `YYYY-MM-DDTHH-MM-SS`

Creating objects from Json (1):\
`create object=<ObjectType> input=<PathToJsonFile>`

Creating list of objects from Json (1):\
`createList object=<ObjectType> input=<PathToJsonFile>`

Listing objects (2, 5):\
`list object=<ObjectType>`\
optional arguments:\
`count=<Count>` : limits number of object by count\
`in=<RelatedObjectType>` : list only objects in a relation to RelatedObject\
`id=<RelatedObjectId>` : lists only objects related to RelatedObject with selected Id\
`before=<TimeStamp>` : list only objects in a relation to RelatedObject before timestamp\
`after=<TimeStamp>` : list only objects in a relation to RelatedObject after timestamp\

Getting results of workout (Exercise and ExerciseWithEquipment) (3):\
`results object=<ObjectType>`\
optional arguments:\
`id=<ObjectId>` : shows only selected object\
`before=<TimeStamp>` : limits to workouts before time\
`after=<TimeStamp>` : limits to workouts after time


List objects of type RelatedObjectType this object is related to (4):\
`listRelated object=<RelatedObjectType> of=<ObjectType>`\
optional arguments:\
`id=<ObjectId>` : shows only objects selected object is related to\
`before=<TimeStamp>` : limits to related objects before time\
`after=<TimeStamp>` : limits to related objects after time\
