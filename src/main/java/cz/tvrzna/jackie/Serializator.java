package cz.tvrzna.jackie;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import cz.tvrzna.jackie.CommonUtils.ESCAPE_CHARACTERS;

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
	 * @param indent
	 *          the indent
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	@SuppressWarnings("unchecked")
	protected static String serialize(Object object, Config config, int indent) throws Exception
	{
		if (object instanceof List)
		{
			return serializeList((List<Object>) object, config, indent);
		}
		else if (object instanceof Map)
		{
			return serializeMap((Map<Object, Object>) object, config, indent);
		}
		else if (object.getClass().isArray())
		{
			return serializeArray((Object[]) object, config, indent);
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
			String data = getSeparator().concat(((String) value).replace(getSeparator(), "\\".concat(getSeparator()))).concat(getSeparator());
			for (ESCAPE_CHARACTERS escape : ESCAPE_CHARACTERS.values())
			{
				data = data.replace(escape.getCharacter(), escape.getValue());
			}
			return data;
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
	 * @param indent
	 *          the indent
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	private static String serializeMap(Map<Object, Object> map, Config config, int indent) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		indent++;
		List<Entry<Object, Object>> list = new ArrayList<>(map.entrySet());
		for (int i = 0; i < list.size(); i++)
		{
			if (i == 0) {
				if (config.isPrettyPrint())
				{
					sb.append(config.getPrettyLineSymbol());
				}
			}

			Entry<Object, Object> entry = list.get(i);

			String key = serializeValue(entry.getKey(), config);
			boolean requireSeparator = !key.startsWith(getSeparator()) || !key.endsWith(getSeparator());
			if (config.isPrettyPrint())
			{
				for (int j = 0; j < indent; j++)
				{
					sb.append(config.getPrettyIndentSymbol());
				}
			}
			sb.append(requireSeparator ? getSeparator() : "").append(key).append(requireSeparator ? getSeparator() : "");
			if (config.isPrettyPrint())
			{
				sb.append(" : ");
			} else {
				sb.append(":");
			}
			sb.append(serialize(entry.getValue(), config, indent));
			if (i < list.size() - 1)
			{
				sb.append(",");
				if (config.isPrettyPrint())
				{
					sb.append(config.getPrettyLineSymbol());
				}
			}
		}
		if (config.isPrettyPrint() && list.isEmpty())
		{
			sb.append(" ");
		}
		indent--;
		if (config.isPrettyPrint() && !list.isEmpty())
		{
			sb.append(config.getPrettyLineSymbol());
			for (int j = 0; j < indent; j++)
			{
				sb.append(config.getPrettyIndentSymbol());
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
	 * @param indent
	 *          the indent
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	private static String serializeList(List<Object> list, Config config, int indent) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (config.isPrettyPrint())
		{
			sb.append(" ");
		}
		for (int i = 0; i < list.size(); i++)
		{
			sb.append(serialize(list.get(i), config, indent));
			if (i < list.size() - 1)
			{
				sb.append(",");
				if (config.isPrettyPrint())
				{
					sb.append(" ");
				}
			}
		}
		if (config.isPrettyPrint())
		{
			sb.append(" ");
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
	 * @param indent
	 *          the indent
	 * @return the string
	 * @throws Exception
	 *           the exception
	 */
	private static String serializeArray(Object[] arr, Config config, int indent) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (config.isPrettyPrint())
		{
			sb.append(" ");
		}
		for (int i = 0; i < arr.length; i++)
		{
			sb.append(serialize(arr[i], config, indent));
			if (i < arr.length - 1)
			{
				sb.append(",");
				if (config.isPrettyPrint())
				{
					sb.append(" ");
				}
			}
		}
		if (config.isPrettyPrint())
		{
			sb.append(" ");
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
