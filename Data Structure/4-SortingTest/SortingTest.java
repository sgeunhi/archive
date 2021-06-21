import java.io.*;
import java.util.*;

public class SortingTest {
    public static void main(String args[]) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            boolean isRandom = false;    // 입력받은 배열이 난수인가 아닌가?
            int[] value;    // 입력 받을 숫자들의 배열
            String nums = br.readLine();    // 첫 줄을 입력 받음
            if (nums.charAt(0) == 'r') {
                // 난수일 경우
                isRandom = true;    // 난수임을 표시

                String[] nums_arg = nums.split(" ");

                int numsize = Integer.parseInt(nums_arg[1]);    // 총 갯수
                int rminimum = Integer.parseInt(nums_arg[2]);    // 최소값
                int rmaximum = Integer.parseInt(nums_arg[3]);    // 최대값

                Random rand = new Random();    // 난수 인스턴스를 생성한다.

                value = new int[numsize];    // 배열을 생성한다.
                for (int i = 0; i < value.length; i++)    // 각각의 배열에 난수를 생성하여 대입
                    value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
            } else {
                // 난수가 아닐 경우
                int numsize = Integer.parseInt(nums);

                value = new int[numsize];    // 배열을 생성한다.
                for (int i = 0; i < value.length; i++)    // 한줄씩 입력받아 배열원소로 대입
                    value[i] = Integer.parseInt(br.readLine());
            }

