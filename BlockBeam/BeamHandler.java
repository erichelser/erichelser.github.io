import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;

class BeamHandler
{
	private ArrayList<Beam> beamList;
	private BeamTrail trail;
	public BeamHandler()
	{
		beamList=new ArrayList<Beam>(0);
		trail=new BeamTrail();
	}
	public int size()
	{
		return beamList.size();
	}
	public int getTrailSize()
	{
		return trail.size();
	}
	public BeamTrail getTrail()
	{
		return trail;
	}
	public Beam get(int i)
	{
		return beamList.get(i);
	}
	public void clear()
	{
		beamList.clear();
	}
	public void addBeam(double row, double col, double heading)
	{
		beamList.add(new Beam(row,col,heading));
	}
	public void advance()
	{
		for(int i=0; i<beamList.size(); i++)
		{
			trail.addPoint(beamList.get(i));
			beamList.get(i).move(1/16.0);
		}
		trail.decay();
	}
	public void validate(int rowCount, int colCount)
	{
		for(int i=0; i<beamList.size(); i++)
			if( ! beamList.get(i).isValid(rowCount,colCount) )
				beamList.remove(i--);
	}
	public void disable(int i)
	{
		beamList.get(i).setDisabled(true);
	}
	public void flushDisabled()
	{
		for(int i=0; i<beamList.size(); i++)
			if(beamList.get(i).getDisabled())
				beamList.remove(i--);
	}
}
