import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;

class Board extends JPanel implements ImageObserver
{
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {return true;} // override abstract method

	private static int marginX = 22;
	private static int marginY = 22;
	private static int tileSize= 25;
	private static int lineSize=  1;
	public static int getTileSize() { return tileSize; }

	private Message status=new Message();

	private Grid grid;
	private BeamHandler drawnBeams;
	public Board()
	{
		grid=new Grid();

		drawnBeams=new BeamHandler();
		status.write("NOT READY");
	}

	public void startLevel(int level)
	{
		grid.initLevel(level);
		drawnBeams.clear();
		status.write("READY");
	}

	public String getLevelHint()
	{
		return grid.getLevelHint();
	}

	public void zoomIn()
	{
		tileSize++;
	}

	public void zoomOut()
	{
		if(tileSize>10)
			tileSize--;
	}

	public void advanceBeam()
	{
		drawnBeams.advance();
		drawnBeams.validate(grid.getRowCount(),grid.getColCount());
	}

	public int getBoardSizeX() { return 2*marginX + (tileSize+lineSize)*grid.getRowCount() + lineSize; }
	public int getBoardSizeY() { return 2*marginY + (tileSize+lineSize)*grid.getColCount() + lineSize; }

	//This can probably get moved out to some view-type class.
	public void paintComponent(Graphics g)
	{
		int panelWidth=this.getBounds().width;
		int panelHeight=this.getBounds().height;
		marginX=(panelWidth - (grid.getColCount()*(tileSize+lineSize)+lineSize))/2;
		marginY=(panelHeight- (grid.getRowCount()*(tileSize+lineSize)+lineSize))/2;
		g.setColor(new Color(222,222,222));
		g.fillRect(0,0,9999,9999); //fill white
		g.setColor(Color.BLACK);

		for(int i=0; i<=grid.getRowCount(); i++)
			g.fillRect(marginX,marginY+(tileSize+lineSize)*i, (tileSize+lineSize)*grid.getColCount()+1,lineSize);
		for(int i=0; i<=grid.getColCount(); i++)
			g.fillRect(marginX+(tileSize+lineSize)*i,marginY, lineSize, (tileSize+lineSize)*grid.getRowCount()+1);

		for(int i=0; i<grid.getRowCount(); i++)
		for(int j=0; j<grid.getColCount(); j++)
			g.drawImage(grid.getBlock(i,j).getImage(),marginX+1+j*(tileSize+lineSize),marginY+1+i*(tileSize+lineSize),this);

		//Draw beams here...
		g.setColor(Color.BLUE);
		int radius=tileSize/6;

		//trails first
		for(int i=0; i<drawnBeams.getTrailSize(); i++)
			g.fillOval((int)((drawnBeams.getTrail().get(i).getCol()+0.5)*(tileSize+lineSize)+marginX - (radius*drawnBeams.getTrail().get(i).getSize())),
			           (int)((drawnBeams.getTrail().get(i).getRow()+0.5)*(tileSize+lineSize)+marginY - (radius*drawnBeams.getTrail().get(i).getSize())),
			           (int)((radius*2+1)*drawnBeams.getTrail().get(i).getSize()),
			           (int)((radius*2+1)*drawnBeams.getTrail().get(i).getSize()) );

		//now beam end points
		for(int i=0; i<drawnBeams.size(); i++)
			g.fillOval((int)((drawnBeams.get(i).getCol()+0.5)*(tileSize+lineSize)+marginX -radius),
			           (int)((drawnBeams.get(i).getRow()+0.5)*(tileSize+lineSize)+marginY -radius), radius*2+1, radius*2+1);


		//Check if game is complete, this can probably get moved down into Grid class.
		if(status.read().equals("READY") && drawnBeams.size()==0)
		{
			boolean cleared=true;
			for(int i=0; cleared && i<grid.getRowCount(); i++)
			for(int j=0; cleared && j<grid.getColCount(); j++)
				cleared = cleared && ! (grid.getBlock(i,j).getBlockType().mustBeCaptured());
			if(cleared)
				status.write("CLEARED");
		}
	}

	public void click(int x, int y)
	{
		int RowClicked=-1;
		int ColClicked=-1;
		if(x>=marginX && y>=marginY)
		{
			x=(x-marginX)/(tileSize+lineSize);
			y=(y-marginY)/(tileSize+lineSize);
			if(0<=x && x<grid.getColCount() && 0<=y && y<grid.getRowCount())
			{
				RowClicked=y;
				ColClicked=x;
			}
		}
		if(RowClicked<0 || ColClicked<0)
			return; //kick out bad clicks

		if(!status.read().equals("FAILED"))
			grid.handleClick(RowClicked,ColClicked,drawnBeams);
	}

	public void collisionCheck()
	{
		grid.collisionCheck(drawnBeams,status);
	}

	public Message getMessageStatus()
	{
		return status;
	}
}
