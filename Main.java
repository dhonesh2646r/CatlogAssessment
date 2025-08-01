import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class CatalogSecretSolver {

    public static BigInteger decode(String value, int base) {
        return new BigInteger(value, base);
    }

    public static BigInteger lagrangeInterpolation(List<BigInteger> xs, List<BigInteger> ys) {
        BigInteger result = BigInteger.ZERO;
        int k = xs.size();

        for (int i = 0; i < k; i++) {
            BigInteger xi = xs.get(i);
            BigInteger yi = ys.get(i);
            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (j == i) continue;
                BigInteger xj = xs.get(j);
                num = num.multiply(xj.negate());
                den = den.multiply(xi.subtract(xj));
            }

            result = result.add(yi.multiply(num).divide(den));
        }

        return result;
    }

    public static BigInteger solveFromFile(String filename) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        Map<Integer, String[]> map = new HashMap<>();
        int k = 0;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.contains("\"k\"")) {
                String[] parts = line.split(":");
                k = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
            } else if (line.matches("\"\\d+\"\\s*:\\s*\\{")) {
                int index = Integer.parseInt(line.split("\"")[1]);
                String baseLine = br.readLine().trim();
                String valueLine = br.readLine().trim();
                String base = baseLine.split(":")[1].replaceAll("[^0-9]", "");
                String value = valueLine.split(":")[1].replaceAll("[^0-9a-zA-Z]", "");
                map.put(index, new String[]{base, value});
            }
        }
        br.close();

        List<Integer> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);

        List<BigInteger> xs = new ArrayList<>();
        List<BigInteger> ys = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            int x = keys.get(i);
            String[] info = map.get(x);
            int base = Integer.parseInt(info[0]);
            BigInteger y = decode(info[1], base);
            xs.add(BigInteger.valueOf(x));
            ys.add(y);
        }

        return lagrangeInterpolation(xs, ys);
    }

    public static void main(String[] args) throws Exception {
        BigInteger secret1 = solveFromFile("testcase1.json");
        System.out.println("Secret from Testcase 1: " + secret1);
    }
}
