
/**		This is a calculator Client program. 
 * 		After connected to the server program, It will send 
 * 		command including inputs to and will get a result from the server
 * 
 * 		@version 1.0
 * 		@author  Anaswara Naderi Vadakkeperatta
 * 
 *        
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;  

public class CalculatorClient {

	public static void main(String[] args) {
		Socket clientSocket;		// TCP/IP socket

		try {
			/* step 1: connect to the server
	        IP address/server name: "local host"
	        port number:  5167
			 */
			clientSocket = new Socket( InetAddress.getByName( "localhost" ), 5167 );

			/* step 2: connect input and output streams to the socket
			 */
			BufferedReader receiveFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter sendToServer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

			String input = "YES"; 
			Scanner keyboard = new Scanner(System.in);

			try {
				/* step 3 : Exchange data */
				//receive first set of data from server with date
				String response = receiveFromServer.readLine();
				System.out.println("echo From Server: " + response);

				String clientCommand = null;
				while(true) {
					
					try {

						if(input.toUpperCase().equals("YES")){
	
							//sending Command line to server to perform various operation
							System.out.println("Enter command : Format is Command >> a op b");
							System.out.println("note : Space between command");
	
							clientCommand = keyboard.nextLine();
							sendToServer.println(clientCommand);
							sendToServer.flush();
							
							//to receive the result from server 
							String result = receiveFromServer.readLine();
							System.out.println(result);
							
							//statements asking user if he wants to continue processing
							System.out.println("Continue ? (YES/NO)");
							input = keyboard.nextLine();
	
						}else if(input.toUpperCase().equals("NO")) {
	
							clientCommand = "STOP";
							sendToServer.println(clientCommand);
							sendToServer.flush();
							break;
						} else {
							System.out.println("Enter valid input YES/NO");
							throw new Exception();
						}
					} catch(Exception e) {
						System.out.println("Try again!");
						input = keyboard.nextLine();
					}

				} //End while
				
				sendToServer.println("Client: closing the connection...");
				sendToServer.flush();
				
			}catch(EOFException eof) {
				System.out.println( "The server has terminated connection!" );
			}
			catch(IOException e) {
				System.out.println("IO error while exchange!");
			}
			System.out.println( "Client: data exchange completed" );

			/* step 4: close the connection to the server
			 */
			System.out.println( "Client: closing the connection..." );
			
			clientSocket.close();
			sendToServer.close();
			receiveFromServer.close();
			keyboard.close();

		}catch(IOException e) {
			e.getStackTrace();
		}
	}//End main

}//End CalculatorClient
