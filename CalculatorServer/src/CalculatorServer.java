
/**		This is a calculator server program. 
 * 		It waits for client connections.When connected processes the command 
 * 		received from user and send the result based on input.
 * 		Result is calculated by calling Calculator class.
 * 
 * 		@version 1.0
 * 		@author  Anaswara Naderi Vadakkeperatta
 * 
 *        
 */

import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

public class CalculatorServer {

	public static void main(String[] args) {

		System.out.println("Server started");
		
		try {
			
			/* step 1: create a server socket
						port number: 5167 */
			ServerSocket serverSocket = new ServerSocket( 5167 );         
			// create a server socket with port number
			Socket socketConnection = null;

			while(true) {

				System.out.println( "listening for a connection..." ); 	
				socketConnection = serverSocket.accept();

				Thread t = new HandleClientThread( socketConnection );
				t.start();

			} //end of while
			
		}catch(IOException e) {
			// I/O error in socket connection
			System.out.println( "I/O error in socket connection" );
			e.printStackTrace();
		}
		
	} //End main
} //End CalculatorServer

//Class to handle each client thread.
class HandleClientThread extends Thread {

	private Socket socketConnection;

	//constructor to initialize  socket connection.
	public HandleClientThread(Socket sock) { socketConnection = sock; }

	public void run() {

		try {
			/* step 3: connect input and output streams to the socket
			 */
			BufferedReader receiveFromClient = new BufferedReader(new InputStreamReader(socketConnection.getInputStream()));
			PrintWriter sendToClient = new PrintWriter(new OutputStreamWriter
					(socketConnection.getOutputStream()));	

			if (socketConnection != null) {
				System.out.println( "I/O streams connected to the socket" );
			}
			// Instantiate a Date object
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy h:mm a ");
			String strDate= formatter.format(date);
			
			//first set of data sent to client with date 
			String respToClient = " Simple Calculator " + strDate + " - response (sent to the client): ";
			System.out.println(respToClient);
			sendToClient.println(respToClient);
			sendToClient.flush();


			try {
				String responseLine = null; 
				
				do { //keep on reading until client send STOP
					
					/* step 4 : Exchange data */
					//receiving command from client, if Stop loop will exit. 
					responseLine = receiveFromClient.readLine();
					
					//calling function to handle input command 
					String result = handleInputCommand(responseLine);
					
					if (result != null) {
						sendToClient.println(result);
						sendToClient.flush();
					}else {
						
						break;
					}

				}while(!responseLine.equals("STOP"));// end do while				

			} catch(EOFException eof) {
				// loss of connection
				System.out.println( "*** THE client has terminated connection ***" );
			}
			catch(IOException e) {
				System.out.println("Error in I/O Exchange");
				e.printStackTrace();  	
			}
			

			/*step5 
			 *    Close sockets and stream Objects
			 */
			if (socketConnection != null) {
				socketConnection.close();
			}

			sendToClient.close();
			receiveFromClient.close();
			socketConnection.close();
			
		}catch(IOException e ) {
			e.printStackTrace();    
		}
	}//end run

	//Method to perform command requested from user
	public String handleInputCommand(String inputCommand) {
		
		String result = null;
		try { 
			if(inputCommand != null) { 
	
				switch(inputCommand) {
					
					case "STOP" : 	System.out.println("STOP command received");
									break;
					default 	: 	System.out.println(inputCommand);
									//Create calculator object
									Calculator cal = new Calculator();
									String output = "Calculator Result: ";
									boolean exceptionStatus= false;
									
									//Process to split the strings in input command 
									ArrayList<String> input = new ArrayList<String>();
									String arr[] = inputCommand.split(" ");
									
									for (String item : arr) {
										input.add(item);
									}
									
									//Storing split strings in variables to further processing
									double num1 = Double.parseDouble(input.get(1).toString());//num1
									String operator = input.get(2).toString();//operator
									double num2 = Double.parseDouble(input.get(3).toString());//num2
									
									System.out.println("client requested  calculation:" + 
											input.get(1).toString() + " " + 
											input.get(2).toString() + " " + 
											input.get(3).toString());
									//Switch case to call calculator functions.
									switch(operator) {
										case "+" : 	cal.add(num1, num2); 
													break;
										case "*" : 	cal.multi(num1, num2);
													break;
										case "-" : 	cal.sub(num1, num2);
													break;								
										case "/" : 	exceptionStatus=cal.div(num1, num2);
													break;
										case "%" : 	cal.mod(num1, num2);
													break;
										default  : 	System.out.println("Enter a valid input!Please run again!");
													break;
									}//end switch(operator)
									
									//Statements to get the results to output based on Exception Status
									if(exceptionStatus) {
										result = cal.getResult();
										System.out.println("response (sent to the client):" + cal.getResult());
										break;
									} else { 
										result =  output + cal.getResult();
										System.out.println("response (sent to the client):" + cal.getResult());
										break;
									}
				}//end switch 

			} // end if 
		} catch(Exception e) {
			result = "Please check Input Command!";
			System.out.println("Command is Wrong!");
		}
		return result;
	}//end handleInputCommand
}//end HandleClientThread
