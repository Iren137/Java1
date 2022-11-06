package by.Iren137.quizer.exceptions;

public class EmptyTasksException extends RuntimeException {
    public EmptyTasksException() {
        super("Bad generation. Empty set of tasks");
    }
}
