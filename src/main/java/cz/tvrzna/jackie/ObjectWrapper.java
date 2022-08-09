package cz.tvrzna.jackie;

/**
 * The Class ObjectWrapper.
 *
 * @author michalt
 */
public class ObjectWrapper
{
	private final Object value;
	private final Class<?> clazz;

	/**
	 * Instantiates a new object wrapper.
	 *
	 * @param value the value
	 * @param clazz the clazz
	 */
	public ObjectWrapper(Object value, Class<?> clazz)
	{
		this.value = value;
		this.clazz = clazz;
	}

	/**
	 * Gets the clazz.
	 *
	 * @return the clazz
	 */
	public Class<?> getClazz()
	{
		return clazz;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Object getValue()
	{
		return value;
	}
}
