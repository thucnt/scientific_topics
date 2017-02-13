package edu.uit.snmr.utils;

import java.util.Comparator;

public class IntegerArrayIndexComparator implements Comparator<Integer>
{
    private final Integer[] array;

    public IntegerArrayIndexComparator(int[] array)
    {
    	this.array = new Integer[array.length];
    	for (int i = 0; i < array.length; i++) {
    		this.array[i] = new Integer(array[i]);
		}
    }

    public Integer[] createIndexArray()
    {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < array.length; i++)
        {
            indexes[i] = i; // Autoboxing
        }
        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2)
    {
         // Autounbox from Integer to int to use as array indexes
        return array[index1].compareTo(array[index2]);
    }
}
