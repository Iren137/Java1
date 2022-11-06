package by.Iren137.quizer.task_generators;

import by.Iren137.quizer.tasks.Task;
import by.Iren137.quizer.tasks.TextTask;

public class TextTaskGenerator implements TaskGenerator {
    String text;
    String answer;

    public TextTaskGenerator(String text, String answer) {
        this.text = text;
        this.answer = answer;
    }

    @Override
    public Task generate() {
        return new TextTask(this.text, this.answer);
    }
}
