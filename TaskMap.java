public class TaskMap {

    String fileName;
    double skipNum;
    double chunck;

    public TaskMap(String fileName, double skipNum, double chunck) {
        this.fileName = fileName;
        this.chunck = chunck;
        this.skipNum = skipNum;
    }

    public void afisare() {
        System.out.println("fileName = " + fileName);

        System.out.println("skipNum = " + skipNum);
        System.out.println("chunck = " + chunck);

    }

    public TaskMap() {
        this.fileName = " ";
        chunck = 0;
        skipNum = 0;

    }

    public String getFileName() {
        return fileName;
    }

    public double getSkipNum() {
        return skipNum;

    }

    public double getChunck() {
        return chunck;
    }

}
