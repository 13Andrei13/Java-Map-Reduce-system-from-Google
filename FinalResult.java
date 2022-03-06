
public class FinalResult {
    String fileName;
    float rang;
    int maxLung;
    int maxApar;

    public FinalResult(String filename, float rang, int maxLung, int maxApar) {
        this.fileName = filename;
        this.rang = rang;
        this.maxLung = maxLung;
        this.maxApar = maxApar;
    }

    public void afisare() {
        System.out.println(fileName + " cu " + String.format("%.2f", rang) + ","
                + maxLung + "," + maxApar);
    }

    public String getFileName() {
        return fileName;
    }

    public float getRang() {
        return rang;
    }

    public int getMaxLung() {
        return maxLung;

    }

    public int getMaxApar() {
        return maxApar;
    }

}
