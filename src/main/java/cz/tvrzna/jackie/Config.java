package cz.tvrzna.jackie;

import java.text.DateFormat;

/**
 * This class is carrier of all configuration applicable.
 *
 * @author michalt
 */
public class Config
{
	private DateFormat dateFormat;
	private boolean prettyPrint = false;

	/**
	 * Gets the date format.
	 *
	 * @return the date format
	 * @since 0.3.0
	 */
	public DateFormat getDateFormat()
	{
		return dateFormat;
	}

	/**
	 * Sets the date format.
	 *
	 * @param dateFormat
	 *          the new date format
	 * @since 0.3.0
	 */
	public void setDateFormat(DateFormat dateFormat)
	{
		this.dateFormat = dateFormat;
	}

	/**
	 * Checks if is pretty print.
	 *
	 * @return true, if is pretty print
	 * @since 0.3.0
	 */
	public boolean isPrettyPrint()
	{
		return prettyPrint;
	}

	/**
	 * Sets the pretty print.
	 *
	 * @param prettyPrint
	 *          the new pretty print
	 * @since 0.3.0
	 */
	public void setPrettyPrint(boolean prettyPrint)
	{
		this.prettyPrint = prettyPrint;
	}

}
