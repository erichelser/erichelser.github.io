import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;

class Grid
{
	private Block[][] grid;
	private int rowCount;
	private int colCount;
	private String levelHint;

	public Grid()
	{
		rowCount=0;
		colCount=0;
		resizeGrid(0,0);
		levelHint="";
	}

	public void resizeGrid(int r, int c)
	{
		rowCount=r;
		colCount=c;
		grid=new Block[rowCount][colCount];
		emptyGrid();
	}

	public int getRowCount() { return rowCount; }
	public int getColCount() { return colCount; }
	public String getLevelHint() { return levelHint; }

	private void emptyGrid()
	{
		for(int i=0; i<rowCount; i++)
		for(int j=0; j<colCount; j++)
		{
			grid[i][j]=new Block();
			grid[i][j].setBlockType(Block.BLOCK_EMPTY);
		}
	}

	public Block getBlock(int i, int j)
	{
		return grid[i][j];
	}

	public void initLevelString(int r, int c, String s, String hint)
	{
		levelHint=hint;
		resizeGrid(r,c);
		for(int i=0; i<r; i++)
		for(int j=0; j<c; j++)
			grid[i][j].setBlockType(Block.interpretSymbol(s.charAt(i*c+j)));
	}

	public void initLevel(int level)
	{
		if(level==1) initLevelString(7,5,"  Z  "
		                                +" ZZ  "
		                                +"  Z  "
		                                +"  Z  "
		                                +"  Z  "
		                                +"  Z  "
		                                +"ZZZZZ"
		                               ,"The object of the game is to capture all the gems on the board. You may capture only one gem at a time by clicking it. Gems "+
		                                "will emit a beam when captured, allowing you to capture other gems. These beams travel in a straight line until they "+
		                                "go outside the grid or strike another object. Square gems emit beams up, down, left, and right.");
		else if(level==2) initLevelString(7,5," XXX "
		                                     +"X   X"
		                                     +"   X "
		                                     +"  X  "
		                                     +" X   "
		                                     +"X    "
		                                     +"XXXXX"
		                                    ,"Diamond gems emit beams along diagonal directions when captured.");
		else if(level==3) initLevelString(7,5,"CCCCC"
		                                     +"   C "
		                                     +"  C  "
		                                     +"   C "
		                                     +"    C"
		                                     +"C   C"
		                                     +" CCC "
		                                    ,"Octagon gems emit beams in all eight directions when captured.");
		else if(level==4) initLevelString(7,5,"   C "
		                                     +"  XZ "
		                                     +" C X "
		                                     +"Z  C "
		                                     +"XZCXZ"
		                                     +"   C "
		                                     +"   X "
		                                    ,"Pay attention to gems' shapes to determine how they react.");
		else if(level==5) initLevelString(7,7,"V....ZV"
		                                     +".....VZ"
		                                     +"V....ZV"
		                                     +".....VZ"
		                                     +"V....ZV"
		                                     +".....VZ"
		                                     +"V....ZV"
		                                    ,"Green gems can only be captured with a beam emitted from another gem.");
		else if(level==6) initLevelString(7,7,"OOOX..."
		                                     +"OC....V"
		                                     +"O.O..OO"
		                                     +"VO...VV"
		                                     +"V.OO. C"
		                                     +"O.O..VZ"
		                                     +"OOX...O"
		                                    ,"Walls block beams from passing through them.");
		else if(level==7) initLevelString(7,7,"VV2.VOC"
		                                     +".6.8.6."
		                                     +"..4.V.4"
		                                     +".6..387"
		                                     +"..4.9.1"
		                                     +"..3..V."
		                                     +"C79...7"
		                                    ,"Mirrors can change a beam's direction.");
		else if(level==8) initLevelString(5,5,"#.#.#"
		                                     +"ZVVZ."
		                                     +"..ZV#"
		                                     +".X.V."
		                                     +"C..Z#"
		                                    ,"Red, spiky gems are hazard gems, and must not be captured. If a beam strikes and captures a hazard "
		                                    +"gem, the game is over and you will have to restart the level.");
		else if(level==9) initLevelString(5,5,"ZZZZZ"
		                                     +"#Z#Z#"
		                                     +"ZZZ#Z"
		                                     +"ZZ.ZZ"
		                                     +"##ZZZ"
		                                    ,"Be careful to the order in which you capture gems. You may accidentally capture a red hazard gem.");
		else if(level==10) initLevelString(7,7,"3.VVX.1"
		                                      +"....#.."
		                                      +"VC789#V"
		                                      +"V.4V.V7"
		                                      +"V.123.1"
		                                      +"V..#VZ."
		                                      +"9V..7VC"
		                                     ,"Let's put it all together now!");



	}

