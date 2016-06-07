class TrailPoint
{
	private double row;
	private double col;
	private double size;
	public TrailPoint(Beam b)
	{
		row=b.getRow();
		col=b.getCol();
		size=1;
	}
	public void decay()
	{
		size-=0.02;
	}
	public double getSize()
	{
		return size;
	}
	public double getRow()
	{
		return row;
	}
	public double getCol()
	{
		return col;
	}
}