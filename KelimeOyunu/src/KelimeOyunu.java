import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import java.io.*;
import java.util.ArrayList;

import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLayeredPane;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class KelimeOyunu extends JFrame {

	private JLayeredPane contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KelimeOyunu frame = new KelimeOyunu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public KelimeOyunu() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1284, 700);
		contentPane = new JLayeredPane();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//program code variables
		
		//Creating panels and adding them to layeredPane
		ImagePanel pnlMenu = new ImagePanel(
		        new ImageIcon("images/mainMenu.png").getImage());
		contentPane.add(pnlMenu);
		
		//adding question panels
		ArrayList<ImagePanel> panelList = new ArrayList<ImagePanel>();
		
		for( int i = 0; i < 14; i ++) {
			panelList.add( new ImagePanel(
			        new ImageIcon("images/soru" + (i + 8) / 2 + ".png").getImage()) );
			panelList.get(i).setWord(i);
			contentPane.add(panelList.get(i));
		}
		
		
		//Timer
		MyTime time = new MyTime();
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				time.decreaseTime();
				if( time.getMinute() == 0 && time.getSecond() == 0) {
					((Timer) evt.getSource()).stop();
				}
				for( int i = 0 ; i < 14; i++) {
					panelList.get(i).setTime(time.getMinute(),  time.getSecond());
					panelList.get(i).repaint();
				}
			}
		};
		Timer timer = new Timer(1000 , taskPerformer); //end of timer
		
		//Creating score board
		PlayerScore playerScore = new PlayerScore();
		JLabel lblScore = new JLabel("");
		lblScore.setBounds(220, 215, 100, 50);
		lblScore.setHorizontalAlignment(JLabel.CENTER);
		lblScore.setFont( new Font( "Times New Roman" , Font.BOLD, 30));
		panelList.get(0).add(lblScore);
		
		JLabel lblRemainingPoints = new JLabel("");
		lblRemainingPoints.setBounds(245, 255, 80, 50);
		lblRemainingPoints.setFont( new Font( "Times New Roman" , Font.BOLD, 30));
		lblRemainingPoints.setForeground( Color.red);
		panelList.get(0).add(lblRemainingPoints);
		
		//Creating count down label
		JLabel lblCountDown = new JLabel("");
		lblCountDown.setBounds(685, 150, 100, 100);
		lblCountDown.setFont( new Font( "Times New Roman" , Font.BOLD, 80));
		lblCountDown.setForeground( Color.red);
		panelList.get(0).add(lblCountDown);
		
		//Creating buttons
		CurrentQuestion currentQuestion = new CurrentQuestion();
		JButton btnStart = new JButton("BASLA");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPane.removeAll();
				contentPane.add(panelList.get(0));
				contentPane.repaint();
				contentPane.revalidate();
				lblScore.setText("0");
				lblRemainingPoints.setText("400");
				timer.start();
			}
		});
		btnStart.setFont( new Font( "Times New Roman", Font.BOLD, 40));
		btnStart.setBounds(710, 481, 335, 78);
		pnlMenu.add(btnStart);
		
		JButton btnShowLetter = new JButton("Harf Al");
	    btnShowLetter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( panelList.get(currentQuestion.getCurrentQuestion()).getRemainingPoints() == 1) {
					panelList.get(currentQuestion.getCurrentQuestion()).showLetter();
					lblRemainingPoints.setText(panelList.get(currentQuestion.getCurrentQuestion()).getRemainingPoints() + "" );
					((JButton) e.getSource()).setEnabled(false);
					timer.stop();
				}
				else {
					panelList.get(currentQuestion.getCurrentQuestion()).showLetter();
					lblRemainingPoints.setText(panelList.get(currentQuestion.getCurrentQuestion()).getRemainingPoints() + "00" );
				}
			}
		});
	    btnShowLetter.setFont( new Font( "Times New Roman", Font.PLAIN, 30));
		btnShowLetter.setBounds(900, 50, 335, 78);
		panelList.get(0).add(btnShowLetter); //end of adding buttons
		
		
		CountDown countDown = new CountDown();
		ActionListener task = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				
				lblCountDown.setText( countDown.getCountDown() + "");
				countDown.decrease();
				if( countDown.getCountDown() == -1) {
					((Timer) evt.getSource()).stop();
				}
			}
		};
		Timer countDownTimer = new Timer(1000 , task);
		
		JButton btnStop = new JButton("Durdur");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				btnStop.setEnabled(false);
				btnShowLetter.setEnabled(false);
				countDownTimer.start();
				
			}
		});
		btnStop.setFont( new Font( "Times New Roman", Font.PLAIN, 30));
		btnStop.setBounds(900, 150, 335, 78);
		panelList.get(0).add(btnStop);
		
		JButton btnCorrect = new JButton("Dogru");
		JButton btnWrong = new JButton("Yanlis");
		
		btnCorrect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( currentQuestion.getCurrentQuestion() != 13) {
					countDownTimer.stop();
					countDown.restart();
					playerScore.addScore( panelList.get(currentQuestion.getCurrentQuestion()).getRemainingPoints() );
					currentQuestion.nextQuestion();
					contentPane.removeAll();
					contentPane.add(panelList.get( currentQuestion.getCurrentQuestion() ) );
					contentPane.repaint();
					contentPane.revalidate();
					btnShowLetter.setEnabled(true);
					btnStop.setEnabled(true);
					lblScore.setText(playerScore.getScore() + "00");
					lblCountDown.setText("");
					panelList.get(currentQuestion.getCurrentQuestion()).add(lblScore);
					panelList.get(currentQuestion.getCurrentQuestion()).add(lblRemainingPoints);
					panelList.get(currentQuestion.getCurrentQuestion()).add(btnCorrect);
					panelList.get(currentQuestion.getCurrentQuestion()).add(btnWrong);
					panelList.get(currentQuestion.getCurrentQuestion()).add(btnStop);
					panelList.get(currentQuestion.getCurrentQuestion()).add(btnShowLetter);
					panelList.get(currentQuestion.getCurrentQuestion()).add(lblCountDown);
					lblRemainingPoints.setText( panelList.get(currentQuestion.getCurrentQuestion()).getTotalPoints() + "00");
					timer.start();
				}
				else {
					btnShowLetter.setEnabled(false);
					btnStop.setEnabled(false);
					btnCorrect.setEnabled(false);
					btnWrong.setEnabled(false);
					timer.stop();
					countDownTimer.stop();
					playerScore.addScore( panelList.get(currentQuestion.getCurrentQuestion()).getRemainingPoints() );
					lblScore.setText(playerScore.getScore() + "00");
				}
			}
		});
		btnCorrect.setFont( new Font( "Times New Roman", Font.PLAIN, 30));
		btnCorrect.setBackground( Color.green);
		btnCorrect.setBounds(900, 250, 150, 78);
		panelList.get(0).add(btnCorrect); 
		
		btnWrong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( currentQuestion.getCurrentQuestion() != 13) {
					countDownTimer.stop();
					countDown.restart();
					playerScore.removeScore( panelList.get(currentQuestion.getCurrentQuestion()).getRemainingPoints() );
					currentQuestion.nextQuestion();
					contentPane.removeAll();
					contentPane.add(panelList.get( currentQuestion.getCurrentQuestion() ) );
					contentPane.repaint();
					contentPane.revalidate();
					btnShowLetter.setEnabled(true);
					btnStop.setEnabled(true);
					lblScore.setText(playerScore.getScore() + "00");
					lblCountDown.setText("");
					panelList.get(currentQuestion.getCurrentQuestion()).add(lblScore);
					panelList.get(currentQuestion.getCurrentQuestion()).add(lblRemainingPoints);
					panelList.get(currentQuestion.getCurrentQuestion()).add(btnCorrect);
					panelList.get(currentQuestion.getCurrentQuestion()).add(btnWrong);
					panelList.get(currentQuestion.getCurrentQuestion()).add(btnStop);
					panelList.get(currentQuestion.getCurrentQuestion()).add(btnShowLetter);
					panelList.get(currentQuestion.getCurrentQuestion()).add(lblCountDown);
					lblRemainingPoints.setText( panelList.get(currentQuestion.getCurrentQuestion()).getTotalPoints() + "00");
					timer.start();
				}
				else {
					btnShowLetter.setEnabled(false);
					btnStop.setEnabled(false);
					btnCorrect.setEnabled(false);
					btnWrong.setEnabled(false);
					timer.stop();
					countDownTimer.stop();
					playerScore.removeScore( panelList.get(currentQuestion.getCurrentQuestion()).getRemainingPoints() );
					lblScore.setText(playerScore.getScore() + "00");
				}
			}
		});
		btnWrong.setFont( new Font( "Times New Roman", Font.PLAIN, 30));
		btnWrong.setBackground(Color.red);
		btnWrong.setBounds(1080, 250, 150, 78);
		panelList.get(0).add(btnWrong);//end of creating buttons
		
	}
}
	
