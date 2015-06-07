package dbtestgenerator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import javax.swing.JRadioButton;

/**
 *
 * @author muse
 */
public class DBTestGenerator extends JFrame implements ActionListener {

    private static JButton finish;
    private static JButton next;
    private static JButton back;
    private static JLabel taskQuestion;
    private static JButton clearAll;
    private static JRadioButton[] ans;
    private static JLabel[] answerChoice;
    private static JLabel[] questionCode;
    private static JLabel totalQuestionsAnswered;
    private static int questionsAnsweredCount = 0;
    private static JPanel questionPanel;
    private static JPanel answerPanel;
    private static JPanel data;
    private static JFrame myFrame;

    private static int[] multipleAnswers;
    private static int model;
    private static int questionPage = 0;
    private static final int NOFQUESTIONS = 12;
    private static final int NOFMODELS = 11;
    private static final int NOFSEQUENCES = 4;
    private static int[] nofInstructions = {1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 3};
    private static int[] nofAssignments = {2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3};
    private static boolean[][] answersGiven;
    private static ArrayList[] answerOptions;// add allResults to shuffle them for presentation
    private static Instruction[][] allInstructions;
    private static String[][] allAssignments;
    private static Value[][] allValues;
    private static Value[][][] allResultsValues;
    private static String[] questions;
    private static String[] codeLines;

    private final int ADD = 0;
    private final int DIRECTION = 1;
    private final int LOSE = 2;
    private static String[][] answerChoiceText;

