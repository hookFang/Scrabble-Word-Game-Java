package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.*;


public class ScrabblePointsController {
    //Creates a list of the model
    List<ScrabbleModel> letter_set = new ArrayList<ScrabbleModel>();

    //stores all the valid words
    Set<String> previousWords = new HashSet<String>();

    //Stores the points
    int points = 0;

    //Total number of letters available
    int numberOfLetters = 0;

    //References to the arraylist in the fxml file
    @FXML
    List<Label> labelList ;
    @FXML
    Label pointsScored ;
    @FXML
    TextField word;
    @FXML
    Button detailsTab;
    @FXML
    ListView previousWordsList;
    @FXML
    private void initialize() {
        System.out.println("Activated");

        //Initializes the scrabble model with 26 letters and random points and random letters available
        char k ='a';
        for(int i =0; i<26; i++,k++) {
            letter_set.add(new ScrabbleModel(k, (byte) (Math.random() * 10 + 1), (int) (Math.random() * 10 + 1)));
        }

        //Stores a total of letter available at the beginning
        for(int i=0; i<letter_set.size(); i++) {
            labelList.get(i).setText(String.valueOf(letter_set.get(i).getLetterPoints()));
            numberOfLetters += letter_set.get(i).getNumberOfLettersAvailable();
        }

    }

    /**
     * This method shows the letters available and points available
     * @param actionEvent
     */
    @FXML
    public void showDetails(javafx.event.ActionEvent actionEvent) {
        //Shows the number of letters available below each letter in the GUI
        if(detailsTab.getText().equalsIgnoreCase("Show Letters Available")) {
            for (int i = 0; i < letter_set.size(); i++) {
                labelList.get(i).setText(String.valueOf(letter_set.get(i).getNumberOfLettersAvailable()));
            }
            detailsTab.setText("Show Points");
        }
        //Shows the points available below each letter in the GUI
        else if(detailsTab.getText().equalsIgnoreCase("Show Points")) {
                for (int i = 0; i < letter_set.size(); i++) {
                    labelList.get(i).setText(String.valueOf(letter_set.get(i).getLetterPoints()));
                }
                detailsTab.setText("Show Letters Available");
            }
    }

    /**
     * This method gets invoked when the submit button gets pressed
     * @param actionEvent
     */
    @FXML
    public void submitWord(ActionEvent actionEvent) {
        //Checks if the word is empty
        if(word.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("The word cannot be empty");
            alert.setContentText("The word cannot be empty. Please Try again !");
            alert.show();
            return;
        }

        /*Checks if we have only one letter is available*/
        if(gameOver())
            return;

        //Gets the word trims it and converts it to lowercase
        String enteredWord = word.getText().trim().toLowerCase();
        char[] wordCharacters = enteredWord.toCharArray();
        boolean wordIsValid = true;
        boolean lettersAreAvailable = true;

        //Checks if the word is valid
        wordIsValid = checkWordIsValid(enteredWord);
        //If the word is not valid it returns nothing and the code below does not run
        if(!wordIsValid)
            return;

        //Checks if all the letters in the word are available
        lettersAreAvailable = checkAllLettersAvailable(wordCharacters);
        //If the word is not valid it returns nothing and the code below does not run
        if(!lettersAreAvailable)
            return;

        //If the the word is valid it sets the points
       if (lettersAreAvailable && wordIsValid) {
           if (previousWords.add(word.getText().trim())) {
               for (int i = 0; i < wordCharacters.length; i++) {
                   for (int j = 0; j < letter_set.size(); j++) {
                       if (wordCharacters[i] == letter_set.get(j).getLetter()) {
                           points += letter_set.get(j).getLetterPoints();
                           letter_set.get(j).setNumberOfLettersAvailable((byte) (letter_set.get(j).getNumberOfLettersAvailable() - 1));
                           numberOfLetters--;
                           if (letter_set.get(j).getNumberOfLettersAvailable() == 0) {
                               labelList.get(j).getParent().setVisible(false);
                           }
                       }
                   }
               }
               //Sets the points
               pointsScored.setText(String.valueOf(points));
               //calls the previous words methods which updates the words
               showPreviousWord();
               //Updates the values shown on screen
               updateDetails();
           } else {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Duplicate Word Present");
               alert.setContentText("Sorry cannot store duplicate words,the same word was already entered");
               alert.show();
           }
       }

       //clears the text entered by user
       word.clear();

    }

    /**
     * Updates details once the word is submitted
     */
    private void updateDetails() {
        if(detailsTab.getText().equalsIgnoreCase("Show Letters Available")) {
            for (int i = 0; i < letter_set.size(); i++) {
                labelList.get(i).setText(String.valueOf(letter_set.get(i).getLetterPoints()));
            }
        }
        else if(detailsTab.getText().equalsIgnoreCase("Show Points")) {
            for (int i = 0; i < letter_set.size(); i++) {
                labelList.get(i).setText(String.valueOf(letter_set.get(i).getNumberOfLettersAvailable()));
            }
        }
    }


