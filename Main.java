import java.io.*;
import java.util.*;

public class Tema2 {
    public static String file;
    public static int W;
    public static double chunck;
    public static int number_files;
    public static List<String> files;
    public static int index = 0;
    public static int indexThread = 0;
    public static List<TaskMap> tasks = new ArrayList<>();
    public static String outFile;

    public static Map<Integer, Dictionary> tasksResults = new HashMap<>();
    public static Map<Integer, Integer> aparitions = new HashMap<>();

    public static List<Dictionary> dicFinal = new ArrayList<>();
    public static List<Dictionary> dicFinalReduce = new ArrayList<>();

    public static List<FinalResult> finalResults = new ArrayList<>();
    public static float[] v;

    public static int Fib(Integer a) {
        int b = 1;
        int c = 1;

        for (int j = 1; j <= a - 1; j++) {
            int aux = c;
            c = c + b;
            b = aux;

        }

        return c;
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Usage: Tema2 <workers> <in_file> <out_file>");
            return;
        }
        // initializare
        W = Integer.parseInt(args[0]);
        outFile = args[2];

        files = new ArrayList<>();
        Thread[] threads = new Thread[W];
        Thread[] threadsReduce = new Thread[W];
        // --------------------
        try (FileInputStream fis = new FileInputStream(args[1]);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);) {

            String linie = br.readLine();
            chunck = Double.parseDouble(linie);

            linie = br.readLine();
            number_files = Integer.parseInt(linie);

            linie = br.readLine();
            while (linie != null) {

                files.add(linie.toString());
                linie = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        int z;
        for (int i = 0; i < number_files; i++) {
            FileReader f = new FileReader(files.get(i));
            BufferedReader b = new BufferedReader(f);
            z = b.read();

            double skipNum = 0;
            double disp = 0;
            while (z != -1) {

                disp++;
                if (disp == chunck) {
                    TaskMap task = new TaskMap(files.get(i), skipNum, chunck);
                    tasks.add(task);
                    skipNum = skipNum + chunck;
                    disp = 0;
                }

                z = b.read();

            }
            TaskMap task = new TaskMap(files.get(i), skipNum, disp);
            tasks.add(task);

            b.close();
            f.close();
        }

        // pornim workeri pentru map
        indexThread = 0;
        for (int i = 0; i < W; i++) {
            List<Integer> taskid = new ArrayList<>();

            index = i;
            while (index < tasks.size()) {
                taskid.add(Integer.valueOf(index));
                index = index + W;
            }
            threads[i] = new Thread(new MyThread(i, taskid));
            threads[i].start();

        }

        for (int i = 0; i < W; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // -------

        // pornim workeri pentru reduce
        for (int i = 0; i < W; i++) {
            List<Integer> taskid = new ArrayList<>();
            index = i;
            while (index < files.size()) {
                taskid.add(index);
                index = index + W;
            }
            threadsReduce[i] = new Thread(new ReduceThread(i, i, taskid));
            threadsReduce[i].start();

        }
        for (int i = 0; i < W; i++) {
            try {
                threadsReduce[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // -----

        for (int i = 0; i < dicFinalReduce.size(); i++) {
            int numar_cuvinte = 0;
            float rang = 0;
            Dictionary dictionary = dicFinalReduce.get(i);
            for (Map.Entry<Integer, Integer> entry : dictionary.getAparitions().entrySet()) {
                numar_cuvinte = numar_cuvinte + entry.getValue();

            }

            for (Map.Entry<Integer, Integer> entry : dictionary.getAparitions().entrySet()) {

                rang = rang + ((float) (Fib(entry.getKey()) * entry.getValue()) / numar_cuvinte);

            }
            int apar = 0;
            for (Map.Entry<Integer, Integer> entry : dictionary.getAparitions().entrySet()) {

                if (entry.getKey() == dictionary.longs.get(0).length()) {
                    apar = entry.getValue();
                }

            }

            FinalResult fResult = new FinalResult(dictionary.getFileName(), rang, dictionary.longs.get(0).length(),
                    apar);
            finalResults.add(fResult);
        }

        v = new float[finalResults.size()];
        for (int i = 0; i < finalResults.size(); i++) {
            v[i] = finalResults.get(i).getRang();

        }
        int ok = 1;
        do {
            ok = 1;
            for (int i = 0; i < v.length - 1; i++) {
                if (v[i] < v[i + 1]) {
                    ok = 0;
                    float aux = v[i];
                    v[i] = v[i + 1];
                    v[i + 1] = aux;
                }
            }

        } while (ok == 0);

        RandomAccessFile file = new RandomAccessFile(outFile, "rw");
        for (int i = 0; i < v.length; i++) {

            for (int j = 0; j < finalResults.size(); j++) {
                if (v[i] == finalResults.get(j).getRang()) {
                    FinalResult fResult = finalResults.get(j);
                    String rangS = String.format("%.2f", v[i]);
                    String fileNameNew = fResult.getFileName().replace("tests/files/", "");
                    String MaxApar = String.valueOf(fResult.getMaxApar());
                    String MaxLung = String.valueOf(fResult.getMaxLung());

                    file.write(fileNameNew.getBytes());
                    file.write(",".getBytes());
                    file.write(rangS.getBytes());
                    file.write(",".getBytes());
                    file.write(MaxLung.getBytes());
                    file.write(",".getBytes());
                    file.write(MaxApar.getBytes());
                    file.write("\n".getBytes());

                }
            }
        }
        file.close();

    }

}
