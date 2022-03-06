import java.io.*;
import java.util.*;

public class MyThread extends Thread {

    private final int thread_id;
    public List<Integer> task = new ArrayList<>();
    public String file;
    public double skipNum;
    public double chunck;
    public String linie;
    FileReader f;
    BufferedReader b;
    public String mutex = "LOCK";

    MyThread(int id, List<Integer> task) {
        this.thread_id = id;
        this.task.addAll(task);
    }

    @Override
    public void run() {

        for (int i = 0; i < task.size(); i++) {
            Map<Integer, Integer> aparitions = new HashMap<>();
            List<String> longs = new ArrayList<>();

            linie = "";
            file = Tema2.tasks.get(task.get(i)).getFileName();
            skipNum = Tema2.tasks.get(task.get(i)).getSkipNum();
            chunck = Tema2.tasks.get(task.get(i)).getChunck();

            try {
                f = new FileReader(file);
                b = new BufferedReader(f);

                if (chunck == Tema2.chunck) {
                    if (skipNum != 0) {
                        chunck++;
                    }

                    if (skipNum == 0) {
                        for (int j = 0; j < chunck; j++) {
                            linie = linie + (char) b.read();
                        }
                        char c = (char) b.read();
                        char ante = linie.charAt((int) (chunck - 1));
                        if ((('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) &&
                                (('a' <= ante && ante <= 'z') || ('A' <= ante && ante <= 'Z'))) {
                            while (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
                                linie = linie + c;
                                c = (char) b.read();
                            }
                        }

                    } else if (skipNum != 0) {
                        b.skip((long) (skipNum - 1));

                        char c = (char) b.read();
                        chunck--;
                        char urm = (char) b.read();
                        chunck--;

                        if ((('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) &&
                                (('a' <= urm && urm <= 'z') || ('A' <= urm && urm <= 'Z'))

                        ) {
                            while (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
                                c = (char) b.read();
                                chunck--;
                            }
                        } else {
                            linie = linie + urm;
                        }
                        for (int j = 0; j < chunck; j++) {
                            linie = linie + (char) b.read();
                        }
                        c = (char) b.read();
                        char ante = linie.charAt((int) (linie.length() - 1));
                        if ((('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) &&
                                (('a' <= ante && ante <= 'z') || ('A' <= ante && ante <= 'Z'))) {
                            while (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
                                linie = linie + c;
                                c = (char) b.read();
                            }
                        }

                    }

                } else {
                    if (skipNum != 0) {
                        chunck++;
                    }

                    b.skip((long) (skipNum - 1));

                    char c = (char) b.read();
                    chunck--;
                    char urm = (char) b.read();
                    chunck--;
                    if ((('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) &&
                            (('a' <= urm && urm <= 'z') || ('A' <= urm && urm <= 'Z'))

                    ) {
                        while (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
                            c = (char) b.read();
                            chunck--;
                        }
                    } else {
                        linie = linie + urm;
                    }
                    for (int j = 0; j < chunck; j++) {
                        linie = linie + (char) b.read();
                    }

                }

                int ok = 0;
                int lungime = 0;
                int max = 0;
                for (int g = 0; g < linie.length(); g++) {

                    char c = linie.charAt(g);

                    if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')
                            || ('0' <= c && c <= '9')) {
                        lungime++;
                        ok = 0;

                    } else {
                        if (lungime != 0) {

                            if (aparitions.containsKey(lungime) == true) {
                                Integer apar = aparitions.get(lungime);

                                apar++;
                                aparitions.put(lungime, apar);

                            } else {
                                aparitions.put(lungime, 1);
                            }
                            if (lungime > max) {
                                max = lungime;
                            }
                            lungime = 0;
                            ok = 1;
                        }
                        ok = 1;
                    }
                }
                if (ok == 0) {
                    if (lungime != 0) {
                        if (lungime > max) {
                            max = lungime;
                        }
                        if (aparitions.containsKey(lungime) == true) {
                            Integer apar = aparitions.get(lungime);

                            apar++;
                            aparitions.put(lungime, apar);

                        } else {
                            aparitions.put(lungime, 1);
                        }
                    }

                }

                String cuvant = "";
                String cuv = "";
                ok = 0;
                lungime = 0;
                for (int g = 0; g < linie.length(); g++) {

                    char c = linie.charAt(g);

                    if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
                        lungime++;
                        ok = 0;
                        cuv = cuv + c;

                    } else {
                        if (lungime == max) {
                            cuvant = cuv;
                            lungime = 0;
                            longs.add(cuvant);

                        } else {
                            cuv = "";
                            ok = 1;
                            lungime = 0;
                        }
                        ok = 1;
                        lungime = 0;

                    }
                }
                if (ok == 0) {
                    if (lungime == max) {
                        cuvant = cuv;

                        lungime = 0;
                        longs.add(cuv);

                    }
                }

                String fisier = Tema2.tasks.get(task.get(i)).getFileName();
                Dictionary dict = new Dictionary(fisier, aparitions, longs);

                synchronized (mutex) {

                    Tema2.dicFinal.add(dict);
                }

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
            try {
                b.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
            try {
                f.close();
            } catch (IOException e) {

                e.printStackTrace();
            }

        }

    }
}
