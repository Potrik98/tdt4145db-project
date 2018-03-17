package dbmanager;

import com.roervik.tdt4145.dbproject.dbmanager.ExerciseDBManager;
import com.roervik.tdt4145.dbproject.model.Exercise;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ExerciseDBManagerTest {
    private ExerciseDBManager exerciseDBManager;

    @Before
    public void openDBManager() throws Exception {
        exerciseDBManager = new ExerciseDBManager();
        exerciseDBManager.loadCreateScript();
    }

    @After
    public void closeDBManager() throws Exception {
        exerciseDBManager.closeConnection();
    }

    @Test
    public void testCreateAndGetExercise() throws Exception {
        final Exercise exercise = Exercise.builder()
                .exerciseId(UUID.randomUUID())
                .description("MyDescription")
                .name("ExerciseName")
                .build();
        exerciseDBManager.createExercise(exercise);

        final Optional<Exercise> retrievedExercise =
                exerciseDBManager.getExerciseById(exercise.getExerciseId());
        assertThat(retrievedExercise).isPresent();
        assertThat(retrievedExercise.get())
                .isEqualToComparingFieldByField(exercise);
    }

    @Test
    public void testGetExerciseWithInvalidId() throws Exception {
        final UUID invalidId = UUID.randomUUID();
        final Optional<Exercise> retrievedExercise =
                exerciseDBManager.getExerciseById(invalidId);
        assertThat(retrievedExercise).isEmpty();
    }
}
