package metier;

public record Point(int x,int y)
{
	@Override
	public final boolean equals(Object obj)
	{
		Point p = (Point) obj;
		return p.x == this.x && p.y == this.y;
	}
	
	public String toString()
	{
		return "x=" + this.x + " y=" + this.y; 
	}
}