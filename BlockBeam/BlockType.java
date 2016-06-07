import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;

class BlockType
{
	private static int uniqueValue=1;

	private int BLOCK_VALUE;
	private Color BLOCK_COLOR;
	private BufferedImage BLOCK_IMAGE;
	private String imageLocation;
	private int mySize;
	private boolean mustBeCaptured;

	private BlockType()
	{
		BLOCK_VALUE=-1;
		BLOCK_COLOR=Color.RED;
		BLOCK_IMAGE=null;
		imageLocation="_NOTAFILE_";
		mySize=0;
		mustBeCaptured=false;
	}

	public BlockType(Color color)
	{
		this();
		BLOCK_VALUE=uniqueValue++;
		BLOCK_COLOR=color;
		setupBlockImage();
	}

	public BlockType(String file)
	{
		this();
		BLOCK_VALUE=uniqueValue++;
		imageLocation=file;
		setupBlockImage();
	}

	public BlockType(String file, boolean m)
	{
		this(file);
		mustBeCaptured=m;
	}

	private void setupBlockImage()
	{
		boolean fileOK=false;
		if(imageLocation.length()>0) // non-empty file string
		{
			try{
				BufferedImage input=ImageIO.read(new File(imageLocation));
				BLOCK_IMAGE=new BufferedImage(Board.getTileSize(),Board.getTileSize(),BufferedImage.TYPE_INT_RGB);
				Graphics2D g=BLOCK_IMAGE.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				g.clearRect(0,0,Board.getTileSize(),Board.getTileSize());
				g.drawImage(input,0,0,Board.getTileSize(),Board.getTileSize(),null);
				g.dispose();
				fileOK=true;
			}
			catch(IOException e){
				fileOK=false;
			}
		}
		if(!fileOK)
		{
			BLOCK_IMAGE=(BufferedImage)(Game.getInstance().createImage(Board.getTileSize(), Board.getTileSize()));
			Graphics g=BLOCK_IMAGE.getGraphics();
			g.setColor(BLOCK_COLOR);
			g.fillRect(0,0,Board.getTileSize(),Board.getTileSize());
		}
		mySize=Board.getTileSize();
	}
	public int getValue() { return BLOCK_VALUE; }
	public Image getImage()
	{
		if(mySize!=Board.getTileSize())
			setupBlockImage();
		return BLOCK_IMAGE;
	}
	public boolean mustBeCaptured() { return mustBeCaptured; }
}