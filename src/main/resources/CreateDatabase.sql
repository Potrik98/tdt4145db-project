CREATE TABLE Workout(
  workoutId integer NOT NULL PRIMARY KEY,
  performance integer,
  personalShape integer,
  startTime TIMESTAMP,
  endTime TIMESTAMP
);

CREATE TABLE Equipment(
  equipmentId integer NOT NULL PRIMARY KEY,
  name varchar(30),
  description varchar(255)
);

CREATE TABLE Exercise(
  exerciseId integer NOT NULL PRIMARY KEY,
  name varchar(30),
  description varchar(255)
);

CREATE TABLE ExerciseWithEquipment(
  exerciseId integer NOT NULL PRIMARY KEY,
  equipmentId integer NOT NULL,
  description varchar(255),
  FOREIGN KEY (equipmentId) REFERENCES Equipment(equipmentId)
);

CREATE TABLE ExerciseGroup(
  groupId integer NOT NULL PRIMARY KEY,
  name varchar(30)
);

CREATE TABLE ExerciseInWorkout(
  workoutId integer NOT NULL,
  exerciseId integer NOT NULL,
  FOREIGN KEY (workoutId) REFERENCES Workout(workoutId),
  FOREIGN KEY (exerciseId) REFERENCES Exercise(exerciseId),
  PRIMARY KEY (workoutId, exerciseId)
);

CREATE TABLE ExerciseWithEquipmentInWorkout(
  workoutId integer NOT NULL,
  exerciseId integer NOT NULL,
  FOREIGN KEY (workoutId) REFERENCES Workout(workoutId),
  FOREIGN KEY (exerciseId) REFERENCES ExerciseWithEquipment(exerciseId),
  PRIMARY KEY (workoutId, exerciseId)
);

CREATE TABLE ExerciseInGroup(
  groupId integer NOT NULL,
  exerciseId integer NOT NULL,
  FOREIGN KEY (groupId) REFERENCES ExerciseGroup(groupId),
  FOREIGN KEY (exerciseId) REFERENCES Exercise(exerciseId),
  PRIMARY KEY (groupId, exerciseId)
);

CREATE TABLE ExerciseWithEquipmentInGroup(
  groupId integer NOT NULL,
  exerciseId integer NOT NULL,
  FOREIGN KEY (groupId) REFERENCES ExerciseGroup(groupId),
  FOREIGN KEY (exerciseId) REFERENCES ExerciseWithEquipment(exerciseId),
  PRIMARY KEY (groupId, exerciseId)
);
