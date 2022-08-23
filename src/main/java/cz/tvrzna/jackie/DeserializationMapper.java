package cz.tvrzna.jackie;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * The Class DeserializationMapper.
 *
 * @author michalt
 */
public class DeserializationMapper
{
	private DeserializationMapper()
	{
	}

	/**
	 * Convert from.
	 *
	 * @param <T>
	 *          the generic type
	 * @param object
	 *          the object
	 * @param clazz
	 *          the clazz
	 * @param field
	 *          the field
	 * @param subClazz
	 *          the sub clazz
	 * @param subClazz2
	 *          the sub clazz 2
	 * @param config
	 *          the config
	 * @return the t
	 * @throws Exception
	 *           the exception
	 */
	@SuppressWarnings("unchecked")
	protected static <T> T convertToObject(Object object, Class<T> clazz, Field field, Class<?> subClazz, Class<?> subClazz2, Config config) throws Exception
	{
		Class<T> tmpClazz = clazz;
		if (Object.class.equals(tmpClazz) && object != null && object.getClass() != null)
		{
			tmpClazz = (Class<T>) object.getClass();
		}

		if (ObjectWrapper.class.isAssignableFrom(object.getClass()))
		{
			return (T) ((ObjectWrapper) object).getValue();
		}

		if ((CommonUtils.SIMPLE_CLASSES.contains(tmpClazz) || Enum.class.isAssignableFrom(tmpClazz)) && !tmpClazz.isArray())
		{
			return (T) deserializeValue((String) object, tmpClazz, config);
		}
		else if (Collection.class.isAssignableFrom(tmpClazz))
		{
			Class<?> lstSubClazz = null;
			if (field != null)
			{
				lstSubClazz = getClassFromField(field, 0);
			}
			else if (subClazz != null)
			{
				lstSubClazz = subClazz;
			}
			if (lstSubClazz == null)
			{
				lstSubClazz = Object.class;
			}
			return (T) convertFromList((List<Object>) object, lstSubClazz, config);
		}
		else if (tmpClazz.isArray())
		{
			Class<?> arrSubClazz = tmpClazz.getComponentType();
			List<?> list = convertFromList((List<Object>) object, arrSubClazz, config);
			if (CommonUtils.PRIMITIVE_CLASSES.contains(arrSubClazz))
			{
				return CommonUtils.convertArrayToPrimitive(list, arrSubClazz);
			}
			return (T) list.toArray((T[]) Array.newInstance(arrSubClazz, 0));
		}
		else if (Map.class.isAssignableFrom(tmpClazz))
		{
			Class<?> keyClazz = null;
			Class<?> valueClazz = null;
			if (field != null)
			{
				keyClazz = getClassFromField(field, 0);
				valueClazz = getClassFromField(field, 1);
			}
			else if (subClazz != null && subClazz2 != null)
			{
				keyClazz = subClazz;
				valueClazz = subClazz2;
			}
			if (keyClazz == null || valueClazz == null)
			{
				keyClazz = Object.class;
				valueClazz = Object.class;
			}
			return (T) convertFromMap((Map<String, Object>) object, keyClazz, valueClazz, config);
		}
		return convertFromMapToObject((Map<String, Object>) object, tmpClazz, config);
	}

