import java.io.*;

public class LCR {
    private static int[] process_ids;

    public static void readRingInfo() throws IOException {
        File input_file = new File("src/input.dat");
        BufferedReader reader = new BufferedReader(new FileReader(input_file.getAbsoluteFile()));
        int process_count = Integer.parseInt(reader.readLine());
        process_ids = new int[process_count];
        for (int i = 0; i < process_count; i++) {
            process_ids[i] = Integer.parseInt(reader.readLine());
        }
        reader.close();
    }

    public static void main(String[] args) throws IOException {
        readRingInfo();
        Thread masterProcess = new Thread(new MasterProcess(process_ids));
        masterProcess.start();
    }
}
