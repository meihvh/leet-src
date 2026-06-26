/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.graalvm.polyglot.HostAccess$Export
 */
package im.leet.api.scripts.api.bindings;

import im.leet.utils.mapper.FabricMapper;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.graalvm.polyglot.HostAccess;

public class ReflectProvider {
    @HostAccess.Export
    public Class<?> findClass(String obfName) throws ClassNotFoundException {
        String real = FabricMapper.remapClassName(obfName);
        if (!real.toLowerCase().startsWith("net.") && !real.toLowerCase().startsWith("org.")) {
            throw new ClassNotFoundException(String.format("Class access exception: %s", obfName));
        }
        try {
            return Class.forName(real);
        }
        catch (ClassNotFoundException e) {
            throw new ClassNotFoundException(String.format("Class not found: %s", obfName), e);
        }
    }

    @HostAccess.Export
    public Object newInstance(String classObf, Object ... args) throws ReflectiveOperationException {
        Class<?> cls = this.findClass(classObf);
        Constructor<?> ctor = this.findBestConstructor(cls, args);
        this.makeAccessible(ctor);
        Object[] conv = this.convertArgsFor(ctor.getParameterTypes(), args, ctor.isVarArgs());
        return ctor.newInstance(conv);
    }

    @HostAccess.Export
    public Object callStatic(String classObf, String methodObf, Object ... args) throws ReflectiveOperationException {
        Class<?> cls = this.findClass(classObf);
        String methodName = FabricMapper.remapMethodName(cls.getName(), methodObf);
        Method m = this.findBestMethod(cls, methodName, true, args);
        this.makeAccessible(m);
        Object[] conv = this.convertArgsFor(m.getParameterTypes(), args, m.isVarArgs());
        return m.invoke(null, conv);
    }

    @HostAccess.Export
    public Object callInstance(Object instance, String methodObf, Object ... args) throws ReflectiveOperationException {
        if (instance == null) {
            throw new IllegalArgumentException("instance == null");
        }
        Class<?> cls = instance.getClass();
        String methodName = FabricMapper.remapMethodName(cls.getName(), methodObf);
        Method m = this.findBestMethod(cls, methodName, false, args);
        this.makeAccessible(m);
        Object[] conv = this.convertArgsFor(m.getParameterTypes(), args, m.isVarArgs());
        return m.invoke(instance, conv);
    }

    @HostAccess.Export
    public Object call(String classOrInstance, Object targetOrNull, String methodObf, Object ... args) throws ReflectiveOperationException {
        if (targetOrNull == null) {
            return this.callStatic(classOrInstance, methodObf, args);
        }
        return this.callInstance(targetOrNull, methodObf, args);
    }

    @HostAccess.Export
    public Object getFieldValue(String classOrInstanceObf, Object instanceIfAny, String fieldObf) throws ReflectiveOperationException {
        Class<?> cls = this.findClass(classOrInstanceObf);
        String fieldName = FabricMapper.remapFieldName(cls.getName(), fieldObf);
        Field f = this.findFieldInHierarchy(cls, fieldName);
        this.makeAccessible(f);
        return f.get(instanceIfAny);
    }

    @HostAccess.Export
    public void setFieldValue(String classOrInstanceObf, Object instanceIfAny, String fieldObf, Object value) throws ReflectiveOperationException {
        Class<?> cls = this.findClass(classOrInstanceObf);
        String fieldName = FabricMapper.remapFieldName(cls.getName(), fieldObf);
        Field f = this.findFieldInHierarchy(cls, fieldName);
        this.makeAccessible(f);
        Object converted = this.convertSingleArgFor(f.getType(), value);
        f.set(instanceIfAny, converted);
    }

    private Field findFieldInHierarchy(Class<?> cls, String name) throws NoSuchFieldException {
        for (Class<?> cur = cls; cur != null; cur = cur.getSuperclass()) {
            try {
                return cur.getDeclaredField(name);
            }
            catch (NoSuchFieldException e) {
                continue;
            }
        }
        throw new NoSuchFieldException("Field " + name + " not found in " + cls.getName());
    }

    private Constructor<?> findBestConstructor(Class<?> cls, Object[] args) throws NoSuchMethodException {
        Constructor<?> best = null;
        for (Constructor<?> c : cls.getDeclaredConstructors()) {
            if (!this.isCallableWith(c.getParameterTypes(), args, c.isVarArgs()) || best != null && !this.betterMatch(best.getParameterTypes(), c.getParameterTypes(), args)) continue;
            best = c;
        }
        if (best == null) {
            throw new NoSuchMethodException("No matching constructor for " + cls.getName());
        }
        return best;
    }