	/**
	 * Deserialize value.
	 *
	 * @param value
	 *          the value
	 * @param clazz
	 *          the clazz
	 * @param config
	 *          the config
	 * @return the object
	 * @throws ParseException
	 *           the parse exception
	 */
	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	private static Object deserializeValue(String value, Class<?> clazz, Config config) throws ParseException
	{
		if ("null".equals(value))
		{
			return null;
		}
		else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz))
		{
			return Boolean.parseBoolean(value);
		}
		else if (Short.class.equals(clazz) || short.class.equals(clazz))
		{
			return Short.parseShort(value);
		}
		else if (Integer.class.equals(clazz) || int.class.equals(clazz))
		{
			return Integer.parseInt(value);
		}
		else if (Long.class.equals(clazz) || long.class.equals(clazz))
		{
			return Long.parseLong(value);
		}
		else if (Float.class.equals(clazz) || float.class.equals(clazz))
		{
			return Float.parseFloat(value);
		}
		else if (Double.class.equals(clazz) || double.class.equals(clazz))
		{
			return Double.parseDouble(value);
		}
		else if (BigInteger.class.equals(clazz))
		{
			return new BigInteger(value);
		}
		else if (BigDecimal.class.equals(clazz))
		{
			return new BigDecimal(value);
		}
		else if (Date.class.equals(clazz))
		{
			DateFormat df = Optional.ofNullable(config.getDateFormat()).orElse(new SimpleDateFormat(CommonUtils.DATE_FORMAT_JSON));
			return df.parseObject(value);
		}
		else if (Enum.class.isAssignableFrom(clazz))
		{
			return Enum.valueOf((Class<? extends Enum>) clazz, value);
		}
		return value.toString();
	}

	/**
	 * Convert from.
	 *
	 * @param <T>
	 *          the generic type
	 * @param object
	 *          the object
	 * @param clazz
	 *          the clazz
	 * @param config
	 *          the config
	 * @return the t
	 * @throws Exception
	 *           the exception
	 */
	protected static <T> T convertToObject(Object object, Class<T> clazz, Config config) throws Exception
	{
		return convertToObject(object, clazz, null, null, null, config);
	}

	/**
	 * Convert from.
	 *
	 * @param <T>
	 *          the generic type
	 * @param object
	 *          the object
	 * @param clazz
	 *          the clazz
	 * @param subClazz
	 *          the sub clazz
	 * @param config
	 *          the config
	 * @return the t
	 * @throws Exception
	 *           the exception
	 */
	protected static <T> T convertToObject(Object object, Class<T> clazz, Class<?> subClazz, Config config) throws Exception
	{
		return convertToObject(object, clazz, null, subClazz, null, config);
	}

	/**
	 * Gets the class from field.
	 *
	 * @param field
	 *          the field
	 * @param index
	 *          the index
	 * @return the class from field
	 */
	private static Class<?> getClassFromField(Field field, int index)
	{
		Object o = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[index];
		if (o instanceof Class)
		{
			return (Class<?>) o;
		}
		else if (o instanceof Type)
		{
			return o.getClass();
		}
		return null;
	}

	/**
	 * Convert from map to object.
	 *
	 * @param <T>
	 *          the generic type
	 * @param map
	 *          the map
	 * @param clazz
	 *          the clazz
	 * @param config
	 *          the config
	 * @return the t
	 * @throws Exception
	 *           the exception
	 */
	private static <T> T convertFromMapToObject(Map<String, Object> map, Class<T> clazz, Config config) throws Exception
	{
		T result = clazz.newInstance();

		for (Field field : CommonUtils.getFields(clazz))
		{
			fillField(result, map, field, config);
		}

		return result;
	}

	/**
	 * Convert from map.
	 *
	 * @param <K>
	 *          the key type
	 * @param <V>
	 *          the value type
	 * @param map
	 *          the map
	 * @param keyClazz
	 *          the key clazz
	 * @param valueClazz
	 *          the value clazz
	 * @param config
	 *          the config
	 * @return the map
	 * @throws Exception
	 *           the exception
	 */
	@SuppressWarnings("unchecked")
	private static <K, V> Map<K, V> convertFromMap(Map<String, Object> map, Class<K> keyClazz, Class<V> valueClazz, Config config) throws Exception
	{
		Map<K, V> result = new LinkedHashMap<>();
		for (Entry<String, Object> entry : map.entrySet())
		{
			result.put((K) deserializeValue(entry.getKey(), keyClazz, config), convertToObject(entry.getValue(), valueClazz, config));
		}
		return result;
	}

	/**
	 * Convert from list.
	 *
	 * @param <T>
	 *          the generic type
	 * @param lst
	 *          the lst
	 * @param clazz
	 *          the clazz
	 * @param config
	 *          the config
	 * @return the list
	 * @throws Exception
	 *           the exception
	 */
	private static <T> List<T> convertFromList(List<Object> lst, Class<T> clazz, Config config) throws Exception
	{
		List<T> result = new ArrayList<>();
		for (Object object : lst)
		{
			result.add(convertToObject(object, clazz, config));
		}
		return result;
	}

	/**
	 * Fill field.
	 *
	 * @param <T>
	 *          the generic type
	 * @param result
	 *          the result
	 * @param map
	 *          the map
	 * @param field
	 *          the field
	 * @param config
	 *          the config
	 * @throws Exception
	 *           the exception
	 */
	private static <T> void fillField(T result, Map<String, Object> map, Field field, Config config) throws Exception
	{
		field.setAccessible(true);
		Object value = map.get(field.getName());
		Class<?> clazz = field.getType();

		if (value == null)
		{
			return;
		}
		field.set(result, convertToObject(value, clazz, field, null, null, config));
	}
}
