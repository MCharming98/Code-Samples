/*
 * Name: Chen-Meng Student
 * Login: cs11faiv * Date: Nov 16, 2017
 * File: EC_Slots.java
 * Sources of Help: None
 * The GUI controller for the slot machine.
*/

//Libraries to import
import objectdraw.*;
import Acme.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Image.*;
import javax.swing.*;
import javax.swing.event.*;

/**
  * The Slots class
  */
public class EC_Slots extends WindowController
implements ActionListener{
  
	//Set canvas dimension to 500x250
  public static final int CANVAS_WIDTH = 500;
  public static final int CANVAS_HEIGHT = 250;
  //Base delay, default to 75
  public static final int DEFAULT_BASE_DELAY = 75;
  //The range of the delay, [10,2000]
  public static final int DELAY_LOWER_BOUND = 10;
  public static final int DELAY_UPPER_BOUND = 2000;
  //The y-offset for center slot on the canvas
  private static final double WHEELS_Y_OFFSET = 5; 
  //The gap between slots
  private static final double SPACE_BETWEEN_WHEELS = 5;
  //Used to divide the canvas and the image
  public static final int CANVAS_DIVIDER = 2;
  public static final int IMAGE_DIVIDER = 2;
  //The dimension of the slot
  public static final double IMAGE_WIDTH = 110;
  public static final double IMAGE_HEIGHT = 145;
  //The center coordinate of the canvas
  public static final double HALF_IMAGE_WIDTH = IMAGE_WIDTH/IMAGE_DIVIDER;
  public static final double HALF_IMAGE_HEIGHT = IMAGE_HEIGHT/IMAGE_DIVIDER;
  //Number of images
  private static final int NUM_OF_IMAGES = 8;
  //The number of images each wheel contains
  private static final int WHEEL_1_TICKS = 22;
	private static final int WHEEL_2_TICKS = WHEEL_1_TICKS + 6;
	private static final int WHEEL_3_TICKS = WHEEL_2_TICKS + 6;
  //The delay of the wheels
	private static final int WHEEL_1_DELAY = 0;
  private static final int WHEEL_2_DELAY = WHEEL_1_DELAY + 25; 
  private static final int WHEEL_3_DELAY = WHEEL_2_DELAY + 25;
  //The bet amount user can choose, min 1 dollar and max 5 dollars
  private static final String[] BET_AMOUNT = {"1","2","3","4","5"};

  //The actual base delay used in the program.
  private static int baseDelay;
  //Number of wins and loses
  private static int wins;
  private static int losses;
  //The coordinate where slots are placed
  private static double leftSlotX;
  private static double leftSlotY;
  private static double centerSlotX;
  private static double centerSlotY;
  private static double rightSlotX;
  private static double rightSlotY;

  //The placeholder for the top part of the canvas
  private JPanel topPanel;
  //The placeholder for the title
  private JPanel titlePanel;
  //The placeholder for wins and loses
  private JPanel infoPanel;
  //The title label
  private JLabel titleLabel;
  //The label for wins and loses
  private JLabel winLabel;
  private JLabel lossLabel;
  //The place holder for win and lose label
  private JPanel winPanel;
  private JPanel losePanel;

  //The placeholder for bottom part of the canvas
  private JPanel footerPanel;
  //The button that controls the spin
  private JButton spinButton;
  //The placeholder for bank deposit info
  private JPanel depositPanel;
  //The bank amount the user has
  private JTextField deposit;
  //The label for indicating deposit
  private JLabel bankAmount;
  //The bet user indicates
  private JComboBox<String> bet;

  //The array storing all images
  public Image[] images = new Image[NUM_OF_IMAGES];
  //The image of a win
  public Image winFile;
  //The three slot wheels
  public SlotWheel leftSlot;
  public SlotWheel centerSlot;
  public SlotWheel rightSlot;
  //The EC winner object
  public EC_Winner ecWinner;

  //Constructor, initializes in base delay value
  public EC_Slots(int baseDelay){
  	this.baseDelay = baseDelay;
  }

  /**
  * The begin method is called once the window opens 
  * @param none
  */
  public void begin(){
    /* Initialize GUI containers and components */
  	topPanel = new JPanel();
    //Set topPanel to Grid layout
  	topPanel.setLayout(new GridLayout(2,1));
  	titlePanel = new JPanel();
    //Set title
  	titleLabel = new JLabel(String.format(PA7Strings.SLOTS_TITLE, "Chen's"));
  	infoPanel = new JPanel();
    infoPanel.setLayout(new GridLayout(1,2));
    //Set placeholder fro wins and loses
    winPanel = new JPanel();
    losePanel =  new JPanel();
    //Set wins and loses
  	winLabel = new JLabel(String.format(PA7Strings.WIN_LABEL, wins));
  	lossLabel = new JLabel(String.format(PA7Strings.LOSS_LABEL, losses));
    //Set bottom panel
  	footerPanel = new JPanel();
    //Set deposit panel
    depositPanel = new JPanel();
    depositPanel.setLayout(new GridLayout(1,2));
    //Set deposit input
    deposit = new JTextField();
    //Set deposit info
    bankAmount = new JLabel("Bank Amount:");
    //Set button
  	spinButton = new JButton(PA7Strings.BUTTON_TEXT);
    spinButton.setEnabled(false);
    //Set bet
    bet = new JComboBox<>(BET_AMOUNT);
    bet.setSelectedItem(BET_AMOUNT[0]);

    //Add components to containers
    titlePanel.add(titleLabel);
    infoPanel.add(Box.createHorizontalGlue());
    infoPanel.add(winPanel);
    winPanel.add(winLabel);
    infoPanel.add(losePanel);
    infoPanel.add(Box.createHorizontalGlue());
    losePanel.add(lossLabel);
    topPanel.add(titlePanel);
    topPanel.add(infoPanel);
    depositPanel.add(bankAmount);
    depositPanel.add(deposit);
    footerPanel.add(depositPanel);
    footerPanel.add(spinButton);
    footerPanel.add(bet);

    //Add panels on canvas
    this.add(topPanel, BorderLayout.NORTH);
    this.add(footerPanel, BorderLayout.SOUTH);

    //Calculate the coordinate of slot machines
    centerSlotX = canvas.getWidth()/CANVAS_DIVIDER-HALF_IMAGE_WIDTH;
    centerSlotY = WHEELS_Y_OFFSET;
    leftSlotX = centerSlotX-IMAGE_WIDTH-SPACE_BETWEEN_WHEELS;
    leftSlotY = centerSlotY;
    rightSlotX = centerSlotX+IMAGE_WIDTH+SPACE_BETWEEN_WHEELS;
    rightSlotY = centerSlotY;
    //Get winenr image file
    winFile = getImage("winner.jpg");

    //Get images for slot machine
    for(int i=0; i<NUM_OF_IMAGES; i++){
      images[i] = getImage(PA7Strings.IMG_NAMES[i]);
    }
    
    //Construct slot wheel and winner
    leftSlot = new SlotWheel(images, WHEEL_1_TICKS, baseDelay+WHEEL_1_DELAY, leftSlotX, leftSlotY, canvas);
    centerSlot = new SlotWheel(images, WHEEL_2_TICKS, baseDelay+WHEEL_2_DELAY, centerSlotX, centerSlotY, canvas);
    rightSlot = new SlotWheel(images, WHEEL_3_TICKS, baseDelay+WHEEL_3_DELAY, rightSlotX, rightSlotY, canvas);
    ecWinner = new EC_Winner(leftSlot, centerSlot, rightSlot, winLabel,
    lossLabel, spinButton, winFile, DEFAULT_BASE_DELAY, deposit, bet, canvas);

    //Let slot wheels and winner listen to button action
    spinButton.addActionListener(leftSlot);
    spinButton.addActionListener(centerSlot);
    spinButton.addActionListener(rightSlot);
    spinButton.addActionListener(ecWinner);

    //Validate all components
    this.validate();
  }

/**
  * Implement when user performs an action event
  * @param evt the event generated
  */
  public void actionPerformed(ActionEvent evt){}

/**
  * The main method for Slots class
  * @param args user input
  * Takes and stores in user input for delay time
  */
  public static void main(String[] args){
    //If user does not enter delay time
  	if(args.length==0){
      //Use default
      baseDelay = DEFAULT_BASE_DELAY;
  	}
    //If user enters one argument for delay time
  	else if(args.length==1){
      //If user enters an valid integer
  		try{
        //Store parsed integer into baseDelay
  			baseDelay = Integer.parseInt(args[0]);
        //If user enters unacceptable delay time 
  			if(baseDelay<DELAY_LOWER_BOUND||baseDelay>DELAY_UPPER_BOUND){
          //Notify user valid range
          System.out.format(PA7Strings.ERR_DELAY_RANGE, DELAY_LOWER_BOUND, DELAY_UPPER_BOUND);
          //Exit program
          System.exit(1);
  			}
  		}
      //If user enters an invalid integer
  		catch(NumberFormatException e){
        //Prints out error message and usage
  			System.out.format(PA7Strings.ERR_INVALID_DELAY, args[0]);
        System.out.println(PA7Strings.EC_USAGE);
        //Exit program
  			System.exit(1);
  		}
  	}
    //If user enters multiple arguments
  	else{
      //Print error and usage
  		System.out.println(PA7Strings.ERR_INVALID_ARGS);
  		System.out.println(PA7Strings.EC_USAGE);
      //Exit system
  		System.exit(1);
  	}

    //Initialize and create the window
    new Acme.MainFrame(new EC_Slots(baseDelay), args, CANVAS_WIDTH, CANVAS_HEIGHT);
  }

}//End of the EC_Slots class


