package cz.tvrzna.jackie;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class SerializationMapper.
 *
 * @author michalt
 */
public class SerializationMapper
{

	private SerializationMapper()
	{
	}

	/**
	 * Converts <code>object</code> to <code>Map</code> or <code>List</code>.
	 *
	 * @param <T>
	 *          the generic type
	 * @param object
	 *          the object
	 * @param config
	 *          the config
	 * @return the object
	 * @throws Exception
	 *           the exception
	 */
	protected static <T> Object convertFromObject(T object, Config config) throws Exception
	{
		if (object == null)
		{
			return null;
		}
		else if ((CommonUtils.SIMPLE_CLASSES.contains(object.getClass()) || Enum.class.isAssignableFrom(object.getClass())) && !object.getClass().isArray())
		{
			return object;
		}
		else if (Map.class.isAssignableFrom(object.getClass()))
		{
			return processMap(object, config);
		}
		else if (Collection.class.isAssignableFrom(object.getClass()))
		{
			return processArray(((Collection<?>) object).toArray(), config);
		}
		else if (object.getClass().isArray())
		{
			if (CommonUtils.PRIMITIVE_CLASSES.contains(object.getClass().getComponentType()))
			{
				return processArray(CommonUtils.convertPrimitiveArrayToObjects(object), config);
			}
			return processArray((Object[]) object, config);
		}
		return processObject(object, config);
	}

	/**
	 * Process <code>object</code> and its each field, that is not final nor
	 * static.
	 *
	 * @param <T>
	 *          the generic type
	 * @param object
	 *          the object
	 * @param config
	 *          the config
	 * @return the map
	 * @throws Exception
	 *           the exception
	 */
	private static <T> Map<String, Object> processObject(T object, Config config) throws Exception
	{
		Map<String, Object> result = new LinkedHashMap<>();

		for (Field field : CommonUtils.getFields(object.getClass()))
		{
			processField(object, result, field, config);
		}

		return result;
	}

	/**
	 * Process field in <code>object</code> and puts the value into
	 * <code>result</code> map.
	 *
	 * @param <T>
	 *          the generic type
	 * @param object
	 *          the object
	 * @param result
	 *          the result
	 * @param field
	 *          the field
	 * @param config
	 *          the config
	 * @throws Exception
	 *           the exception
	 */
	private static <T> void processField(T object, Map<String, Object> result, Field field, Config config) throws Exception
	{
		field.setAccessible(true);
		Object value = field.get(object);
		if (null != value)
		{
			String name = field.getName();
			JackieProperty property = field.getAnnotation(JackieProperty.class);
			if (property != null)
			{
				name = property.value();
			}
			result.put(name, convertFromObject(value, config));
		}
	}

	/**
	 * Handle array.
	 *
	 * @param array
	 *          the array
	 * @param config
	 *          the config
	 * @return the list
	 * @throws Exception
	 *           the exception
	 */
	private static List<Object> processArray(Object[] array, Config config) throws Exception
	{
		List<Object> result = new ArrayList<>();
		for (Object obj : array)
		{
			result.add(convertFromObject(obj, config));
		}
		return result;
	}

	/**
	 * Process map.
	 *
	 * @param map
	 *          the map
	 * @param config
	 *          the config
	 * @return the map
	 * @throws Exception
	 *           the exception
	 */
	private static Map<?, ?> processMap(Object map, Config config) throws Exception
	{
		Map<Object, Object> result = new LinkedHashMap<>();
		for (Map.Entry<?, ?> entry : ((Map<?, ?>) map).entrySet())
		{
			result.put(entry.getKey(), convertFromObject(entry.getValue(), config));
		}
		return result;
	}

}
