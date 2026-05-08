public class PerformedEntry extends WorkoutEntry {
    private static final long serialVersionUID = 1L;

    private int actualSets;
    private int actualReps;
    private double actualWeight;

    public PerformedEntry(int plannedSets, int plannedReps, double plannedWeight,
                          int exerciseId, int actualSets, int actualReps, double actualWeight) {
        super(plannedSets, plannedReps, plannedWeight, exerciseId);
        updateActual(actualSets, actualReps, actualWeight);
    }

    public void updateActual(int sets, int reps, double weight) {
        if (sets < 0 || reps < 0 || weight < 0) return;
        this.actualSets = sets;
        this.actualReps = reps;
        this.actualWeight = weight;
    }

    public double getActualWeight() { return actualWeight; }

    public String actualSummary() {
        return actualSets + "x" + actualReps + " @ " + actualWeight;
    }
}