            // 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
            while (true) {
                int[] newvalue = (int[]) value.clone();    // 원래 값의 보호를 위해 복사본을 생성한다.

                String command = br.readLine();

                long t = System.currentTimeMillis();
                switch (command.charAt(0)) {
                    case 'B':    // Bubble Sort
                        newvalue = DoBubbleSort(newvalue);
                        break;
                    case 'I':    // Insertion Sort
                        newvalue = DoInsertionSort(newvalue);
                        break;
                    case 'H':    // Heap Sort
                        newvalue = DoHeapSort(newvalue);
                        break;
                    case 'M':    // Merge Sort
                        newvalue = DoMergeSort(newvalue);
                        break;
                    case 'Q':    // Quick Sort
                        newvalue = DoQuickSort(newvalue);
                        break;
                    case 'R':    // Radix Sort
                        newvalue = DoRadixSort(newvalue);
                        break;
                    case 'X':
                        return;    // 프로그램을 종료한다.
                    default:
                        throw new IOException("잘못된 정렬 방법을 입력했습니다.");
                }
                if (isRandom) {
                    // 난수일 경우 수행시간을 출력한다.
                    System.out.println((System.currentTimeMillis() - t) + " ms");
                } else {
                    // 난수가 아닐 경우 정렬된 결과값을 출력한다.
                    for (int i = 0; i < newvalue.length; i++) {
                        System.out.println(newvalue[i]);
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoBubbleSort(int[] value) {
        for (int i = 0; i < value.length; i++) {
            for (int k = 0; k < value.length - i - 1; k++) {
                // 가장 큰 값은 마지막 인덱스에 저장되며 마지막 인덱스를 제외하고 반복 작업 수행
                if (value[k] > value[k + 1]) {
                    swap(value, k + 1, k);
                // 인접한 두 값을 비교해나가면서 더 큰 값을 오른쪽으로 이동
                }
            }
        }
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoInsertionSort(int[] value) {
        for (int i = 1; i < value.length; i++) {
            int k = i - 1;
            int tempValue = value[i];
            // 이전에 소팅된 값들과 비교할 대상을 임시로 저장
            while (k >= 0 && tempValue < value[k]) {
                value[k + 1] = value[k];
                k--;
            // 임시로 저장된 값과 이전의 소팅된 값들의 크기를 비교해서 자리를 이동하며 알맞은 위치에 삽입
            }
            value[k + 1] = tempValue;
        }
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoHeapSort(int[] value) {
        int size = value.length - 1;
        for (int i = size / 2; i >= 0; i--) {
            percolateDown(value, i, size);
        }
        //  build heap
        for (int i = size; i > 0; i--) {
            swap(value, i, 0);
            size--;
            percolateDown(value, 0, size);
        }
        // root의 값을 빼내고, last의 값을 root로 옮긴 다음 percolateDown 작업 반복
        return (value);
    }

    private static void percolateDown(int[] value, int i, int size) {
        int leftChild = 2 * i + 1;
        int rightChild = 2 * i + 2;
        int largest;
        if (leftChild <= size && value[leftChild] > value[i]) {
            largest = leftChild;
        } else {
            largest = i;
        }
        if (rightChild <= size && value[rightChild] > value[largest]) {
            largest = rightChild;
        }
        // child와 parent의 값들을 비교하여 더 큰 값을 임시로 largest에 저장
        if (largest != i) {
            swap(value, i, largest);
            percolateDown(value, largest, size);
        // largest에 임시로 저장된 값이 호출된 값과 다르면 서로 자리를 바꾼 뒤 percolateDown 호출
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoMergeSort(int[] value) {
        mergeSort(value, 0, value.length - 1);
        return (value);
    }

    private static void mergeSort(int[] value, int first, int last) {
        int middle;
        if (first < last) {
            middle = (first + last) / 2;
            mergeSort(value, first, middle);
            mergeSort(value, middle + 1, last);
            merge(value, first, middle, last);
            // 입력 받은 배열을 절반으로 계속 나누는 작업을 진행한 뒤
            // 크기 비교를 하면서 merge 작업 수행으로 sorting
        }
    }

    private static void merge(int[] value, int first, int middle, int last) {
        int[] tempSorted = new int[last - first + 1];
        // merge는 in-place sorting 작업이 불가능하기 때문에 임시로 값을 저장할 배열을 생성
        int i = 0;
        int min1 = first;
        int min2 = middle + 1;
        // 각각의 절반으로 나누어진 배열의 가장 좌측 값이 최솟값
        while (min1 <= middle && min2 <= last) {
            if (value[min1] <= value[min2]) {
                tempSorted[i++] = value[min1++];
            } else {
                tempSorted[i++] = value[min2++];
            }
        }
        // 최솟값을 비교해나가면서 새로운 배열에 값들을 저장
        while (min1 <= middle) {
            tempSorted[i++] = value[min1++];
        }
        while (min2 <= last) {
            tempSorted[i++] = value[min2++];
        }
        // 절반으로 나누어진 배열 중 하나의 배열의 인덱스가 중간을 넘어서면
        // 그 순서대로 그대로 임시 배열에 저장
        for (int k = first; k <= last; k++) {
            value[k] = tempSorted[k - first];
        }
        // 임시로 저장한 배열의 값을 원래의 배열에 복사
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoQuickSort(int[] value) {
        quick(value, 0, value.length - 1);
        return (value);
    }

    private static int partition(int[] value, int first, int last) {
        int pivot = value[(first + last) / 2];
        // partitioning 방법은 다양하지만 평균적인 중간 지점의 값을 pivot으로 저장
        while (first <= last) {
            while (value[first] < pivot) first++;
            while (value[last] > pivot) last--;
            if (first <= last) {
                swap(value, first, last);
                first++;
                last--;
            }
         // first와 last의 인덱스가 교차할 때까지 pivot을 기준으로
         // 작은 값들은 왼쪽 큰 값들은 오른쪽에 저장
        }
        return first;
    }

    private static int[] quick(int[] value, int first, int last) {
        int pivotIndex = partition(value, first, last);
        if (first < pivotIndex - 1)
            quick(value, first, pivotIndex - 1);
        if (pivotIndex < last)
            quick(value, pivotIndex, last);
        // pivot을 기준으로 나누어진 값들을 독립적으로 배열의 크기가 0이나 1이 될 때까지 반복 수행
        return value;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoRadixSort(int[] value) {
        if (isNegativeExist(value)) {
            negativeNum(value);
            positiveNum(value);
            int[] sortedValue = new int[value.length];
            System.arraycopy(negativeNum(value), 0, sortedValue, 0, negativeNum(value).length);
            System.arraycopy(positiveNum(value), 0, sortedValue, negativeNum(value).length, positiveNum(value).length);
            for (int i = 0; i < value.length; i++) {
                value[i] = sortedValue[i];
            }
            // 음수의 존재 여부를 판단한 후 음수가 존재한다면 따로 배열에 저장하여 radixSort를 수행
        } else {
            int m = maxValue(value);
            for (int exp = 1; m / exp > 0; exp *= 10)
                countSort(value, exp);
            // 각 자릿수의 값들을 알아내서 비교
            // ex) 1234를 10으로 나눈 나머지는 4(일의 자리) 10으로 나눈 몫을 10으로 나눈 나머지는 3(십의 자리) ...
        }
        return (value);
    }

    private static int maxValue(int[] value) {
        int max = value[0];
        for (int temp : value) {
            if (temp > max) {
                max = temp;
            }
        }
        // 가장 큰 수의 자릿수를 알아내기 위해 가장 큰 수를 리턴
        return max;
    }

    private static void countSort(int[] value, int exp) {
        int[] sortedValue = new int[value.length];
        int[] count = new int[10];
        // 각 자리의 값 0~9를 저장한 count 배열 생성
        Arrays.fill(count, 0);

        for (int i = 0; i < value.length; i++) {
            count[(value[i] / exp) % 10]++;
        // 각 자리의 값이 0~9까지 갯수를 더해가면서 저장
        }
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        // count 값을 누적해서 저장
        }
        for (int i = value.length - 1; i >= 0; i--) {
            sortedValue[count[(value[i] / exp) % 10] - 1] = value[i];
            count[(value[i] / exp) % 10]--;
        // 원래의 순서 유지(stable sort)를 위해 뒤에서 부터 중복된 값을 저장
        }
        for (int i = 0; i < value.length; i++) {
            value[i] = sortedValue[i];
        }
    }

    private static int[] negativeNum(int[] value) {
        int negativeCount = 0;
        int j = 0;
        for (int num : value) {
            if (num < 0)
                negativeCount++;
        }
        int[] negativeToPositive = new int[negativeCount];
        for (int i = 0; i < value.length; i++) {
            if (value[i] < 0)
                negativeToPositive[j++] = -value[i];
        }
        int negativeM = maxValue(negativeToPositive);
        for (int exp = 1; negativeM / exp > 0; exp *= 10) {
            countSort(negativeToPositive, exp);
        }
        int[] negativeArray = new int[negativeToPositive.length];
        for (int i = 0; i < negativeToPositive.length; i++) {
            negativeArray[i] = -negativeToPositive[negativeArray.length - i - 1];
        }
        return negativeArray;
        // 음수의 값들만 따로 양수로 변환 후 radixSort 작업 후 음수의 배열로 저장
    }

    private static int[] positiveNum(int[] value) {
        int positiveCount = 0;
        int j = 0;
        for (int num : value) {
            if (num > 0)
                positiveCount++;
        }
        int[] positiveArray = new int[positiveCount];
        for (int i = 0; i < value.length; i++) {
            if (value[i] > 0)
                positiveArray[j++] = value[i];
        }
        int positiveM = maxValue(positiveArray);
        for (int exp = 1; positiveM / exp > 0; exp *= 10) {
            countSort(positiveArray, exp);
        }
        return positiveArray;
        // 양수의 값들만 따로 positiveNum 배열에 저장
    }

    private static boolean isNegativeExist(int[] value) {
        for (int num : value) {
            if (num < 0)
                return true;
        }
        return false;
        // radixSort를 진행할 때 음수의 존재 여부에 판단
    }

    private static void swap(int[] value, int a, int b) {
        int tempValue = value[a];
        value[a] = value[b];
        value[b] = tempValue;
    }
}
