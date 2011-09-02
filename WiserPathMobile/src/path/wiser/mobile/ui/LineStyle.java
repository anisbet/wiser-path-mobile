/**
 * 
 */
package path.wiser.mobile.ui;

import android.graphics.Paint;
import android.graphics.Paint.Style;

/**
 * This is the line style for this application.
 * 
 * @author andrew
 * 
 */
public class LineStyle
{

	private static final float	DEFAULT_TEXT_SIZE	= 14;
	private int					alpha;
	private int					red;
	private int					green;
	private int					blue;

	private int					strokeWidth;

	public LineStyle()
	{
		this.alpha = 255;
		this.red = 80;
		this.green = 150;
		this.blue = 30;
		this.strokeWidth = 1;
	}

	public LineStyle( int alpha, int red, int green, int blue, int strokeWidth )
	{
		this.alpha = alpha;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.strokeWidth = strokeWidth;
	}

	/**
	 * @return the strokeWidth
	 */
	public int getStrokeWidth()
	{
		return strokeWidth;
	}

	/**
	 * @param strokeWidth the strokeWidth to set
	 */
	public void setStrokeWidth( int strokeWidth )
	{
		this.strokeWidth = strokeWidth;
	}

	/**
	 * @return the alpha
	 */
	public int getAlpha()
	{
		return alpha;
	}

	/**
	 * @param alpha the alpha to set
	 */
	public void setAlpha( int alpha )
	{
		this.alpha = alpha;
	}

	/**
	 * @return the red
	 */
	public int getRed()
	{
		return red;
	}

	/**
	 * @param red the red to set
	 */
	public void setRed( int red )
	{
		this.red = red;
	}

	/**
	 * @return the green
	 */
	public int getGreen()
	{
		return green;
	}

	/**
	 * @param green the green to set
	 */
	public void setGreen( int green )
	{
		this.green = green;
	}

	/**
	 * @return the blue
	 */
	public int getBlue()
	{
		return blue;
	}

	/**
	 * @param blue the blue to set
	 */
	public void setBlue( int blue )
	{
		this.blue = blue;
	}

	/**
	 * Sets the paint object's attributes abstracting away debris from the calling function.
	 * 
	 * @param paint
	 */
	public void setPaintAttributes( Paint paint )
	{
		paint.setARGB( alpha, red, green, blue );
		paint.setTextSize( DEFAULT_TEXT_SIZE );
		paint.setStyle( Style.STROKE );
		paint.setStrokeWidth( strokeWidth );
	}

}