    private Method findBestMethod(Class<?> cls, String methodName, boolean wantStatic, Object[] args) throws NoSuchMethodException {
        Method best = null;
        for (Class<?> cur = cls; cur != null; cur = cur.getSuperclass()) {
            for (Method m : cur.getDeclaredMethods()) {
                if (!m.getName().equals(methodName) || Modifier.isStatic(m.getModifiers()) != wantStatic || !this.isCallableWith(m.getParameterTypes(), args, m.isVarArgs()) || best != null && !this.betterMatch(best.getParameterTypes(), m.getParameterTypes(), args)) continue;
                best = m;
            }
        }
        if (best == null) {
            throw new NoSuchMethodException("Method " + methodName + " not found in " + cls.getName());
        }
        return best;
    }

    private boolean isCallableWith(Class<?>[] paramTypes, Object[] args, boolean varargs) {
        if (varargs ? args.length < paramTypes.length - 1 : paramTypes.length != args.length) {
            return false;
        }
        for (int i = 0; i < paramTypes.length; ++i) {
            if (varargs && i == paramTypes.length - 1) {
                Class<?> comp = paramTypes[i].getComponentType();
                for (int j = i; j < args.length; ++j) {
                    if (this.isCompatible(comp, args[j])) continue;
                    return false;
                }
                break;
            }
            if (i >= args.length) {
                return false;
            }
            if (this.isCompatible(paramTypes[i], args[i])) continue;
            return false;
        }
        return true;
    }

    private boolean isCompatible(Class<?> paramType, Object arg) {
        if (arg == null) {
            return !paramType.isPrimitive();
        }
        Class<?> aClass = this.unwrapPolyglot(arg).getClass();
        if (paramType.isPrimitive()) {
            return this.primitiveAccepts(paramType, aClass);
        }
        return this.box(paramType).isAssignableFrom(this.box(aClass));
    }

    private boolean primitiveAccepts(Class<?> primitive, Class<?> provided) {
        if (primitive == Boolean.TYPE) {
            return provided == Boolean.class || provided == Boolean.TYPE;
        }
        if (primitive == Byte.TYPE) {
            return provided == Byte.class || provided == Byte.TYPE;
        }
        if (primitive == Character.TYPE) {
            return provided == Character.class || provided == Character.TYPE;
        }
        if (primitive == Short.TYPE) {
            return provided == Short.class || provided == Short.TYPE || provided == Byte.class;
        }
        if (primitive == Integer.TYPE) {
            return provided == Integer.class || provided == Integer.TYPE || provided == Short.class || provided == Byte.class || provided == Character.class;
        }
        if (primitive == Long.TYPE) {
            return provided == Long.class || provided == Long.TYPE || provided == Integer.class || provided == Short.class || provided == Byte.class || provided == Character.class;
        }
        if (primitive == Float.TYPE) {
            return provided == Float.class || provided == Float.TYPE || provided == Long.class || provided == Integer.class || provided == Short.class || provided == Byte.class;
        }
        if (primitive == Double.TYPE) {
            return provided == Double.class || provided == Double.TYPE || provided == Float.class || provided == Long.class || provided == Integer.class || provided == Short.class || provided == Byte.class;
        }
        return false;
    }

    private Class<?> box(Class<?> c) {
        if (!c.isPrimitive()) {
            return c;
        }
        if (c == Boolean.TYPE) {
            return Boolean.class;
        }
        if (c == Byte.TYPE) {
            return Byte.class;
        }
        if (c == Character.TYPE) {
            return Character.class;
        }
        if (c == Short.TYPE) {
            return Short.class;
        }
        if (c == Integer.TYPE) {
            return Integer.class;
        }
        if (c == Long.TYPE) {
            return Long.class;
        }
        if (c == Float.TYPE) {
            return Float.class;
        }
        if (c == Double.TYPE) {
            return Double.class;
        }
        return c;
    }

    private boolean betterMatch(Class<?>[] current, Class<?>[] candidate, Object[] args) {
        int scoreCur = 0;
        int scoreCand = 0;
        for (int i = 0; i < candidate.length && i < args.length; ++i) {
            Class<?> a = current[Math.min(i, current.length - 1)];
            Class<?> b = candidate[Math.min(i, candidate.length - 1)];
            Class<?> actual = this.unwrapPolyglot(args[i]).getClass();
            if (a == actual) {
                scoreCur += 2;
            }
            if (b == actual) {
                scoreCand += 2;
            }
            if (a.isAssignableFrom(actual)) {
                ++scoreCur;
            }
            if (!b.isAssignableFrom(actual)) continue;
            ++scoreCand;
        }
        return scoreCand > scoreCur;
    }

