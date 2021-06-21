import java.io.*;

public class Matching {
    public static void main(String args[]) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0)
                    break;

                command(input);
            } catch (IOException e) {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }
        }
    }

    private static void command(String input) throws FileNotFoundException {
        PatternTable pt = PatternTable.getInstance();
        char operator = input.charAt(0);
        if (operator == '<') {
            String[] file = input.split(" ");
            String fileName = file[1]; // 파일 경로
            pt.readTextFromFile(fileName);
        } else if (operator == '@') {
            String[] index = input.split(" ");
            int indexNumber = Integer.parseInt(index[1]); // 출력할 입력 번호
            System.out.println(pt.searchSlot(indexNumber));
        } else if (operator == '?') {
            String patternString = input.substring(2); // 패턴
            pt.searchPattern(patternString);
        }
    }
}

class PatternTable {
    private static final PatternTable pt = new PatternTable();
    private hashTable<Pattern> patternTable;

    public PatternTable() {
    }

    public static PatternTable getInstance() {
        return pt;
    }

    public void readTextFromFile(String fileName) {
        try {
            patternTable = new hashTable<>();
            File file = new File(fileName);
            BufferedReader bReader = new BufferedReader(new FileReader(file));
            String lineReader;
            String text = "";
            while ((lineReader = bReader.readLine()) != null) {
                text += (lineReader + "\n");
            }
            String[] lines = text.split("\\n");
            bReader.close();
            readLine(lines);
        } catch (IOException e) {
            System.out.println("오류 : " + e.toString());
        }
    } // 파일을 한 줄씩 읽어서 String 배열로 저장

    public void readLine(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length() - 5; j++) {
                Pattern pattern = new Pattern(lines[i].substring(j, j + 6), i + 1, j + 1);
                int hashCode = hx(pattern.toString());
                patternTable.insert(pattern, hashCode);
            }
        }
    } // 한 줄씩 6개의 문자로 쪼개서 삽입

    public static int hx(String str) {
        int sumOfValue = 0;
        for (int i = 0; i < str.length(); i++) {
            sumOfValue += str.charAt(i);
        }
        int hx = sumOfValue % 100;
        return hx;
    } // 해쉬코드 계산

    public String searchSlot(int index) {
        AVLTree<Pattern> indexSlot = patternTable.treeAt(index);
        if (indexSlot == null) {
            return "EMPTY";
        } else
            return indexSlot.getRoot().printAVL();
    }

    public void searchPattern(String target) {
        LinkedList<Pattern> foundList = new LinkedList<>();
        int n = 0;
        for (int i = 0; i < target.length() - 5; i++) {
            if (i + 6 == target.length() || i == 6 * n) {
                n++;
                Pattern pattern = new Pattern(target.substring(i, i + 6));
                int hashCode = hx(pattern.toString());
                if (patternTable.search(pattern, hashCode) != null) {
                    LinkedList<Pattern> newList = patternTable.search(pattern, hashCode).getList();
                    if (i == 0)
                        foundList = newList.copy();
                    else {
                        LinkedListIterator<Pattern> foundListIterator = new LinkedListIterator<>(foundList);
                        while (foundListIterator.getCurr() != null) {
                            final int foundPatternLine = foundListIterator.getCurr().getItem().getLine();
                            final int foundPatternIndex = foundListIterator.getCurr().getItem().getIndex();
                            LinkedListIterator<Pattern> newListIterator = new LinkedListIterator<>(newList);
                            while (newListIterator.getCurr() != null) {
                                final int newPatternLine = newListIterator.getCurr().getItem().getLine();
                                final int newPatternIndex = newListIterator.getCurr().getItem().getIndex();
                                if (newPatternLine < foundPatternLine)
                                    newListIterator.next();
                                else if (newPatternLine == foundPatternLine) {
                                    if (newPatternIndex < foundPatternIndex + i)
                                        newListIterator.next();
                                    else if (newPatternIndex == foundPatternIndex + i)
                                        break;
                                    else {
                                        foundListIterator.remove();
                                        break;
                                    }
                                } else {
                                    foundListIterator.remove();
                                    break;
                                }
                            }
                            if (newListIterator.getCurr() == null) {
                                foundListIterator.remove();
                            }
                            foundListIterator.next();
                        }
                    }
                } else {
                    foundList = new LinkedList<>();
                    break;
                }
            }
        }
        if (foundList.size() == 0)
            System.out.println("(0, 0)");
        else {
            LinkedListNode<Pattern> firstNode = foundList.first();
            String locations = "";
            for (int j = 0; j < foundList.size() - 1; j++) {
                locations += (firstNode.getItem().getLocation() + " ");
                firstNode = firstNode.getNext();
            }
            locations += firstNode.getItem().getLocation();
            System.out.println(locations);
        }
    }
} // target -> 길이가 6인 패턴으로 저장 -> 이 패턴을 그 다음 길이가 6인 패턴과 비교 후(Line/Index) 연속되면 유지 그렇지 않으면 삭제

