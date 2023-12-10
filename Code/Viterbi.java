import org.bytedeco.opencv.presets.opencv_core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Viterbi {

    private static Map<String, Map<String, Double>> states;         // scores of states from states (tags)
    private static Map<String, Map<String, Double>> observations;           // scores of states (tags) from observations (words)
    private static List<String> words;          // list of words in the input file
    private List<Map<String, String>> backTrack;             // keep a track of current state in viterbi tagging

    /**
     * constructor method
     * @param states map of scores of tags from other tags
     * @param observations map of scores of tags from words
     * @param fileName name of the file that needs to be read
     * @throws Exception (if file does not exist or is empty)
     */
    public Viterbi(Map<String, Map<String, Double>> states, Map<String, Map<String, Double>> observations, String fileName) throws Exception{
        this.states = states;
        this.observations = observations;
        words = new ArrayList<>();
        readFile(fileName);
        tagging();
    }

    /**
     * viterbi tagging algorithm that finds the next most probable tag for a word
     */
    private void tagging() {
        // initialize backTrack
        backTrack = new ArrayList<>();
        // create a new set that contains current states
        Set<String> currStates = new HashSet<>();
        // create a new map that contains the state and its score
        Map<String, Double> currScores = new HashMap<>();
        // add a starting tag
        currStates.add("#");
        // put the tag in currScores with score 0
        currScores.put("#", 0.0);
        Map<String, String> pointer =  new HashMap<>();
        pointer.put("#", null);
        backTrack.add(pointer);
        // index of current state
        int i = 0;
        // for every word in words list
        for (String word: words){
            // create a new set nextStates
            Set<String> nextStates = new HashSet<>();
            // create a new map nextScores
            Map<String, Double> nextScores = new HashMap<>();
            // for every state in currStates
            for (String state: currStates) {
                // if states map contains the state
                if (states.containsKey(state)) {
                    // for every neighbor state of state in states map
                    for (String transition: states.get(state).keySet()) {
                        // add it to nextStates
                        nextStates.add(transition);
                        // observation score
                        double obsScore;
                        // if observations map's key state contains the observation (word)
                        if (observations.get(transition).containsKey(word)) {
                            // observation score is that word's score
                            obsScore = observations.get(transition).get(word);
                        } else {
                            // a high negative value for unseen observation
                            obsScore = -100;
                        }
                        // score of the state = current score of the state + score of the next state + observation score
                        double score = currScores.get(state) + states.get(state).get(transition) + obsScore;
                        // if the next state (transition) is not in nextScores map or the new score is greater than the score of the other next state (transition)
                        if ((!nextScores.containsKey(transition)) || score > nextScores.get(transition)) {
                            // add the transition state to nextScores
                            nextScores.put(transition, score);
                        }
                    }
                    // if states does not contain the state
                } else {
                    // for every state in states
                    for (String transition: states.keySet()) {
                        // add it to nextStates
                        nextStates.add(transition);
                        // observation score
                        double obsScore;
                        // if observations map's key state contains the observation (word)
                        if (observations.get(transition).containsKey(word)) {
                            // observation score is that word's score
                            obsScore = observations.get(transition).get(word);
                        } else {
                            // a high negative value for unseen observation
                            obsScore = -100;
                        }
                        // score of the state = current score of the state + observation score
                        double score = currScores.get(state) + obsScore;
                        // if the state is not in nextScores map or the new score is greater than the score of the other state (transition)
                        if ((!nextScores.containsKey(transition)) || score > nextScores.get(transition)) {
                            // add the transition state to nextScores
                            nextScores.put(transition, score);
                        }
                    }
                }
            }
            // find the state with largest score in nextScores map
            double highest = -999999999;
            String bestState = null;
            for (String nScore: nextScores.keySet()){
                if (nextScores.get(nScore) > highest) {
                    bestState = nScore;
                    highest = nextScores.get(nScore);
                }
            }
            for (String prevState: backTrack.get(i++).keySet()) {
                // create a new map pointer that stores the best state and the previous state
                pointer = new HashMap<>();
                pointer.put(bestState, prevState);
                // add pointer to the list backTrack
                backTrack.add(pointer);
            }
            // assign nextStates to currStates
            currStates = nextStates;
            // assign nextScores to currScores
            currScores = nextScores;
        }
        System.out.println(backTrack);
    }

    /**
     * read input file and create a list of all the words in the file
     * @param fileName input file
     * @throws Exception in case the file is empty or does not exist
     */
    private void readFile(String fileName) throws Exception {
        BufferedReader input = new BufferedReader(new FileReader(fileName));
        String inLine;
        // while there is a line to read
        while ((inLine = input.readLine()) != null) {
            // split the line into words
            String[] line = inLine.split(" ");
            // for every word in array line
            for (String inWord: line) {
                // add the word to words list
                words.add(inWord);
            }
        }
        // close the input file
        input.close();
    }

    /**
     * gets backTrack
     * @return list backTrack
     */
    public List<Map<String, String>> getBackTrack() {
        return backTrack;
    }
}
