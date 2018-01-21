/* 
*	This program performs calculator functions 
*	and store the result in member variable
*
* 		@version 1.0
* 		@author  Anaswara Naderi Vadakkeperatta
* 
*/
public class Calculator {
	private  String result;
	
	//Method to return the result 
	public String getResult() {
			return  result;
	}
	
	public void add(double num1, double num2){
		this.result = Double.toString(num1 + num2);
	}

	public void sub(double num1, double num2){
		this.result = Double.toString(num1 - num2);
	}
	
	public void multi(double num1, double num2){
		this.result = Double.toString(num1 * num2);
	}

	public boolean div(double num1, double num2){
		boolean exceptionStatus = false;
		try{
			this.result = Double.toString(num1 / num2);
			
			if (num2 == 0) {
				throw new IllegalArgumentException();
			}
		}catch(IllegalArgumentException Ie) {
			exceptionStatus = true;
			this.result = "Cannot be divided by 0";
		}
		return exceptionStatus;
	}
	public void mod(double num1, double num2){
		this.result = Double.toString(num1 % num2);
	}	
}
 
