package com.lehow.mygsonconvert;

import android.support.annotation.Nullable;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.NoSuchElementException;
import okhttp3.ResponseBody;
import okio.Buffer;

final class Utils {
  static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

  private Utils() {
  }

  static Class<?> getRawType(Type type) {
    checkNotNull(type, "type == null");
    if (type instanceof Class) {
      return (Class) type;
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Type rawType = parameterizedType.getRawType();
      if (!(rawType instanceof Class)) {
        throw new IllegalArgumentException();
      } else {
        return (Class) rawType;
      }
    } else if (type instanceof GenericArrayType) {
      Type componentType = ((GenericArrayType) type).getGenericComponentType();
      return Array.newInstance(getRawType(componentType), 0).getClass();
    } else if (type instanceof TypeVariable) {
      return Object.class;
    } else if (type instanceof WildcardType) {
      return getRawType(((WildcardType) type).getUpperBounds()[0]);
    } else {
      throw new IllegalArgumentException(
          "Expected a Class, ParameterizedType, or GenericArrayType, but <"
              + type
              + "> is of type "
              + type.getClass().getName());
    }
  }

  static boolean equals(Type a, Type b) {
    if (a == b) {
      return true;
    } else if (a instanceof Class) {
      return a.equals(b);
    } else if (a instanceof ParameterizedType) {
      if (!(b instanceof ParameterizedType)) {
        return false;
      } else {
        ParameterizedType pa = (ParameterizedType) a;
        ParameterizedType pb = (ParameterizedType) b;
        return equal(pa.getOwnerType(), pb.getOwnerType()) && pa.getRawType()
            .equals(pb.getRawType()) && Arrays.equals(pa.getActualTypeArguments(),
            pb.getActualTypeArguments());
      }
    } else if (a instanceof GenericArrayType) {
      if (!(b instanceof GenericArrayType)) {
        return false;
      } else {
        GenericArrayType ga = (GenericArrayType) a;
        GenericArrayType gb = (GenericArrayType) b;
        return equals(ga.getGenericComponentType(), gb.getGenericComponentType());
      }
    } else if (a instanceof WildcardType) {
      if (!(b instanceof WildcardType)) {
        return false;
      } else {
        WildcardType wa = (WildcardType) a;
        WildcardType wb = (WildcardType) b;
        return Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds()) && Arrays.equals(
            wa.getLowerBounds(), wb.getLowerBounds());
      }
    } else if (a instanceof TypeVariable) {
      if (!(b instanceof TypeVariable)) {
        return false;
      } else {
        TypeVariable<?> va = (TypeVariable) a;
        TypeVariable<?> vb = (TypeVariable) b;
        return va.getGenericDeclaration() == vb.getGenericDeclaration() && va.getName()
            .equals(vb.getName());
      }
    } else {
      return false;
    }
  }

  static Type getGenericSupertype(Type context, Class<?> rawType, Class<?> toResolve) {
    if (toResolve == rawType) {
      return context;
    } else {
      if (toResolve.isInterface()) {
        Class<?>[] interfaces = rawType.getInterfaces();
        int i = 0;

        for (int length = interfaces.length; i < length; ++i) {
          if (interfaces[i] == toResolve) {
            return rawType.getGenericInterfaces()[i];
          }

          if (toResolve.isAssignableFrom(interfaces[i])) {
            return getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
          }
        }
      }

      if (!rawType.isInterface()) {
        while (rawType != Object.class) {
          Class<?> rawSupertype = rawType.getSuperclass();
          if (rawSupertype == toResolve) {
            return rawType.getGenericSuperclass();
          }

          if (toResolve.isAssignableFrom(rawSupertype)) {
            return getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
          }

          rawType = rawSupertype;
        }
      }

      return toResolve;
    }
  }

  private static int indexOf(Object[] array, Object toFind) {
    for (int i = 0; i < array.length; ++i) {
      if (toFind.equals(array[i])) {
        return i;
      }
    }

    throw new NoSuchElementException();
  }

  private static boolean equal(Object a, Object b) {
    return a == b || a != null && a.equals(b);
  }

  static int hashCodeOrZero(Object o) {
    return o != null ? o.hashCode() : 0;
  }

  static String typeToString(Type type) {
    return type instanceof Class ? ((Class) type).getName() : type.toString();
  }

  static Type getSupertype(Type context, Class<?> contextRawType, Class<?> supertype) {
    if (!supertype.isAssignableFrom(contextRawType)) {
      throw new IllegalArgumentException();
    } else {
      return resolve(context, contextRawType,
          getGenericSupertype(context, contextRawType, supertype));
    }
  }

  static Type resolve(Type context, Class<?> contextRawType, Type toResolve) {
    while (true) {
      if (toResolve instanceof TypeVariable) {
        TypeVariable<?> typeVariable = (TypeVariable) toResolve;
        toResolve = resolveTypeVariable(context, contextRawType, typeVariable);
        if (toResolve != typeVariable) {
          continue;
        }

        return toResolve;
      }

      Type newOwnerType;
      if (toResolve instanceof Class && ((Class) toResolve).isArray()) {
        Class<?> original = (Class) toResolve;
        Type componentType = original.getComponentType();
        newOwnerType = resolve(context, contextRawType, componentType);
        return (Type) (componentType == newOwnerType ? original
            : new Utils.GenericArrayTypeImpl(newOwnerType));
      }

      Type ownerType;
      if (toResolve instanceof GenericArrayType) {
        GenericArrayType original = (GenericArrayType) toResolve;
        ownerType = original.getGenericComponentType();
        newOwnerType = resolve(context, contextRawType, ownerType);
        return (Type) (ownerType == newOwnerType ? original
            : new Utils.GenericArrayTypeImpl(newOwnerType));
      }

      if (toResolve instanceof ParameterizedType) {
        ParameterizedType original = (ParameterizedType) toResolve;
        ownerType = original.getOwnerType();
        newOwnerType = resolve(context, contextRawType, ownerType);
        boolean changed = newOwnerType != ownerType;
        Type[] args = original.getActualTypeArguments();
        int t = 0;

        for (int length = args.length; t < length; ++t) {
          Type resolvedTypeArgument = resolve(context, contextRawType, args[t]);
          if (resolvedTypeArgument != args[t]) {
            if (!changed) {
              args = (Type[]) args.clone();
              changed = true;
            }

            args[t] = resolvedTypeArgument;
          }
        }

        return (Type) (changed ? new Utils.ParameterizedTypeImpl(newOwnerType,
            original.getRawType(), args) : original);
      }

      if (toResolve instanceof WildcardType) {
        WildcardType original = (WildcardType) toResolve;
        Type[] originalLowerBound = original.getLowerBounds();
        Type[] originalUpperBound = original.getUpperBounds();
        Type upperBound;
        if (originalLowerBound.length == 1) {
          upperBound = resolve(context, contextRawType, originalLowerBound[0]);
          if (upperBound != originalLowerBound[0]) {
            return new Utils.WildcardTypeImpl(new Type[] { Object.class },
                new Type[] { upperBound });
          }
        } else if (originalUpperBound.length == 1) {
          upperBound = resolve(context, contextRawType, originalUpperBound[0]);
          if (upperBound != originalUpperBound[0]) {
            return new Utils.WildcardTypeImpl(new Type[] { upperBound }, EMPTY_TYPE_ARRAY);
          }
        }

        return original;
      }

      return toResolve;
    }
  }

  private static Type resolveTypeVariable(Type context, Class<?> contextRawType,
      TypeVariable<?> unknown) {
    Class<?> declaredByRaw = declaringClassOf(unknown);
    if (declaredByRaw == null) {
      return unknown;
    } else {
      Type declaredBy = getGenericSupertype(context, contextRawType, declaredByRaw);
      if (declaredBy instanceof ParameterizedType) {
        int index = indexOf(declaredByRaw.getTypeParameters(), unknown);
        return ((ParameterizedType) declaredBy).getActualTypeArguments()[index];
      } else {
        return unknown;
      }
    }
  }

  private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
    GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
    return genericDeclaration instanceof Class ? (Class) genericDeclaration : null;
  }

  static void checkNotPrimitive(Type type) {
    if (type instanceof Class && ((Class) type).isPrimitive()) {
      throw new IllegalArgumentException();
    }
  }

  static <T> T checkNotNull(@Nullable T object, String message) {
    if (object == null) {
      throw new NullPointerException(message);
    } else {
      return object;
    }
  }

  static boolean isAnnotationPresent(Annotation[] annotations, Class<? extends Annotation> cls) {
    Annotation[] var2 = annotations;
    int var3 = annotations.length;

    for (int var4 = 0; var4 < var3; ++var4) {
      Annotation annotation = var2[var4];
      if (cls.isInstance(annotation)) {
        return true;
      }
    }

    return false;
  }

  static ResponseBody buffer(ResponseBody body) throws IOException {
    Buffer buffer = new Buffer();
    body.source().readAll(buffer);
    return ResponseBody.create(body.contentType(), body.contentLength(), buffer);
  }

  static <T> void validateServiceInterface(Class<T> service) {
    if (!service.isInterface()) {
      throw new IllegalArgumentException("API declarations must be interfaces.");
    } else if (service.getInterfaces().length > 0) {
      throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
    }
  }

  static Type getParameterUpperBound(int index, ParameterizedType type) {
    Type[] types = type.getActualTypeArguments();
    if (index >= 0 && index < types.length) {
      Type paramType = types[index];
      return paramType instanceof WildcardType ? ((WildcardType) paramType).getUpperBounds()[0]
          : paramType;
    } else {
      throw new IllegalArgumentException(
          "Index " + index + " not in range [0," + types.length + ") for " + type);
    }
  }

  static boolean hasUnresolvableType(Type type) {
    if (type instanceof Class) {
      return false;
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Type[] var2 = parameterizedType.getActualTypeArguments();
      int var3 = var2.length;

      for (int var4 = 0; var4 < var3; ++var4) {
        Type typeArgument = var2[var4];
        if (hasUnresolvableType(typeArgument)) {
          return true;
        }
      }

      return false;
    } else if (type instanceof GenericArrayType) {
      return hasUnresolvableType(((GenericArrayType) type).getGenericComponentType());
    } else if (type instanceof TypeVariable) {
      return true;
    } else if (type instanceof WildcardType) {
      return true;
    } else {
      String className = type == null ? "null" : type.getClass().getName();
      throw new IllegalArgumentException(
          "Expected a Class, ParameterizedType, or GenericArrayType, but <"
              + type
              + "> is of type "
              + className);
    }
  }

  static Type getCallResponseType(Type returnType) {
    if (!(returnType instanceof ParameterizedType)) {
      throw new IllegalArgumentException(
          "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
    } else {
      return getParameterUpperBound(0, (ParameterizedType) returnType);
    }
  }

  private static final class WildcardTypeImpl implements WildcardType {
    private final Type upperBound;
    private final Type lowerBound;

    WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
      if (lowerBounds.length > 1) {
        throw new IllegalArgumentException();
      } else if (upperBounds.length != 1) {
        throw new IllegalArgumentException();
      } else {
        if (lowerBounds.length == 1) {
          if (lowerBounds[0] == null) {
            throw new NullPointerException();
          }

          Utils.checkNotPrimitive(lowerBounds[0]);
          if (upperBounds[0] != Object.class) {
            throw new IllegalArgumentException();
          }

          this.lowerBound = lowerBounds[0];
          this.upperBound = Object.class;
        } else {
          if (upperBounds[0] == null) {
            throw new NullPointerException();
          }

          Utils.checkNotPrimitive(upperBounds[0]);
          this.lowerBound = null;
          this.upperBound = upperBounds[0];
        }
      }
    }

    @Override public Type[] getUpperBounds() {
      return new Type[] { this.upperBound };
    }

    @Override public Type[] getLowerBounds() {
      return this.lowerBound != null ? new Type[] { this.lowerBound } : Utils.EMPTY_TYPE_ARRAY;
    }

    @Override public boolean equals(Object other) {
      return other instanceof WildcardType && Utils.equals(this, (WildcardType) other);
    }

    @Override public int hashCode() {
      return (this.lowerBound != null ? 31 + this.lowerBound.hashCode() : 1)
          ^ 31 + this.upperBound.hashCode();
    }

    @Override public String toString() {
      return this.lowerBound != null ? "? super " + Utils.typeToString(this.lowerBound)
          : (this.upperBound == Object.class ? "?"
              : "? extends " + Utils.typeToString(this.upperBound));
    }
  }

  private static final class GenericArrayTypeImpl implements GenericArrayType {
    private final Type componentType;

    GenericArrayTypeImpl(Type componentType) {
      this.componentType = componentType;
    }

    @Override public Type getGenericComponentType() {
      return this.componentType;
    }

    @Override public boolean equals(Object o) {
      return o instanceof GenericArrayType && Utils.equals(this, (GenericArrayType) o);
    }

    @Override public int hashCode() {
      return this.componentType.hashCode();
    }

    @Override public String toString() {
      return Utils.typeToString(this.componentType) + "[]";
    }
  }

  public static final class ParameterizedTypeImpl implements ParameterizedType {
    private final Type ownerType;
    private final Type rawType;
    private final Type[] typeArguments;

    ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
      if (rawType instanceof Class && ownerType == null != (((Class) rawType).getEnclosingClass()
          == null)) {
        throw new IllegalArgumentException();
      } else {
        Type[] var4 = typeArguments;
        int var5 = typeArguments.length;

        for (int var6 = 0; var6 < var5; ++var6) {
          Type typeArgument = var4[var6];
          Utils.checkNotNull(typeArgument, "typeArgument == null");
          Utils.checkNotPrimitive(typeArgument);
        }

        this.ownerType = ownerType;
        this.rawType = rawType;
        this.typeArguments = (Type[]) typeArguments.clone();
      }
    }

    @Override public Type[] getActualTypeArguments() {
      return (Type[]) this.typeArguments.clone();
    }

    @Override public Type getRawType() {
      return this.rawType;
    }

    @Override public Type getOwnerType() {
      return this.ownerType;
    }

    @Override public boolean equals(Object other) {
      return other instanceof ParameterizedType && Utils.equals(this, (ParameterizedType) other);
    }

    @Override public int hashCode() {
      return Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode() ^ Utils.hashCodeOrZero(
          this.ownerType);
    }

    @Override public String toString() {
      if (this.typeArguments.length == 0) {
        return Utils.typeToString(this.rawType);
      } else {
        StringBuilder result = new StringBuilder(30 * (this.typeArguments.length + 1));
        result.append(Utils.typeToString(this.rawType));
        result.append("<").append(Utils.typeToString(this.typeArguments[0]));

        for (int i = 1; i < this.typeArguments.length; ++i) {
          result.append(", ").append(Utils.typeToString(this.typeArguments[i]));
        }

        return result.append(">").toString();
      }
    }
  }
}
