package com.example.test;


import lombok.extern.slf4j.Slf4j;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.StringLiteral;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * @author chengsp
 * @date 2020/6/18 15:46
 */
@Slf4j
public class ASTTest {
    public static final Pattern LOG_PATTERN = Pattern.compile("(logger|log)\\.(info|debug|error|warn).+\\+.+",
            Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) throws IOException {
        iteratorFile(new File("D:\\IdeaProjects\\cost-all\\cInfoUpdate\\cinfo-update-service\\src\\main\\java\\com\\eigpay\\cinfoupgrade\\service\\bizhandler\\chief\\inst\\impl"), file -> {

            String absolutePath = file.getAbsolutePath();
            String content = parseLogAndReplace(absolutePath);
            if (content != null) {
                log.info("写入:{}", absolutePath);
                write(content, absolutePath);
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

    private static String parseLogAndReplace(String path) {
        ASTParser astParser = ASTParser.newParser(AST.JLS8);
        byte[] input;
        CompilationUnit compilationUnit = null;
        String fileContent = null;
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path))) {
            input = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(input);
            fileContent = new String(input);
            astParser.setKind(ASTParser.K_COMPILATION_UNIT);
            astParser.setSource(fileContent.toCharArray());
            compilationUnit = ((CompilationUnit) astParser.createAST(null));
            compilationUnit.recordModifications();
            compilationUnit.accept(new ASTVisitor() {
                @Override
                public boolean visit(MethodInvocation node) {
                    String s = node.toString();
                    if (LOG_PATTERN.matcher(s).find()) {
                        List arguments = node.arguments();
                        Object argument = arguments.get(0);
                        if (argument instanceof InfixExpression) {
                            InfixExpression infixExpression = (InfixExpression) argument;
                            String content = concatWithBrace(infixExpression);
                            AST tempAST = infixExpression.getAST();
                            StringLiteral stringLiteral = tempAST.newStringLiteral();
                            stringLiteral.setLiteralValue(content);
                            stringLiteral.setEscapedValue(content);
                            arguments.set(0, stringLiteral);
                            List<Expression> c = collectExpression(infixExpression);
                            arguments.addAll(c);
                        }

                        //System.out.println(node);
                    }
                    return true;
                }


            });
            //CompilationUnit unit = (CompilationUnit) ASTNode.copySubtree(compilationUnit.getAST(), compilationUnit);
            //return new String(new CompilationUnitImpl(compilationUnit).getContents());
            return compilationUnit.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Expression> collectExpression(InfixExpression infixExpression) {
        List<Expression> argExpressions = new ArrayList<>();
        List<Expression> expressionList = mergeExpression(infixExpression);
        for (int i = 0; i < expressionList.size(); i++) {
            Expression expression = expressionList.get(i);
            if (!(expression instanceof StringLiteral)) {
                argExpressions.add(((Expression) ASTNode.copySubtree(expression.getAST(), expression)));
            }
        }
        return argExpressions;
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

    private static String concatWithBrace(InfixExpression infixExpression) {
        StringBuilder s = new StringBuilder();
        List<Expression> expressionList = mergeExpression(infixExpression);
        if (!(expressionList.get(0) instanceof StringLiteral)) {
            s.append("{}");
        }

        for (int i = 0; i < expressionList.size(); i++) {
            Expression expression = expressionList.get(i);
            if (i + 1 < expressionList.size()) {
                if (!(expressionList.get(i + 1) instanceof StringLiteral)) {
                    if (expression instanceof StringLiteral) {
                        s.append(((StringLiteral) expression).getEscapedValue().replaceAll("\"", "")).append("{}");
                    } else {
                        s.append("{}");
                    }
                } else {
                    s.append(((StringLiteral) expressionList.get(i + 1)).getEscapedValue().replaceAll("\"", ""));
                    i++;
                }
            }
        }
        s = new StringBuilder("\"" + s + "\"");
        return s.toString();
    }

    private static List<Expression> mergeExpression(InfixExpression infixExpression) {
        List<Expression> expressionList = new ArrayList<>(2 + infixExpression.extendedOperands().size());
        expressionList.add(infixExpression.getLeftOperand());
        expressionList.add(infixExpression.getRightOperand());
        expressionList.addAll(infixExpression.extendedOperands());
        return expressionList;
    }


}
