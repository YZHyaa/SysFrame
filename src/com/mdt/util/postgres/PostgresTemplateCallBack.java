package com.mdt.util.postgres;

import java.sql.ResultSet;

public interface PostgresTemplateCallBack<E> {

	public E purseRs(ResultSet rs);
	
}
