package by.Iren137.quizer.exceptions;

public class EmptyOperationsException extends RuntimeException {
    public EmptyOperationsException() {
        super("No operations");
    }
}
