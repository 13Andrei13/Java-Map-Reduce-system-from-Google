import java.util.List;
import java.util.Map;

public class Dictionary {

    String fileName;
    Map<Integer, Integer> aparitions;
    List<String> longs;

   

    public Dictionary(String fileName, Map<Integer, Integer> aparitions, List<String> longs) {
        this.fileName = fileName;
        this.aparitions = aparitions;
        this.longs = longs;

    }

    public void afisare() {
        System.out.println("--------------------------------------");
        System.out.println("fileName = " + fileName);
        for (Map.Entry<Integer, Integer> entry : aparitions.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        for (int i = 0; i < longs.size(); i++) {
            System.out.println(longs.get(i));
        }
        System.out.println("-------------------");
    }

    public String getFileName() {
        return fileName;
    }

    public Map<Integer, Integer> getAparitions() {
        return aparitions;
    }

    public List<String> getLongs() {
        return longs;
    }

}
