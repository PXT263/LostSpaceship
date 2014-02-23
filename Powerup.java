import java.util.Random;

public class Powerup
{
    protected int x;
    protected int y;

    public Powerup()
    {
	Random rand = new Random();
	int g = rand.nextInt(300);
	x = g + 150;
	y = 0;
    }

    public void move()
    {
	y += 1;
    }
}
