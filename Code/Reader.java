import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reader {

    private Map<String, Map<String, Double>> states;
    private Map<String, Map<String, Double>> observations;
    private Map<String, Integer> totalStateOccurences;
    private Map<String, Integer> totalObsOccurences;

    /**
     * intializes variables, and acts as blanket method to call class methods in the relevant sequence
     * @param sentenceFileName
     * @param tagFileName
     * @throws Exception
     */
    public Reader(String sentenceFileName, String tagFileName) throws Exception{
        // initialize variables
        List<String[]> sentences = new ArrayList<>();
        List<String[]> tags = new ArrayList<>();
        states = new HashMap<>();
        observations = new HashMap<>();
        totalStateOccurences = new HashMap<>();
        totalObsOccurences = new HashMap<>();
        // get input from sentence and tag files and add store it in lists of strings
        readText(sentences, tags, sentenceFileName, tagFileName);
        // create observation and tag maps
        obsMap(sentences, tags);
        stateMap(tags);
        // transform values in maps to log probabilities
        logTransform();
    }

    /**
     * returns map of tag to tag transitions
     * @return
     */
    public Map<String, Map<String, Double>> getStates() {
        return states;
    }

    /**
     * returns map of observations to tags
     * @return
     */
    public Map<String, Map<String, Double>> getObservations() {
        return observations;
    }

    /**
     * creates a map from observed words to their tags and the frequencies of the words in each tag
     * @param sentences         list of sentences in input file
     * @param tags              corresponding list of tags for each word in the sentences
     */
    private void obsMap(List<String[]> sentences, List<String[]> tags) {
        // go over each sentence
        for (int i = 0; i < sentences.size(); i++) {
            // go over each word in the sentence
            for (int j = 0; j < sentences.get(i).length; j++) {
                // get current tag and word from relevant lists
                String tag = tags.get(i)[j];
                String word = sentences.get(i)[j].toLowerCase();
                // if the map already contains said observation
                if (observations.containsKey(tag)) {
                    // check if the word is already connected to said tag
                    if(observations.get(tag).containsKey(word)) {
                        observations.get(tag).put(word, observations.get(tag).get(word) + 1);
                    } else {
                        observations.get(tag).put(word, 1.0);
                    }
                // if map doesn't contain current word, create a new entry for it
                } else {
                    Map<String, Double> wordFreq = new HashMap<>();
                    wordFreq.put(word, 1.0);
                    observations.put(tag, wordFreq);
                }
                // increment count of total times the said tag has occurred
                if (totalObsOccurences.containsKey(tag)) {
                    totalObsOccurences.put(tag, totalObsOccurences.get(tag) + 1);
                } else {
                    totalObsOccurences.put(tag, 1);
                }
            }
        }
    }

    /**
     * creates a map from tags to other tags they are connected to
     * @param tags          list of tags in input file
     */
    public void stateMap(List<String[]> tags) {
        // go over all tags sentence by sentence
        for (int i = 0; i < tags.size(); i++) {
            // go over each individual tag in a sentence
            for (int j = 0; j < tags.get(i).length - 1; j++){
                String currTag = tags.get(i)[j];
                String nextTag = tags.get(i)[j + 1];
                // check if the map already contains current tag
                if (states.containsKey(currTag)) {
                    // check if the current tag has been connected to next tag in the past and increment number of times this has occured
                    if (states.get(currTag).containsKey(nextTag)) {
                        states.get(currTag).put(nextTag, states.get(currTag).get(nextTag) + 1);
                    }
                    // otherwise, make this record for the first time
                    else {
                        states.get(currTag).put(nextTag, 1.0);
                    }
                }
                // otherwise create a new entry in the map
                else {
                    Map<String, Double> stateFreq = new HashMap<>();
                    stateFreq.put(nextTag, 1.0);
                    states.put(currTag, stateFreq);
                }
                // increment the total number of times tag has occurred
                if (totalStateOccurences.containsKey(currTag)) {
                    totalStateOccurences.put(currTag, totalStateOccurences.get(currTag) + 1);
                } else {
                    totalStateOccurences.put(currTag, 1);
                }
            }
        }
    }

    /**
     * creates lists of sentences and tags for those sentences from fileNames
     * @param sentences             list of strings to which sentences from input file will be added
     * @param tags                  list of tags for which corresponding tags from input file will be added
     * @param sentenceFileName      name of file from which to get sentences
     * @param tagFileName           name of file from whcih to get corresponding tags
     * @throws Exception            in case files are not found
     */
    private void readText(List<String[]> sentences, List<String[]> tags, String sentenceFileName, String tagFileName) throws Exception {
        // open files as per given file Names
        BufferedReader sentenceReader = new BufferedReader(new FileReader(sentenceFileName));
        BufferedReader tagReader = new BufferedReader(new FileReader(tagFileName));
        String sentence;
        String tag;
        // go over each sentence in the file
        while ((sentence = sentenceReader.readLine()) != null) {
            // break down each sentence into words (separated by spaces)
            String[] sentenceParts = sentence.split(" ");
            // add list of words in sentences to master list of sentences
            sentences.add(sentenceParts);
        }
        // go over each sentence of tags in the file
        while ((tag = tagReader.readLine()) != null) {
            // break down tag sentence into constituent tags
            String[] tagParts = tag.split(" ");
            // add list of tags in sentence to master list of tags
            tags.add(tagParts);
        }
        // close file readers
        sentenceReader.close();
        tagReader.close();
    }

    /**
     * transforms frequency values in observation and tag maps to log probabilities
     */
    private void logTransform() {
        // go over each tag in the states map
        for (String state: states.keySet()) {
            // go over each tag that given tag is connected to
            for (String grammar: states.get(state).keySet()) {
                // get probability of tag being connected to this tag by dividing frequency of connection by total number of times tag has occured
                double prob = states.get(state).get(grammar) / totalStateOccurences.get(state);
                // put log value of probability in map
                states.get(state).put(grammar, Math.log(prob));
            }
        }

        // go over each tag in the observations map
        for (String obs: observations.keySet()) {
            // go over each word for each tag
            for (String score: observations.get(obs).keySet()) {
                // get probability of tag being connected to this word by dividing frequency of connection by total number of times tag has occured
                double prob = observations.get(obs).get(score) / totalObsOccurences.get(obs);
                // put log value of probability in map
                observations.get(obs).put(score, Math.log(prob));
            }
        }

    }
}
