# pos-chahakgoyal

# Hard-Coded Files:
We created a new input file, indiaTest. We then created two maps, indiaObs and indiaTags. For indiaObs, we created a map of tags to a map of words and scores, and for indiaTags, we create a map of tags to a map of tags and scores. We then used the Viterbi algorithm to get the output.
The output that we got was pretty similar to the file that we created which convinced us that the code was correct.
# Viterbi Tagging:
In the algorithm, we always started with a # and then explored potential transitions. For other tags, we only iterated over possible transitions (next states) of said tags. We only added tags to the next states set and next scores map if they either did not already exist in the set or if their score was higher than that previously scored. This ensured there was no duplication in nextStates. At the end of this loop in every cycle, we added the state with the highest score to the backtrack list as a map, with the current state as key and the previous state from which it had come as the value.
# Overall Testing Performance:
To test the performance of the Viterbi algorithm, we created a new class Performance.java.
There, we took tagFile (file with tags) and inferredTags (backTrack list of maps) as parameters.
We read the tagFile and compared every word with its corresponding tag in the list of maps. If those two matched, we added 1 to ‘correct’ variable. If those two didn’t match, we added 1 to ‘incorrect’ variable.
When the unseen observation penalty was -5, our algorithm performed poorly because, for many of the seen observations, the logarithmic values were negative. So, when we changed the observation penalty to -100, the algorithm performed way better.
# Example Test Sentences:
These are some sentences from simple-test-sentences.txt:
the dog saw trains in the night .
my watch glows in the night .
This is a made-up sentence:
1. You saw my cow .
2. My dog glows trains.
Since we are training our model according to simple-test-tags.txt and simple-test-sentences.txt, the first made-up sentence will not be tagged as expected. The model has not been trained for the words that it contains. The second made-up sentence will be tagged as expected because it is similar to the trained model.
