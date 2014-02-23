public class Laser
{
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;

    public Laser(int x, int y)
    {
	this.x = (double)x;
	this.y = (double)y;
	dx = 0;
	dy = -6;
    }

    public Laser (int x, int y, double dx, double dy)
    {
	this.x = (double)x;
	this.y = (double)y;
	this.dx = dx;
	this.dy = dy;
    }

    public Laser (double x, double y, double dx, double dy)
    {
	this.x = x;
	this.y = y;
	this.dx = dx;
	this.dy = dy;
    }

    public void move()
    {
        x += dx;
	y += dy;
    }
}
