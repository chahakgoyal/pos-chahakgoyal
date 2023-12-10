public class Test {
    public static void main(String[] args) throws Exception {
        String fileName = "texts/brown-train";
        // Console console = new Console(fileName + "-sentences.txt");
        Reader reader = new Reader(fileName + "-sentences.txt", fileName + "-tags.txt");
        Viterbi viterbi = new Viterbi(reader.getStates(), reader.getObservations(), "texts/brown-test-sentences.txt");
        Performance.comparePerformance("texts/brown-test-tags.txt", viterbi.getBackTrack());
    }
}

