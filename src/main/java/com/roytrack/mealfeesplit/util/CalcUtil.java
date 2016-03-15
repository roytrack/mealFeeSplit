package com.roytrack.mealfeesplit.util;




import java.math.BigDecimal;


/**
 * 数学运算类
 * 为了能够进行精确的数学运算和比较写了这个类
 * 提供精确的加减乘除方法
 * 提供精确的比较大小的方法
 * @author roytrack
 * @version 1.0
 * @date 2013年8月16日 14:10:05
 * */

public class CalcUtil {

	/**
	 * 提供精确的加法运算
	 * 可以容纳任意多的(double,Double,float,Float,String,Bigdecimal)参数，最起码一个，否则返回0
	 * 返回BigDecimal 自行进行精度和舍入方式的确定
	 * @param  ds 任意多个参数（double,Double,float,Float,String）
	 * @return  Bigdecimal  n个参数的和 实际返回的是Bigdecimal 
	 * */
	public static <T> BigDecimal add(T... ds ){
		BigDecimal BCops=new BigDecimal("0");
		if(ds!=null&&ds.length>0){
			for (T theop:ds){
				BCops=BCops.add(new BigDecimal(String.valueOf(theop instanceof String ? convertNull((String) theop) : theop)));
			}
		}
		return BCops;
	}
	/**
	 * 提供精确的减法运算
	 * 进行a-b计算
	 * 可以通过是否填写scale参数动态选择是否设置精度 舍入方式，
	 * 如果不设置scale则默认为2,舍入方式为BigDecimal.ROUND_HALF_UP（四舍五入）,
	 * 如果设置scale传入两个参数则取第一个为精度，第二个为舍入方式
	 * 如果设置scale传入一个参数，则取其为精度，舍入方式为BigDecimal.ROUND_HALF_UP（四舍五入）
	 * 如果设置超过两个参数则第三个无效
	 * #舍入方式参考
	 *  参数								 								int值
	 *	 BigDecimal.ROUND_UP   								0
	 *	 BigDecimal.ROUND_DOWN							1
	 *	 BigDecimal.ROUND_CEILING						2
	 *	 BigDecimal.ROUND_FLOOR							3
	 *	 BigDecimal.ROUND_HALF_UP						4
	 *	 BigDecimal.ROUND_HALF_DOWN				5
	 *	 BigDecimal.ROUND_HALF_EVEN					6
	 *	 BigDecimal.ROUND_UNNECESSARY				7
	 * @param  a（double,Double,float,Float,String） 减数
	 * @param  b（double,Double,float,Float,String） 被减数
	 * @param  scale（double,Double,float,Float,String）精度、舍入方式
	 * @return  Bigdecimal   a-b
	 * */
	public static <T> BigDecimal subtract(T a,T b ){
		return subtract(a,b, 2 , BigDecimal.ROUND_HALF_UP);
	}
	public static <T> BigDecimal subtract(T a,T b ,int scale ){
		return subtract(a,b, scale , BigDecimal.ROUND_HALF_UP);
	}
 	public static <T> BigDecimal subtract(T a,T b, int scale ,int sheru){
		BigDecimal BCa=new BigDecimal(String.valueOf(a instanceof String ? convertNull((String) a) : a));
		BigDecimal BCb=new BigDecimal(String.valueOf(b instanceof String ? convertNull((String) b) : b));
		BCa=BCa.subtract(BCb);
		BCa=BCa.setScale(scale,sheru);
		return BCa;
	}
	/**
	 * 提供精确的乘法运算，
	 * 可以容纳任意多的(double,Double,float,Float,String,Bigdecimal)参数，最起码一个，否则返回0
	 * 返回BigDecimal 自行进行精度和舍入方式的确定
	 * @param  ds 任意多个参数（double,Double,float,Float,String）
	 * @return  Bigdecimal  n个参数的和 实际返回的是Bigdecimal 
	 * */
	public static <T>BigDecimal multiply(T... ds ){
		BigDecimal BCops=new BigDecimal("0");
		if(ds!=null&&ds.length>0){
			BCops=new BigDecimal("1");
			for (T theop:ds){
				BCops=BCops.multiply(new BigDecimal(String.valueOf(theop instanceof String ? convertNull((String) theop) : theop)));
			}
		}
		return BCops;
	}
	
