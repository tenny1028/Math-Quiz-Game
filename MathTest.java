/*
 * Copyright (c) 2013 Jasper Reddin
 * All rights reserved
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import javax.swing.*;

/**
 * 
 * @author Jasper
 * 
 */

@SuppressWarnings("serial")
public class MathTest extends JFrame implements ActionListener, KeyListener{
	
	JScrollPane scrollPane;
	JPanel inputPanel;
	static JTextArea consoleMessages;
	static JTextField input;
	static int numberOfTimesPlayed;
	static int number1;
	static int number2;
	static int total;
	static int score;
	static Random generator = new Random();
	static JButton enter;
	static String LastLine1;
	static int difficultyLevel;
	
	// This field stores what the program
	// is waiting for when it waits for 
	// user input.
	static int state;
	
	// Difficulty fields
	public static final int ELEMENTARY_DIFFICULTY = 0;
	public static final int MIDDLE_SCHOOL_DIFFICULTY = 1;
	public static final int HIGH_SCHOOL_DIFFICULTY = 2;
	// State fields
	public static final int VARIABLE_STATE = 0;
	public static final int DIFFICULTY_CHANGING_STATE = 1;
	
	// These fields have been added for the local
	// Multiplayer mode, but I don't think I will
	// put that feature in quite yet.
	/*ObjectOutputStream outputstream;
	ObjectInputStream inputstream;
	ServerSocket serverThing;
	Socket connection;*/
	
	
	public MathTest() {
		super("Math Quiz");
		BorderLayout b33 = new BorderLayout(5,5);
		setLayout(b33);
		
		LastLine1 = "";

		consoleMessages = new JTextArea(35,60);
		consoleMessages.setEditable(false);
		consoleMessages.setFont(new java.awt.Font("Courier", 0, 12));
		consoleMessages.setLineWrap(true);
		consoleMessages.setWrapStyleWord(true);
		scrollPane = new JScrollPane(consoleMessages);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.NORTH);
		EnterText("MATH QUIZ!!! Let's see how much you know...");
		EnterText("To see a list of a commands, type /help or /?.");
		
		input = new JTextField(45);
		input.setFont(new java.awt.Font("Courier", 0, 12));
		inputPanel = new JPanel();
		enter = new JButton("Enter");
		enter.addActionListener(this);
		input.addKeyListener(this);
		inputPanel.add(input);
		inputPanel.add(enter);
		add(inputPanel, BorderLayout.SOUTH);
		
		addWindowFocusListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		        input.requestFocusInWindow();
		    }
		});
		
		EnterText("Please enter your difficulty: Elementary, Middle School, High School");
		setQuestionState(DIFFICULTY_CHANGING_STATE);
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		this.setLocation(550,160);
		pack();
	}
	
	public static void EnterText(String input){
		String currentValue = consoleMessages.getText();
		String newValue = currentValue + input + "\n";
		consoleMessages.setText(newValue);
		LastLine1 = input;
	}
	
	public static void setDifficulty(int newDifficulty){
		difficultyLevel = newDifficulty;
	}
	
	public static void setQuestionState (int newState){
		state = newState;
	}

	public static void newNumbers(){
		
		numberOfTimesPlayed += 1;
		EnterText("Question #" + numberOfTimesPlayed);
		
		if (difficultyLevel == 0){
			number1 = generator.nextInt(11);
			number2 = generator.nextInt(11);
		} else if (difficultyLevel == 1){
			number1 = generator.nextInt(21);
			number2 = generator.nextInt(21);
		} else if (difficultyLevel == 2) {
			number1 = generator.nextInt(41) - 20;
			number2 = generator.nextInt(41) - 20;
		}
		
		total= number1 + number2;
		EnterText("What is " + number1 + " + " + number2 + "?");
	}
	
	public static void nextQuestion(int numberOne, int numberTwo, int answer){
		if (numberOfTimesPlayed <= 9){
				if (total == answer){
					EnterText("You are correct!");
					score += 10;
					
					newNumbers();
				} else {
					EnterText("Wrong.");

					newNumbers();

				}
		} else {
			
			if (total == answer){
				EnterText("You are correct!");
				score += 10;
			}else{
				EnterText("Wrong.");
			}
			
			EnterText("You are finished with your quiz.");
			EnterText("Here's your score: " + score + "%");
			
			EnterText("Play again? (Y/N)");
		}
	}
	
	public void somethingHappened(){
		
		String txt;
		String txtToLowerCase;
		txt = input.getText();
		txtToLowerCase = txt.toLowerCase();
		
		if (state == VARIABLE_STATE){
			if (txtToLowerCase.equals("y") || txtToLowerCase.equals("/restart")){
				EnterText("Please enter your difficulty: Elementary, Middle School, High School");
				setQuestionState(DIFFICULTY_CHANGING_STATE);
			
				input.selectAll();
			} else if (txtToLowerCase.equals("n") || txtToLowerCase.equals("/quit")){
				EnterText("Okay. Good-bye.");
				EnterText("Thank you for playing.");
				EnterText("Please quit this application by clicking the red button on");
				EnterText("the top of the window.");
			
				input.setText("");
			
				input.setEditable(false);
				enter.setEnabled(false);
				setQuestionState(0);
			}else if(txtToLowerCase.equals("/clear")){
				consoleMessages.setText("");
				input.selectAll();
				EnterText(LastLine1); 
				setQuestionState(0);
			}else if (txtToLowerCase.contains("/setfont")){
				char data[] = txt.toCharArray();
				String newFont = String.copyValueOf(data, 9, data.length - 9);
			
				input.setFont(new java.awt.Font(newFont, 0, 12));
				consoleMessages.setFont(new java.awt.Font(newFont,0, 12));
			
				EnterText("Font changed to " + newFont);
			
				input.selectAll();
				setQuestionState(0);
			}else if(txtToLowerCase.contains("/say")){
				char data[] = txt.toCharArray();
				String txtForWrapTest = String.copyValueOf(data, 5, data.length - 5);
				EnterText(txtForWrapTest);
			
				input.selectAll();
				setQuestionState(0);
			}else if (txtToLowerCase.equals("/help") || txtToLowerCase.equals("/?")){
				EnterText("Here are all the available commands, their arguments, and what they do:\n");
				EnterText("/clear: Clears the entire log except the last line that has been displayed.\n");
				EnterText("/help (or /?): Displays all the available commands, their arguments, and what they do.\n");
				EnterText("/restart (or y): Restarts the game and resets the score to 0.\n");
				EnterText("/say <msg>: Displays the message you type.\n");
				EnterText("/setfont <font>: Changes the font of the program to the font you choose. Note that the font is case-sensitive.\n");
				EnterText("/quit (or n): Disables the controls and requires you to quit the program.\n");
				input.selectAll();
				setQuestionState(0);
			}else {
				try{ 
					int inputValue = Integer.parseInt(input.getText());
					nextQuestion(number1, number2, inputValue);
					input.requestFocusInWindow();
					input.selectAll();
				} catch (NumberFormatException e){
					EnterText("Please type a number or one of the");
					EnterText("commands available");
					input.selectAll();
				}
				setQuestionState(0);
			}
		}else if(state == DIFFICULTY_CHANGING_STATE){
					switch (txtToLowerCase) {
						case "elementary":
							setDifficulty(ELEMENTARY_DIFFICULTY);
							EnterText("Starting a new game set in the Elementary Difficulty.");
							numberOfTimesPlayed = 1;
							score = 0;
							EnterText("Question #" + numberOfTimesPlayed);
							number1 = generator.nextInt(11);
							number2 = generator.nextInt(11);
							total = number1 + number2;
							input.requestFocusInWindow();
							input.selectAll();
							EnterText("What is " + number1 + " + " + number2 + "?");
							setQuestionState(0);
							break;
						case "middle school":
							setDifficulty(MIDDLE_SCHOOL_DIFFICULTY);
							EnterText("Starting a new game set in the Middle School Difficulty.");
							numberOfTimesPlayed = 1;
							score = 0;
							EnterText("Question #" + numberOfTimesPlayed);
							number1 = generator.nextInt(11);
							number2 = generator.nextInt(11);
							total = number1 + number2;
							EnterText("What is " + number1 + " + " + number2 + "?");
							input.requestFocusInWindow();
							input.selectAll();
							setQuestionState(0);
							break;
						case "high school":
							setDifficulty(HIGH_SCHOOL_DIFFICULTY);
							EnterText("Starting a new game set in the High School Difficulty.");
							numberOfTimesPlayed = 1;
							score = 0;
							EnterText("Question #" + numberOfTimesPlayed);
							number1 = generator.nextInt(11);
							number2 = generator.nextInt(11);
							total = number1 + number2;
							EnterText("What is " + number1 + " + " + number2 + "?");		
							input.requestFocusInWindow();
							input.selectAll();
							setQuestionState(0);
							break;
						default:
							EnterText("That is not available at the time. Please choose a difficulty level.");
					}
		}else{
			EnterText("Error: State not defined. Please report this bug to http://sourceforge.net/projects/mathquizgame/tickets/");
		}
	}

	public void actionPerformed(ActionEvent actionEvent) {
		somethingHappened();
		
	}
	public void keyTyped(KeyEvent e){
		
	}
	
	public void keyPressed(KeyEvent keyEvent){
		if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
			somethingHappened();
        }
	}
	public void keyReleased(KeyEvent e){
		
	}

	public static void main(String[] args) {
		
		new MathTest();

	}

}