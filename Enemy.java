public class Enemy
{
    protected int x;
    protected int y;
    protected int dx;

    protected long previousShotTime;


    public Enemy()
    {
	x = 0;
	y = 70;
	previousShotTime = -(long)(Math.random() * 1000);
	dx = 2;
    }

    public Enemy(int dx)
    {
	if (dx < 0)
	    {	
		x = 600;
	    }
	else {x = 0;}
	y = 70;
	previousShotTime = -(long)(Math.random() * 1000);
	this.dx = dx;
    }

    public void move()
    {
	x += dx;
    }
}
