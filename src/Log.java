public class Log {
    public static void text(String message) {
        System.out.println(ConsoleColor.DEFAULT + message);
    }

    public static void info(String message) {
        System.out.println(ConsoleColor.INFO + message + ConsoleColor.DEFAULT);
    }

    public static void warning(String message) {
        System.out.println(ConsoleColor.WARNING + message + ConsoleColor.DEFAULT);
    }

    public static void error(String message) {
        System.out.println(ConsoleColor.ERROR + message + ConsoleColor.DEFAULT);
    }


}
