import java.io.Serializable;

public class Exercise implements Serializable {
    private static final long serialVersionUID = 1L;

    private int exerciseId;
    private String name;
    private String muscleGroup;

    public Exercise(int exerciseId, String name, String muscleGroup) {
        this.exerciseId = exerciseId;
        this.name = name;
        this.muscleGroup = muscleGroup;
    }

    public void updateInfo(String name, String muscleGroup) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
        this.muscleGroup = (muscleGroup == null) ? "" : muscleGroup.trim();
    }

    public String getName() { return name; }
    public int getExerciseId() { return exerciseId; }

    @Override
    public String toString() {
        String mg = (muscleGroup == null || muscleGroup.isEmpty()) ? "" : (" (" + muscleGroup + ")");
        return "exerciseId=" + exerciseId + "  " + name + mg;
    }
}