class hashTable<T extends Comparable<T>> {
    private static final int tableSize = 100;
    private AVLTree<T>[] table;

    public hashTable() {
        this.table = new AVLTree[tableSize];
    }

    public void insert(T item, int hashCode) {
        if (table[hashCode] == null) {
            table[hashCode] = new AVLTree<>();
        }
        table[hashCode].insert(item);
    }

    public AVLTreeNode search(T item, int hashCode) {
        if (table[hashCode] == null) {
            return null;
        }
        return table[hashCode].search(item);
    }

    public AVLTree<T> treeAt(int index) {
        return table[index];
    }
}

class LinkedListNode<T> {
    private T item;
    private LinkedListNode<T> next;

    public LinkedListNode(T obj) {
        this.item = obj;
        this.next = null;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T obj) {
        this.item = obj;
    }

    public void setNext(LinkedListNode<T> next) {
        this.next = next;
    }

    public LinkedListNode<T> getNext() {
        return this.next;
    }

    public final void insertNext(T obj) {
        setNext(new LinkedListNode<>(obj));
    }

    public final void removeNext() {
        setNext(next.getNext());
    }
}

class LinkedList<T> {
    LinkedListNode<T> head;
    int numItems;

    public LinkedList() {
        head = new LinkedListNode<T>(null);
    }

    public boolean isEmpty() {
        return head.getNext() == null;
    }

    public int size() {
        return numItems;
    }

    public LinkedListNode<T> first() {
        return head.getNext();
    }

    public void insert(T item) {
        LinkedListNode<T> last = head;
        while (last.getNext() != null) {
            last = last.getNext();
        }
        last.insertNext(item);
        numItems += 1;
    }

    public LinkedList<T> copy() {
        LinkedList<T> copyList = new LinkedList<>();
        LinkedListNode<T> copyLast = copyList.head;
        LinkedListNode<T> last = head;
        while (last.getNext() != null) {
            last = last.getNext();
            copyLast.insertNext(last.getItem());
            copyLast = copyLast.getNext();
            copyList.numItems++;
        }
        return copyList;
    }
}

class LinkedListIterator<T> {
    private LinkedList<T> list;
    private LinkedListNode<T> curr;
    private LinkedListNode<T> prev;

    public LinkedListNode<T> getCurr() {
        return curr;
    }

    public LinkedListIterator(LinkedList<T> list) {
        this.list = list;
        this.curr = list.first();
        this.prev = list.head;
    }

    public void next() {
        prev = curr;
        curr = curr.getNext();
    }

    public void remove() {
        prev.removeNext();
        list.numItems--;
        curr = prev;
        prev = null;
    }
}

class AVLTreeNode<T extends Comparable<T>> {
    T item;
    AVLTreeNode<T> leftChild;
    AVLTreeNode<T> rightChild;
    int height;
    LinkedList<T> list;