    public static void main(String[] args) {
        new DBTestGenerator();

        //myFrame.toFront();
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public DBTestGenerator() {
        answerChoiceText = new String[NOFQUESTIONS][NOFMODELS];
        myFrame = new JFrame("D&B Test");
        next = new JButton("NEXT");
        next.addActionListener(this);
        back = new JButton("BACK");
        back.addActionListener(this);
        answerPanel = new JPanel();
        totalQuestionsAnswered = new JLabel("0/20");
        questionPanel = new JPanel();
        questionCode = new JLabel[7];
        questionCode[0] = new JLabel();
        questionCode[1] = new JLabel();
        questionCode[2] = new JLabel();
        questionCode[3] = new JLabel();
        questionCode[4] = new JLabel();
        questionCode[5] = new JLabel();
        questionCode[6] = new JLabel();
        data = new JPanel();
        finish = new JButton("FINISH");
        finish.addActionListener(this);
        taskQuestion = new JLabel("Q" + questionPage);

        clearAll = new JButton("CLEAR");
        clearAll.addActionListener(this);
        ans = new JRadioButton[11];
        ans[0] = new JRadioButton();
        ans[1] = new JRadioButton();
        ans[2] = new JRadioButton();
        ans[3] = new JRadioButton();
        ans[4] = new JRadioButton();
        ans[5] = new JRadioButton();
        ans[6] = new JRadioButton();
        ans[7] = new JRadioButton();
        ans[8] = new JRadioButton();
        ans[9] = new JRadioButton();
        ans[10] = new JRadioButton();
        //ans[11] = new JRadioButton();

        ans[0].addActionListener(this);
        ans[1].addActionListener(this);
        ans[2].addActionListener(this);
        ans[3].addActionListener(this);
        ans[4].addActionListener(this);
        ans[5].addActionListener(this);
        ans[6].addActionListener(this);
        ans[7].addActionListener(this);
        ans[8].addActionListener(this);
        ans[9].addActionListener(this);
        ans[10].addActionListener(this);

        answerChoice = new JLabel[11];
        answerChoice[0] = new JLabel("1)");
        answerChoice[1] = new JLabel("2)");
        answerChoice[2] = new JLabel("3)");
        answerChoice[3] = new JLabel("4)");
        answerChoice[4] = new JLabel("5)");
        answerChoice[5] = new JLabel("6)");
        answerChoice[6] = new JLabel("7)");
        answerChoice[7] = new JLabel("8)");
        answerChoice[8] = new JLabel("9)");
        answerChoice[9] = new JLabel("10)");
        answerChoice[10] = new JLabel("11)");
        //answerChoice[11] = new JLabel();
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        myFrame.setPreferredSize(new Dimension(700, 500));
        myFrame.setLayout(new BorderLayout());
        myFrame.add(questionPanel, BorderLayout.NORTH);
        myFrame.add(answerPanel, BorderLayout.CENTER);
        myFrame.add(data, BorderLayout.SOUTH);

        questionPanel.setLayout(new GridLayout(8, 1));
        questionPanel.add(taskQuestion);
        questionPanel.add(questionCode[0]);
        questionPanel.add(questionCode[1]);
        questionPanel.add(questionCode[2]);
        questionPanel.add(questionCode[3]);
        questionPanel.add(questionCode[4]);
        questionPanel.add(questionCode[5]);
        questionPanel.add(questionCode[6]);

        data.setLayout(new GridLayout(1, 6));

        data.add(next);
        data.add(back);
        data.add(clearAll);
        data.add(totalQuestionsAnswered);
        data.add(finish);
        answerPanel.setLayout(new GridLayout(11, 2));
        for (int i = 0; i < NOFMODELS; i++) {
            answerPanel.add(ans[i]);
            answerPanel.add(answerChoice[i]);
        }

        myFrame.pack();
        myFrame.setVisible(true);

        //init false all answersGiven
        answersGiven = new boolean[NOFQUESTIONS][NOFMODELS];
        for (int page = 0; page < NOFQUESTIONS; page++) {
            for (int q = 0; q < NOFMODELS; q++) {
                answersGiven[page][q] = false;
            }
        }
        //init all questions
        codeLines = new String[questionCode.length];
        questions = new String[NOFQUESTIONS];
        allValues = new Value[NOFQUESTIONS][3];
        allResultsValues = new Value[NOFQUESTIONS][NOFMODELS][3];
        allInstructions = new Instruction[NOFQUESTIONS][3];
        for (int n = 0; n < NOFQUESTIONS; n++) {
            questions[n] = generateQuestion(n);
            //System.out.println("C3 page n:" + n);
            splitCodeLines(questions[n]);
        }

        //init allResultsValues
        for (int i = 0; i < NOFQUESTIONS; i++) {
            for (int j = 0; j < NOFMODELS; j++) {
                for (int k = 0; k < nofAssignments[i]; k++) {
                    allResultsValues[i][j][k] = new Value();
                }
            }
        }
        //init first Pages question
        taskQuestion.setText("Q" + questionPage + ": " + questions[0]);
        splitCodeLines(questions[0]);

        for (int i = 0; i < questionCode.length; i++) {
            questionCode[i].setText(codeLines[i]);
        }
        repaint();

        //generate model Answers for each question
        generateAnswerChoiceTexts();

        //shuffle the answers on each page
        ArrayList answersAList = new ArrayList();
        for (int i = 0; i < NOFQUESTIONS; i++) {
            //add all String answers to ArrayList
            answersAList.add("2");

        }
        Collections.shuffle(answersAList);

        //display all answers 
        for (int i = 0; i < NOFQUESTIONS; i++) {
            displayAnswerChoice(i);

        }

        repaint();
    }

    //////////////////////////////////////////////////    
    private String getModelAnswer(int questionPage, int i) {
        String n = "";

        return n;
    }

    private static void splitCodeLines(String q) {
        codeLines = q.split(";");
        //error here: oob
        for (int i = 0; i < nofInstructions[questionPage]; i++) {
            System.out.println("" + codeLines[i]);
        }
    }

    // display questions in questionCode labels
    private static void setQuestionsToLabels() {

        for (int i = 0; i < questionCode.length; i++) {
            //System.out.println("C1001 11042015: QP" + questionPage + questionCode.length + "  " + questions[questionPage].length());
            //generate codeLines
            splitCodeLines(questions[questionPage]);
            questionCode[i].setText(codeLines[i]);
        }
    }

    private static String generateQuestion(int n) {
        //n is the questionNumber aka questionPage

        String question = "";

        ArrayList varname1 = new ArrayList();
        ArrayList varname2 = new ArrayList();

        String abc = "abcdefghijkmnpqrstuvwxyz";
        ArrayList abcArray = new ArrayList();

        for (int i = 0; i < abc.length(); i++) {
            abcArray.add(abc.substring(i, i + 1));
        }

        String v[] = new String[nofAssignments[n]];
        int[] integers = new int[nofAssignments[n]];
        int indexOfVariableName;
        //TODO remove repetitions
        for (int i = 0; i < nofAssignments[n]; i++) {
            indexOfVariableName = (int) (Math.random() * abcArray.size());
            //System.out.println("C4: index of letter:"+ indexOfVariableName);
            v[i] = (String) abcArray.get(indexOfVariableName);
            //System.out.println("C3: " + v[i]);
            abcArray.remove(indexOfVariableName);
        }
        integers[0] = 10;
        for (int i = 1; i < nofAssignments[n]; i++) {
            integers[i] = (int) (Math.random() * 3) + 1;
            integers[i] *= 10;
            integers[i] += integers[i - 1];
            varname1.add(v[i]);
        }
        //create the first list    
        varname1.add(v[0]);
        if (!v[1].equals(v[0])) {
            varname1.add(v[1]);
        }
        if (nofAssignments[n] > 2) {
            if (!v[1].equals(v[2]) && !v[0].equals(v[2])) {
                varname1.add(v[2]);
            }
        }
        //create the second list    
        varname2.add(v[0]);
        if (nofAssignments[n] > 1) {
            varname2.add(v[1]);
        }
        if (nofAssignments[n] > 2) {
            varname2.add(v[2]);
        }
        // mix them up until no tautologies remain 
        if (nofAssignments[n] > 1) {
            do {
                Collections.shuffle(varname1);
                Collections.shuffle(varname2);
            } while ((varname1.get(0).equals(varname2.get(0)) && varname1.get(1).equals(varname2.get(1)))
                    || (varname1.get(0).equals(varname2.get(0)))
                    || (varname1.get(1).equals(varname2.get(1))));
        }

        question = generateallValues(n, question, v, integers);

        //init instructions
        //
        for (int i = 0; i < nofInstructions[n]; i++) {
            question += varname1.get(i) + " = " + varname2.get(i) + ";";
            String left = (String) varname1.get(i);
            String right = (String) varname2.get(i);
            allInstructions[n][i] = new Instruction(left, right);
            System.out.println("All instructions:Q" + n + " instr i" + i
                    + "  lhs" + allInstructions[n][i].lhs + "rhs" + allInstructions[n][i].rhs);
        }
        for (int i = 0; i < (7 - (nofAssignments[n] + 1 + nofInstructions[n])); i++) {
            question += " ;";
        }
        //System.out.println("C2:value of question :" + question);
        return question;
    }

    private static String generateallValues(int questionIndex, String question, String[] v, int[] integers) {
        //init variable definitions AND CREATES allValues[][]

        for (int j = 0; j < nofAssignments[questionIndex]; j++) {
            question += "int " + v[j] + " = " + integers[j] + ";";
            String[] s = new String[2];
            s[0] = v[j];
            s[1] = Integer.toString(integers[j]);
            allValues[questionIndex][j] = stringToValue(s);
            System.out.println("generateQuestion():allValues ="
                    + allValues[questionIndex][j].name
                    + allValues[questionIndex][j].number);
        }
        question += " ;";
        return question;
    }

    public static Value stringToValue(String[] s) {
        Value v = new Value();
        v.number = Integer.parseInt(s[1]);
        v.name = s[0];
        return v;
    }
//    
//click on answer     
//

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if ((obj) == (clearAll)) {
            //System.out.println("C4: getting to clear it all up for this page");
            for (int i = 0; i < NOFMODELS; i++) {
                ans[i].setSelected(false);
                answersGiven[questionPage][i] = false;
            }
            myFrame.repaint();
        } else if ((obj) == (next)) {

            if (questionPage >= (NOFQUESTIONS - 1)) {
                questionPage = 0;

            } else {
                questionPage++;
            }
            taskQuestion.setText("Q" + questionPage + questions[questionPage]);
            getPage(questionPage);
            displayAnswerChoice(questionPage);
            myFrame.repaint();
        } else if (obj == (back)) {
            if (questionPage > 0) {
                questionPage--;
            } else if (questionPage == 0) {
                questionPage = (NOFQUESTIONS - 1);
            }
            taskQuestion.setText("Q" + questionPage + questions[questionPage]);
            getPage(questionPage);
            displayAnswerChoice(questionPage);
            myFrame.repaint();
        } else if ((obj) == (ans[0])) {
            if (ans[0].isSelected()) {
                answersGiven[questionPage][0] = true;
            } else {
                answersGiven[questionPage][0] = false;
            }
        } else if ((obj) == (ans[1])) {
            if (ans[1].isSelected()) {
                answersGiven[questionPage][1] = true;
            } else {
                answersGiven[questionPage][1] = false;
            }
        } else if ((obj) == (ans[2])) {
            if (ans[2].isSelected()) {
                answersGiven[questionPage][2] = true;
            } else {
                answersGiven[questionPage][2] = false;
            }
        } else if ((obj) == (ans[3])) {
            if (ans[3].isSelected()) {
                answersGiven[questionPage][3] = true;
            } else {
                answersGiven[questionPage][3] = false;
            }
        } else if ((obj) == (ans[4])) {
            if (ans[4].isSelected()) {
                answersGiven[questionPage][4] = true;
            } else {
                answersGiven[questionPage][4] = false;
            }
        } else if ((obj) == (ans[5])) {
            if (ans[5].isSelected()) {
                answersGiven[questionPage][5] = true;
            } else {
                answersGiven[questionPage][5] = false;
            }
        } else if ((obj) == (ans[6])) {
            if (ans[6].isSelected()) {
                answersGiven[questionPage][6] = true;
            } else {
                answersGiven[questionPage][6] = false;
            }
        } else if ((obj) == (ans[7])) {
            if (ans[7].isSelected()) {
                answersGiven[questionPage][7] = true;
            } else {
                answersGiven[questionPage][7] = false;
            }
        } else if ((obj) == (ans[8])) {
            if (ans[8].isSelected()) {
                answersGiven[questionPage][8] = true;
            } else {
                answersGiven[questionPage][8] = false;
            }
        } else if ((obj) == (ans[9])) {
            if (ans[9].isSelected()) {
                answersGiven[questionPage][9] = true;
            } else {
                answersGiven[questionPage][9] = false;
            }
        } else if ((obj) == (ans[10])) {
            if (ans[10].isSelected()) {
                answersGiven[questionPage][10] = true;
            } else {
                answersGiven[questionPage][10] = false;
            }
        } else if (obj == (finish)) {
            //check if sure ok and cancel
            for (int page = 0; page < NOFQUESTIONS; page++) {
                for (int q = 0; q < NOFMODELS; q++) {
                    System.out.println("answers given page:" + page + " " + answersGiven[page][q]);;
                }
            }

            score();
            //write score to file
            //myFrame.dispose();
        }

    }

