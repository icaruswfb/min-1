package br.com.min.utils;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CommonsReflection {


	/**
	 * @param c
	 * @return o primeiro tipo parametrizado da classe do objeto dado, se tiver.
	 */
	public static Class<?> getParameterizedClass(Class<?> c) {
		return getParameterizedClass(c, 1);
	}

	/**
	 * @param c
	 * @param index
	 * @return o primeiro tipo parametrizado da classe do objeto dado, se tiver.
	 */
	public static Class<?> getParameterizedClass(Class<?> c, int index) {
		Type[] types = getParameterizedTypes((Class<ParameterizedType>)c);
		return (index <= types.length) ?  (Class<?>) types[index - 1] : null;
	}

	/**
	 * @param c
	 * @return todos os tipos parametrizados da classe do objeto dado.
	 */
	public static Type[] getParameterizedTypes(Class<?> c) {

		Type superClass = c.getGenericSuperclass(); 
		if (!(superClass instanceof ParameterizedType)) {
			superClass = c.getSuperclass().getGenericSuperclass();
		}

		return (superClass instanceof ParameterizedType) ? ((ParameterizedType) superClass)
				.getActualTypeArguments() : new Type[] {};

	}



}
