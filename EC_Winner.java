/*
 * Name: Chen-Meng Student
 * Login: cs11faiv * Date: Nov 16, 2017
 * File: EC_Winner.java
 * Sources of Help: None
 * The winning logic of the slot wheel machine.
*/

//Libraries to import
import objectdraw.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

/* The EC_Winner class */
public class EC_Winner extends ActiveObject
implements ActionListener{

	//Canvas dimension is 500x250
  public static final int CANVAS_WIDTH = 500;
  public static final int CANVAS_HEIGHT = 250;
  //Used to divide the canvas and image
  public static final int DIVIDER = 2;
  //The dimension of the slot
  public static final double IMAGE_WIDTH = 110;
  public static final double IMAGE_HEIGHT = 145;
  //The y-offset for center slot on the canvas
  private static final double WHEELS_Y_OFFSET = 5; 
  //The dimension of the winner image
  public static final int WINNER_WIDTH = 120;
  public static final int WINNER_HEIGHT = 62;
  //The prize for win is 15 times bet
  public static final int PRIZE = 15;

  //The three slot wheels
  private SlotWheel left;
  private SlotWheel center;
  private SlotWheel right;
  //The win and lose labels
  private JLabel winLabel;
  private	JLabel loseLabel;
  //The button that controls the spin
  private JButton spinButton;
  //The winner image
  private VisibleImage winner; 
  //The bank amount the user has
  private JTextField deposit;
  //The bet user indicates
  private JComboBox<String> bet;
  //The delay time
  private int baseDelay;
  //The drawing canvas
  private DrawingCanvas canvas;
  //Status whether user has made an initial spin
  private boolean initialClicked;
  //The status whether three slots are spinning
  private boolean isNotSpinning;
  //Track if the program is pending for next check after user spins the wheel.
  private boolean pending;
  //Status if the user has money left
  private boolean noMoney;
  //The number of wins and loses
  private int wins;
  private int loses;

  //Constructor
	public EC_Winner(SlotWheel left, SlotWheel center, SlotWheel right, 
	JLabel winLabel, JLabel loseLabel, JButton spinButton, Image winFile, 
  int baseDelay, JTextField deposit, JComboBox<String> bet, DrawingCanvas canvas){
 
    //Initialize values
		this.left = left;
		this.center = center;
		this.right = right;
		this.winLabel = winLabel;
		this.loseLabel = loseLabel;
		this.spinButton = spinButton;
    this.deposit = deposit;
    this.bet = bet;
    //Construct the winner image and hide
		this.winner = new VisibleImage(winFile, canvas.getWidth()/DIVIDER-WINNER_WIDTH/DIVIDER,
    WHEELS_Y_OFFSET+IMAGE_HEIGHT/DIVIDER-WINNER_HEIGHT/DIVIDER, canvas);
		this.winner.hide();
		this.baseDelay = baseDelay;
		this.canvas = canvas;

    //Required by active object interface
		start();
	}

/**
  * Implement when user performs an action event
  * @param evt the event generated
  */
  public void actionPerformed(ActionEvent evt){
    //User made a initial spin once the button is clicked
    initialClicked = true;
    //User has to enter deposit to click button,
    //and doing so makes deposit fixed
    deposit.setEnabled(false);
    //The program is pending for next win or lose detection
    pending = true;
    //Disable button
    spinButton.setEnabled(false);
    //Slots are not spinning while clicking the button
    isNotSpinning = false;
    //Cannot change bet once spins
    bet.setEnabled(false);
    //Hide the winner image
    winner.hide();
  }

/**
  * The method is called when the program starts running
  */
  public void run(){
    //Forever loop
  	while(true){
      if(noMoney){
        spinButton.setEnabled(false);
        bet.setEnabled(false);
        deposit.setText("GO HOME");
      }
      else{
        //Slot machine is not spinning when all slot wheels are not spinning
    		isNotSpinning = (left.notSpinning() && center.notSpinning() && right.notSpinning());
        //If slot mathine is not spinning, button is clicked, and program is pending for next decision
        if(!deposit.getText().equals("") && !initialClicked){
          spinButton.setEnabled(true);
        }
    		if(isNotSpinning && initialClicked && pending){
          //The button and bet are enabled
          spinButton.setEnabled(true);
          bet.setEnabled(true);
          //If three images are the same, the user wins
    			if(left.index == center.index && right.index == center.index && left.index == right.index){
            //Show winner images
    				winner.show();
            //Increment number of wins
    				wins++;
            //Update info label
    				winLabel.setText(String.format(PA7Strings.WIN_LABEL, wins));
            //Add prize amount to deposit
            deposit.setText(Integer.toString(Integer.parseInt(deposit.getText())
             - (bet.getSelectedIndex()+1) + (bet.getSelectedIndex()+1)*PRIZE));
            //The program is set not pending for next decision
            pending = false;
    			}
          //Otherwise user loses
          else{
            //Increment number of loses
            loses++;
            //Update info label
            loseLabel.setText(String.format(PA7Strings.LOSS_LABEL, loses));
            //Deduct bet
            deposit.setText(Integer.toString(Integer.parseInt(deposit.getText()) - (bet.getSelectedIndex()+1)));
            //If deposit is less than 0
            if(Integer.parseInt(deposit.getText())<=0){
              //User has no money
              noMoney = true;
            }
            //The program is set not pending for next decision
            pending = false;
          }
    		}
      }
      //Delay a while
  		pause(baseDelay);
  	}
  }
}