import java.awt.Color;

/**
 *  A class that will be the data for a node, and which holds location
 *  and color information as well as the actual node data.
 * 
 * @param <T>  the type of the node data
 */
public class PlacedData<T> {
  /** the node data */
  private T data;

  /** location x */
  private int x;

  /** location y */
  private int y;

  /** node rendering color */
  private Color color;

  public PlacedData(T data, int x, int y) {
    this.data = data;
    this.x = x;
    this.y = y;
    this.color = Color.cyan;
  }

  public int getX() { return x; }
  public int getY() { return y; }
  public Color getColor() { return color; }
  public void setX(int x) { this.x = x; }
  public void setY(int y) { this.y = y; }
  public void setColor(Color color) { this.color = color; }
  public T getData() { return this.data; }

  public String toString() { return data.toString() + "@(" + x + "," + y + ")"; }
}
