import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;

class Beam
{
	private boolean disabled;
	private double heading;
	private double row;
	private double col;
	public Beam(double x, double y, double a)
	{
		disabled=false;
		row=x;
		col=y;
		heading=a;
		simplifyAngle();
	}
	public void move(double amount)
	{
		double row_d = Math.sin(heading/180.0*3.1415926);
		double col_d = Math.cos(heading/180.0*3.1415926);
		row_d = amount * row_d / Math.max(Math.abs(row_d),Math.abs(col_d));
		col_d = amount * col_d / Math.max(Math.abs(row_d),Math.abs(col_d));
		row-=row_d;
		col+=col_d;
	}
	public boolean isValid(int rowCount, int colCount)
	{
		return ! (row<=-1 || rowCount<=row || col<=-1 || colCount<=col);
	}
	public void setDisabled(boolean b)
	{
		disabled=b;
	}
	public boolean getDisabled()
	{
		return disabled;
	}
	public double getHeading()
	{
		return heading;
	}
	public double getCol()
	{
		return col;
	}
	public double getRow()
	{
		return row;
	}
	private void simplifyAngle()
	{
		while(heading< -180) heading+=360;
		while(heading>  180) heading-=360;		
	}
	public boolean headingIsBetween(double A, double B)
	{
		//assume that A<B.
		//Heading will be simplified to be within the range [-180, +180].
		//The logical OR is to include cases for when you need to check for a heading of +/- 180 degrees.

		simplifyAngle();
		return (A<=heading && heading<=B) ||
			((A==180 || B==180 || A==-180 || B==-180) && (heading==180 || heading==-180));
	}
	public boolean headingIsNotBetween(double A, double B)
	{
		simplifyAngle();
		return (heading<=A || B<=heading) ||
			((A==180 || B==180 || A==-180 || B==-180) && (heading==180 || heading==-180));
	}
	public void reflectAroundAngle(double R)
	{
		simplifyAngle();
		heading=-heading-2*R;
		simplifyAngle();
	}
}




