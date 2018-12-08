/*
 * @authors
 * 	Yohanes Agung Immanuel
 * @since
 * 	September 2015
 */

package com.smu.simplemovieapp.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;




/**
 * Database Basic Class
 * 
 * @author
 * 	Yohanes Agung Immanuel
 * @since
 * 	March 2016
 * @see
 * 	1. All class on <b>com.SFAWingsV2.libs.db</b> has been updated with region to support code folding <br>
 *  2. To add region, please follow this template bellow : <br>
 *  	<pre>
 *  	{@code
 *  	// StartRegion Module::SubModule
 *  	
 *   	// EndRegion
 *   	}</pre>
 *     Where Module like a Business Area (eg. Soap, IceCream, and Etc.)<br>
 *     Where SubModule like Intended and associated for use and with the user (eg. Sales, Supervisor, Merchandising, VanSales, Etc.)<br><br>
 *     
 *     Please check this example bellow :<br>
 *     <pre>
 *   	{@code
 *  	// StartRegion Soap::Sales
 *  	
 *   	// EndRegion
 *     	}</pre>
 *     
 *     Rules of Thumb both Module and SubModule<br>
 *     1. All in CammelCase. (Eg. (IceCream)<br>
 *     2. No Space Allowed if Module or SubModule consists of two words. (Eg. VanSales)<br>
 *  3. For multipurpose function that used from outside region (function that used more than one module)
 *  	please give a tag like bellow :<br>
 *  	<pre>
 *  	{@code
 *  	// <used-in="Module1::SubModule1;Module2::SubModule2;((...));ModuleN::SubModuleN"/>
 *  	}</pre>
 *  
 *  	Please check example bellow :  <br>
 *  	<pre>
 *  	{@code
 *  	// StartRegion Soap::Sales
 *  	((some code here))
 *    	
 *  	// <used-in="Soap::Supervisor;IceCream::Sales;IceCream::Supervisor"/>
 *  	public BL_m_cmb_schedule_bcp(Context context){
 *  		((some code here))
 *   	}
 *
 *   	((some code here))
 *     	// EndRegion
 *		</pre>
 *  4. Please follow the rules to make consistency and well-orginized library<br>
 *  5. Thanks for your cooperation and understanding<br>
 */
public class DB_base {
	// StartRegion Global
	protected Context __context;
	protected DB_sqlite_helper __db_sqlite_helper;
	protected SQLiteDatabase __sqliteDatabase;
	
	public DB_base(Context context) {
		__context = context;
	}
	
	public DB_base open() throws SQLException {
		__db_sqlite_helper = new DB_sqlite_helper(__context);
		__sqliteDatabase = __db_sqlite_helper.getReadableDatabase();
		
		return this;
	}
	
	public void close() {
		__sqliteDatabase.close();
		__db_sqlite_helper.close();
	}

}