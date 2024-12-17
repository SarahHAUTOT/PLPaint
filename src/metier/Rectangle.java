package metier;

public class Rectangle
{
	private int x;
	private int y;
	private int xEnd;
	private int yEnd;

	public Rectangle(int x, int y, int xEnd, int yEnd)
	{
		this.x = Math.min(x, xEnd);
		this.y = Math.min(y, yEnd);
		this.xEnd = Math.max(x, xEnd);
		this.yEnd = Math.max(y, yEnd);
	}

	public int x() { return x; }

	public int y() { return y; }
	
	public int yEnd() { return yEnd; }

	public int xEnd() { return xEnd; }
}