	public void handleClick(int rowClicked, int colClicked, BeamHandler drawnBeams)
	{
		if(grid[rowClicked][colClicked].getBlockType()==Block.BLOCK_NORMAL
		|| grid[rowClicked][colClicked].getBlockType()==Block.BLOCK_NORMAL_CHAIN)
		{
			grid[rowClicked][colClicked].setBlockType(Block.BLOCK_EMPTY);
			for(int i=0; i<4; i++)
				drawnBeams.addBeam(rowClicked,colClicked,i*90);
		}
		else if(grid[rowClicked][colClicked].getBlockType()==Block.BLOCK_DIAGONAL)
		{
			grid[rowClicked][colClicked].setBlockType(Block.BLOCK_EMPTY);
			for(int i=0; i<4; i++)
				drawnBeams.addBeam(rowClicked,colClicked,i*90+45);
		}
		else if(grid[rowClicked][colClicked].getBlockType()==Block.BLOCK_ALLWAY)
		{
			grid[rowClicked][colClicked].setBlockType(Block.BLOCK_EMPTY);
			for(int i=0; i<8; i++)
				drawnBeams.addBeam(rowClicked,colClicked,i*45);
		}
	}

	public void collisionCheck(BeamHandler drawnBeams, Message status)
	{
		final double TOLERANCE=0.0001;
		for(int i=0; i<drawnBeams.size(); i++)
		{
		for(int j=0; j<grid.length; j++)
		for(int k=0; k<grid[j].length; k++)
			if(Math.abs(drawnBeams.get(i).getRow()-j)<TOLERANCE && Math.abs(drawnBeams.get(i).getCol()-k)<TOLERANCE)
			{
				if(grid[j][k].getBlockType()==Block.BLOCK_NORMAL ||
				   grid[j][k].getBlockType()==Block.BLOCK_DIAGONAL ||
				   grid[j][k].getBlockType()==Block.BLOCK_ALLWAY ||
				   grid[j][k].getBlockType()==Block.BLOCK_GREEN )
				{
					grid[j][k].setBlockType(Block.BLOCK_EMPTY);
					drawnBeams.disable(i);
				}
				else if(grid[j][k].getBlockType()==Block.BLOCK_WALL)
					drawnBeams.disable(i);
				else if(grid[j][k].getBlockType()==Block.BLOCK_MIRROR_N)
				{
					if(drawnBeams.get(i).headingIsBetween(-180,0))
						drawnBeams.get(i).reflectAroundAngle(0);
					else
						drawnBeams.disable(i);
				}
				else if(grid[j][k].getBlockType()==Block.BLOCK_MIRROR_S)
				{
					if(drawnBeams.get(i).headingIsBetween(0,180))
						drawnBeams.get(i).reflectAroundAngle(0);
					else
						drawnBeams.disable(i);
				}
				else if(grid[j][k].getBlockType()==Block.BLOCK_MIRROR_W)
				{
					if(drawnBeams.get(i).headingIsBetween(-90,90))
						drawnBeams.get(i).reflectAroundAngle(90);
					else
						drawnBeams.disable(i);
				}
				else if(grid[j][k].getBlockType()==Block.BLOCK_MIRROR_E)
				{
					if(drawnBeams.get(i).headingIsNotBetween(-90,90))
						drawnBeams.get(i).reflectAroundAngle(90);
					else
						drawnBeams.disable(i);
				}

				else if(grid[j][k].getBlockType()==Block.BLOCK_MIRROR_NE)
				{
					if(drawnBeams.get(i).headingIsNotBetween(-45,135))
						drawnBeams.get(i).reflectAroundAngle(45);
					else
						drawnBeams.disable(i);
				}
				else if(grid[j][k].getBlockType()==Block.BLOCK_MIRROR_NW)
				{
					if(drawnBeams.get(i).headingIsBetween(-135,45))
						drawnBeams.get(i).reflectAroundAngle(135);
					else
						drawnBeams.disable(i);
				}
				else if(grid[j][k].getBlockType()==Block.BLOCK_MIRROR_SE)
				{
					if(drawnBeams.get(i).headingIsNotBetween(-135,45))
						drawnBeams.get(i).reflectAroundAngle(-45);
					else
						drawnBeams.disable(i);
				}
				else if(grid[j][k].getBlockType()==Block.BLOCK_MIRROR_SW)
				{
					if(drawnBeams.get(i).headingIsBetween(-45,135))
						drawnBeams.get(i).reflectAroundAngle(-135);
					else
						drawnBeams.disable(i);
				}
				else if(grid[j][k].getBlockType()==Block.BLOCK_HAZARD)
				{
					grid[j][k].setBlockType(Block.BLOCK_HAZARD_X);
					status.write("FAILED");
					drawnBeams.disable(i);
				}
				else if(grid[j][k].getBlockType()==Block.BLOCK_NORMAL_CHAIN)
				{
					grid[j][k].setBlockType(Block.BLOCK_EMPTY);
					for(int m=0; m<4; m++)
						drawnBeams.addBeam(j,k,m*90);
					drawnBeams.disable(i);
				}

			}
		}
		drawnBeams.flushDisabled();
	}
}