    private void score() {
        //count the consiistency of the most represented model

    }

    private void getPage(int questionPage) {
        //reset all radio buttons to false
        for (int q = 0; q < NOFMODELS; q++) {
            ans[q].setSelected(false);
        }
        setQuestionsToLabels();
        displayPageAnswers(questionPage);
    }

    private void displayPageAnswers(int questionPage) {

        for (int i = 0; i < NOFMODELS; i++) {
            if (answersGiven[questionPage][i] == true) {
                ans[i].setSelected(true);
            } else {
                ans[i].setSelected(false);
            }
        }

    }

    private void generateAnswerChoiceTexts() {

        String[] optionValues = new String[NOFQUESTIONS];
        boolean[] construct = new boolean[3];

        for (int i = 0; i < NOFQUESTIONS; i++) {
            for (int j = 0; j < NOFMODELS; j++) {
                optionValues[i] = "";
                //TODO for each model take values of every assignment 
                //DONE!    
                //TODO assignments by model
                //
                construct = createConstruct(j);
                createResultsValues(construct, i, j);
                for (int k = 0; k < nofAssignments[i]; k++) {
                    String name = allResultsValues[i][j][k].name;
                    String number = Integer.toString(allResultsValues[i][j][k].number);
                    optionValues[i] += name + " = " + number + ";";
                }
                answerChoiceText[i][j] = optionValues[i];
            }
        }
        //TODO output every value of this question to answerChoiceTexts String
    }

