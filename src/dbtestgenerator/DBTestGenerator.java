package dbtestgenerator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.swing.*;
import java.util.*;
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

    private static String[][] answerChoiceText;

    public static void main(String[] args) {
        new DBTestGenerator();

        //myFrame.toFront();
    }

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
        for(int i = 0;i < NOFQUESTIONS;i++){
            for (int j = 0; j < NOFMODELS;j++){
                for (int k = 0; k < nofAssignments[i];k++){
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
    private String getModelAnswer(String q, int i) {
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

        String question = "";

        ArrayList varname1 = new ArrayList();
        ArrayList varname2 = new ArrayList();

        String abc = "abcdefghijkmnpqrstuvwxyz";
        ArrayList abcArray = new ArrayList();
        
        for(int i = 0; i < abc.length(); i++){
            abcArray.add(abc.substring(i, i + 1));
        }
        
        String v[] = new String[nofAssignments[n]];
        int[] integers = new int[nofAssignments[n]];
        int indexOfVariableName;
        //TODO remove repetitions
        for (int i = 0; i < nofAssignments[n]; i++) {
            indexOfVariableName = (int) (Math.random() * abcArray.size());
            //System.out.println("C4: index of letter:"+ indexOfVariableName);
            v[i] = (String)abcArray.get(indexOfVariableName);
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
        //init variable definitions AND CREATES allValues[][]
        for (int j = 0; j < nofAssignments[n]; j++) {
            question += "int " + v[j] + " = " + integers[j] + ";";
            String[] s = new String[2];
            s[0] = v[j];
            s[1] = Integer.toString(integers[j]);
            allValues[n][j] = Value.stringToValue(s);
        }
        question += " ;";

        //init instructions
        //
        for (int i = 0; i < nofInstructions[n]; i++) {
            question += varname1.get(i) + " = " + varname2.get(i) + ";";
            String left =(String)varname1.get(i);
            String right =(String)varname2.get(i);
            allInstructions[n][i] = new Instruction(left,right);
            
        }
        for (int i = 0; i < (7 - (nofAssignments[n] + 1 + nofInstructions[n])); i++) {
            question += " ;";
        }
        //System.out.println("C2:value of question :" + question);
        return question;
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
        } //        if (( obj)==(ans[11])) {
        //
        //        }
        else if (obj == (finish)) {
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
//already doing that 
//    private void convertAssignmentsToallValues() {
//        for (int i = 0; i < NOFQUESTIONS; i++) {
//            for (int k = 0; k < 3; k++) {
//                //TODO strins to values
//                String[] s = new String[2];
//                s[0] = allAssignments[i][k].substring(4, 5);
//                s[1] = allAssignments[i][k].substring(8);
//                allValues[i][k]
//                        = Value.stringToValue(s);
//            }
//        }
//    }

    private void generateAnswerChoiceTexts() {
        
        String[] optionValues = new String[NOFQUESTIONS];
        boolean[] construct = new boolean[3];
        
        for (int i = 0; i < NOFQUESTIONS; i++) {
            for (int j = 0; j < NOFMODELS; j++) {
                optionValues[i] ="";
                //TODO for each model take values of every assignment 
                //DONE!    
                //TODO assignments by model
                //
                construct = createConstruct(j);
                create_ResultsValues(construct, i, j);
                for(int k = 0;k < nofAssignments[i];k++){                    
                    String name = allResultsValues[i][j][k].name;
                    String number = Integer.toString(allResultsValues[i][j][k].number);
                    optionValues[i] +=  name + " = "+ number + ";";
                }
                answerChoiceText[i][j] = optionValues[i];
            }
        }
        //TODO output every value of this question to answerChoiceTexts String
    }
    
    private void create_ResultsValues(boolean[] construct, int questionIndex,
        int modelId){
        Value[][] originalValues = new Value[NOFQUESTIONS][3];
        Value[] resultingValues = new Value[3];
        for(int k = 0;k < nofAssignments[questionIndex];k++){
            resultingValues[k] = originalValues[questionIndex][k]; 
        }
        
        Instruction[] currentInstructions = new Instruction[3];
        int v = 0;
        String s = ""; 
        final int ADD = 0;
        final int DIRECTION = 1;
        final int LOSE = 2;        
        if(construct != null){
            //get 
            for(int a = 0;a < nofAssignments[questionIndex];a++){
                originalValues[questionIndex][a] = allValues[questionIndex][a];                
            }
            //get all instructions
            for(int k = 0;k < nofInstructions[questionIndex];k++){
                currentInstructions[k] = allInstructions[questionIndex][k];
            }
            //apply construct on the instructions  changing originalValues
            //       
            for(int k = 0;k < nofInstructions[questionIndex];k++){
                if(construct[DIRECTION]){
                    //find the int value  of the name in the instruction
                    for(int m = 0;m < nofAssignments[questionIndex];m++){
                        s = currentInstructions[k].lhs;
                        if(originalValues[questionIndex][m].findNumber(s)>0){
                            v = originalValues[questionIndex][m].findNumber(s);                            
                        }
                    }                     
                }
                if(construct[ADD]);
                if(construct[LOSE]);
                
                
            }
            
        }else{
            //3 options
        }
        //TODO add resultingValues to allResultValues
        for(int k = 0;k < nofAssignments[questionIndex];k++){
            resultingValues[k].number = v;
                resultingValues[k].name = s;
            allResultsValues[questionIndex][modelId][k] = resultingValues[k] ;
        }        
    }
    
    private boolean[] createConstruct(int modelNumber){
        boolean[] construct = new boolean[3];  
        //0 -- add
        //1 -- left/right
        //2 -- lose value
        switch (modelNumber){
            case 0:
                construct[0] =false;
                construct[1] =false;
                construct[2] =false;
                break;
            case 1:
                construct[0] =false;
                construct[1] =false;
                construct[2] =true;
                break;
            case 2:
                construct[0] =false;
                construct[1] =true;
                construct[2] =false;
                break;
            case 3:
                construct[0] =false;
                construct[1] =true;
                construct[2] =true;
                break;
            case 4:
                construct[0] =true;
                construct[1] =false;
                construct[2] =false;
                break;
            case 5:
                construct[0] =true;
                construct[1] =false;
                construct[2] =true;
                break;
            case 6:
                construct[0] =true;
                construct[1] =true;
                construct[2] =false;
                break;
            case 7:
                construct[0] =true;
                construct[1] =true;
                construct[2] =true;
                break;
            default :
                System.out.println("not an assignment");
                construct = null;
                break;
        }
        
        return construct;
    }
    

    private void displayAnswerChoice(int questionPage) {
        for (int i = 0; i < NOFMODELS; i++) {
            answerChoice[i].setText(answerChoiceText[questionPage][i]);
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

    public static Value[] model(int modelNumber, String[] ass, String[] instr) {
        int n;
        Value a, b;
        a = new Value();
        b = new Value();
        //get the code from allAssignments 
        //for all instructions of current questionPage
        a.number = 2;// test value
        b.number = 1;// test value   
        a.name = "a";//set values according to instructions
        b.name = "b";
        //

        Value[] modelAnswer = new Value[2];

        switch (modelNumber) {
            /////////M1
            //
            case 1:

                modelAnswer = m1(a.number, b.number);
                break;
            ////////M2
            //
            case 2:
                modelAnswer = m2(a.number, b.number);
                break;
            ////////M3
            //
            case 3:
                modelAnswer = m3(a.number, b.number);
                break;

            ////////M4
            //
            case 4:
                modelAnswer = m4(a.number, b.number);
                break;
            ////////M5
            //
            case 5:
                modelAnswer = m5(a.number, b.number);
                break;
            ////////M6
            //
            case 6:
                modelAnswer = m6(a.number, b.number);

                break;
            ////////M7
            //
            case 7:
                modelAnswer = m7(a.number, b.number);
                break;
            ////////M8
            //
            case 8:

                modelAnswer = m8(a.number, b.number);
                break;
            ////////M9
            //
            case 9:
                modelAnswer = m9(a.number, b.number);

                break;
            ////////M10
            //
            case 10:

                modelAnswer = m10(a.number, b.number);
                break;
            ////////M11
            //
            case 11:

                modelAnswer = m11(a.number, b.number);
                break;

            default:
                System.out.println("Wrong model number");
        }
        return modelAnswer;
    }
///////////////////////////////////////////////////    
//  List of Models 
// ////////////////////////////////////////////////

    public static Value[] m1(int first, int second) {
        //Assign: to Left
        //Lose value
        Value[] v = new Value[2];
        first = second;
        second = 0;

        v[0].number = first;
        v[1].number = second;

        return v;
    }

    public static Value[] m2(int first, int second) {
        //Assign: to Left
        //Keep value
        Value[] v = new Value[2];
        first = second;
        v[0].number = first;
        v[1].number = second;

        return v;
    }

    public static Value[] m3(int first, int second) {
        //Assign to Right
        //Lose value
        Value[] v = new Value[2];
        second = first;
        second = 0;
        v[0].number = first;
        v[1].number = second;

        return v;
    }

    public static Value[] m4(int first, int second) {
        //Assign to right
        //Keep value
        Value[] v = new Value[2];
        second = first;

        v[0].number = first;
        v[1].number = second;

        return v;
    }

    public static Value[] m5(int first, int second) {
        //Add and Assign to Left
        //Lose value
        Value[] v = new Value[2];
        first = first + second;
        second = 0;
        v[0].number = first;
        v[1].number = second;

        return v;
    }

    public static Value[] m6(int first, int second) {
        //Add and Assign to Left
        //Keep value
        Value[] v = new Value[2];
        first = first + second;
        v[0].number = first;
        v[1].number = second;

        return v;
    }

    public static Value[] m7(int first, int second) {
        //Add and Assign to Right
        //Lose value
        Value[] v = new Value[2];
        second = second + first;
        first = 0;
        v[0].number = first;
        v[1].number = second;

        return v;
    }

    public static Value[] m8(int first, int second) {
        //Add and Assign to Right
        //Keep value
        Value[] v = new Value[2];
        second = second + first;
        v[0].number = first;
        v[1].number = second;

        return v;
    }

    public static Value[] m9(int first, int second) {
        //values do not change
        Value[] v = new Value[2];
        v[0].number = first;
        v[1].number = second;

        return v;
    }

    public static Value[] m10(int first, int second) {
        //assign means equal
        Value[] v = new Value[2];
        v[0].number = first;
        v[1].number = second;

        return v;
    }

    public static Value[] m11(int first, int second) {
        //swap
        Value[] v = new Value[2];
        int swapBuffer = first;
        first = second;
        second = swapBuffer;
        v[0].number = first;
        v[1].number = second;

        return v;
    }

    public static class Value {

        static int number;
        static String name;

        public static Value stringToValue(String[] s) {
            Value v = new Value();
            v.number = Integer.parseInt(s[1]);
            v.name = s[0];
            return v;
        }
        public static String valuesToString(Value[] results) {
            String s = "";
            for (Value v : results) {
                s = s + v.name + " = " + v.number + ";   ";
            }
            return s;
        }
        public static String valueToString(Value r){
            String s = "";
            s = r.name + " = " + r.number + ";   ";
            return s;
        }
        public static int findNumber(String n){
            if(n.equals(name)){
                return number;
            }
            else return 0;
        }
    }
    
    public static class Instruction{
        String lhs;
        String rhs;
        public Instruction(String l, String r){
            lhs = l;
            rhs = r;
        }
        public Instruction(){
            lhs = "";
            rhs = "";
        }
        public static Instruction stringToInstruction(String s){
            return new Instruction(s.substring(0,1), s.substring(1, 2));
        }
        public static Instruction codeToInstruction(String c){
            return new Instruction(c.substring(0,1), c.substring(4,5));
        }
    }
    
}
