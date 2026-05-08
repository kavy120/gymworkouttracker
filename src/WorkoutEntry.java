import java.io.Serializable;

public class WorkoutEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int NEXT_ID = 1;

    private int entryId;
    private int plannedSets;
    private int plannedReps;
    private double plannedWeight;
    private int exerciseId;

    public WorkoutEntry(int plannedSets, int plannedReps, double plannedWeight, int exerciseId) {
        this.entryId = NEXT_ID++;
        this.plannedSets = plannedSets;
        this.plannedReps = plannedReps;
        this.plannedWeight = plannedWeight;
        this.exerciseId = exerciseId;
    }

    public void updatePlanned(int sets, int reps, double weight) {
        if (sets < 0 || reps < 0 || weight < 0) return;
        this.plannedSets = sets;
        this.plannedReps = reps;
        this.plannedWeight = weight;
    }

    public int getEntryId() { return entryId; }
    public int getPlannedSets() { return plannedSets; }
    public int getPlannedReps() { return plannedReps; }
    public double getPlannedWeight() { return plannedWeight; }
    public int getExerciseId() { return exerciseId; }

    public String plannedSummary() {
        return plannedSets + "x" + plannedReps + " @ " + plannedWeight;
    }

    @Override
    public String toString() {
        return "entryId=" + entryId + " planned=" + plannedSummary();
    }

    public static void resetNextId(int next) { if (next > 0) NEXT_ID = next; }
    public static int getNextId() { return NEXT_ID; }
}