    public AVLTreeNode(T item) {
        this.item = item;
        this.list = new LinkedList<>();
        this.list.insert(item);
        this.leftChild = null;
        this.rightChild = null;
        this.height = 0;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public AVLTreeNode<T> getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(AVLTreeNode leftChild) {
        this.leftChild = leftChild;
    }

    public AVLTreeNode<T> getRightChild() {
        return rightChild;
    }

    public void setRightChild(AVLTreeNode rightChild) {
        this.rightChild = rightChild;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public LinkedList<T> getList() {
        return list;
    }

    @Override
    public String toString() {
        return this.item.toString();
    }

    public String printAVL() {
        if (getItem() != null) {
            String rootString = getItem().toString();
            String leftChildString = "";
            String rightChildString = "";
            if (getLeftChild() != null) {
                leftChildString = " " + getLeftChild().printAVL();
            }
            if (getRightChild() != null) {
                rightChildString = " " + getRightChild().printAVL();
            }
            return rootString + leftChildString + rightChildString;
        } else
            return "EMPTY";
    }
} // 전위순회 방식으로 AVLTree 출력

class AVLTree<T extends Comparable<T>> {
    private AVLTreeNode<T> root;

    public AVLTreeNode<T> getRoot() {
        return root;
    }

    public void insert(T item) {
        root = insertItem(root, item);
    }

    private AVLTreeNode<T> insertItem(AVLTreeNode<T> tNode, T item) {
        if (tNode == null) {
            tNode = new AVLTreeNode<>(item);
            return tNode;
        } else {
            if (item.compareTo(tNode.getItem()) < 0) {
                tNode.setLeftChild(insertItem(tNode.getLeftChild(), item));
            } else if (item.compareTo(tNode.getItem()) > 0) {
                tNode.setRightChild(insertItem(tNode.getRightChild(), item));
            } else {
                tNode.getList().insert(item);
            }
        }
        tNode.setHeight(Math.max(height(tNode.getLeftChild()), height(tNode.getRightChild())) + 1);
        tNode = insertRotation(item, tNode);
        return tNode;
    } // 주어진 item과 AVLTree의 각 노드의 item 비교(String) -> 삽입

    private int height(AVLTreeNode<T> tNode) {
        return tNode == null ? -1 : tNode.getHeight();
    }

    private int getBalance(AVLTreeNode<T> tNode) {
        if (tNode == null) {
            return 0;
        }
        return height(tNode.getLeftChild()) - height(tNode.getRightChild());
    } // 높이 차 = 왼쪽 서브트리의 높이 - 오른쪽 서브트리의 높이
      // k일 경우 왼쪽 서브트리의 높이가 k만큼 -k일 경우 오른쪽 서브트리의 높이가 k만큼 높음

    private AVLTreeNode<T> rightRotation(AVLTreeNode<T> parent) {
        AVLTreeNode<T> newParent = parent.getLeftChild();
        AVLTreeNode<T> rightChildOfLeftChild = newParent.getRightChild();
        newParent.setRightChild(parent);
        parent.setLeftChild(rightChildOfLeftChild);
        parent.setHeight(Math.max(height(parent.getLeftChild()), height(parent.getRightChild())) + 1);
        newParent.setHeight(Math.max(height(newParent.getLeftChild()), height(newParent.getRightChild())) + 1);
        return newParent;
    }

    private AVLTreeNode<T> leftRotation(AVLTreeNode<T> parent) {
        AVLTreeNode<T> newParent = parent.getRightChild();
        AVLTreeNode<T> LeftChildOfRightChild = newParent.getLeftChild();
        newParent.setLeftChild(parent);
        parent.setRightChild(LeftChildOfRightChild);
        parent.setHeight(Math.max(height(parent.getLeftChild()), height(parent.getRightChild())) + 1);
        newParent.setHeight(Math.max(height(newParent.getLeftChild()), height(newParent.getRightChild())) + 1);
        return newParent;
    }

    private AVLTreeNode<T> insertRotation(T item, AVLTreeNode<T> tNode) {
        int bal = getBalance(tNode);

        if (bal == 2 && item.compareTo(tNode.getLeftChild().getItem()) < 0) {
            return rightRotation(tNode); // LL
        } else if (bal == 2 && item.compareTo(tNode.getLeftChild().getItem()) > 0) {
            tNode.setLeftChild(leftRotation(tNode.getLeftChild()));
            return rightRotation(tNode); // LR
        } else if (bal == -2 && item.compareTo(tNode.getRightChild().getItem()) < 0) {
            tNode.setRightChild(rightRotation(tNode.getRightChild()));
            return leftRotation(tNode); // RL
        } else if (bal == -2 && item.compareTo(tNode.getRightChild().getItem()) > 0) {
            return leftRotation(tNode); // RR
        }
        return tNode;
    } // 삽입 이후 균형이 깨지는 최초 부모 노드에서 4가지 경우로 나누어 회전을 수행

    public AVLTreeNode<T> search(T item) {
        AVLTreeNode<T> curr = root;
        if (root == null) {
            return null;
        } else {
            while (true) {
                if (curr.getItem().compareTo(item) > 0) {
                    if (curr.getLeftChild() == null)
                        return null;
                    else
                        curr = curr.getLeftChild();
                } else if (curr.getItem().compareTo(item) < 0) {
                    if (curr.getRightChild() == null)
                        return null;
                    else
                        curr = curr.getRightChild();
                } else
                    return curr;
            }
        }
    }
} // 주어진 item과 AVLTree의 각 노드의 item 비교(String) -> 같은 것이 존재하면 해당 노드 리턴 아니면 null 리턴

class Pattern implements Comparable<Pattern> {
    private int line;
    private int index;
    private String pattern;

    public Pattern(String pattern) {
        this.pattern = pattern;
    }

    public Pattern(String pattern, int line, int index) {
        this.pattern = pattern;
        this.line = line;
        this.index = index;
    }

    public int getLine() {
        return line;
    }

    public int getIndex() {
        return index;
    }

    public String getLocation() {
        return "(" + line + ", " + index + ")";
    }

    @Override
    public int compareTo(Pattern pattern) {
        return this.pattern.compareTo(pattern.pattern);
    }

    @Override
    public String toString() {
        return pattern;
    }
}

