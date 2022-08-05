package cz.tvrzna.jackie;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * The Class Serializator.
 *
 * @author michalt
 */
public class Serializator
{

	private Serializator()
	{
	}

	/**
	 * Serialize <code>object</code> to <code>String</code>.
	 *
	 * @param object
	 *          the object
	 * @param config
	 *          the config
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	@SuppressWarnings("unchecked")
	protected static String serialize(Object object, Config config) throws Exception
	{
		if (object instanceof List)
		{
			return serializeList((List<Object>) object, config);
		}
		else if (object instanceof Map)
		{
			return serializeMap((Map<Object, Object>) object, config);
		}
		else if (object.getClass().isArray())
		{
			return serializeArray((Object[]) object, config);
		}
		else
		{
			return serializeValue(object, config);
		}
	}

	/**
	 * Serialize value.
	 *
	 * @param value
	 *          the value
	 *
	 * @param config
	 *          the config
	 * @return the string
	 */
	private static String serializeValue(Object value, Config config)
	{
		if (value == null)
		{
			return "null";
		}
		else if (value instanceof String)
		{
			return getSeparator().concat(((String) value).replace(getSeparator(), "\\".concat(getSeparator()))).concat(getSeparator());
		}
		else if (value instanceof Number)
		{
			return value.toString().replace(",", ".");
		}
		else if (value instanceof Boolean)
		{
			return value.toString();
		}
		else if (value instanceof Date)
		{
			DateFormat df = Optional.ofNullable(config.getDateFormat()).orElse(new SimpleDateFormat(CommonUtils.DATE_FORMAT_JSON));
			return getSeparator().concat(df.format(value)).concat(getSeparator());
		}
		return getSeparator().concat((value.toString()).replace(getSeparator(), "\\".concat(getSeparator()))).concat(getSeparator());
	}

	/**
	 * Serialize map.
	 *
	 * @param map
	 *          the map
	 * @param config
	 *          the config
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	private static String serializeMap(Map<Object, Object> map, Config config) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		List<Entry<Object, Object>> list = new ArrayList<>(map.entrySet());
		for (int i = 0; i < list.size(); i++)
		{
			Entry<Object, Object> entry = list.get(i);

			String key = serializeValue(entry.getKey(), config);
			boolean requireSeparator = !key.startsWith(getSeparator()) || !key.endsWith(getSeparator());

			sb.append(requireSeparator ? getSeparator() : "").append(key).append(requireSeparator ? getSeparator() : "");
			sb.append(":");
			sb.append(serialize(entry.getValue(), config));
			if (i < list.size() - 1)
			{
				sb.append(",");
			}
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Serialize list.
	 *
	 * @param list
	 *          the list
	 * @param config
	 *          the config
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	private static String serializeList(List<Object> list, Config config) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < list.size(); i++)
		{
			sb.append(serialize(list.get(i), config));
			if (i < list.size() - 1)
			{
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Serialize array.
	 *
	 * @param arr
	 *          the list
	 * @param config
	 *          the config
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	private static String serializeArray(Object[] arr, Config config) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < arr.length; i++)
		{
			sb.append(serialize(arr[i], config));
			if (i < arr.length - 1)
			{
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Gets the separator.
	 *
	 * @return the separator
	 */
	private static String getSeparator()
	{
		return CommonUtils.DEFAULT_SEPARATOR;
	}
}
