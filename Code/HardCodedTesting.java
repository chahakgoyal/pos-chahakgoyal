import java.util.HashMap;
import java.util.Map;

public class HardCodedTesting {
    public static void main(String[] args) throws Exception{
        Map<String, Map<String, Double>> indiaTags = new HashMap<>();
        Map<String, Map<String, Double>> indiaObs = new HashMap<>();

        Map<String, Double> india = new HashMap<>();
        india.put("India", 1.0);
        indiaObs.put("NP", india);
        Map<String, Double> is = new HashMap<>();
        india.put("is", 1.0);
        indiaObs.put("CNJ", is);
        Map<String, Double> a = new HashMap<>();
        india.put("a", 1.0);
        indiaObs.put("DET", a);
        Map<String, Double> great = new HashMap<>();
        india.put("great", 1.0);
        indiaObs.put("ADJ", great);
        Map<String, Double> country = new HashMap<>();
        india.put("country", 1.0);
        indiaObs.put("N", country);
        Map<String, Double> period = new HashMap<>();
        period.put(".", 1.0);
        indiaObs.put(".", period);

        Map<String, Double> NP = new HashMap<>();
        NP.put("CNJ", 1.0);
        indiaTags.put("NP", NP);
        Map<String, Double> CNJ = new HashMap<>();
        CNJ.put("DET", 1.0);
        indiaTags.put("CNJ", CNJ);
        Map<String, Double> DET = new HashMap<>();
        DET.put("ADJ", 1.0);
        indiaTags.put("DET", DET);
        Map<String, Double> ADJ = new HashMap<>();
        ADJ.put("N", 1.0);
        indiaTags.put("ADJ", ADJ);
        Map<String, Double> N = new HashMap<>();
        N.put(".", 1.0);
        indiaTags.put("N", N);

        Viterbi viterbi = new Viterbi(indiaTags, indiaObs, "texts/indiaTest");
    }
}
