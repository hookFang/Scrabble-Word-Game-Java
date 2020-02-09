package sample;

public class ScrabbleModel {
    //Instance variables
    char letter;
    byte numberOfLettersAvailable;
    int letterPoints;

    //Constructors
    public ScrabbleModel() {

    }

    /**
     * Constructor that sets the values at beginning
     * @param letter
     * @param numberOfLettersAvailable
     * @param letterPoints
     */
    public ScrabbleModel(char letter, byte numberOfLettersAvailable, int letterPoints) {
        this.letter = letter;
        this.numberOfLettersAvailable = numberOfLettersAvailable;
        this.letterPoints = letterPoints;
    }

    /*Constructor with the parameter of class, its used to copy values to another array list without referencing.*/
    public ScrabbleModel(ScrabbleModel Model) {
        this.letter = Model.letter;
        this.numberOfLettersAvailable = Model.numberOfLettersAvailable;
        this.letterPoints = Model.letterPoints;
    }

    /*Getters and setters*/
    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public byte getNumberOfLettersAvailable() {
        return numberOfLettersAvailable;
    }

    public void setNumberOfLettersAvailable(byte numberOfLettersAvailable) {
        this.numberOfLettersAvailable = numberOfLettersAvailable;
    }

    public int getLetterPoints() {
        return letterPoints;
    }

    public void setLetterPoints(int letterPoints) {
        this.letterPoints = letterPoints;
    }

    @Override
    public String toString() {
        return "ScrabbleModel{" +
                "letter=" + letter +
                ", numberOfLettersAvailable=" + numberOfLettersAvailable +
                ", letterPoints=" + letterPoints +
                '}';
    }
}