    private void createResultsValues(boolean[] construct, int questionIndex,
            int modelId) {
        //get first variable name based on direction
        //get second variable name convert to number
        //if add add to second variable
        //else replace second variable
        //if lose lose number of first variable

        Value[][] originalValues = new Value[NOFQUESTIONS][3];
        for (int u = 0; u < NOFQUESTIONS; u++) {
            for (int v = 0; v < 3; v++) {
                originalValues[u][v] = new Value();
            }
        }
        //get start values for the question
        for (int a = 0; a < nofAssignments[questionIndex]; a++) {
            String nameOrig = allValues[questionIndex][a].name;
            int valueOrig = allValues[questionIndex][a].number;
            originalValues[questionIndex][a].name = nameOrig;
            originalValues[questionIndex][a].number = valueOrig;
        }
        Value[] resultingValues = new Value[3];
        
        
        Value[] currentValues = new Value[nofAssignments[questionIndex]];
        for (int k = 0; k < nofAssignments[questionIndex]; k++) {
            currentValues[k] = originalValues[questionIndex][k];
            resultingValues[k] = originalValues[questionIndex][k];
        }
        Instruction[] currentInstructions = new Instruction[3];
        currentInstructions[0] = new Instruction();
        currentInstructions[1] = new Instruction();
        currentInstructions[2] = new Instruction();

        String zeroed = "";
        String currentLeftName = "";
        String currentRightName = "";
        int currentLeftNumber = -2;
        int currentRightNumber = -2;
        String startingLeftName ="";
        String startingRightName ="";
        Value startingLeftValue = new Value();
        Value startingRightValue = new Value();
        if (construct != null) {

            //get all instructions for this question
            for (int k = 0; k < nofInstructions[questionIndex]; k++) {
                currentInstructions[k] = allInstructions[questionIndex][k];
                System.out.println("Instructions Q" + questionIndex + ":  "
                        + currentInstructions[k].lhs
                        + currentInstructions[k].rhs);
            }

            //apply construct on the instructions using originalValues to begin with
            for (int k = 0; k < nofInstructions[questionIndex]; k++) {
                    //zero values
                    
                    zeroed = "";
                    //get instruction left Value.name
                    startingLeftValue.name = currentInstructions[k].lhs;
                    //get instruction left Value.number
                    for (int m = 0; m < nofAssignments[questionIndex]; m++) {
                        if (originalValues[questionIndex][m].name.equals(startingLeftValue.name)) {
                            startingLeftValue.number = resultingValues[m].number;
                            //System.out.println("@DIRECTION left to right ");
                        }
                    } 
                    //get instructions right  Value.name
                    startingRightValue.name = currentInstructions[k].rhs;
                    //get instruction right 
                    for (int m = 0; m < nofAssignments[questionIndex]; m++) {
                        if (originalValues[questionIndex][m].name.equals(startingRightValue.name)) {
                            startingRightValue.number = resultingValues[m].number;
                            //System.out.println("@DIRECTION left to right ");
                        }
                    }
                    //main logic block----------------------------------------------
                    if (construct[DIRECTION] == false) {
                        if (construct[ADD]) {
                            if (construct[LOSE]) {
                                currentRightNumber = startingLeftValue.number
                                        + startingRightValue.number;
                                zeroed = startingLeftValue.name;
                                
                                System.out.println("Model:0");
                            } else if (construct[LOSE] == false) {
                                currentRightNumber = startingLeftValue.number
                                        + startingRightValue.number;
                                System.out.println("Model:1");
                            } else;
                        } else if (construct[ADD] == false) {
                            if (construct[LOSE]) {
                                currentRightNumber = startingLeftValue.number;
                                zeroed = startingLeftValue.name;
                                
                                System.out.println("Model:4");
                            } else if (construct[LOSE] == false) {
                                currentRightNumber = startingLeftValue.number;
                                System.out.println("Model:5");
                            }
                        }
                        
                        //find right name from the currentValues
                        int indexRight = findValue(startingRightValue, currentValues);
                        //assign right currentRightNumber to currentValues
                        assert indexRight >= 0;
                        currentValues[indexRight].number = currentRightNumber; 
                        indexRight = -1;
                        //find left name from the currentValues
                        int indexLeft = findValue(startingLeftValue, currentValues);
                        assert indexLeft >= 0;
                        //assign left cuurent number to currentValues
                        currentValues[indexLeft].number = startingLeftValue.number;
                        indexLeft = -1;                        
                        if (!zeroed.equals("")) {
                            for (int x = 0; x < nofAssignments[questionIndex]; x++) {
                                if (currentValues[x].name.equals(zeroed)) {
                                    currentValues[x].number = 0;
                                    //System.out.println("@DIRECTION left to right ");
                                }
                            }
                        } else;
                        
                    } else if (construct[DIRECTION] == true) {
                        //find the int value  of the name in the instruction
                        if (construct[ADD]) {
                            if (construct[LOSE]) {
                                currentLeftNumber = startingRightValue.number
                                        + startingLeftValue.number;
                                zeroed = startingRightValue.name;
                                
                                System.out.println("Model:2");
                            } else if (construct[LOSE] == false) {
                                currentLeftNumber = startingRightValue.number
                                        + startingLeftValue.number;
                                System.out.println("Model:3");
                            } else;
                        } else if (construct[ADD] == false) {
                            if (construct[LOSE]) {
                                currentLeftNumber = startingRightValue.number;
                                zeroed = startingRightValue.name;
                                
                                System.out.println("Model:6");
                            } else if (construct[LOSE] == false) {
                                currentLeftNumber = startingRightValue.number;
                                System.out.println("Model:7");
                            }
                        } else;
                        
                        
                        //find left name from the currentValues
                        int indexLeft = findValue(startingLeftValue, currentValues);
                        assert indexLeft >= 0;
                        //assign left cuurent number to currentValues
                        currentValues[indexLeft].number = currentLeftNumber;
                        indexLeft = -1;                        
                                                
                        //find right name from the currentValues
                        int indexRight = findValue(startingRightValue, currentValues);
                        //assign right currentRightNumber to currentValues
                        assert indexRight >= 0;
                        currentValues[indexRight].number = startingRightValue.number; 
                        indexRight = -1;
                        if (!zeroed.equals("")) {
                            for (int x = 0; x < nofAssignments[questionIndex]; x++) {
                                if (currentValues[x].name.equals(zeroed)) {
                                    currentValues[x].number = 0;
                                    //System.out.println("@DIRECTION left to right ");
                                }
                            }
                        } else;
                        
                    } else;
                    
                
                //set resultingValues values to currentValues
                for (int t = 0; t < nofAssignments[questionIndex]; t++) {
                    if (currentValues[t].number >= 0) {//copy numbers to resultingValues
                        //vn has to be the same as list of allValues[questionIndex][] of this question
                        resultingValues[t].name = currentValues[t].name;
                        resultingValues[t].number = currentValues[t].number;
//                        
                        allResultsValues[questionIndex][modelId][t] = resultingValues[t];
                    }
                }

            }
            //set resultingValues based on the last current Values
            
            System.out.print("END of calculating Model" + modelId + ": ");
            for (int z = 0; z < nofAssignments[questionIndex]; z++) {
                System.out.println("RESULTS:" + resultingValues[z].name
                        + "   " + resultingValues[z].number);
            }

            System.out.println("END variables worked out for Q" + questionIndex);
        } else {
            //3 options:
            //swap
            //equality
            //no change
        }

    }
    private int findValue(Value saught, Value[] allValues ){
        int index = -1;
        for(int t = 0; t < allValues.length; t++){
            if(saught.name.equals(allValues[t].name))index = t;
        }    
        return index;
    }
    
    
    private boolean[] createConstruct(int modelNumber) {
        boolean[] construct = new boolean[3];
        //0 -- add
        //1 -- left/right
        //2 -- lose value
        final int ADD = 0;
        final int DIRECTION = 1;
        final int LOSE = 2;
        switch (modelNumber) {
            case 0:
                construct[ADD] = false;
                construct[DIRECTION] = false;
                construct[LOSE] = false;
                break;
            case 1:
                construct[ADD] = false;
                construct[DIRECTION] = false;
                construct[LOSE] = true;
                break;
            case 2:
                construct[ADD] = false;
                construct[DIRECTION] = true;
                construct[2] = false;
                break;
            case 3:
                construct[ADD] = false;
                construct[DIRECTION] = true;
                construct[LOSE] = true;
                break;
            case 4:
                construct[ADD] = true;
                construct[DIRECTION] = false;
                construct[LOSE] = false;
                break;
            case 5:
                construct[ADD] = true;
                construct[DIRECTION] = false;
                construct[LOSE] = true;
                break;
            case 6:
                construct[ADD] = true;
                construct[DIRECTION] = true;
                construct[LOSE] = false;
                break;
            case 7:
                construct[ADD] = true;
                construct[DIRECTION] = true;
                construct[LOSE] = true;
                break;
            default:
                System.out.println("not an assignment");
                construct = null;
                break;
        }

        return construct;
    }