    /**
     * This method gets the label from fxml and stores the previous words into it
     */
    private void showPreviousWord() {
         /*This is how you could do with a label and add the commas in between and not in the last word*/
       // int lastWord = 0;
//        for(String word : previousWords){
//            if(lastWord++ == previousWords.size() - 1 )
//            previousWordLabel.setText(previousWordLabel.getText() + word);
//            else
//                previousWordLabel.setText(previousWordLabel.getText() + word + ", ");

        //Clears before each time you add a word
        previousWordsList.getItems().clear();
        //Populates the list view with new values
        for(String word : previousWords){
            previousWordsList.getItems().add(word);
        }
    }

    //Checks if the letters are available before processing
    private boolean checkAllLettersAvailable(char[] letterArray) {
        boolean validity = true;
        List<ScrabbleModel> tempModel = new ArrayList<>();
        for (int i = 0; i < letter_set.size(); i++) {
            tempModel.add(new ScrabbleModel(letter_set.get(i)));
        }

        //Exits from the outerloop is the condition is true
        outerloop:
        for (int i = 0; i < letterArray.length; i++) {
            for (int j = 0; j < tempModel.size(); j++) {
                if (letterArray[i] == tempModel.get(j).getLetter()) {
                    tempModel.get(j).setNumberOfLettersAvailable((byte) (tempModel.get(j).getNumberOfLettersAvailable() - 1));
                    if (tempModel.get(j).getNumberOfLettersAvailable() < 0) {
                        validity = false;
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("The Letter is not available");
                        alert.setContentText("The letter " + letter_set.get(j).getLetter() + " is not available. Letter " +
                                        letter_set.get(j).getLetter() + " can only be used " + letter_set.get(j).getNumberOfLettersAvailable() + " times.");
                        alert.show();
                        break outerloop;
                    }
                }
            }
        }
        return  validity;
    }

    private boolean checkWordIsValid(String enteredWord) {
        boolean wordLengthIsValid = true, wordHasVowel = true;
        //Checks the length of the word entered
        if(!(enteredWord.length()>=2 && enteredWord.length()<=8)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Doesn't Meet Word Requirements");
            alert.setContentText("Sorry word length should be greater than or equal to 2 and less than or equal to 8 !");
            alert.showAndWait();
            return false;
        }
        //Checks if the word contains a vowel
        if(!(enteredWord.indexOf('a')>=0 || enteredWord.indexOf('e')>=0 || enteredWord.indexOf('i')>=0 || enteredWord.indexOf('o')>=0 || enteredWord.indexOf('u')>=0 || enteredWord.indexOf('y')>=0)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Doesn't Meet Word Requirements");
            alert.setContentText("Sorry word must contain at least one vowel !");
            alert.show();
            return false;
        }
        return true;
    }

    private boolean gameOver() {
        //Temporary  values that store the present number of vowels available
        byte a_left = 0;
        byte e_left = 0;
        byte i_left = 0;
        byte o_left = 0;
        byte u_left = 0;
        byte y_left = 0;

        //Checks if exactly only 1 letter is available
        if(numberOfLetters == 1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("GAME OVER");
            alert.setHeaderText("GAME OVER");
            alert.setContentText("You only have 1 more letter available.");
            alert.show();
            return true;
        }
        else if(numberOfLetters < 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("GAME OVER");
            alert.setHeaderText("GAME OVER");
            alert.setContentText("You only have 1 more letter available.");
            alert.show();
            return true;
        }

        /*Gets the number of vowels available*/
        for(ScrabbleModel temp : letter_set) {
            if (temp.getLetter() == 'a') {
                 a_left = temp.getNumberOfLettersAvailable();
            }
            if (temp.getLetter() == 'e') {
                 e_left = temp.getNumberOfLettersAvailable();
            }
            if (temp.getLetter() == 'i') {
                 i_left = temp.getNumberOfLettersAvailable();
            }
            if (temp.getLetter() == 'o') {
                 o_left = temp.getNumberOfLettersAvailable();
            }
            if (temp.getLetter() == 'u') {
                 u_left = temp.getNumberOfLettersAvailable();
            }
            if (temp.getLetter() == 'y') {
                 y_left = temp.getNumberOfLettersAvailable();
            }

        }

        //Checks if any vowels are available
        if(a_left <=0 && e_left <=0 && i_left <=0 && o_left <=0 && u_left <= 0 && y_left <= 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("GAME OVER");
            alert.setHeaderText("GAME OVER");
            alert.setContentText("You dont have anymore vowels left");
            alert.show();
            return true;
        }
        return false;
    }

}