class ImagePanel extends JPanel {

	  private Image img;
	  private String minute;
	  private String second;
	  private String word;
	  private String meaning;
	  private char[] lettersArray;
	  private ArrayList<JLabel> letters;
	  private int totalPoints;
	  private int remainingPoints;
	  private ArrayList<Integer> showedLetters;


	  public ImagePanel(Image img) {
		//properties
		minute = "";
		second = "";
		
		//adding background image and layout
	    this.img = img;
	    Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	  	
	    //creating letters
	    
	    //letter positions
	    ArrayList<Integer> letterPositions = new ArrayList<Integer>();
	    letterPositions.add(185);
	    letterPositions.add(290);
	    letterPositions.add(400);
	    letterPositions.add(515);
	    letterPositions.add(625);
	    letterPositions.add(735);
	    letterPositions.add(845);
	    letterPositions.add(955);
	    letterPositions.add(1070);
	    letterPositions.add(1175);
	    
	    //adding letters
	    letters = new ArrayList<JLabel>();
	    for( int i = 0 ; i < 10; i++) {
	    	letters.add( new JLabel(""));
	    	letters.get(i).setFont(new Font("Times New Roman", Font.BOLD, 60));
	    	letters.get(i).setBounds(letterPositions.get(i), 315, 202, 132);
	    	add(letters.get(i));
	    } //end of adding letters
	    
	  }

