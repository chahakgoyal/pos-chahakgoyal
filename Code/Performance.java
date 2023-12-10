import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Performance {

    /**
     * gives the number of correct and incorrect words
     * @param tagFile input file containing tags
     * @param inferredTags tags that we got from viterbi tagging
     * @throws Exception
     */
    public static void comparePerformance(String tagFile, List<Map<String, String>> inferredTags) throws Exception {
        int incorrect = 0;
        int correct = 0;
        List<String> words = new ArrayList<>();
        BufferedReader input = new BufferedReader(new FileReader(tagFile));
        String inLine;
        // while there is a line to read
        while ((inLine = input.readLine()) != null) {
            String[] line = inLine.split(" ");
            // for every word in line
            for (String inWord: line) {
                // add it to words list
                words.add(inWord);
            }
        }
        // close the file
        input.close();

        int i = 1;
        // for every word in words
        for (String word: words) {
            // if the word is equal to the inferred tag
            for (String inferredWord: inferredTags.get(i++).keySet()) {
                if (word.equals(inferredWord)){
                    // add 1 to correct
                    correct++;
                } else {
                    // otherwise add 1 to incorrect
                    incorrect++;
                }
            }
        }
        // print out the message
        System.out.println("Our algorithm predicts " + correct + " words correctly and " + incorrect + " words incorrectly.");
    }

}
