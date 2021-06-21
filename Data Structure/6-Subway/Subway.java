import java.io.*;
import java.util.*;

public class Subway {
    public static void main(String[] args) {
        SubwayDB sdb = SubwayDB.getInstance();
        try {
            sdb.readDBFromFile(args[0]);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0)
                    break;
                command(sdb, input);
            }
        } catch (IOException e) {
            System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
        }
    }

    private static void command(SubwayDB sdb, String input) throws FileNotFoundException {
        String[] fromTo = input.split(" ");
        ArrayList<String> startNumbers = sdb.getKeyByValue(fromTo[0]);
        ArrayList<String> endNumbers = sdb.getKeyByValue(fromTo[1]);
        ArrayList[] path = new ArrayList[startNumbers.size() * endNumbers.size()];
        int count = 0;
        for (String s : startNumbers) {
            for (String e : endNumbers) {
                path[count] = new ArrayList<Station>();
                path[count++] = sdb.GetShortestPathByDijkstra(s, e);
            }
        }
        // 출발역과 도착역 간의 경로 쌍을 모두 찾는다.
        ArrayList<Station> result = sdb.getShortestPath(path);
        sdb.printShortestPath(result, result.get(0));
        // 경로 중 소요 시간이 가장 적은 것을 선택하여 출력한다.
    }
}

class SubwayDB {
    private static final SubwayDB sdb = new SubwayDB();
    private HashMap<String, Station> subwayMap = new HashMap<>();
    private HashMap<String, ArrayList<String>> transferMap = new HashMap<>();

    public SubwayDB() {
    }

    private HashMap<String, Station> clone(HashMap<String, Station> subwayMap) {
        HashMap<String, Station> result = new HashMap<>();
        for (String key : subwayMap.keySet()) {
            result.put(key, new Station(subwayMap.get(key)));
        }
        return result;
    }

    public static SubwayDB getInstance() {
        return sdb;
    }

    public HashMap<String, Station> getSubwayMap() {
        return subwayMap;
    }

    public void readDBFromFile(String fileName) throws IOException {
        try {
            subwayMap = new HashMap<>();
            File file = new File(fileName);
            BufferedReader bReader = new BufferedReader(new FileReader(file));
            String lineReader = bReader.readLine();
            String DB1 = "";
            String DB2 = "";
            while (!lineReader.isBlank()) {
                DB1 += (lineReader + "\n");
                lineReader = bReader.readLine();
            }
            lineReader = bReader.readLine();
            while (lineReader != null) {
                DB2 += (lineReader + "\n");
                lineReader = bReader.readLine();
            }
            String[] stationDB = DB1.split("\\n");
            String[] adjacentDB = DB2.split("\\n");
            bReader.close();
            makeTransferDB(stationDB);
            makeStationDB(stationDB);
            makeAdjacentDB(adjacentDB);
        } catch (IOException e) {
            System.out.println("오류 : " + e.toString());
        }
    } // 파일을 한 줄씩 읽어서 stationDB / adjacentDB / transferDB 생성

