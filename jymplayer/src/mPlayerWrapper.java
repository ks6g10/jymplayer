import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.HashMap;


public class mPlayerWrapper {
	private static Process mplayerProcess;
	private static PrintStream mplayerIn;
	private static BufferedReader mplayerOutErr;
	private static PipedInputStream  readFrom;
	private static PipedOutputStream writeTo;
	private static LineRedirecter inputStream;
	private static LineRedirecter outputStream;
	private static final String linuxPath = "/usr/bin/mplayer";
	private static final String winPath = "C:\\Program Files (x86)\\SMPlayer\\smplayer.exe";
	private static final String mplayerArgs = " -slave -quiet -idle";
	private static String runtimeExec;// = winPath.concat(mplayerArgs);
	
	private static void initProcess() {
		try {
			if(System.getProperty("os.name").split(" ")[0].equals("Windows"))
				runtimeExec = winPath.concat(mplayerArgs);
			else
				runtimeExec = linuxPath.concat(mplayerArgs);
			mplayerProcess = Runtime.getRuntime().exec(runtimeExec);
			readFrom = new PipedInputStream(256*1024);
			try {
				writeTo = new PipedOutputStream(readFrom);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mplayerOutErr = new BufferedReader(new InputStreamReader(readFrom));
			// create the threads to redirect the standard output and error of MPlayer
			inputStream = new LineRedirecter(mplayerProcess.getInputStream(), writeTo);
			inputStream.start();
			outputStream = new LineRedirecter(mplayerProcess.getErrorStream(), writeTo);
			outputStream.start();
			mplayerIn = new PrintStream(mplayerProcess.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public  mPlayerWrapper() {
		initProcess();
	}
	
	public void onExit() {
		mplayerIn.print("quit");
		mplayerIn.print("\n");
		mplayerIn.flush();
		try {
		    mplayerProcess.waitFor();
		}
		catch (InterruptedException e) {}
		inputStream.destroy();
		outputStream.destroy();
		mplayerProcess.destroy();
	}
	
	public void run(String argURL) {
			// the standard input of MPlayer
		//check if mplayer have quit
		System.out.println(argURL);
		try {
			if(mplayerProcess.exitValue() == 0) {
				initProcess();//if, just restart
			}
				
	 } catch (IllegalThreadStateException exc) {
		 System.out.println("not exit"+exc.getMessage());
     }
			System.out.println("loading");
			mplayerIn.println("loadfile \""+argURL +"\" 0");
			//mplayerIn.print("\n");
			mplayerIn.flush();
//			mplayerIn.print("set_property time_pos 300");
//			mplayerIn.print("\n");
//			mplayerIn.flush();
		
	}
	
}
class LineRedirecter extends Thread {
    /** The input stream to read from. */
    private InputStream in;
    /** The output stream to write to. */
    private OutputStream out;
    BufferedReader reader;
    PrintStream printStream;
    /**
     * @param in the input stream to read from.
     * @param out the output stream to write to.
     * @param prefix the prefix used to prefix the lines when outputting to the logger.
     */
    LineRedirecter(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }
    
    public void destroy() {
    	printStream.flush();
    	printStream.close();
    	try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	LineRedirecter.yield();
    }

    public void run()
    {
        try {
            // creates the decorating reader and writer
            reader = new BufferedReader(new InputStreamReader(in));
            printStream = new PrintStream(out);
            String line;
            
            // read line by line
            while ( (line = reader.readLine()) != null) {
                printStream.println(line);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