    private void displayAnswerChoice(int questionPage) {
        boolean[] construction = {true, true, true};
        String[] constructOptions = {
            "ADD", "NORMAL",
            "Left2Right", "Right2Left",
            "LOSE", "KEEP"
        };
        String[] constr = new String[3];
        constr[0] = "";
        constr[1] = "";
        constr[2] = "";
        for (int i = 0; i < NOFMODELS; i++) {
            construction = createConstruct(i);
            //constr[0] = (construction[0]) ? constructOptions[0] : constructOptions[1];
            //constr[1] = (construction[1]) ? constructOptions[2] : constructOptions[3];
            //constr[2] = (construction[2]) ? constructOptions[4] : constructOptions[5];
            String answerwithconstruct = answerChoiceText[questionPage][i];
            //answerwithconstruct += constr[0] + constr[1] + constr[2];
            answerChoice[i].setText(answerwithconstruct);
        }
    }

    public static int getNumber(int index, String[] code) {
        int r = -1;
        if (!code[index].isEmpty()) {
            r = Integer.parseInt(code[index].substring(1));
        }
        return r;
    }

    //from the assignment
    public static String getName(int index, String[] code) {
        String r = "";
        if (!code[index].isEmpty()) {
            r = code[index].substring(0, 1);

        }
        return r;
    }

