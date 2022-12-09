package by.Iren137.quizer.tasks;

import by.Iren137.quizer.quiz.enums.Result;

import java.util.ArrayList;

public class TextTask implements Task {
    private final String text;
    private final String answer;

    public TextTask(
            String text,
            String answer
    ) {
        if (text == null || answer == null) {
            throw new IllegalArgumentException("Text or/and answer is null");
        }
        this.text = text + '\n';
        this.answer = answer;
    }

    @Override
    public String getText() {
        return text;
    }
    public String getAnswer(){
        return this.answer;
    }

    @Override
    public Result validate(String answer) {
        if (answer == null || answer.length() == 0) {
            return Result.INCORRECT_INPUT;
        }
        if (answer.equals(this.answer)) {
            return Result.OK;
        } else {
            return Result.WRONG;
        }
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        TextTask task = (TextTask) obj;
        return ((this.text.equals(task.text) && this.answer.equals(task.answer)));
    }

    public static class Generator implements Task.Generator {
        ArrayList<TextTask> textTasks = new ArrayList<>();
        int current = 0;

        public void Add(String text, String answer) {
            this.textTasks.add(new TextTask(text, answer));
        }

        @Override
        public Task generate() {
            Task out = this.textTasks.get(this.current);
            this.current++;
            return out;
        }
    }
}
