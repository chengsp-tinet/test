package com.example.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chengsp
 * @date 2020/6/18 10:19
 */
public class ConcatToPlaceHolder {
    public static final Pattern LOG_PATTERN = Pattern.compile("(logger|log)\\.(info|debug|error|warn)\\((.+)\\);", Pattern.CASE_INSENSITIVE);
    public static final Pattern STRING_PATTERN = Pattern.compile("(\".+?\")", Pattern.CASE_INSENSITIVE);
    public static final Pattern VAR_PATTERN = Pattern.compile("\\+\\s*(\\w+(\\.\\w+\\(.+?\\))?)",
            Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) {
        iteratorFile(new File("D:\\IdeaProjects\\cost-all"), file -> {
            String s = convertLog(file.getAbsolutePath());
            if (s != null) {
                write(s, file.getAbsolutePath());
            }
        });


    }

    private static void write(String string, String fileName) {
        try (FileWriter ffw = new FileWriter(fileName)) {
            ffw.write(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void iteratorFile(File file, Consumer<File> action) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    iteratorFile(f, action);
                }
            }

        } else {
            if (file.getName().endsWith(".java")) {
                action.accept(file);
            }
        }
    }

    private static String convertLog(String fileName) {
        StringBuilder builder = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String statement = null;
            boolean match = false;
            builder = new StringBuilder(10000);
            while ((statement = br.readLine()) != null) {
                StringBuffer buffer = new StringBuffer(200);
                Matcher matcher = LOG_PATTERN.matcher(statement);
                match = false;
                while (matcher.find()) {
                    String logName = matcher.group(1);
                    String logLevel = matcher.group(2);
                    String logContent = matcher.group(3);
                    List<String> stringStatement = findPlaceHolder(logContent, STRING_PATTERN);
                    String tempStrStatement = stringStatement.toString().replaceAll("\\[\\s*\"", "\"");
                    tempStrStatement = tempStrStatement.replaceAll("\"\\s*]", "\"");
                    tempStrStatement = tempStrStatement.replaceAll("\", \"", "{}");
                    List<String> varStatement = findPlaceHolder(logContent, VAR_PATTERN);
                    if (!varStatement.isEmpty()) {
                        match = true;
                        tempStrStatement = tempStrStatement.substring(0, tempStrStatement.length() - 1) + "{}\"";
                        String varTempStatement = varStatement.toString().replaceAll("\\[", ", ");
                        varTempStatement = varTempStatement.substring(0, varTempStatement.length() - 1);
                        String content = tempStrStatement + varTempStatement;
                        statement = logName + "." + logLevel + "(" +
                                content +
                                ");";

                        matcher.appendReplacement(buffer, statement);
                    }
                }
                matcher.appendTail(buffer);
                if (match) {
                    builder.append(buffer.toString()).append("\r\n");
                } else {
                    builder.append(statement).append("\r\n");
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (builder == null) {
            return null;
        }
        builder.subSequence(0, builder.length() - 2);
        return builder.toString();
    }

    public static List<String> findPlaceHolder(String s, Pattern pattern) {
        Matcher matcher = pattern.matcher(s);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list;
    }
}

