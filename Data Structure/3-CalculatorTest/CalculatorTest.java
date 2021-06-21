import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;

public class CalculatorTest {

    public static void main(String args[]) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                String input = br.readLine();
                if (input.compareTo("q") == 0)
                    break;

                command(input);
            } catch (Exception e) {
                System.out.println("ERROR");
            }
        }
    }

    public static boolean isNumber(String num) {
        try {
            Long.parseLong(num);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
// 문자열에 저장된 값이 숫자인지를 판별하기 위한 함수이다.

    public static int precedence(char c) {
        if (c == '+' | c == '-')
            return 1;
        else if (c == '*' | c == '/' | c == '%')
            return 2;
        else if (c == '~')
            return 3;
        else if (c == '^')
            return 4;
        else
            return -1;
    }
// 각 연산자의 우선순위에 따라 값을 부여한 함수이다.

    private static void command(String input) throws Exception {
        Pattern p1 = Pattern.compile("[0-9]\\s+[0-9]");
        Matcher m1 = p1.matcher(input);
        if (m1.find()) {
            throw new Exception("ERROR");
        }
        // 숫자 사이에 공백이 포함되어 있는 수식 오류 처리
        String infixExp = input
                .replaceAll(" ", "")
                .replaceAll("\t", "")
                .replaceAll("((?<=[+*\\-^%/(])|(?<=^))[-]", "~");
        // 입력 받은 수식으로부터 공백과 \t을 모두 없앤 후 unary '-'의 경우 '~'로 치환한다.
        Pattern p2 = Pattern.compile("[^0-9~+\\-*/%^()]");
        Matcher m2 = p2.matcher(infixExp);
        Pattern p3 = Pattern.compile("[(]");
        Matcher m3 = p3.matcher(infixExp);
        Pattern p4 = Pattern.compile("[)]");
        Matcher m4 = p4.matcher(infixExp);
        int m3Count = 0,m4Count = 0;
        String postfixExp = "";
        boolean endNumSign = false; // 숫자의 끝을 알려주는 신호
        Stack<Character> operStack = new Stack<>(); // 연산자를 저장할 stack

        if (m2.find()) {
            throw new Exception("ERROR");
            // 정의되지 않은 기호를 포함한 수식 오류 처리
        } else {
            while (m3.find()) {
                m3Count++;
            }
            while (m4.find()) {
                m4Count++;
            }
            if (m3Count != m4Count)
                throw new Exception("ERROR");
            // 짝이 맞지 않은 괄호 오류 처리
        }
        for (int i = 0; i < infixExp.length(); i++) {
            char c = infixExp.charAt(i);
            if (precedence(c) > 0) {
                if (endNumSign) {
                    postfixExp += " ";
                    endNumSign = false;
                }
                if (c == '^' || c == '~') {
                    while (!operStack.isEmpty() && operStack.peek() != '(' && precedence(operStack.peek()) > precedence(c))
                        postfixExp += operStack.pop() + " ";

                } else {
                    while (!operStack.isEmpty() && operStack.peek() != '(' && precedence(operStack.peek()) >= precedence(c))
                        postfixExp += operStack.pop() + " ";
                }
                operStack.push(c);
            } else {
                if (c == '(') {
                    if (endNumSign) {
                        postfixExp += " ";
                        endNumSign = false;
                    }
                    operStack.push(c);
                } else if (c == ')') {
                    if (endNumSign) {
                        postfixExp += " ";
                        endNumSign = false;
                    }
                    while (operStack.peek() != '(') {
                        postfixExp += operStack.pop() + " ";
                    }
                    operStack.pop();
                } else {
                    postfixExp += c;
                    endNumSign = true;
                }
            }
        }
        if (endNumSign)
            postfixExp += " ";
        while (!operStack.isEmpty()) {
            postfixExp += operStack.pop() + " ";
        }

        postfixExp = postfixExp.trim();

        // 연산자의 우선순위를 고려하여 스택을 이용해서 infix를 postfix 변환한다.
        // 출력할 때 (숫자와 숫자) (연산자와 숫자) 사이에 공백을 두고 있어야하므로 숫자가 끝나는 신호를 잘 이용해야 한다.
        // unary '-'와 ^의 경우 right-associative 그 외의 경우는 left-associative 연산자이기에 우선순위를
        // 다툴 때 같은 연산자를 만났을 경우를 주의해야 한다.

        String[] postfixExpArray = postfixExp.split(" "); // 공백을 기준으로 숫자와 기호를 배열에 저장한다.
        Stack<String> calStack = new Stack<>();
        long operand1, operand2, result = 0;
        for (String op : postfixExpArray) {
            if (isNumber(op)) {
                calStack.push(op);
            } else if (op.equals("+")) {
                operand2 = Long.parseLong(calStack.pop());
                operand1 = Long.parseLong(calStack.pop());
                result = operand1 + operand2;
                calStack.push(Long.toString(result));
            } else if (op.equals("-")) {
                operand2 = Long.parseLong(calStack.pop());
                operand1 = Long.parseLong(calStack.pop());
                result = operand1 - operand2;
                calStack.push(Long.toString(result));
            } else if (op.equals("~")) {
                operand1 = Long.parseLong(calStack.pop());
                result = -operand1;
                calStack.push(Long.toString(result));
            } else if (op.equals("*")) {
                operand2 = Long.parseLong(calStack.pop());
                operand1 = Long.parseLong(calStack.pop());
                result = operand1 * operand2;
                calStack.push(Long.toString(result));
            } else if (op.equals("^")) {
                operand2 = Long.parseLong(calStack.pop());
                operand1 = Long.parseLong(calStack.pop());
                if (operand1 == 0 && operand2 < 0) {
                    throw new Exception("ERROR");
                } else {
                    result = (long) Math.pow(operand1, operand2);
                    calStack.push(Long.toString(result));
                } //(y<0일 때) 0^y 경우 오류 처리
            } else if (op.equals("%")) {
                operand2 = Long.parseLong(calStack.pop());
                operand1 = Long.parseLong(calStack.pop());
                if (operand2 == 0) {
                    throw new Exception("ERROR");
                } else {
                    result = operand1 % operand2;
                    calStack.push(Long.toString(result));
                } // x%0 경우 오류 처리
            } else if (op.equals("/")) {
                operand2 = Long.parseLong(calStack.pop());
                operand1 = Long.parseLong(calStack.pop());
                if (operand2 == 0) {
                    throw new Exception("ERROR");
                } else {
                    result = operand1 / operand2;
                    calStack.push(Long.toString(result));
                } // x/0 경우 오류 처리
            }
        }
        // 배열에 저장된 값을 돌면서 동시에 stack을 이용하여 계산한 결과값을 result에 저장한다.

        System.out.print(postfixExp);
        System.out.print("\n");
        System.out.println(result);

        // 오류를 모두 통과한 infix의 postfix와 결과값을 출력하기 위함이다.
    }
}
