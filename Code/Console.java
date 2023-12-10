import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Console {

    private List<String> sentences;             // list of sentences
    private List<String> tagSentences;              // list of tag sentences

    /**
     * constructor method
     * @param fileName name of input file
     * @throws Exception
     */
    public Console(String fileName) throws Exception{
        BufferedReader sentenceReader = new BufferedReader(new FileReader(fileName));
        sentences = new ArrayList<>();
        tagSentences = new ArrayList<>();
        String sentence;
        // while there is a sentence to read in input file
        while ((sentence = sentenceReader.readLine()) != null) {
            // add the sentence to sentences list
            sentences.add(sentence);
        }
        askForInput();
    }

    /**
     * asks the user to input tags for a sentence
     * @throws Exception
     */
    private void askForInput() throws Exception {
        // for every sentence in sentences list
        for (String sentence: sentences) {
            // give the user a line
            System.out.println("Please give tags for '" + sentence + "' separated by spaces");
            // ask the user for tags
            Scanner io = new Scanner(System.in);
            String tagSentence = io.nextLine();
            // while the number of tags doesn't match the number of words in a sentence
            while (tagSentence.split(" ").length != sentence.split(" ").length) {
                // ask the user for input
                System.out.println("Incorrect number of tags. Please retry");
                tagSentence = io.nextLine();
            }
            // add the sentence that the user typed in to tagSentences list
            tagSentences.add(tagSentence);
        }
        // write a new file
        BufferedWriter writer = new BufferedWriter(new FileWriter("texts/user-inputTags.txt"));
        // for every tags sentence in tagSentences
        for (String tags: tagSentences) {
            // write it to the file
            writer.write(tags + "\n");
        }
        // close the file
        writer.close();
    }
}
