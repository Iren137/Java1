package by.Iren137.quizer.exceptions;

public class EmptyArgumentsException extends RuntimeException {
    public EmptyArgumentsException() {
        super("No arguments");
    }
}
