
class Message
{
	//Boxing method for String

	private String text;
	public Message() { text=""; }
	public void write(String s) { text=s; }
	public String read() { return text; }
}
