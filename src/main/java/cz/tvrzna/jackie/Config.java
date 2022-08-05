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
}
