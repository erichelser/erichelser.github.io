import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;

class BeamTrail
{
	private ArrayList<TrailPoint> trailList;
	public BeamTrail()
	{
		trailList=new ArrayList<TrailPoint>(0);
	}
	public void addPoint(Beam b)
	{
		trailList.add(new TrailPoint(b));
	}
	public void decay()
	{
		for(int i=0; i<trailList.size(); i++)
			trailList.get(i).decay();
		for(int i=0; i<trailList.size(); i++)
			if(trailList.get(i).getSize()<=0)
				trailList.remove(i--);
	}
	public int size()
	{
		return trailList.size();
	}
	public TrailPoint get(int i)
	{
		return trailList.get(i);
	}
}

