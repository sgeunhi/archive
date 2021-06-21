import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BigInteger {
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("");

    public BigInteger(int i) {
    }

    public BigInteger(int[] number) {
    }

    public char[] bigNum;
    public boolean sign;
    // sign이 true이면 양수 false이면 음수

    public BigInteger(char[] array, boolean sign) {
        this.bigNum = array;
        this.sign = sign;
    }

    public char[] getNum() {
        return bigNum;
    }

    public boolean getSign() {
        return sign;
    }

    public BigInteger(String s) {
    }


    public BigInteger add(BigInteger big) {
        int maxSize = getNum().length > big.getNum().length ? getNum().length : big.getNum().length;
        int minSize = getNum().length < big.getNum().length ? getNum().length : big.getNum().length;
        char[] addArray = new char[maxSize + 1];
        int carry = 0, sum;
        for (int i = 0; i < minSize; i++) {
            sum = (getNum()[i] - '0') + (big.getNum()[i] - '0') + carry;
            carry = sum / 10; // 몫
            sum = sum % 10; // 나머지
            addArray[i] = (char) (sum + '0');
        }
        for (int i = minSize; i < maxSize; i++) {
            if (getNum().length > big.getNum().length) {
                sum = getNum()[i] - '0' + carry;
            } else {
                sum = big.getNum()[i] - '0' + carry;
            }
            carry = sum / 10;
            sum = sum % 10;
            addArray[i] = (char) (sum + '0');
        }
        if (carry == 1) {
            addArray[maxSize] = '1';
        }
        BigInteger addResult = new BigInteger(addArray, getSign());
        return addResult;
    }

    public BigInteger subtract(BigInteger big) {
        char[] resultArray = new char[100];
        BigInteger A = this;
        BigInteger B = big;
        boolean resultSign = true;
        int carry = 0, minus;

        if (getNum().length > big.getNum().length) {
            resultSign = (getSign() == true) ? true : false;
        } else if (getNum().length == big.getNum().length) {
            for (int i = getNum().length; i >= 0; i--) {
                if (getNum()[i] > big.getNum()[i]) {
                    resultSign = (getSign() == true) ? true : false;
                    break;
                } else if (getNum()[i] < big.getNum()[i]) {
                    resultSign = (getSign() == true) ? false : true;
                    A = big;
                    B = this;
                    break;
                } else if (i == 0) {
                    return new BigInteger(new char[]{'0'}, true);
                }
            }
        } else {
            resultSign = (getSign() == true) ? false : true;
            A = big;
            B = this;
        }
        for (int i = 0; i < B.getNum().length; i++) {
            if (A.getNum()[i] > B.getNum()[i]) {
                minus = (A.getNum()[i] - '0') - (B.getNum()[i] - '0') + carry;
                carry = 0;
            } else {
                minus = (A.getNum()[i] - '0') - (B.getNum()[i] - '0') + 10 + carry;
                carry = -1;
            }
            resultArray[i] = (char) (minus + '0');
        }
        for (int i = B.getNum().length; i < A.getNum().length; i++) {
            if (carry != 0 && (A.getNum()[i] - '0') == 0) {
                minus = (A.getNum()[i] - '0') + 10 + carry;
                carry = -1;
                resultArray[i] = (char) (minus + '0');
            } else {
                minus = (A.getNum()[i] - '0') + carry;
                carry = 0;
                resultArray[i] = (char) (minus + '0');
            }
        }
//        for (int i = 0; A.getNum()[i] != 0 || B.getNum()[i] != 0 || carry != '0'; i++) {
//            char a = (A.getNum()[i] == 0) ? 0 : (char) (A.getNum()[i] - '0');
//            char b = (B.getNum()[i] == 0) ? 0 : (char) (B.getNum()[i] - '0');
//            minus = (A.getNum()[i] - '0') - (B.getNum()[i] - '0') + carry;
//            if (minus < 0) {
//                minus += 10;
//                carry = -1;
//            } else {
//                carry = 0;
//            }
//            resultArray[i] = (char) (minus + '0');
//        }
        BigInteger subtractResult = new BigInteger(resultArray, resultSign);
        return subtractResult;
    }
//       int i;
//        int carry = 0, minus = 0;
//        if ((signA & signB)) {
//          if (bigNumA.length > bigNumB.length) {
//            for (int i = 0; i < bigNumB.length; i++) {
//                if (bigNumA[i] > bigNumB[i]) {
//                    minus = (bigNumA[i] - '0') - (bigNumB[i] - '0') + carry;
//                    carry = 0;
//                    resultArray[i] = (char) (minus + '0');
//                } else {
//                    minus = 10 - Math.abs((bigNumA[i] - '0') - (bigNumB[i] - '0')) + carry;
//                    carry = -1;
//                    resultArray[i] = (char) (minus + '0');
//                }
//            }
//            for (i = bigNumB.length; i < bigNumA.length; i++) {
//                if (carry != 0 && (bigNumA[i] - '0') == 0) {
//                    minus = 10 - Math.abs((bigNumA[i] - '0') + carry);
//                    carry = -1;
//                    resultArray[i] = (char) (minus + '0');
//                } else {
//                    minus = (bigNumA[i] - '0') + carry;
//                    carry = 0;
//                    resultArray[i] = (char) (minus + '0');
//                }
//            } // 수식 제일 뒤에 0들 제거해줘야 함.
//            subtractResult = (new BigInteger(this.bigNum, true)).subtract(new BigInteger(big.bigNum, true));
//            subtractResult.sign = true;
//            subtractResult.bigNum = resultArray;
//        } else if (bigNumA.length == bigNumB.length) {
//            for (i = bigNumA.length - 1; i > -1; i--) {
//                if (bigNumA[i] == bigNumB[i]) {
//                    continue;
//                } else {
//                    break;
//                }
//            }
//            if (bigNumA[i] > bigNumB[i]) {
//                // A-B 메소드 호출
//                subtractResult = (new BigInteger(this.bigNum, true)).subtract(new BigInteger(big.bigNum, true));
//                subtractResult.sign = true;
//                subtractResult.bigNum = resultArray;
//            } else { // A-B 메소드 호출해서 부호 반대로 지정
//                subtractResult = (new BigInteger(big.bigNum, true)).subtract(new BigInteger(this.bigNum, true));
//                subtractResult.sign = false;
//                subtractResult.bigNum = resultArray;
//            }
//        } else {
//            subtractResult = (new BigInteger(big.bigNum, true)).subtract(new BigInteger(this.bigNum, true));
//            subtractResult.sign = false;
//            subtractResult.bigNum = resultArray;
//            // B가 A보다 더 큰 수 일 때는 A-B 메소드 호출해서 부호 반대로 지정
//        }
//    } else if(!(signA |signB))
//
//    { // 여기서부터 다시 해야 댐
//        subtractResult = (new BigInteger(this.bigNum, false)).add(new BigInteger(big.bigNum, false));
//        subtractResult.bigNum = resultArray;
//
//
//    } else
//
//    {
//        //
//    }
//
//        return subtractResult;
//}
//    minus = 10 - Math.abs((bigNumA[i] - '0') - (bigNumB[i] - '0')) + carry; // 10 - 절대값 + carryout
//    carry = sum / 10; // 몫
//    sum = sum % 10; // 나머지
//    resultarray[i] = (char) (sum + '0');
//        for (i = minSize; i < maxSize; i++) {
//            if (bigNumA.length > bigNumB.length) {
//                sum = bigNumA[i] - '0' + carry;
//                carry = sum / 10;
//                sum = sum % 10;
//                resultArray[i] = (char) (sum + '0');
//            } else {
//                sum = bigNumB[i] - '0' + carry;
//                carry = sum / 10;
//                sum = sum % 10;
//                resultArray[i] = (char) (sum + '0');
//
//            }
//        }
//        if (carry == 1) {
//            resultArray[maxSize] = '1';
//        }
//
//        return result;
//    }


    public BigInteger multiply(BigInteger big) {
        BigInteger A = this;
        BigInteger B = big;
        byte carry = 0;
        char units = 0;
        char[] multiplyArray = new char[200];
        BigInteger multiplyResult = (getSign() == big.getSign()) ? new BigInteger(multiplyArray, true) : new BigInteger(multiplyArray, false);
        BigInteger eachResult;
//        if(getNum().length<big.getNum().length){
//            A = big;
//            B=  this;
//        }
        for (int i = 0; getNum()[i] != 0; i++) {
            for (int k = 0; big.getNum()[k] != 0 || carry != 0; k++) {
                char b = (big.getNum()[k] == 0) ? 0 : (char) (big.getNum()[k] - '0');
                units = (char) (((getNum()[i] - '0') * b + carry) % 10 + '0');
                carry = (byte) (((getNum()[i] - '0') * b + carry) / 10);
                multiplyArray[i + k] = units;
            }
            eachResult = new BigInteger(multiplyArray, true);
            multiplyResult = multiplyResult.add(eachResult);
        }
        return multiplyResult;
    }

    @Override
    public String toString() {
        String resultString = "";
        if (getSign() == false) {
            resultString = "-";
        }
//        char[] resultArray = new char[bigNum.length];
//        for (int i = bigNum.length - 1; i >= 0; i--) {
//            resultArray[bigNum.length - 1 - i] = bigNum[i];
//        }
//        String resultString = String.valueOf(resultArray);

        for (int i = getNum().length - 1; i >= 0; i--) {
            resultString = resultString + getNum()[i];
        }

        return resultString;
    }


    static BigInteger evaluate(String input) throws IllegalArgumentException {
//        Scanner sc = new Scanner(System.in);
//        String mathExpression = sc.nextLine(); // mathExpression : 입력 받은 수식
//        String space = "\\s"; // space : 공백 \\s : 공백 regex
        String mathExp = input.replaceAll(" ", ""); // 입력 받은 수식으로부터 공백 모두 제거 > mathExp
        String mathExpReverse = new StringBuilder(mathExp).reverse().toString(); // mathExp 거꾸로 저장 mathExpReverse

        String bigNum1 = null; // 연산할 숫자 1
        String bigNum2 = null; // 연산할 숫자 2
        char sign1 = ' '; // 숫자 1의 부호
        char sign2 = ' '; // 숫자 2의 부호
        char operator = ' '; // 연산자
//        boolean flag = false; // sign = '+' > true

        Pattern pat1 = Pattern.compile("\\*");
        Matcher m1 = pat1.matcher(mathExpReverse);
        Pattern pat2 = Pattern.compile("([+-]{2})");
        Matcher m2 = pat2.matcher(mathExpReverse);
        Pattern pat3 = Pattern.compile("([0-9][+-][0-9])");
        Matcher m3 = pat3.matcher(mathExpReverse);

        // bigNum1과 bigNum2의 마지막 인덱스에 저장된 원소가 숫자일 경우 +로 반환

        if (m1.find()) {
            bigNum1 = mathExpReverse.substring(0, m1.start());  // 처음부터 "*" 문자 이전 까지 bigNum1에 저장
            bigNum2 = mathExpReverse.substring(m1.end());   // "*" 문자 이후 문자열부터 끝까지 bigNum2에 저장
            operator = '*';
            // multiply 함수 호출
            // sign1과 sign2의 부호가 같을 경우 양의 값, 다를 경우 음의 값으로 출력
        } else if (m2.find()) {
            bigNum1 = mathExpReverse.substring(0, m2.start() + 1); // 처음부터 "[+-]{2}" 문자 이전 까지 저장
            bigNum2 = mathExpReverse.substring(m2.end()); //  "[+-]{2}" 문자 이후 문자열부터 끝까지 저장
            operator = mathExpReverse.charAt(m2.end() - 1);
            // m2.start()은 bigNum1의 부호의 index / m1.start()+1과 m2.end()-1는 연산자 +|-의 인덱스
            // 두 부호가 같으면 plus / 두 부호가 다르면 minus
            // sign1과 operator가 같으면 sum 함수 다르면 substract 함수 호출
        } else if (m3.find()) {
            bigNum1 = mathExpReverse.substring(0, m3.start() + 1); // 처음부터 "[+-]{2}" 문자 이전 까지 저장
            bigNum2 = mathExpReverse.substring(m3.end() - 1); //  "[+-]{2}" 문자 이후 문자열부터 끝까지 저장
            operator = mathExpReverse.charAt(m3.start() + 1);
            // m3.start() + 1 | m3.end()-2 의 인덱스가 연산자를 의미
            // operator에 담긴 부호에 따른 함수 호출
        }
        sign1 = bigNum1.charAt(bigNum1.length() - 1);
        sign2 = bigNum2.charAt(bigNum2.length() - 1);
        boolean signA;
        boolean signB;
        // signA와 signB가 true이면 양수 false이면 음수
        char[] bigNumA;
        char[] bigNumB;

        if ((sign1 != '+') && (sign1 != '-')) {
            signA = true;
            bigNumA = bigNum1.toCharArray();
        } else {
            bigNumA = bigNum1.substring(0, bigNum1.length() - 1).toCharArray();
            if (sign1 == '-') {
                signA = false;
            } else
                signA = true;
        }
        if ((sign2 != '+') && (sign2 != '-')) {
            signB = true;
            bigNumB = bigNum2.toCharArray();
        } else {
            bigNumB = bigNum2.substring(0, bigNum2.length() - 1).toCharArray();
            if (sign2 == '-') {
                signB = false;
            } else
                signB = true;
        }
        BigInteger a = new BigInteger(bigNumA, signA);
        BigInteger b = new BigInteger(bigNumB, signB);
        BigInteger result;
        if (operator == '+') {
            result = (signA == signB) ? a.add(b) : a.subtract(b);
            return result;
        } else if (operator == '-') {
            result = (signA == signB) ? a.subtract(b) : a.add(b);
            return result;
        } else {
            result = a.multiply(b);
            return result;
        }

        // implement here
        // parse input
        // using regex is allowed

        // One possible implementation
        // BigInteger num1 = new BigInteger(arg1);
        // BigInteger num2 = new BigInteger(arg2);
        // BigInteger result = num1.add(num2);
        // return result;

    }

    public static void main(String[] args) throws Exception {
        try (InputStreamReader isr = new InputStreamReader(System.in)) {
            try (BufferedReader reader = new BufferedReader(isr)) {
                boolean done = false;
                while (!done) {
                    String input = reader.readLine();

                    try {
                        done = processInput(input);
                    } catch (IllegalArgumentException e) {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }

    static boolean processInput(String input) throws IllegalArgumentException {
        boolean quit = isQuitCmd(input);

        if (quit) {
            return true;
        } else {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
            return false;
        }
    }

    static boolean isQuitCmd(String input) {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
