package cz.tvrzna.jackie;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	@SuppressWarnings("unchecked")
	protected static String serialize(Object object) throws Exception
	{
		if (object instanceof List)
		{
			return serializeList((List<Object>) object);
		}
		else if (object instanceof Map)
		{
			return serializeMap((Map<String, Object>) object);
		}
		else
		{
			return serializeValue(object);
		}
	}

	/**
	 * Serialize value.
	 *
	 * @param value
	 *          the value
	 * @return the string
	 */
	private static String serializeValue(Object value)
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
			DateFormat df = new SimpleDateFormat(CommonUtils.DATE_FORMAT_JSON);
			return getSeparator().concat(df.format(value)).concat(getSeparator());
		}
		return getSeparator().concat((value.toString()).replace(getSeparator(), "\\".concat(getSeparator()))).concat(getSeparator());
	}

	/**
	 * Serialize map.
	 *
	 * @param map
	 *          the map
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	private static String serializeMap(Map<String, Object> map) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		List<Entry<String, Object>> list = new ArrayList<>(map.entrySet());
		for (int i = 0; i < list.size(); i++)
		{
			Entry<String, Object> entry = list.get(i);

			sb.append(getSeparator()).append(entry.getKey()).append(getSeparator());
			sb.append(":");
			sb.append(serialize(entry.getValue()));
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
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	private static String serializeList(List<Object> list) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < list.size(); i++)
		{
			sb.append(serialize(list.get(i)));
			if (i < list.size() - 1)
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
