package ui;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

public class TextBox implements Drawable {

    protected Sprite background;
    private boolean needUpdate = true;

    /** The text to display */
    protected String string;
    protected int charSize = 25;
    protected float interline;
    protected Font font;

    /** Text elements */
    List<Text> texts = new ArrayList<>();

    /** Transformable */
    protected Vector2f position = Vector2f.ZERO;
    protected Vector2f size;

    public TextBox() {
        this.setText("");
        this.setCharacterSize(25);
    }

    public TextBox(String text, Font font, int charSize, Vector2f boxSize) {
        this.setText(text);
        this.setFont(font);
        this.setCharacterSize(charSize);
        this.setSize(boxSize);
    }

    private void createBox() {
        if (!needUpdate)
            return;
        if (this.string == null || this.font == null || this.size == null || this.position==null) {
            return;
        }

        final String[] words = this.string.split(" ");
        final Text lineText = new Text("", getFont(), this.charSize);
        int lineCount = 0;
        for (int wordCount = 0; wordCount < words.length; wordCount++)
        {
            // Try to put the whole world in - check the width
            // if the width is too big, remove
            String oldText = lineText.getString();
            lineText.setString(oldText + " " + words[wordCount]);
            // float textWidth = lineText.getGlobalBounds().width;
            if (wordCount == words.length -1) {
                lineText.setPosition(position.x, position.y + lineCount*(charSize+interline));
                texts.add(lineText);
            }
            else if (lineText.getGlobalBounds().width > this.size.x) {
                // Remove the latest
                wordCount --;
                // System.out.println(oldText);
                Text valid = new Text(oldText, getFont(),this.charSize);
                valid.setPosition(position.x, position.y + lineCount*(charSize+interline));
                this.texts.add(valid);
                lineText.setString("");
                lineCount++;
            }
        }

        needUpdate = false;
    }

    public void setFont(Font font) {
        this.font = font;
        this.texts.forEach(t -> t.setFont(font));
        needUpdate = true;
        createBox();
    }

    public void setSize(Vector2f size) {
        this.size = size;
        needUpdate = true;
        createBox();
    }

    public Font getFont() {
        return font;
    }

    /**
     * Changes the color of the text
     * @param color
     */
    public void setColor(Color color) {
        this.texts.forEach(t -> t.setColor(color));
        needUpdate = true;
        createBox();
    }

    public void setText(String text) {
        this.string = text;
        needUpdate = true;
        createBox();
    }

    public void setCharacterSize(int size) {
        this.charSize = size;
        this.interline = size/2;
        this.texts.forEach(t -> t.setCharacterSize(size));
        needUpdate = true;
        createBox();
    }

    @Override
    public void draw(RenderTarget arg0, RenderStates arg1) {
        texts.forEach(t -> t.draw(arg0, arg1));
    }

    public Vector2f getPosition() {
        return position;
    }

    public float getRotation() {
        return this.texts.isEmpty() ? 0 : this.texts.get(0).getRotation();
    }

    public void move(Vector2f arg0) {
        texts.forEach(t -> t.move(arg0));
    }

    
    public final void move(float arg0, float arg1) {
        this.move(new Vector2f(arg0,arg1));

    }

    
    public void rotate(float arg0) {
        texts.forEach(t -> t.rotate(arg0));
    }

    public void scale(Vector2f arg0) {
        texts.forEach(t -> t.scale(arg0));;
    }

    public final void scale(float arg0, float arg1) {
        this.scale(new Vector2f(arg0, arg1));

    }

    public void setPosition(Vector2f arg0) {
        this.position = arg0;
        if (this.texts.isEmpty()) return;
        Vector2f displacement = Vector2f.sub(texts.get(0).getPosition(), position);
        texts.forEach(t -> t.setPosition(Vector2f.sub(t.getPosition(),displacement)));
    }

    public final void setPosition(float arg0, float arg1) {
        this.setPosition(new Vector2f(arg0,arg1));
    }

    public final FloatRect getGlobalBounds() {
        return new FloatRect(this.getPosition(), this.getSize());
    }

    public Vector2f getSize() {
        if (this.texts.isEmpty()) return Vector2f.ZERO;
        float height = this.texts.size() * (this.texts.get(0).getGlobalBounds().height + interline);
        return new Vector2f(this.size.x, height);
    }


}
