
import java.util.*;

public class ReduceThread implements Runnable {
    private final int thread_id;
    public int file_number;
    public String fileName;
    public List<Dictionary> myList;
    public Map<Integer, Integer> aparitions;
    public List<String> longs;
    public String mutex = "LOCK";
    public List<Integer> numberTasks = new ArrayList<>();

    ReduceThread(int id, int file_number, List<Integer> numberTasks) {
        this.thread_id = id;
        this.file_number = file_number;
        this.numberTasks = numberTasks;

    }

    @Override
    public void run() {
        for (int x = 0; x < numberTasks.size(); x++) {
            aparitions = new HashMap<>();
            longs = new ArrayList<>();
            myList = new ArrayList<>();
            // initializez fisierul
            fileName = Tema2.files.get(numberTasks.get(x));
            // preiau dictionarele
            for (int i = 0; i < Tema2.dicFinal.size(); i++) {
                if (Tema2.dicFinal.get(i).getFileName() == fileName) {

                    myList.add(Tema2.dicFinal.get(i));

                }
            }

            // combin mapurile
            for (int i = 0; i < myList.size(); i++) {
                for (Map.Entry<Integer, Integer> entry : myList.get(i).getAparitions().entrySet()) {
                    if (aparitions.containsKey(entry.getKey()) == true) {
                        Integer apar = aparitions.get(entry.getKey());
                        apar = apar + entry.getValue();
                        aparitions.put(entry.getKey(), apar);
                    } else {
                        aparitions.put(entry.getKey(), entry.getValue());
                    }

                }
            }

            // aflu lungimea maxima
            int max = 0;
            for (Map.Entry<Integer, Integer> entry : aparitions.entrySet()) {
                if (entry.getKey() > max) {
                    max = entry.getKey();
                }
            }

            for (int i = 0; i < myList.size(); i++) {
                List<String> longG = myList.get(i).getLongs();
                for (int j = 0; j < longG.size(); j++) {
                    if (longG.get(j).length() == max) {
                        longs.add(longG.get(j));

                    }
                }
            }
            Dictionary dict = new Dictionary(fileName, aparitions, longs);
            synchronized (mutex) {
                Tema2.dicFinalReduce.add(dict);
            }

        }

    }

}
