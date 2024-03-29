package cmpt276.proj.finddamatch.model.gameLogic;

import java.util.Stack;

import cmpt276.proj.finddamatch.BuildConfig;
import cmpt276.proj.finddamatch.model.Card;
import cmpt276.proj.finddamatch.model.DeckGenerator;
import cmpt276.proj.finddamatch.model.Game;
import cmpt276.proj.finddamatch.model.Image;

/**
 * Game logic to implement the Game interface
 */
public class GameImpl implements Game {
    private long referenceTime;
    private long elapsedTime;
    private DeckGenerator dealer;
    private Stack<Card> drawPile;
    private Stack<Card> discardPile;
    private boolean isPaused;

    /**
     * GameImpl object that holds necessary stacks and variables
     *
     * @param dealer
     * @param time
     */
    public GameImpl(DeckGenerator dealer, long time) {
        this.referenceTime = time;
        this.dealer = dealer;
        this.drawPile = dealer.generate();
        this.discardPile = new Stack<>();
        this.elapsedTime = 0;
        this.isPaused = false;
        draw();
    }

    /**
     * check method will verify if input image exists in card on top of drawPile stack
     *
     * @param image
     * @return
     */
    @Override
    public boolean check(Image image) {
        return drawPile.peek().exists(image) && discardPile.peek().exists(image);
    }

    /**
     * draws card on top of drawPile stack and pops same card pushes to discardPile
     *
     * @return
     */
    @Override
    public Card draw() {
        if (BuildConfig.DEBUG && drawPile.isEmpty()) {
            throw new AssertionError("Empty Stack");
        }
        Card card = drawPile.pop();
        discardPile.push(card);
        return card;
    }

    @Override
    public void update(Image image) {
    }

    @Override
    public boolean isPaused() {
        return this.isPaused;
    }

    /**
     * resets to game conditions of a brand new game
     *
     * @param time
     */
    @Override
    public void reset(long time) {
        discardPile.clear();
        drawPile = dealer.generate();
        referenceTime = time;
        elapsedTime = 0;
        isPaused = false;
        draw();
    }

    /**
     * indexes class time when pause command is called, used to track paused time,
     * in order to calculate total game time for final score.
     *
     * @param time
     */
    @Override
    public void pause(long time) {
        if (isPaused) {
            return;
        }
        updateTime(time);
        isPaused = true;
    }

    /**
     * resumes game state from pause, indexes time at resume, to be used to
     * calculate total game time for final score.
     *
     * @param time
     */
    @Override
    public void resume(long time) {
        if (!isPaused) {
            return;
        }
        referenceTime = time;
        isPaused = false;
    }

    /**
     * returns the top card in the discardPile without popping.
     *
     * @return
     */
    @Override
    public Card peekDiscard() {
        if (BuildConfig.DEBUG && discardPile.empty()) {
            throw new AssertionError("Stack Empty");
        }
        return discardPile.peek();
    }

    /**
     * returns the top card from the drawPile without popping.
     *
     * @return
     */
    @Override
    public Card peekDraw() {
        if (BuildConfig.DEBUG && drawPile.empty()) {
            throw new AssertionError("Stack Empty");
        }
        return drawPile.peek();
    }

    /**
     * checks if all cards in drawPile stack have been popped, then returns boolean value.
     *
     * @return
     */
    @Override
    public boolean isGameDone() {
        return drawPile.empty();
    }

    /**
     * computes current total elapsed game time and returns.
     *
     * @param time
     * @return
     */
    @Override
    public long queryTime(long time) {
        if (!isPaused) {
            updateTime(time);
        }
        return elapsedTime;
    }

    /**
     * update time function is a time tracking function, used to compute total elapsed time
     * for a given event in game logic, then adds the elapsed event time to game elapsed time.
     *
     * @param time
     */
    private void updateTime(long time) {
        long previousTime = referenceTime;
        referenceTime = time;
        elapsedTime += referenceTime - previousTime;
    }

    /**
     * returns the game's deck generator, so that GameActivity has access to DeckGenerator, useful
     * for when exporting cards.
     * @return dealer
     */
    public DeckGenerator getDeckGenerator(){
        return this.dealer;
    }
}