	/**
	 * 提供精确的除法运算
	 * 进行a/b计算
	 * 可以通过是否填写scale参数动态选择是否设置精度 舍入方式，
	 * 如果不设置scale则默认为2,舍入方式为BigDecimal.ROUND_HALF_UP（四舍五入）,
	 * 如果设置scale传入两个参数则取第一个为精度，第二个为舍入方式
	 * 如果设置scale传入一个参数，则取其为精度，舍入方式为BigDecimal.ROUND_HALF_UP（四舍五入）
	 * 如果设置超过两个参数则第三个无效
	 * #舍入方式参考
	 *  参数								 								int值
	 *	 BigDecimal.ROUND_UP   								0
	 *	 BigDecimal.ROUND_DOWN							1
	 *	 BigDecimal.ROUND_CEILING						2
	 *	 BigDecimal.ROUND_FLOOR							3
	 *	 BigDecimal.ROUND_HALF_UP						4
	 *	 BigDecimal.ROUND_HALF_DOWN				5
	 *	 BigDecimal.ROUND_HALF_EVEN					6
	 *	 BigDecimal.ROUND_UNNECESSARY				7
	 * @param a（double,Double,float,Float,String） 除数
	 * @param  b（double,Double,float,Float,String） 被除数
	 * @param  scale（double,Double,float,Float,String）精度、舍入方式
	 * @return  Bigdecimal   a/b
	 * */
	public static <T> BigDecimal divide(T a,T b ){
		return divide(a,b,2, BigDecimal.ROUND_HALF_UP);
	}
	public static <T> BigDecimal divide(T a,T b,int scale){
		return divide(a,b,scale, BigDecimal.ROUND_HALF_UP);
	}
 	public static <T> BigDecimal divide(T a,T b, int scale,int sheru ){
		BigDecimal BCa=new BigDecimal(String.valueOf(a instanceof String ? convertNull((String) a) : a));
		BigDecimal BCb=new BigDecimal(String.valueOf(b instanceof String ? convertNull((String) b) : b));
		BCa=BCa.divide(BCb,scale,sheru);
		return BCa;
	}
	/**
	 * 比较两值的大小 <br>
	 * a&ltb		-1 <br>
	 * a=b		0  <br>
	 * a&gtb		1 <br>
	 * @param <T> a 
	 * @param <T> b
	 * @return int 
	 * */
	public static <T> int compare(T a,T b){
		BigDecimal BCa=new BigDecimal(String.valueOf(a instanceof String ? convertNull((String) a) : a));
		BigDecimal BCb=new BigDecimal(String.valueOf(b instanceof String ? convertNull((String) b) : b));
		return BCa.compareTo(BCb);
	}
	
	/**
	 * 格式化浮点数，默认保留两位小数 四舍五入
	 * 第一个为要格式化的数字 第二个为保留小数位数 如果有第三个是舍入方式
	 * *  参数								 								int值
	 *	 BigDecimal.ROUND_UP   								0
	 *	 BigDecimal.ROUND_DOWN							1
	 *	 BigDecimal.ROUND_CEILING						2
	 *	 BigDecimal.ROUND_FLOOR							3
	 *	 BigDecimal.ROUND_HALF_UP						4
	 *	 BigDecimal.ROUND_HALF_DOWN				5
	 *	 BigDecimal.ROUND_HALF_EVEN					6
	 *	 BigDecimal.ROUND_UNNECESSARY				7
	 * */
	public static <T> double formatDouble(T a){
		return formatDouble(a,2, BigDecimal.ROUND_HALF_UP);
	}
	public static <T> double formatDouble(T a, int scale ){
		return formatDouble(a,scale, BigDecimal.ROUND_HALF_UP);
	}
 	public static <T> double formatDouble(T a,  int scale ,int sheru){
		BigDecimal bd = new BigDecimal(String.valueOf(a instanceof String ? convertNull((String) a) : a));
        bd = bd.setScale(scale, sheru);
        return bd.doubleValue();
	}

	public static String convertNull(String str){
		if(str==null||str.equals("null")||str.length()==0){
			return "0";
		}else{
			return str;
		}
	}
	
	
	//demo
//	public static void main(String[] args) {
//		System.out.println(StringUtils.convertNull((String)null));
//		BigDecimal adda=new BigDecimal("555");
//		System.out.println("add   "+add(adda,999999999.04));
//		System.out.println("subtract   "+subtract(49493.6666,2344323.555,4,BigDecimal.ROUND_HALF_UP));
//		System.out.println("multiply   "+multiply(11,22.22,33.4533));
//		System.out.println("divide   "+divide(5555,234,5,BigDecimal.ROUND_HALF_UP));
//		System.out.println(String.valueOf(999999999.04));
//		BigDecimal abc= new BigDecimal(String.valueOf(999999999.04));
//		abc=abc.add(new BigDecimal("0.01"));
//		abc=abc.setScale(2);
//		abc=abc.setScale(0,BigDecimal.ROUND_HALF_UP);
//		System.out.println(abc.toString());
//		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&");
//		double a=55.0000,b=55;
//		System.out.println(a==b);
//	    System.out.println(0.05 + 0.01);
//	    System.out.println(1.0 - 0.42);
//	    System.out.println(4.015 * 100);
//	    System.out.println(123.3 / 100);
//
//		
//	}


}
