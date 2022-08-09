package cz.tvrzna.jackie;

import java.text.DateFormat;
import java.util.List;
import java.util.Map;

/**
 * Main <code>Jackie</code> class, that provides converting of objects to JSON
 * as <code>String</code> and JSON <code>String</code> to objects.
 *
 * @author michalt
 */
public class Jackie
{

	private Config config = new Config();

	/**
	 * Converts single object into JSON as <code>String</code>.
	 *
	 * @param <T>
	 *          the generic type
	 * @param object
	 *          the object
	 * @return the string
	 */
	public <T> String toJson(T object)
	{
		try
		{
			Object o = SerializationMapper.convertFromObject(object, config);
			return Serializator.serialize(o, config, 0);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts single object or array from JSON to instance of
	 * <code>clazz</code>. <br>
	 * If <code>clazz</code> is going to be a <code>List</code>, @see
	 * Jackie#fromJson(String, Class, Class) to define the Type of list. <br>
	 * If <code>clazz</code> is going to be <code>Map</code>, @see
	 * Jackie#fromJson(String, Class, Class, Class) to define key and value
	 * classes, if you want to avoid to be used <code>String</code> and
	 * <code>Object</code>.
	 *
	 * @param <T>
	 *          the generic type
	 * @param json
	 *          the json
	 * @param clazz
	 *          the clazz
	 * @return the t
	 */
	public <T> T fromJson(String json, Class<T> clazz)
	{
		return fromJson(json, clazz, null, null);
	}

	/**
	 * Converts List or array from JSON. If it converts list,
	 * <code>subClazz</code> needs to be defined as generic type of List.
	 *
	 * @param <T>
	 *          the generic type
	 * @param json
	 *          the json
	 * @param clazz
	 *          the clazz
	 * @param subClazz
	 *          the sub clazz
	 * @return the t
	 */
	public <T> T fromJson(String json, Class<T> clazz, Class<?> subClazz)
	{
		return fromJson(json, clazz, subClazz, null);
	}

	/**
	 * Converts Map from JSON. It needs to have defined <code>keyClazz</code> as
	 * class of Key attribute and <code>valueClazz</code> as class of Value
	 * attribute.
	 *
	 * @param <T>
	 *          the generic type
	 * @param json
	 *          the json
	 * @param clazz
	 *          the clazz
	 * @param keyClazz
	 *          the key clazz
	 * @param valueClazz
	 *          the value clazz
	 * @return the t
	 */
	public <T> T fromJson(String json, Class<T> clazz, Class<?> keyClazz, Class<?> valueClazz)
	{
		if (json == null)
		{
			return null;
		}
		try
		{
			Object o = Deserializator.deserialize(json, config);
			return DeserializationMapper.convertToObject(o, clazz, null, keyClazz, valueClazz, config);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts List&lt;clazz&gt; from JSON.
	 *
	 * @param <T>
	 *          the generic type
	 * @param json
	 *          the json
	 * @param clazz
	 *          the clazz
	 * @return the list
	 * @since 0.3.0
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> fromJsonList(String json, Class<T> clazz)
	{
		return fromJson(json, List.class, clazz);
	}

	/**
	 * Converts Map&lt;keyClazz, valueClazz&gt; from JSON.
	 *
	 * @param <T>
	 *          the generic type
	 * @param <U>
	 *          the generic type
	 * @param json
	 *          the json
	 * @param keyClazz
	 *          the key clazz
	 * @param valueClazz
	 *          the value clazz
	 * @return the map
	 * @since 0.3.0
	 */
	@SuppressWarnings("unchecked")
	public <T, U> Map<T, U> fromJsonMap(String json, Class<T> keyClazz, Class<U> valueClazz)
	{
		return fromJson(json, Map.class, keyClazz, valueClazz);
	}

	/**
	 * Uses custom date format for each date operation. If default date format
	 * should be used, just set <code>null</code>.
	 *
	 * @param customDateFormat
	 *          the custom date format
	 * @return the jackie
	 * @since 0.3.0
	 */
	public Jackie withCustomDateFormat(DateFormat customDateFormat)
	{
		config.setDateFormat(customDateFormat);
		return this;
	}

	/**
	 * Printing to JSON with pretty print.
	 *
	 * @return the jackie
	 * @since 0.3.0
	 */
	public Jackie withPrettyPrint()
	{
		return withPrettyPrint(true);
	}

	/**
	 * Printing to JSON with or without pretty print.
	 *
	 * @param prettyPrint
	 *          the pretty print
	 * @return the jackie
	 * @since 0.3.0
	 */
	public Jackie withPrettyPrint(boolean prettyPrint)
	{
		config.setPrettyPrint(prettyPrint);
		return this;
	}

	/**
	 * Gets the config.
	 *
	 * @return the config
	 * @since 0.3.3
	 */
	protected Config getConfig() {
		return config;
	}

}