    private void makeAccessible(AccessibleObject ao) {
        try {
            ao.setAccessible(true);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private Object[] convertArgsFor(Class<?>[] paramTypes, Object[] args, boolean varargs) {
        Object[] res = new Object[paramTypes.length];
        if (varargs) {
            int fixed = paramTypes.length - 1;
            for (int i = 0; i < fixed; ++i) {
                res[i] = this.convertSingleArgFor(paramTypes[i], args.length > i ? args[i] : null);
            }
            Class<?> comp = paramTypes[fixed].getComponentType();
            int varCount = Math.max(0, args.length - fixed);
            Object varArray = Array.newInstance(comp, varCount);
            for (int i = 0; i < varCount; ++i) {
                Array.set(varArray, i, this.convertSingleArgFor(comp, args[fixed + i]));
            }
            res[fixed] = varArray;
        } else {
            for (int i = 0; i < paramTypes.length; ++i) {
                res[i] = this.convertSingleArgFor(paramTypes[i], args.length > i ? args[i] : null);
            }
        }
        return res;
    }

    private Object convertSingleArgFor(Class<?> paramType, Object arg) {
        Number n;
        Object un = this.unwrapPolyglot(arg);
        if (un == null) {
            return null;
        }
        if (paramType.isPrimitive()) {
            if (un instanceof Number) {
                n = (Number)un;
                if (paramType == Integer.TYPE) {
                    return n.intValue();
                }
                if (paramType == Long.TYPE) {
                    return n.longValue();
                }
                if (paramType == Short.TYPE) {
                    return n.shortValue();
                }
                if (paramType == Byte.TYPE) {
                    return n.byteValue();
                }
                if (paramType == Float.TYPE) {
                    return Float.valueOf(n.floatValue());
                }
                if (paramType == Double.TYPE) {
                    return n.doubleValue();
                }
                if (paramType == Character.TYPE) {
                    return Character.valueOf((char)n.intValue());
                }
            }
            if (paramType == Boolean.TYPE && un instanceof Boolean) {
                return un;
            }
        }
        if (paramType.isInstance(un)) {
            return un;
        }
        if (paramType.isEnum() && un instanceof String) {
            Object e = Enum.valueOf(paramType, (String)un);
            return e;
        }
        if (Number.class.isAssignableFrom(paramType) && un instanceof Number) {
            n = (Number)un;
            if (paramType == Integer.class) {
                return n.intValue();
            }
            if (paramType == Long.class) {
                return n.longValue();
            }
            if (paramType == Short.class) {
                return n.shortValue();
            }
            if (paramType == Byte.class) {
                return n.byteValue();
            }
            if (paramType == Float.class) {
                return Float.valueOf(n.floatValue());
            }
            if (paramType == Double.class) {
                return n.doubleValue();
            }
        }
        return un;
    }

    private Object unwrapPolyglot(Object maybePoly) {
        if (maybePoly == null) {
            return null;
        }
        try {
            Class<?> valueClass = Class.forName("org.graalvm.polyglot.Value");
            if (valueClass.isInstance(maybePoly)) {
                Method isHostObject = valueClass.getMethod("isHostObject", new Class[0]);
                Boolean host = (Boolean)isHostObject.invoke(maybePoly, new Object[0]);
                if (host.booleanValue()) {
                    return valueClass.getMethod("asHostObject", new Class[0]).invoke(maybePoly, new Object[0]);
                }
                Method fitsLong = valueClass.getMethod("fitsInLong", new Class[0]);
                if (((Boolean)fitsLong.invoke(maybePoly, new Object[0])).booleanValue()) {
                    return valueClass.getMethod("asLong", new Class[0]).invoke(maybePoly, new Object[0]);
                }
                Method fitsDouble = valueClass.getMethod("fitsInDouble", new Class[0]);
                if (((Boolean)fitsDouble.invoke(maybePoly, new Object[0])).booleanValue()) {
                    return valueClass.getMethod("asDouble", new Class[0]).invoke(maybePoly, new Object[0]);
                }
                Method isString = valueClass.getMethod("isString", new Class[0]);
                if (((Boolean)isString.invoke(maybePoly, new Object[0])).booleanValue()) {
                    return valueClass.getMethod("asString", new Class[0]).invoke(maybePoly, new Object[0]);
                }
                Method isBoolean = valueClass.getMethod("isBoolean", new Class[0]);
                if (((Boolean)isBoolean.invoke(maybePoly, new Object[0])).booleanValue()) {
                    return valueClass.getMethod("asBoolean", new Class[0]).invoke(maybePoly, new Object[0]);
                }
                Method hasArrayElements = valueClass.getMethod("hasArrayElements", new Class[0]);
                if (((Boolean)hasArrayElements.invoke(maybePoly, new Object[0])).booleanValue()) {
                    long len = (Long)valueClass.getMethod("getArraySize", new Class[0]).invoke(maybePoly, new Object[0]);
                    Object[] arr = new Object[(int)len];
                    int i = 0;
                    while ((long)i < len) {
                        Object el = valueClass.getMethod("getArrayElement", Long.TYPE).invoke(maybePoly, i);
                        arr[i] = this.unwrapPolyglot(el);
                        ++i;
                    }
                    return arr;
                }
                return valueClass.getMethod("as", Class.class).invoke(maybePoly, Object.class);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return maybePoly;
    }
}

