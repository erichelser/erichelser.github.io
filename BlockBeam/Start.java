import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;

public class Start implements Runnable
{
	private static Game g;
	public static void main(String[] args)
	{
		(new Thread(new Start())).start();
	}
	public void run()
	{
		JFrame jf=new JFrame("text");
		jf.setPreferredSize(new Dimension(800,600));

		g=new Game();
		jf.add(g);
		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);

		(new Thread(g)).start();

		/*while(true)
		{
			try{ Thread.sleep((int)(1000)); }
			catch(InterruptedException e) {}
			g.repaint();
		}*/
	}
}

class Game extends JPanel implements Runnable, MouseListener, ActionListener
{
	private static final int MAX_LEVEL=99;
	private static boolean repaintOK=false;
	private static boolean superRepaintOK=false;
	private static boolean initDone=false;
	private static Game singleton;
	private Board board;
	JPanel panel1;
	private Periodic periodic;

	private JPanel panel2;
	private JButton reset;

	private JButton levelUp;
	private JButton levelDown;
	private int levelNumber;
	private JLabel levelMsg;

	private JLabel message;

	private JPanel hintboxPanel;
	private JTextArea hintboxLabel;

	private JButton zoomIn;
	private JButton zoomOut;

	public void run()
	{
		singleton=this;
		board=new Board();
		periodic=new Periodic(100,board,this);

		setLayout(new BorderLayout());

		panel1=board;
		panel1.addMouseListener(this);
		panel1.setVisible(true);

		levelNumber=1;

		levelMsg=new JLabel("Level: "+levelNumber);
		levelMsg.setHorizontalAlignment(JLabel.CENTER);
		levelMsg.setVerticalAlignment(JLabel.CENTER);

		levelUp=new JButton("+");
		levelDown=new JButton("-");

		reset=new JButton("START");

		reset.addActionListener(this);
		levelUp.addActionListener(this);
		levelDown.addActionListener(this);

		message=new JLabel("MESSAGE");
		message.setHorizontalAlignment(JLabel.CENTER);
		message.setVerticalAlignment(JLabel.CENTER);

		zoomIn=new JButton("Zoom In");
		zoomIn.addActionListener(this);
		zoomOut=new JButton("Zoom Out");
		zoomOut.addActionListener(this);
		hintboxLabel=new JTextArea("Use the - and + buttons to select a level, then press Start to load it.");
		hintboxLabel.setLineWrap(true);
		hintboxLabel.setWrapStyleWord(true);
		hintboxLabel.setEditable(false);

		hintboxPanel=new JPanel(new BorderLayout());
		hintboxPanel.add(hintboxLabel,BorderLayout.NORTH);

		JPanel margin=new JPanel();
		margin.setSize(1,1);
		hintboxPanel.add(margin,BorderLayout.CENTER);


		panel2=new JPanel(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.weightx=0.5;
		c.weighty=0.5;

		c.gridx=0;
		c.gridy=0;
		c.gridwidth=3;
		c.gridheight=1;
		panel2.add(levelMsg,c);

		c.gridx=1;
		c.gridy=1;
		c.gridwidth=1;
		c.gridheight=1;
		panel2.add(levelUp,c);

		c.gridx=0;
		c.gridy=1;
		c.gridwidth=1;
		c.gridheight=1;
		panel2.add(levelDown,c);

		c.gridx=2;
		c.gridy=1;
		c.gridwidth=1;
		c.gridheight=1;
		panel2.add(reset,c);

		c.gridx=3;
		c.gridy=0;
		c.gridwidth=1;
		c.gridheight=2;
		c.weightx=0.9;
		c.ipadx=70;
		c.fill=GridBagConstraints.NONE;
		panel2.add(message,c);

		c.gridx=4;
		c.gridy=0;
		c.gridwidth=1;
		c.gridheight=1;
		c.weightx=0.5;
		c.ipadx=0;
		c.fill=GridBagConstraints.NONE;
		panel2.add(zoomIn,c);
		c.gridx=4;
		c.gridy=1;
		c.gridwidth=1;
		c.gridheight=1;
		c.weightx=0.5;
		c.ipadx=0;
		c.fill=GridBagConstraints.NONE;
		panel2.add(zoomOut,c);
		
		add(panel2,BorderLayout.NORTH);
		add(panel1,BorderLayout.CENTER);
		add(hintboxPanel,BorderLayout.SOUTH);

		repaintOK=true;
		superRepaintOK=true;
		initDone=true;
		setVisible(true);
		repaint();

		try{ Thread.sleep((int)(1000)); }
		catch(InterruptedException e) {}

		board.click(0,0);
		superRepaintOK=true;
	}

	public void mousePressed(MouseEvent e)
	{
		board.click(e.getX(),e.getY());
		superRepaintOK=true;
		repaint();
	}

	public void paintComponent(Graphics g)
	{
		if(repaintOK)
		{
			board.repaint();
			try{
				message.setText(board.getMessageStatus().read());
			}
			catch(NullPointerException e){
				System.out.println(e);
				message.setText(e.toString());
			}
		}
	}

	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==reset)
		{
			board.startLevel(levelNumber);
			hintboxLabel.setText(board.getLevelHint());
			superRepaintOK=true;
		}
		else if(e.getSource()==levelDown && levelNumber>1)
		{
			levelNumber--;
			superRepaintOK=true;
		}
		else if(e.getSource()==levelUp && levelNumber<MAX_LEVEL)
		{
			levelNumber++;
			superRepaintOK=true;
		}
		else if(e.getSource()==zoomIn)
		{
			board.zoomIn();
			superRepaintOK=true;
		}
		else if(e.getSource()==zoomOut)
		{
			board.zoomOut();
			superRepaintOK=true;
		}


		levelMsg.setText("Level: "+levelNumber);
		repaint();
	}
	public void summonRepaint()
	{
		repaint();
	}
	public static Game getInstance() { return singleton; }
}



