package cz.tvrzna.jackie;

/**
 * The Class ObjectWrapper.
 *
 * @author michalt
 */
public class ObjectWrapper<T>
{
	private final T value;
	private final Class<T> clazz;

	/**
	 * Instantiates a new object wrapper.
	 *
	 * @param value the value
	 * @param clazz the clazz
	 */
	@SuppressWarnings("unchecked")
	public ObjectWrapper(T value)
	{
		this.value = value;
		this.clazz = value != null ? (Class<T>) value.getClass() : null;
	}

	/**
	 * Gets the clazz.
	 *
	 * @return the clazz
	 */
	public Class<T> getClazz()
	{
		return clazz;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public T getValue()
	{
		return value;
	}
}
