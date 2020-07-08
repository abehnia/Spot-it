package cmpt276.proj.finddamatch.gameLogic;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import cmpt276.proj.finddamatch.model.Card;
import cmpt276.proj.finddamatch.model.Image;
import cmpt276.proj.finddamatch.model.ImageMockImpl;

import static java.lang.Math.sqrt;

public class  CardImpl implements Card {
    private ArrayList<Image> images;

    public static final ArrayList<Card> PRE_GENERATED_CARDS = new ArrayList<Card>();

    public static final CardImpl CARD1 = new CardImpl(
            new ImageImpl(0),
            new ImageImpl( 1),
            new ImageImpl(2));

    public static final CardImpl CARD2 = new CardImpl(
            new ImageImpl( 2),
            new ImageImpl( 3),
            new ImageImpl(4));

    public static final CardImpl CARD3 = new CardImpl(
            new ImageImpl( 0),
            new ImageImpl( 4),
            new ImageImpl( 5));

    public static final CardImpl CARD4 = new CardImpl(
            new ImageImpl( 0),
            new ImageImpl( 3),
            new ImageImpl( 6));

    public static final CardImpl CARD5 = new CardImpl(
            new ImageImpl( 1),
            new ImageImpl( 4),
            new ImageImpl( 6));

    public static final CardImpl CARD6 = new CardImpl(
            new ImageImpl(1),
            new ImageImpl( 3),
            new ImageImpl( 5));

    public static final CardImpl CARD7 = new CardImpl(
            new ImageImpl(2),
            new ImageImpl(5),
            new ImageImpl(6));

    static {
        PRE_GENERATED_CARDS.add(CARD1);
        PRE_GENERATED_CARDS.add(CARD2);
        PRE_GENERATED_CARDS.add(CARD3);
        PRE_GENERATED_CARDS.add(CARD4);
        PRE_GENERATED_CARDS.add(CARD5);
        PRE_GENERATED_CARDS.add(CARD6);
        PRE_GENERATED_CARDS.add(CARD7);
    }

    public CardImpl(Image... images) {
        this.images = new ArrayList<>();
        this.images.addAll(Arrays.asList(images));

    }

    @Override
    public Image get(int index) {
        return images.get(index);
    }

    @Override
    public boolean exists(Image image) {
        for (Image savedImage : images) {
            if (savedImage.getID() == image.getID()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return images.size();
    }

    public boolean isBounded() {
        for (int i = 0; i < this.size(); ++i) {
            double distanceBetweenCircles = sqrt(this.get(i).getX() * this.get(i).getX()
                    + this.get(i).getY() * this.get(i).getY());
            double imageRadius = this.get(i).getRadius();
            if (distanceBetweenCircles + imageRadius > 1f) return false;
        }
        return true;
    }

    public boolean isNotOverlap() {
        for (int i = 0; i < this.size() - 1; ++i) {
            for (int j = i + 1; j < this.size(); ++j) {
                double sumOf2Radius = this.get(i).getRadius()
                        + this.get(j).getRadius();
                double distanceBetweenCircles = sqrt((this.get(i).getX() - this.get(j).getX())
                        * (this.get(i).getX() - this.get(j).getX()))
                        + (this.get(i).getY() - this.get(j).getY())
                        * (this.get(i).getY() - this.get(j).getY());
                if (sumOf2Radius > distanceBetweenCircles) return false;
            }
        }
        return true;
    }

    public void randomizeEverything() {
        Random random = new Random();
        float leftBoundedPosition = -1F;
        float rightBoundedPosition = 1F;
        float leftBoundedRadius = 0.2F;
        float rightBoundedRadius = 1.0F;
        for (int i = 0; i < this.size(); ++i) {
            this.get(i).setX(random.nextFloat() * (rightBoundedPosition - leftBoundedPosition) + leftBoundedPosition);
            this.get(i).setY(random.nextFloat() * (rightBoundedPosition - leftBoundedPosition) + leftBoundedPosition);
            this.get(i).setOrientation(random.nextFloat());
            //TODO Fix this line of code to match your logic, maybe setRadius()
            this.get(i).setRadius(random.nextFloat() * (rightBoundedRadius - leftBoundedRadius) + leftBoundedRadius);
        }
    }

    @Override
    public void randomize() {
        while (!this.isBounded() && !this.isNotOverlap()) this.randomizeEverything();
    }

    @NonNull
    @Override
    public Iterator<Image> iterator() {
        return images.iterator();
    }
}
