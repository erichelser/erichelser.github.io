class Periodic implements Runnable
{
	private boolean running;
	private double frequency;
	private Board board;
	private Game parent;
	public Periodic(double freq, Board b, Game g)
	{
		frequency=freq;
		board=b;
		parent=g;
		running=true;
		(new Thread(this)).start();
	}
	public void run()
	{
		while(running)
		{
			try{ Thread.sleep((int)(1000/frequency)); }
			catch(InterruptedException e) {}
			board.advanceBeam();
			board.collisionCheck();
			parent.summonRepaint();
		}
	}
	public void stop()
	{
		running=false;
	}
}