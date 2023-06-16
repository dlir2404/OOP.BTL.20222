import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Ball extends Rectangle {
    Random random;
    int xVelocity;
    int yVelocity;
    Ball(int x, int y, int width, int height) {
        super(x,y,width,height);
        random = new Random();
        int randomXDirection = random.nextInt(2);
        if(randomXDirection == 0) randomXDirection--;
        setXDirection(randomXDirection);

        int randomYDirection = random.nextInt(2);
        if(randomYDirection == 0) randomYDirection--;
        setYDirection(randomYDirection);
    }

    public void setXDirection(int randomXDIrection) {
        xVelocity = randomXDIrection;
    }

    public void setYDirection(int randomYDIrection) {
        yVelocity = randomYDIrection;
    }
    
    public void move() {

    }
}
