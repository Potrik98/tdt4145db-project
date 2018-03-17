CREATE TABLE Workout(
  workoutId varchar(36) NOT NULL PRIMARY KEY,
  performance integer,
  personalShape integer,
  startTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  endTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Equipment(
  equipmentId varchar(36) NOT NULL PRIMARY KEY,
  name varchar(30),
  description varchar(255)
);

CREATE TABLE Exercise(
  exerciseId varchar(36) NOT NULL PRIMARY KEY,
  name varchar(30),
  description varchar(255)
);

CREATE TABLE ExerciseWithEquipment(
  exerciseId varchar(36) NOT NULL PRIMARY KEY,
  equipmentId varchar(36) NOT NULL,
  description varchar(255),
  FOREIGN KEY (equipmentId) REFERENCES Equipment(equipmentId)
);

CREATE TABLE ExerciseGroup(
  groupId varchar(36) NOT NULL PRIMARY KEY,
  name varchar(30)
);

CREATE TABLE ExerciseInWorkout(
  workoutId varchar(36) NOT NULL,
  exerciseId varchar(36) NOT NULL,
  FOREIGN KEY (workoutId) REFERENCES Workout(workoutId),
  FOREIGN KEY (exerciseId) REFERENCES Exercise(exerciseId),
  PRIMARY KEY (workoutId, exerciseId)
);

CREATE TABLE ExerciseWithEquipmentInWorkout(
  workoutId varchar(36) NOT NULL,
  exerciseId varchar(36) NOT NULL,
  FOREIGN KEY (workoutId) REFERENCES Workout(workoutId),
  FOREIGN KEY (exerciseId) REFERENCES ExerciseWithEquipment(exerciseId),
  PRIMARY KEY (workoutId, exerciseId)
);

CREATE TABLE ExerciseInGroup(
  groupId varchar(36) NOT NULL,
  exerciseId varchar(36) NOT NULL,
  FOREIGN KEY (groupId) REFERENCES ExerciseGroup(groupId),
  FOREIGN KEY (exerciseId) REFERENCES Exercise(exerciseId),
  PRIMARY KEY (groupId, exerciseId)
);

CREATE TABLE ExerciseWithEquipmentInGroup(
  groupId varchar(36) NOT NULL,
  exerciseId varchar(36) NOT NULL,
  FOREIGN KEY (groupId) REFERENCES ExerciseGroup(groupId),
  FOREIGN KEY (exerciseId) REFERENCES ExerciseWithEquipment(exerciseId),
  PRIMARY KEY (groupId, exerciseId)
);