	  public void paintComponent(Graphics g) {
	    g.drawImage(img, 0, 0, null);
	    g.setFont(new Font("Times New Roman", Font.BOLD	, 30));
	    g.setColor( Color.black);
	    g.drawString( minute + " : " + second ,  260, 470);
	  }
	  
	  public void setTime( int minute , int second) {
		  this.minute = "0" + minute;
		  if( second - 10 < 0) {
			  this.second = "0" + second;
		  }
		  else
			  this.second = second + "";
	  }
	  
	  public void setWord( int questionNumber) throws IOException {
		  Words words = new Words();
		  this.word = words.getWordsList().get(questionNumber);
		  this.meaning = words.getMeaningsList().get(questionNumber);
		  this.lettersArray = this.word.toCharArray();
		  showedLetters = new ArrayList<Integer>();
		  this.totalPoints = word.length();
		  this.remainingPoints = word.length();
		  
		  JTextArea textArea = new JTextArea( this.meaning, 6, 20);
	      textArea.setFont(new Font("Times New Roman", Font.PLAIN, 25));
	      textArea.setForeground( Color.white);
	      textArea.setLineWrap(true);
	      textArea.setWrapStyleWord(true);
	      textArea.setOpaque(false);
	      textArea.setEditable(false);
	      textArea.setBounds(200, 485, 1000, 100);

	      add(textArea);
	  }
	  
	  public void showLetter() {
		  if( word.length() == 10) {
			  int random;
			  do {
				  random = (int) (Math.random() * lettersArray.length) ;
			  } while( showedLetters.contains(random));
			  letters.get(random).setText( lettersArray[random] + "");
			  remainingPoints--;
			  showedLetters.add(random);
		  }
		  else {
			  int random;
			  do {
				  random = (int) (Math.random() * lettersArray.length) ;
			  } while( showedLetters.contains(random));
			  letters.get(random + 1).setText( lettersArray[random] + "");
			  remainingPoints--;
			  showedLetters.add(random);
		  }
	  }
	  
	  public int getRemainingPoints() {
		  return remainingPoints;
	  }
	  
	  public int getTotalPoints() {
		  return totalPoints;
	  }
	  
	  
	  
}

class MyTime{
	//properties
	int minute;
	int second;
	//constructors
	public MyTime() {
		minute = 4;
		second = 0;
	}
	
	//methods
	public void decreaseTime() {
		if( second == 0 && minute != 0) {
			minute--;
			second = 59;
		}
		else if (second == 0 && minute == 0){
		}
		else {
			second--;
		}
	}
	
	public int getMinute() {
		return minute;
	}
	
	public int getSecond() {
		return second;
	}
}

class Words{
	//properties
	ArrayList<String> words = new ArrayList<String>();
	ArrayList<String> meanings = new ArrayList<String>();
	//constructor
	
	//methods
	public Words() throws IOException{
		File fileWords = new File("words/words.txt");
		File fileMeanings = new File("words/meanings.txt");
		  
		BufferedReader readerWords = new BufferedReader(new FileReader(fileWords));
		BufferedReader readerMeanings = new BufferedReader(new FileReader(fileMeanings)); 
		
		String word; 
		String meaning;
		
		while (( word = readerWords.readLine() ) != null) 
		  words.add(word); 
		while (( meaning = readerMeanings.readLine() ) != null) 
			  meanings.add(meaning); 
	}
	
	public String get(int questionNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<String> getWordsList(){
		return words;
	}
	
	public ArrayList<String> getMeaningsList(){
		return meanings;
	}
}

class CurrentQuestion {
	int currentQuestion;
	
	public CurrentQuestion() {
		currentQuestion = 0;
	}
	
	public void nextQuestion() {
		currentQuestion++;
	}
	
	public int getCurrentQuestion() {
		return currentQuestion;
	}
}

class PlayerScore{
	private int score;
	
	public PlayerScore() {
		score = 0;
	}
	
	public void addScore( int score) {
		this.score += score;
	}
	
	public void removeScore( int score) {
		this.score -= score;
	}
	
	public int getScore() {
		return score;
	}
	
}

class CountDown{
	int countDown;
	
	public CountDown() {
		countDown = 30;
	}
	
	public void decrease() {
		countDown--;
	}
	
	public int getCountDown() {
		return countDown;
	}
	
	public void restart() {
		countDown = 30;
	}
}

