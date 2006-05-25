package org.codehaus.groovy.grails.commons.support;

import junit.framework.TestCase;
import org.codehaus.groovy.grails.support.ClassUtils;

public class ClassUtilsTests extends TestCase {

    public void testBooleanMatchesboolean() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(Boolean.class, boolean.class));
    }

    public void testbooleanMatchesBoolean() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(boolean.class, Boolean.class));
    }

    public void testIntegerMatchesint() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(Integer.class, int.class));
    }

    public void testintMatchesInteger() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(int.class, Integer.class));
    }

    public void testShortMatchesshort() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(Short.class, short.class));
    }

    public void testshortMatchesShort() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(short.class, Short.class));
    }

    public void testByteMatchesbyte() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(Byte.class, byte.class));
    }

    public void testbyteMatchesByte() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(byte.class, Byte.class));
    }

    public void testCharacterMatcheschar() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(Character.class, char.class));
    }

    public void testcharMatchesCharacter() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(char.class, Character.class));
    }

    public void testLongMatcheslong() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(Long.class, long.class));
    }

    public void testlongMatchesLong() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(long.class, Long.class));
    }

    public void testFloatMatchesfloat() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(Float.class, float.class));
    }

    public void testfloatMatchesFloat() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(float.class, Float.class));
    }

    public void testDoubleMatchesdouble() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(Double.class, double.class));
    }

    public void testdoubleMatchesDouble() {
        assertTrue(ClassUtils.isMatchBetweenPrimativeAndWrapperTypes(double.class, Double.class));
    }


}
