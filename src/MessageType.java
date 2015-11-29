/**
 * Classify message into two types.
 * Type 1 MESSAGE
 * Type 2 LOGOUT
 */
import java.io.*;

public class MessageType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int MESSAGE = 1, LOGOUT = 2;
	private int type;
	private String message;
	
	MessageType(int type, String message) {
		this.type = type;
		this.message = message;
	}
	
	int getType() {
		return type;
	}
	String getMessage() {
		return message;
	}
}

