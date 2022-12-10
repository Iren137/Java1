package by.Iren137.quizer;

import by.Iren137.quizer.quiz.Quiz;
import by.Iren137.quizer.quiz.enums.Generators;
import by.Iren137.quizer.quiz.enums.Operations;
import by.Iren137.quizer.quiz.enums.Result;
import by.Iren137.quizer.quiz.enums.Tasks;
import by.Iren137.quizer.task_generators.GroupTaskGenerator;
import by.Iren137.quizer.task_generators.PoolTaskGenerator;
import by.Iren137.quizer.tasks.Task;
import by.Iren137.quizer.tasks.TextTask;
import by.Iren137.quizer.tasks.math_tasks.EquationTask;
import by.Iren137.quizer.tasks.math_tasks.ExpressionTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.instrument.IllegalClassFormatException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import static by.Iren137.quizer.Constants.*;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray quizArray;
        if (args.length == 0) {
            quizArray = (JSONArray) parser.parse(new FileReader(jsonWay));
        } else {
            quizArray = (JSONArray) parser.parse(new FileReader(args[0]));
        }
        Map<String, Quiz> quizMap = getQuizMap(quizArray);

        System.out.println(ANSI_PURPLE + "Write name of the test or \"NAMES\" to show tests' names" + ANSI_WHITE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String variant = tryToInput(quizMap, reader);

        Quiz currentQuiz = quizMap.get(variant);
        while (!currentQuiz.isFinished()) {
            Task task = currentQuiz.nextTask();
            System.out.print(task.getText());
            String answer = reader.readLine();
            Result result = currentQuiz.provideAnswer(answer);
            switch (result) {
                case OK -> System.out.println(ANSI_GREEN + "Right!" + ANSI_WHITE);
                case WRONG -> {
                    System.out.println(ANSI_RED + "Wrong!!!" + ANSI_WHITE);
                    System.out.println(ANSI_CYAN + "Right answer is \"" + task.getAnswer() + "\"" + ANSI_WHITE);
                }
                case INCORRECT_INPUT -> System.out.println(ANSI_YELLOW + "Incorrect input. Try again" + ANSI_WHITE);
            }
        }
        NumberFormat nf = new DecimalFormat("#.######");
        System.out.print(ANSI_BLUE + "Your score: " + ANSI_WHITE);
        System.out.println(ANSI_BLUE + nf.format(currentQuiz.getMark()) + ANSI_WHITE);
    }

    public static String tryToInput(Map<String, Quiz> quizMap, BufferedReader reader) throws IOException {
        String variant = reader.readLine();
        while (variant.equals("") || (!quizMap.containsKey(variant) && !variant.equals("NAMES"))) {
            System.out.println(ANSI_RED + "Wrong name. Try again" + ANSI_WHITE);
            variant = reader.readLine();
        }
        if (variant.equals("NAMES")) {
            printTestNames(quizMap);
            return tryToInput(quizMap, reader);
        } else {
            return variant;
        }
    }

    public static void printTestNames(Map<String, Quiz> quizMap) {
        System.out.println(ANSI_PURPLE + "Tests' names:" + ANSI_WHITE);
        String[] names = quizMap.keySet().toArray(new String[0]);
        Arrays.sort(names);
        for (String key : names) {
            System.out.println(ANSI_WHITE + key + ANSI_WHITE);
        }
    }

    /**
     * @return тесты в {@link Map}, где
     * ключ     - название теста {@link String}
     * значение - сам тест       {@link Quiz}
     */
    static Map<String, Quiz> getQuizMap(JSONArray quizArray) throws IOException, ParseException {
        HashMap<String, Quiz> result = new HashMap<>();
        for (Object quizObj : quizArray) {
            try {
                JSONObject quiz = (JSONObject) quizObj;
                String name = (String) quiz.get("name");
                int taskCount = (int) (long) quiz.get("number");
                String string_generator = (String) quiz.get("generator");
                Generators generator = switch (string_generator) {
                    case "EquationTaskGenerator" -> Generators.EquationGenerator;
                    case "ExpressionTaskGenerator" -> Generators.ExpressionGenerator;
                    case "TextTaskGenerator" -> Generators.TextGenerator;
                    case "GroupTaskGenerator" -> Generators.GroupGenerator;
                    case "PoolTaskGenerator" -> Generators.PoolGenerator;
                    default -> Generators.Error;
                };
                boolean allowDuplicates = true;
                if (generator == Generators.PoolGenerator) {
                    allowDuplicates = (int) (long) quiz.get("duplication") == 1;
                }
                JSONArray tasks = (JSONArray) ((JSONObject) quizObj).get("tasks");
                switch (generator) {
                    case TextGenerator -> result.put(name, new Quiz(getTextTaskGenerator(tasks), taskCount));
                    case ExpressionGenerator ->
                            result.put(name, new Quiz(getExpressionTaskGenerator(tasks), taskCount));
                    case EquationGenerator -> result.put(name, new Quiz(getEquationTaskGenerator(tasks), taskCount));
                    case GroupGenerator -> result.put(name, new Quiz(getGroupTaskGenerator(tasks), taskCount));
                    case PoolGenerator -> {
                        PoolTaskGenerator poolTaskGenerator = getPoolTaskGenerator(tasks, allowDuplicates);
                        if (taskCount > poolTaskGenerator.size()) {
                            taskCount = poolTaskGenerator.size();
                        }
                        result.put(name, new Quiz(poolTaskGenerator, taskCount));
                    }
                    default -> throw new IllegalClassFormatException("Unknown type of generator");
                }
            } catch (IllegalArgumentException | IllegalClassFormatException | NullPointerException e) {
                System.out.println(ANSI_RED + "Bad task. It can't be parsed" + ANSI_WHITE);
            }
        }
        return result;
    }

    private static Task getTextTask(JSONObject jsonObject) {
        try {
            String taskText = (String) jsonObject.get("task");
            String taskAnswer = (String) jsonObject.get("answer");
            return new TextTask(taskText, taskAnswer);
        } catch (ClassCastException ignored) {
        }
        throw new IllegalArgumentException("Invalid answer");
    }

    private static TextTask.Generator getTextTaskGenerator(JSONArray quiz) {
        try {
            TextTask.Generator out = new TextTask.Generator();
            for (Object obj : quiz) {
                JSONObject o = (JSONObject) obj;
                out.Add((String) o.get("task"), (String) o.get("answer"));
            }
            return out;
        } catch (ClassCastException ignored) {
        }
        throw new IllegalArgumentException("Invalid answer");
    }

    static EnumSet<Operations> getOperations(JSONObject jsonObject) throws IllegalClassFormatException {
        Object[] operationsObjectArray = ((JSONArray) jsonObject.get("Operations")).toArray();
        EnumSet<Operations> operations = EnumSet.noneOf(Operations.class);
        for (Object obj : operationsObjectArray) {
            String op = (String) obj;
            Operations operation;
            switch (op) {
                case "Sum" -> operation = Operations.generateSum;
                case "Difference" -> operation = Operations.generateDifference;
                case "Multiplication" -> operation = Operations.generateMultiplication;
                case "Division" -> operation = Operations.generateDivision;
                default -> throw new IllegalClassFormatException("Unknown operation");
            }
            operations.add(operation);
        }
        return operations;
    }

    private static Task getExpressionTask(JSONObject jsonObject) throws IllegalClassFormatException {
        double min = ((Number) jsonObject.get("min")).doubleValue();
        double max = ((Number) jsonObject.get("max")).doubleValue();
        EnumSet<Operations> operations = getOperations(jsonObject);
        int accuracy = (int) (long) jsonObject.get("accuracy");
        return new ExpressionTask(min, max, operations, accuracy);
    }

    private static ExpressionTask.Generator getExpressionTaskGenerator(JSONArray jsonObject) throws IllegalClassFormatException {
        ExpressionTask.Generator out = new ExpressionTask.Generator();
        for (Object o : jsonObject) {
            JSONObject obj = (JSONObject) o;
            double minNumber = ((Number) obj.get("min")).doubleValue();
            double maxNumber = ((Number) obj.get("max")).doubleValue();
            EnumSet<Operations> operations = getOperations(obj);
            int accuracy = (int) (long) obj.get("accuracy");
            out.Add(minNumber, maxNumber, operations, accuracy);
        }
        return out;
    }

    private static Task getEquationTask(JSONObject jsonObject) throws IllegalClassFormatException {
        double min = ((Number) jsonObject.get("min")).doubleValue();
        double max = ((Number) jsonObject.get("max")).doubleValue();
        EnumSet<Operations> operations = getOperations(jsonObject);
        int accuracy = (int) (long) jsonObject.get("accuracy");
        return new EquationTask(min, max, operations, accuracy);
    }

    private static EquationTask.Generator getEquationTaskGenerator(JSONArray jsonObject) throws IllegalClassFormatException {
        EquationTask.Generator out = new EquationTask.Generator();
        for (Object o : jsonObject) {
            JSONObject obj = (JSONObject) o;
            double minNumber = ((Number) obj.get("min")).doubleValue();
            double maxNumber = ((Number) obj.get("max")).doubleValue();
            EnumSet<Operations> operations = getOperations(obj);
            int accuracy = (int) (long) obj.get("accuracy");
            out.Add(minNumber, maxNumber, operations, accuracy);
        }
        return out;
    }

    private static GroupTaskGenerator getGroupTaskGenerator(JSONArray jsonObject) throws IllegalClassFormatException {
        Collection<Task.Generator> result = new ArrayList<>();
        for (Object o : jsonObject) {
            JSONObject obj = (JSONObject) o;
            String generator = (String) obj.get("type");
            Generators generators = switch (generator) {
                case "EquationTaskGenerator" -> Generators.EquationGenerator;
                case "ExpressionTaskGenerator" -> Generators.ExpressionGenerator;
                case "TextTaskGenerator" -> Generators.TextGenerator;
                default -> Generators.Error;
            };
            switch (generators) {
                case TextGenerator -> result.add(getTextTaskGenerator((JSONArray) obj.get("input")));
                case ExpressionGenerator -> result.add(getExpressionTaskGenerator((JSONArray) obj.get("input")));
                case EquationGenerator -> result.add(getEquationTaskGenerator((JSONArray) obj.get("input")));
                default -> throw new IllegalClassFormatException("Unknown type of generator");
            }
        }
        return new GroupTaskGenerator(result);
    }

    private static PoolTaskGenerator getPoolTaskGenerator(JSONArray jsonObject, boolean allowDuplicates) throws IllegalClassFormatException {
        Collection<Task> result = new ArrayList<>();
        for (Object o : jsonObject) {
            JSONObject obj = (JSONObject) o;
            String generator = (String) obj.get("type");
            Tasks generators = switch (generator) {
                case "EquationTask" -> Tasks.EquationTask;
                case "ExpressionTask" -> Tasks.ExpressionTask;
                case "TextTask" -> Tasks.TextTask;
                default -> Tasks.Error;
            };
            switch (generators) {
                case TextTask -> result.add(getTextTask((JSONObject) obj.get("input")));
                case ExpressionTask -> result.add(getExpressionTask((JSONObject) obj.get("input")));
                case EquationTask -> result.add(getEquationTask((JSONObject) obj.get("input")));
                default -> throw new IllegalClassFormatException("Unknown type of generator");
            }
        }
        return new PoolTaskGenerator(allowDuplicates, result);
    }
}