    public static class Value {

        int number;
        String name;

        public Value() {
            this.number = 0;
            this.name = "";
        }

        public Value(int v, String n) {
            this.number = v;
            this.name = n;
        }

        public String toString(Value[] results) {
            String s = "";
            for (Value v : results) {
                s = s + v.name + " = " + v.number + ";   ";
            }
            return s;
        }

        public String valueToString(Value r) {
            String s = "";
            s = r.name + " = " + r.number + ";   ";
            return s;
        }

        public int returnValueOf(String n) {
            if (n.equals(name)) {
                return number;
            } else {
                return -1;
            }
        }
    }

    public static class Instruction {

        String lhs;
        String rhs;

        public Instruction(String l, String r) {
            this.lhs = l;
            this.rhs = r;
        }

        public Instruction() {
            this.lhs = "";
            this.rhs = "";
        }

        public void setSides(String l, String r) {
            this.lhs = l;
            this.rhs = r;
        }

        public String getLeft() {
            return this.lhs;
        }

        public String getRight() {
            return this.rhs;
        }

        public static Instruction stringToInstruction(String s) {
            return new Instruction(s.substring(0, 1), s.substring(1, 2));
        }

        public static Instruction codeToInstruction(String c) {
            return new Instruction(c.substring(0, 1), c.substring(4, 5));
        }
    }

}