    public void makeStationDB(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
            String[] stationInfo = lines[i].split(" ");
            Station s = new Station(stationInfo);
            subwayMap.put(s.stationNumber, s);
        }
    } // 역의 정보 저장


    public void makeAdjacentDB(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
            String[] adjacentInfo = lines[i].split(" ");
            Edge e = new Edge(adjacentInfo);
            subwayMap.get(e.getFrom()).adjacentList.add(e);
        }
        Iterator<String> transIt = transferMap.keySet().iterator();
        while (transIt.hasNext()) {
            String keys = transIt.next();
            ArrayList<String> tempList = transferMap.get(keys);
            if (tempList.size() > 1) {
                // 환승역이라면
                String[] tempArray = tempList.toArray(new String[0]);
                for (String stationNumber : tempList) {
                    for (int i = 0; i < tempArray.length; i++) {
                        if (!stationNumber.equals(tempArray[i])) {
                            subwayMap.get(stationNumber).adjacentList.add(new Edge(stationNumber, tempArray[i], 5));
                            // 환승 구간일 경우 소요시간 5 입력
                        }
                    }
                }
            }
        }
    } // 인접 역 간의 정보 저장


    public void makeTransferDB(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
            String[] stationInfo = lines[i].split(" ");
            String stationNumber = stationInfo[0];
            String stationName = stationInfo[1];
            if (transferMap.containsKey(stationName)) {
                transferMap.get(stationInfo[1]).add(stationNumber);
            } else {
                ArrayList<String> stationNumbers = new ArrayList<>();
                stationNumbers.add(stationNumber);
                transferMap.put(stationName, stationNumbers);
            }
        }
    }  // 환승역 정보 저장

    public ArrayList<String> getKeyByValue(String from) {
        Iterator<String> it = subwayMap.keySet().iterator();
        ArrayList<String> start = new ArrayList<>();
        while (it.hasNext()) {
            String keys = it.next();
            if (subwayMap.get(keys).stationName.equals(from)) {
                start.add(keys);
            }
        }
        return start;
    }  // 입력 받는 값이 역 이름이기 때문에 unique 보장되지 않는다. 같은 이름(고유 번호 다름) list 저장

    public ArrayList<Station> GetShortestPathByDijkstra(String from, String to) {
        HashMap<String, Station> subwayMapCopy = clone(subwayMap);
        // 한 input당 찾을 수 있는 경로 모두 찾기 위해 subwayMap을 복사해서 사용
        Station startingStation = subwayMapCopy.get(from);
        Station endStation = subwayMapCopy.get(to);
        PriorityQueue<Station> candidate = new PriorityQueue<>();
        startingStation.setDistance(0);
        // 시작 지점 distance 초기화
        candidate.add(startingStation);
        while (!candidate.isEmpty()) {
            Station shortestStation = candidate.poll();
            shortestStation.setVisited(true);
            if (shortestStation.getStationName().equals(endStation.getStationName())) {
                break;
            }
            // 도착역을 찾는 순간 루프 탈출
            for (Edge e : shortestStation.getAdjacentList()) {
                Station v = subwayMapCopy.get(e.getTo());
                int Edge = e.getW();
                int distance = shortestStation.getDistance() + Edge;
                if (distance < v.getDistance() && !v.isVisited()) {
                    v.setPreStation(shortestStation);
                    v.setDistance(distance);
                    candidate.add(v);
                }
            }
        }
        ArrayList<Station> path = new ArrayList<>();
        Station temp = endStation;
        while (temp.getPreStation() != null) {
            path.add(temp);
            temp = temp.getPreStation();
        }
        path.add(temp);
        // 이전 역 정보를 이용하여 역순으로 정보 저장
        return path;
    }

    public ArrayList<Station> getShortestPath(ArrayList[] path) {
        ArrayList<Station> result = path[0];
        for (int i = 1; i < path.length; i++) {
            if (result.get(0).getDistance() > ((Station) path[i].get(0)).getDistance())
                result = path[i];
        } // 찾은 경로의 마지막 역의 소요 시간을 비교하여 최솟값을 가지는 경로 리턴
        return result;
    }

    public void printShortestPath(ArrayList<Station> path, Station d) {
        Collections.reverse(path);// 저장된 경로 역순으로 바꿈
        String start = path.get(0).stationName;
        String destination = d.stationName;
        String buffer = start;
        String shortestPath = "";
        for (Station s : path) {
            if (buffer.equals(s.stationName)) {
                if (!buffer.equals(start) && !buffer.equals(destination)) {
                    buffer = "[" + buffer + "]";
                } // 출발역과 도착역이 중복되는 경우는 제외하고 [  ] 출력
            } else {
                shortestPath += buffer + " ";
                buffer = s.stationName;
            }
        }
        shortestPath += buffer;
        System.out.println(shortestPath);
        System.out.println(d.distance);
    }
}

class Station implements Comparable<Station> {
    String stationNumber;
    String stationName;
    String lineNumber;
    int distance;
    ArrayList<Edge> adjacentList;
    Station preStation;
    boolean visited;

    public Station(String[] data) {
        this.stationNumber = data[0];
        this.stationName = data[1];
        this.lineNumber = data[2];
        this.distance = Integer.MAX_VALUE;
        this.adjacentList = new ArrayList<>();
        this.preStation = null;
        this.visited = false;
    }

    public Station(String staNum, String staName, String lineNum) {
        this.stationNumber = staNum;
        this.stationName = staName;
        this.lineNumber = lineNum;
        this.distance = Integer.MAX_VALUE;
        this.adjacentList = new ArrayList<>();
        this.preStation = null;
        this.visited = false;
    }

    Station(Station s) {
        stationNumber = s.stationNumber;
        stationName = s.stationName;
        lineNumber = s.lineNumber;
        distance = s.distance;
        if (s.getPreStation() != null)
            preStation = new Station(s.getPreStation().stationNumber, s.getPreStation().stationName, s.getPreStation().lineNumber);
        adjacentList = s.adjacentList;
    }

    public String getStationNumber() {
        return stationNumber;
    }

    public String getStationName() {
        return stationName;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int d) {
        this.distance = d;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public ArrayList<Edge> getAdjacentList() {
        return adjacentList;
    }

    public Station getPreStation() {
        return preStation;
    }

    public Station setPreStation(Station preStation) {
        return this.preStation = preStation;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public int compareTo(Station s) {
        return this.distance - s.distance;
    }

    @Override
    public String toString() {
        return stationName;
    }
} // 역 고유번호 / 역 이름 / 역 노선번호 / 인접리스트 저장하고 있는 Database

class Edge {
    String from;
    String to;
    int w;

    public Edge(String[] data) {
        this.from = data[0];
        this.to = data[1];
        this.w = Integer.parseInt(data[2]);
    }

    public Edge(String f, String t, int w) {
        this.from = f;
        this.to = t;
        this.w = w;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }
} // 인접한 역을 이어주는 Edge Class

// output.txt와 myoutput.txt가 일치하지 않다고 나오는 몇몇 testcase 모두 직접 확인해보았을 때,
// 소요시간은 모두 동일하고(최단 경로) 올바른 경로인 경우가 한 output당 한 두 개씩 존재하는 것을 확인하였습니다.