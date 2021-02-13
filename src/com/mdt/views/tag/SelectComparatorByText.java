package com.mdt.views.tag;

import java.text.Collator;
import java.util.Comparator;
import java.util.Map;


public class SelectComparatorByText implements Comparator {

	private boolean asc;
	Comparator comp = Collator.getInstance();
	
	public SelectComparatorByText(boolean asc)
	  {
	    this.asc = asc;
	  }
	
	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		Map.Entry entry1 = (Map.Entry)o1;
	    Map.Entry entry2 = (Map.Entry)o2;
	    if ((entry1.getValue() != null) && (entry2.getValue() != null))
	    {
	      if (this.asc) {
	        return this.comp.compare(entry1.getValue(), entry2.getValue());
	      }
	      return this.comp.compare(entry2.getValue(), entry1.getValue());
	    }
	    return -1;
	}
	
}
